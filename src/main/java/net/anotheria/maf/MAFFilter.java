package net.anotheria.maf;

import net.anotheria.maf.action.AbortExecutionException;
import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionFactory;
import net.anotheria.maf.action.ActionFactoryException;
import net.anotheria.maf.action.ActionForward;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.ActionMappingsConfigurator;
import net.anotheria.maf.action.CommandForward;
import net.anotheria.maf.action.CommandHandled;
import net.anotheria.maf.action.CommandRedirect;
import net.anotheria.maf.action.DefaultActionFactory;
import net.anotheria.maf.action.NoOperationCommand;
import net.anotheria.maf.annotation.ActionAnnotation;
import net.anotheria.maf.annotation.ActionErrorHandler;
import net.anotheria.maf.annotation.ActionErrorHandlers;
import net.anotheria.maf.annotation.ActionsAnnotation;
import net.anotheria.maf.annotation.CommandForwardAnnotation;
import net.anotheria.maf.annotation.CommandRedirectAnnotation;
import net.anotheria.maf.annotation.ErrorHandlerAnnotation;
import net.anotheria.maf.bean.ErrorBean;
import net.anotheria.maf.bean.FormBean;
import net.anotheria.maf.errorhandling.DefaultErrorHandler;
import net.anotheria.maf.errorhandling.ErrorHandler;
import net.anotheria.maf.errorhandling.ErrorHandlerFactory;
import net.anotheria.maf.errorhandling.ErrorHandlersProcessor;
import net.anotheria.maf.util.FormObjectMapper;
import net.anotheria.maf.validation.ValidationAware;
import net.anotheria.maf.validation.ValidationError;
import net.anotheria.maf.validation.ValidationException;
import net.anotheria.moskito.core.predefined.Constants;
import net.anotheria.moskito.core.predefined.FilterStats;
import net.anotheria.moskito.core.predefined.ServletStats;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.stats.Interval;
import net.anotheria.util.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MAFFilter is the dispatcher filter of the MAF. We are using a Filter instead of Servlet to be able to inject MAF parts in huge we-map-everything-through-one-servlet systems (aka spring).
 * In particular it is useful to inject moskito-webui which is maf-based into an existing spring application.
 * @author lrosenberg
 *
 */
public class MAFFilter implements Filter, IStatsProducer {


	/**
	 * Marker.
	 */
	private static final Marker FATAL = MarkerFactory.getMarker("FATAL");
	/**
	 * Stats for get request.
	 */
	private ServletStats getStats;
	/**
	 * List of stats as required by stats producer interface.
	 */
	private List<IStats> cachedStatList;
	/**
	 * Path on which to react.
	 */
	private String path;
    /**
     * Unique identificator of this producer (the class name of filter or a readable form of it).
     * @see IStatsProducer
     */
    private String producerId;
    /**
     * The id of the category the producer belongs to.
     * @see IStatsProducer
     */
    private String subsystem;
    /**
     * The id of the subsystem the producer belongs to.
     * @see IStatsProducer
     */
    private String category;
	/**
	 * Log.
	 */
	private static Logger log = LoggerFactory.getLogger(MAFFilter.class);
	/**
	 * ActionMappings config.
	 */
	private ActionMappings mappings;
	
	/**
	 * Actionfactory instance to create and manage action objects.
	 */
	private ActionFactory actionFactory = new DefaultActionFactory();

	/**
	 * {@link ErrorHandlerFactory} instance to create and manage {@link net.anotheria.maf.errorhandling.ErrorHandler} objects.
	 */
	private ErrorHandlerFactory errorHandlerFactory = new ErrorHandlerFactory();
	
	@Override
	public void destroy() {
		
	}

