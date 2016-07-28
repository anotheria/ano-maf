 package net.anotheria.maf.action;

import net.anotheria.maf.builtin.ShowMappingsAction;

import java.util.HashMap;
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
	private final ConcurrentMap<String, String> aliases = new ConcurrentHashMap<>();
	/**
	 * Action mappings.O
	 */
	private final ConcurrentMap<String, ActionMapping> mappings = new ConcurrentHashMap<>();

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
	 * @param path
	 * @param type
	 * @param commands
	 */
	public void addMapping(String path, String type, ActionCommand... commands){
		mappings.put(path, new ActionMapping(path, type, commands));
	}

	/**
	 * Adds a mapping.
	 * @param path
	 * @param type
	 * @param forwards
	 */
	public void addMapping(String path, String type, ActionForward... forwards){
		mappings.put(path, new ActionMapping(path, type, forwards));
	}
	
	public void addForward(String actionPath, String forwardPath){
		addMapping(actionPath, ForwardAction.class, new ActionForward("forward", forwardPath));
	}
	
	/**
	 * Adds a mapping.
	 * @param path
	 * @param type
	 * @param commands
	 */
	public void addMapping(String path, Class<? extends Action> type, ActionCommand... commands){
		mappings.put(path, new ActionMapping(path, type.getName(), commands));
	}

	/**
	 * Adds an 1.0 style mapping.
	 * @param path
	 * @param type
	 * @param forwards
	 */
	public void addMapping(String path, Class<? extends Action> type, ActionForward... forwards){
		mappings.put(path, new ActionMapping(path, type.getName(), forwards));
	}

	/**
	 * Adds an alias.
	 * @param sourcePath
	 * @param targetPath
	 */
	public void addAlias(String sourcePath, String targetPath){
		aliases.put(sourcePath, targetPath);
	}

	public ActionMapping findMapping(String actionPath) {
		while (true) {
			String alias = aliases.get(actionPath);
			if (alias != null) {
				actionPath = alias;
				continue;
			}
			return mappings.get(actionPath);
		}
	}
	
	public Map<String, String> getAliases(){
		Map<String, String> ret = new HashMap<>(aliases);
        return ret;
	}

	//TODO this method allows indirect modification of action mappings, it should probably instead clone the mappings (TOFIX).
	public Map<String, ActionMapping> getMappings(){
		Map<String, ActionMapping> ret = new HashMap<>(mappings);
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
