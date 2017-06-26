 package net.anotheria.maf.action;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.anotheria.maf.builtin.ShowMappingsAction;

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
	 * This command will be executed if an error happens during the command execution.  We recommend to use a CommandForward. You can access original error under the name maf.error in the request.
	 */
	private ActionCommand onError;

	/**
	 * This command is executed if a not found action has been requested. We recommend to use a CommandRedirect.
	 */
	private ActionCommand onNotFound;
	
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
	
	public ActionMappings(){
		addAlias("maf/showMappings", "/maf/showMappings");
		addMapping("/maf/showMappings", ShowMappingsAction.class);
	}
	public ActionCommand getOnError() {
		return onError;
	}

	public void setOnError(ActionCommand onError) {
		this.onError = onError;
	}

	public ActionCommand getOnNotFound() {
		return onNotFound;
	}

	public void setOnNotFound(ActionCommand onNotFound) {
		this.onNotFound = onNotFound;
	}


}