	@Override public void init(FilterConfig config) throws ServletException {
		getStats = new FilterStats("cumulated", getMonitoringIntervals());
		cachedStatList = new ArrayList<IStats>();
		cachedStatList.add(getStats);
		
		path = config.getInitParameter("path");
		if (path==null)
			path = "";

        producerId = config.getInitParameter("producerId");
        if (producerId==null)
            producerId = "maffilter";

        subsystem = config.getInitParameter("subsystem");
        if (subsystem==null)
            subsystem = "maf";

        category = config.getInitParameter("category");
        if (category==null)
            category = "filter";
		
		ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(this);

		String actionFactoryClazzName = config.getInitParameter("actionFactory");
		if (!StringUtils.isEmpty(actionFactoryClazzName)) {
			try {
				actionFactory = (ActionFactory)Class.forName(actionFactoryClazzName).newInstance();
			} catch(Exception e) {
				log.error("Couldn't initialize custom actionFactory: "+actionFactoryClazzName, e);
			}
		}
		
		mappings = new ActionMappings();
		
		List<ActionMappingsConfigurator> configurators = getConfigurators();
		for (ActionMappingsConfigurator configurator : configurators){
			try{
				configurator.configureActionMappings(mappings);
			}catch(Throwable t){
				log.error(FATAL, "Configuration failed by configurator " + configurator, t);
			}
		}

        // Configure by annotations
        String annotatedClassesPackage = config.getInitParameter("configureByAnnotations");
        if (!StringUtils.isEmpty(annotatedClassesPackage)) {
			final Reflections reflections = new Reflections(annotatedClassesPackage);
			configureActionsByAnnotations(reflections);
			configureActionsErrorHandlersByAnnotations(reflections);
			configureGlobalErrorHandlersByAnnotations(reflections);
        }

        // set default error handler
		if (mappings.getDefaultErrorHandler() == null) {
			mappings.setDefaultErrorHandler(DefaultErrorHandler.class);
		}
	}

	/**
	 * Allows to configure actions via annotations.
	 *
	 * @param reflections {@link Reflections} instance with scanned package which contains annotated classes
	 */
	private void configureActionsByAnnotations(final Reflections reflections) {
		Set<Class<?>> actionTypes = new HashSet<Class<?>>();
		actionTypes.addAll(reflections.getTypesAnnotatedWith(ActionAnnotation.class));
		actionTypes.addAll(reflections.getTypesAnnotatedWith(ActionsAnnotation.class));
		for(Class<?> clazz: actionTypes) {
			if (!Action.class.isAssignableFrom(clazz)) {
				String message = String.format("Class %s annotated with %s or %s is not inherited from %s",
						clazz.getName(), ActionAnnotation.class.getName(), ActionsAnnotation.class.getName(), Action.class.getName());
				log.error(message);
				throw new RuntimeException(message);
			}
			List<ActionAnnotation> maps = new ArrayList<ActionAnnotation>();
			ActionAnnotation mapAnnotation = clazz.getAnnotation(ActionAnnotation.class);
			if (mapAnnotation != null) {
				maps.add(mapAnnotation);
			}
			ActionsAnnotation mapsAnnotation = clazz.getAnnotation(ActionsAnnotation.class);
			if (mapsAnnotation != null) {
				Collections.addAll(maps, mapsAnnotation.maps());
			}
			for (ActionAnnotation map: maps) {
				if (!path.equals(map.context())) {
					continue;
				}
				List<ActionCommand> forwards = new ArrayList<ActionCommand>();
				for (CommandForwardAnnotation forward: map.forwards()) {
					forwards.add(new CommandForward(forward.name(), forward.path()));
				}
				for (CommandRedirectAnnotation redirect: map.redirects()) {
					forwards.add(new CommandRedirect(redirect.name(), redirect.target(), redirect.code()));
				}
				mappings.addMapping(map.path(), (Class<Action>)clazz, forwards.toArray(new ActionCommand[forwards.size()]));
			}
		}
	}

	/**
	 * Allows to configure action error handlers via annotations.
	 *
	 * @param reflections {@link Reflections} instance with scanned package which contains annotated classes
	 */
	private void configureActionsErrorHandlersByAnnotations(final Reflections reflections) {
		final Set<Class<?>> actionTypes = new HashSet<Class<?>>();
		actionTypes.addAll(reflections.getTypesAnnotatedWith(ActionErrorHandlers.class));
		actionTypes.addAll(reflections.getTypesAnnotatedWith(ActionErrorHandler.class));

		for(Class<?> clazz: actionTypes) {
			if (!Action.class.isAssignableFrom(clazz)) {
				final String message = String.format("Class %s annotated with %sor %s is not inherited from %s",
						clazz.getName(), ActionErrorHandler.class.getName(), ActionErrorHandlers.class.getName(), Action.class.getName());
				log.error(message);
				throw new RuntimeException(message);
			}

			final List<ActionErrorHandler> actionErrorHandlerAnnotations = new ArrayList<>();

			final ActionErrorHandler actionErrorHandlerAnnotation = clazz.getAnnotation(ActionErrorHandler.class);
			if (actionErrorHandlerAnnotation != null) {
				actionErrorHandlerAnnotations.add(actionErrorHandlerAnnotation);
			}

			final ActionErrorHandlers actionErrorHandlersAnnotation = clazz.getAnnotation(ActionErrorHandlers.class);
			if (actionErrorHandlersAnnotation != null) {
				Collections.addAll(actionErrorHandlerAnnotations, actionErrorHandlersAnnotation.value());
			}

			for (ActionErrorHandler handlerAnnotation : actionErrorHandlerAnnotations) {
				final Class<? extends Throwable> exception = handlerAnnotation.exception();
				final Class<? extends ErrorHandler> handler = handlerAnnotation.handler();

				mappings.addActionErrorHandler(clazz.getName(), exception, handler);
			}
		}
	}

