package net.anotheria.maf.action;

import net.anotheria.maf.builtin.ShowMappingsAction;
import net.anotheria.maf.errorhandling.ErrorHandler;
import net.anotheria.maf.errorhandling.ErrorHandlersHolder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Configuration of the Framework. This class contains all mappings the framework will react on.
 * @author another
 *
 */
public final class ActionMappings {
	
	/**
	 * Action aliases.
	 */
	private final ConcurrentMap<String, String> aliases = new ConcurrentHashMap<String, String>();
	/**
	 * Action mappings.
	 */
	private final ConcurrentMap<String, ActionMapping> mappings = new ConcurrentHashMap<String, ActionMapping>();

	/**
	 * Global error handlers holder.
	 */
	private final ErrorHandlersHolder globalErrorHandlersHolder = new ErrorHandlersHolder();
	/**
	 * Default error handler.
	 * It will be executed in case if error has not been handled by either the action's error handler or by the global error handlers.
	 */
	private Class<? extends ErrorHandler> defaultErrorHandler;

	/**
	 * Actions error handlers.
	 */
	private final ConcurrentMap<String, ErrorHandlersHolder> actionErrorHandlers = new ConcurrentHashMap<>();

	/**
	 * Adds a mapping.
	 * @param path path to which given ActionCommand(s) are mapped.
	 * @param type type of ActionMapping created.
	 * @param commands var-arg array of ActionCommands to map to given path.
	 */
	public void addMapping(String path, String type, ActionCommand... commands){
		mappings.put(path, new ActionMapping(path, type, commands));
	}

	/**
	 * Adds a mapping.
	 *
	 * @param path         path to which given ActionCommand(s) are mapped
	 * @param type         type of ActionMapping created
	 * @param error        the {@link Throwable} class to handle by error handler
	 * @param errorHandler the {@link ErrorHandler} class associated with current action
	 * @param commands     var-arg array of ActionCommands to map to given path
	 */
	public void addMapping(String path, String type, Class<? extends Throwable> error, Class<? extends ErrorHandler> errorHandler, ActionCommand... commands) {
		mappings.put(path, new ActionMapping(path, type, commands));
		addActionErrorHandler(type, error, errorHandler);
	}

	/**
	 * Adds a mapping.
	 * @param path path to which given ActionForward(s) are mapped.
	 * @param type type of ActionMapping created.
	 * @param forwards var-arg array of ActionForwards to map to given path.
	 */
	public void addMapping(String path, String type, ActionForward... forwards){
		mappings.put(path, new ActionMapping(path, type, forwards));
	}

	public void addForward(String actionPath, String forwardPath){
		addMapping(actionPath, ForwardAction.class, new ActionForward("forward", forwardPath));
	}
	
	/**
	 * Adds an 1.0 style mapping.
	 * @param path path to which given ActionCommand(s) are mapped.
	 * @param type type of ActionMapping created.
	 * @param commands var-arg array of ActionCommands to map to given path.
	 */
	public void addMapping(String path, Class<? extends Action> type, ActionCommand... commands){
		mappings.put(path, new ActionMapping(path, type.getName(), commands));
	}

	/**
	 * Adds a mapping.
	 *
	 * @param path         path to which given ActionCommand(s) are mapped
	 * @param type         type of ActionMapping created
	 * @param error        the {@link Throwable} class to handle by error handler
	 * @param errorHandler the {@link ErrorHandler} class associated with current action
	 * @param commands     var-arg array of ActionCommands to map to given path
	 */
	public void addMapping(String path, Class<? extends Action> type, Class<? extends Throwable> error, Class<? extends ErrorHandler> errorHandler, ActionCommand... commands) {
		mappings.put(path, new ActionMapping(path, type.getName(), commands));
		addActionErrorHandler(type.getName(), error, errorHandler);
	}

	/**
	 * Adds an 1.0 style mapping.
	 * @param path path to which given ActionForward(s) are mapped.
	 * @param type type of ActionMapping created.
	 * @param forwards var-arg array of ActionForwards to map to given path.
	 */
	public void addMapping(String path, Class<? extends Action> type, ActionForward... forwards){
		mappings.put(path, new ActionMapping(path, type.getName(), forwards));
	}

	/**
	 * Adds an alias.
	 * @param sourcePath alias name.
	 * @param targetPath alias target.
	 */
	public void addAlias(String sourcePath, String targetPath){
		aliases.put(sourcePath, targetPath);
	}
	
	public ActionMapping findMapping(String actionPath){
		String alias = aliases.get(actionPath);
		if (alias!=null)
			return findMapping(alias);
		return mappings.get(actionPath);
	}
	
	public Map<String, String> getAliases(){
		HashMap<String, String> ret = new HashMap<String, String>();
		ret.putAll(aliases);
		return ret;
	}

	//TODO this method allows indirect modification of action mappings, it should probably instead clone the mappings (TOFIX).
	public Map<String, ActionMapping> getMappings(){
		HashMap<String, ActionMapping> ret = new HashMap<String, ActionMapping>();
		ret.putAll(mappings);
		return ret;
	}

	/**
	 * Allows to specify the global error handler type for the given error type.
	 *
	 * @param error        the class of error
	 * @param errorHandler the class of error handler
	 */
	public void addErrorHandler(final Class<? extends Throwable> error, final Class<? extends ErrorHandler> errorHandler) {
		this.globalErrorHandlersHolder.addHandler(error, errorHandler);
	}

	public ActionMappings(){
		addAlias("maf/showMappings", "/maf/showMappings");
		addMapping("/maf/showMappings", ShowMappingsAction.class);
	}

	public void setDefaultErrorHandler(Class<? extends ErrorHandler> defaultErrorHandler) {
		this.defaultErrorHandler = defaultErrorHandler;
	}

	public Class<? extends ErrorHandler> getDefaultErrorHandler() {
		return defaultErrorHandler;
	}

	/**
	 * Returns the list of error handler types which can handle the given error type.
	 *
	 * @param errorClazz the type of error
	 * @return the list of error handler types or empty if handlers were not found
	 */
	public List<Class<? extends ErrorHandler>> getGlobalErrorHandlers(Class<? extends Throwable> errorClazz) {
		return this.globalErrorHandlersHolder.getHandlers(errorClazz);
	}

	/**
	 * Allows to specify the error handler of the given error for the action.
	 *
	 * @param actionType the action type
	 * @param error      the class of error
	 * @param handler    the class of error handler
	 */
	public synchronized void addActionErrorHandler(final String actionType, final Class<? extends Throwable> error, Class<? extends ErrorHandler> handler) {
		ErrorHandlersHolder holder = this.actionErrorHandlers.get(actionType);
		if (holder == null) {
			holder = new ErrorHandlersHolder();
		}

		holder.addHandler(error, handler);
		this.actionErrorHandlers.put(actionType, holder);
	}

	/**
	 * Returns the list of error handler types which can handle the given error type for the given action type.
	 *
	 * @param actionType the action type
	 * @param error      the class of error
	 * @return the list of error handler types
	 */
	public List<Class<? extends ErrorHandler>> getActionErrorHandler(final String actionType, final Class<? extends Throwable> error) {
		final ErrorHandlersHolder holder = this.actionErrorHandlers.get(actionType);
		if (holder == null) {
			return Collections.emptyList();
		}

		return holder.getHandlers(error);
	}
}
