 package net.anotheria.maf.action;

import net.anotheria.maf.builtin.ShowMappingsAction;
import net.anotheria.maf.errorhandling.ErrorHandler;

import java.util.ArrayList;
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
	 * Error handlers mappings: {@link Throwable} to the list of {@link ErrorHandler}.
	 */
	private final ConcurrentMap<Class<? extends Throwable>, List<Class<? extends ErrorHandler>>> errorHandlers = new ConcurrentHashMap<>();

	/**
	 * Default error handler.
	 * It will be executed in case if error has not been handled by either the action's error handler or by the global error handlers.
	 */
	private Class<? extends ErrorHandler> defaultErrorHandler;

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
	 * @param errorHandler the {@link ErrorHandler} class associated with current action
	 * @param commands     var-arg array of ActionCommands to map to given path
	 */
	public void addMapping(String path, String type, Class<? extends ErrorHandler> errorHandler, ActionCommand... commands) {
		mappings.put(path, new ActionMapping(path, type, errorHandler, commands));
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
	 * @param errorHandler the {@link ErrorHandler} class associated with current action
	 * @param commands     var-arg array of ActionCommands to map to given path
	 */
	public void addMapping(String path, Class<? extends Action> type, Class<? extends ErrorHandler> errorHandler, ActionCommand... commands) {
		mappings.put(path, new ActionMapping(path, type.getName(), errorHandler, commands));
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
	 * Allows to specify the error handler type for the given error type.
	 *
	 * @param error        the class of error
	 * @param errorHandler the class of error handler
	 */
	public synchronized void addErrorHandler(final Class<? extends Throwable> error, final Class<? extends ErrorHandler> errorHandler) {
		List<Class<? extends ErrorHandler>> handlers = this.errorHandlers.get(error);
		if (handlers == null) {
			handlers = new ArrayList<>();
		}

		handlers.add(errorHandler);
		this.errorHandlers.put(error, handlers);
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
	public List<Class<? extends ErrorHandler>> getErrorHandlers(Class<? extends Throwable> errorClazz) {
		final List<Class<? extends ErrorHandler>> handlers = errorHandlers.get(errorClazz);
		if (handlers == null) {
			return Collections.emptyList();
		}

		return new ArrayList<>(handlers);
	}
}