	/**
	 * Allows to configure global error handlers via annotations.
	 *
	 * @param reflections {@link Reflections} instance with scanned package which contains annotated classes
	 */
	private void configureGlobalErrorHandlersByAnnotations(final Reflections reflections) {
		final Set<Class<?>> errorHandlerTypes = new HashSet<>(reflections.getTypesAnnotatedWith(ErrorHandlerAnnotation.class));

		for (Class<?> errorHandlerClazz : errorHandlerTypes) {
			if (!ErrorHandler.class.isAssignableFrom(errorHandlerClazz)) {
				final String message = String.format(
						"Class %s annotated with %s is not inherited from %s",
						errorHandlerClazz.getName(), ErrorHandlerAnnotation.class.getName(), ErrorHandler.class.getName()
				);

				log.error(message);
				throw new RuntimeException(message);
			}

			final ErrorHandlerAnnotation annotation = errorHandlerClazz.getAnnotation(ErrorHandlerAnnotation.class);
			final Class<? extends Throwable> exceptionClazz = annotation.exception();

			mappings.addErrorHandler(exceptionClazz, (Class<? extends ErrorHandler>) errorHandlerClazz);
		}
	}

	@Override public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
		if (!(sreq instanceof HttpServletRequest)){
			chain.doFilter(sreq, sres);
			return;
		}
		
		MAFExecutionContext executionContext = MAFExecutionContext.currentExecutionContext();
		executionContext.setFilter(this);
		executionContext.setMappings(mappings);
		
		HttpServletRequest req = (HttpServletRequest)sreq;
		HttpServletResponse res = (HttpServletResponse)sres;
		String servletPath = req.getServletPath();
		if (servletPath==null || servletPath.length()==0)
			servletPath = req.getPathInfo();
		
		if (!(servletPath==null)){
			if ((path.length()==0 || servletPath.startsWith(path)) && !isPathExcluded(servletPath)){
				doPerform(req, res, servletPath);
				//optionally allow the chain to run further?
				return;
			}
		}

		chain.doFilter(req, res);			
	}
	
	/**
	 * Decides whether the servlet path is excluded from execution by this filter.
	 * Derived class can customize filter chain traversal procedure by
	 * overriding this method.
	 * 
	 * @param servletPath path to the servlet.
	 * @return true if can not be performed by this filter, false otherwise.
	 */
	protected boolean isPathExcluded(String servletPath) {
		return false;
	}
 
	private void doPerform(HttpServletRequest req, HttpServletResponse res, String servletPath) throws ServletException, IOException {
		
		getStats.addRequest();
		long startTime = System.nanoTime();
		try{
			String actionPath = servletPath.substring(path.length());
			if (actionPath==null || actionPath.length()==0){
				if (getDefaultActionName()!=null)
					actionPath = getDefaultActionName();
			}
			ActionMapping mapping = mappings.findMapping(actionPath);
			if (mapping == null){
				if (mappings.getOnNotFound() != null) {
					executeCommand(mappings.getOnNotFound(), req, res);
					return;
				}

				res.sendError(404, "Action "+actionPath+" not found.");
				return;
			}
			Action action;
			try{
				action = actionFactory.getInstanceOf(mapping.getType());
			}catch(ActionFactoryException e){
				throw new ServletException("Can't instantiate "+mapping.getType()+" for path: "+actionPath+", because: "+e.getMessage(), e);
			}
			
			ActionCommand command = null;
			try{
				action.preProcess(mapping, req, res);
				FormBean bean = FormObjectMapper.getModelObjectMapped(req, action);
				if(bean != null){
					List<ValidationError> errors = FormObjectMapper.validate(req, bean);
					if(!errors.isEmpty()) {
						if(action instanceof ValidationAware) {
							command = ((ValidationAware)action).executeOnValidationError(mapping, bean, errors, req, res);
						}else{
							throw new ServletException("Mapper validation failed: "+errors);
						}
					}
				}
				
				if (command==null)
					command = action.execute(mapping, bean, req, res);

				action.postProcess(mapping, req, res);
			} catch(ValidationException e){
				throw new ServletException("Error in processing: "+e.getMessage(), e);
			} catch(AbortExecutionException e){
				//do nothing
			} catch(Throwable t){
				command = handleError(t, req, res, mapping, action);
			}
			
			if (command!=null){
				//support for 1.0 style
				executeCommand(command, req,  res);
			}

		}catch(ServletException e){
			getStats.notifyServletException(e);
			throw e;
		}catch(IOException e){
			getStats.notifyIOException(e);
			throw e;
		}catch(RuntimeException e){
			getStats.notifyRuntimeException(e);
			throw e;
		}catch(Error e){
			getStats.notifyError();
			throw e;
		}finally{
			long executionTime = System.nanoTime()-startTime;
			getStats.addExecutionTime(executionTime);
			getStats.notifyRequestFinished();
		}
	}

	private ActionCommand handleError(Throwable error, HttpServletRequest req, HttpServletResponse res, ActionMapping mapping, Action action) throws ServletException, IOException {
		ActionCommand result = null;

		// action error handlers
		final List<Class<? extends ErrorHandler>> actionErrorHandlers = mappings.getActionErrorHandler(mapping.getType(), error.getClass());
		if (!actionErrorHandlers.isEmpty()) {
			result = new ErrorHandlersProcessor(errorHandlerFactory, error, action, mapping, req, res).process(actionErrorHandlers);
		}

		// global error handlers
		if ((result == null || result instanceof NoOperationCommand) && !mappings.getGlobalErrorHandlers(error.getClass()).isEmpty()) {
			final List<Class<? extends ErrorHandler>> errorHandlers = mappings.getGlobalErrorHandlers(error.getClass());
			result = new ErrorHandlersProcessor(errorHandlerFactory, error, action, mapping, req, res).process(errorHandlers);
		}

		// default error handler in case if error wasn't handled above
		if ((result == null || result instanceof NoOperationCommand)) {
			final ErrorHandler defaultErrorHandler;

			if (mappings.getDefaultErrorHandler() == DefaultErrorHandler.class && mappings.getOnError() != null) {
				defaultErrorHandler = errorHandlerFactory.getDefaultErrorHandlerInstance(mappings.getOnError());
			} else {
				defaultErrorHandler = errorHandlerFactory.getInstance(mappings.getDefaultErrorHandler());
			}

			req.setAttribute(ErrorBean.NAME, new ErrorBean(error));
			result = defaultErrorHandler.handleError(error, action, mapping, req, res);
		}

		return result;
	}

	private void executeCommand(ActionCommand command, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		if (command instanceof CommandHandled) {
			// nothing to do
			return;
		}

		if (command instanceof ActionForward){
			ActionForward forward = (ActionForward)command;
			req.getRequestDispatcher(forward.getPath()).forward(req, res);
		}
		if (command instanceof CommandForward){
			CommandForward forward = (CommandForward)command;
			req.getRequestDispatcher(forward.getPath()).forward(req, res);
		}
		if (command instanceof CommandRedirect){
			CommandRedirect redirect = (CommandRedirect)command;
			if (redirect.getCode()==302){
				res.sendRedirect(redirect.getTarget());
			}else{
				res.setHeader("Location", redirect.getTarget());
				res.setStatus(redirect.getCode());
			}

		}

	}


	@Override public List<IStats> getStats() {
		return cachedStatList;
	}

	/**
	 * Override this method to setup custom monitoring intervals.
	 * @return array of monitoring intervals to use.
	 */
	protected Interval[] getMonitoringIntervals(){
		return Constants.getDefaultIntervals();
	}
	
	/**
	 * Override this operation to perform access control to moskitoUI. Default is yes (true).
	 * @param req the HttpServletRequest
	 * @param res the HttpServletResponse
	 * @return true if the operation is allowed (post or get).
	 */
	protected boolean operationAllowed(HttpServletRequest req, HttpServletResponse res){
		return true;
	}

	@Override public String getProducerId() {
        return producerId;
    }

	@Override public String getSubsystem() {
        return subsystem;
	}

	@Override public String getCategory() {
         return category;
	}
	
	/**
	 * Overwrite this method and return configurators for your project.
	 * @return list of {@link ActionMappingsConfigurator} instances to configure your project.
	 */
	protected List<ActionMappingsConfigurator> getConfigurators(){
		return new ArrayList<ActionMappingsConfigurator>();
	}
	
	/**
	 * If not null an empty path is replaced by this default action name, for example 'index'.
	 * @return default action.
	 */
	protected String getDefaultActionName(){
		return null;
	}

	protected ActionFactory getActionFactory() {
		return actionFactory;
	}

	protected void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}
	
}
