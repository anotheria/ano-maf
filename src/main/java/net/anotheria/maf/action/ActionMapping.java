package net.anotheria.maf.action;

import java.util.HashMap;
import java.util.Map;
/**
 * An action mapping is a combination of enter path for an action, class name of the class which executes the action and a map of possible forwards.  
 * @author lrosenberg
 */
public class ActionMapping {
	/**
	 * Path for the action to react to.
	 */
	private String path;
	/**
	 * Classname of the action class.
	 */
	private String type;
	/**
	 * Map with action forwards.
	 */
	private Map<String, ActionCommand> commands;

	/**
	 * Creates new ActionMapping.
	 * @param aPath path of this ActionMapping.
	 * @param aType type of action that will handle this mapping.
	 * @param someCommands var-arg array of ActionCommands bound to this ActionMapping.
	 */
	public ActionMapping(String aPath, String aType, ActionCommand... someCommands){
		path = aPath;
		type = aType;
		commands = new HashMap<String, ActionCommand>();
		if (someCommands!=null)
			for (ActionCommand c : someCommands)
				commands.put(c.getName(), c);
		
	}

	/**
	 * Returns a stored forward for the given forward name.
	 * @param name name of the forward.
	 * @return ActionForward if found, null if not found and throws IllegalArgumentException if
	 * ActionCommand with given name if not an ActionForward.
	 * @deprecated use findCommand instead.
	 */
	public ActionForward findForward(String name){
		ActionCommand c = findCommand(name);
		if (c instanceof ActionForward)
			return (ActionForward)c;
		if (c!=null)
			throw new IllegalArgumentException("Command "+name+" is not a forward");
		return null;
			
	}

	/**
	 * Find ActionCommand with given name bound to this ActionMapping.
	 * @param name name of the ActionCommand we wan't to find.
	 * @return found ActionCommand or {@code null} if not found.
	 */
	public ActionCommand findCommand(String name){
		return commands.get(name);
	}

	/**
	 * Get path of this ActionMapping.
	 * @return path of this ActionMapping.
	 */
	public String getPath(){
		return path;
	}

	/**
	 * Get type of action that handles this mapping.
	 * @return type of action that handles this mapping.
	 */
	public String getType(){
		return type;  
	}

	/**
	 * Shortcut to #findCommand("success").
	 * @return ActionCommand with name "success" if found.
	 */
	public ActionCommand success(){
		return findCommand("success");
	}

	/**
	 * Shortcut to #findCommand("error").
	 * @return ActionCommand with name "error" if found.
	 */
	public ActionCommand error(){
		return findCommand("error");
	}
	/**
	 * Shortcut to #findCommand("dialog").
	 * @return ActionCommand with name "dialog" if found.
	 */
	public ActionCommand dialog() {
		return findCommand("dialog");
	}
	/**
	 * Shortcut to #findCommand("redirect").
	 * @return CommandRedirect with name "redirect" if found.
	 */
	public CommandRedirect redirect(){
		return CommandRedirect.class.cast(findCommand("redirect"));
	}
	
	@Override public String toString(){
		return getPath()+" - "+getType()+" - "+commands;
	}

	/**
	 * Get all ActionCommands bound to this ActionMapping.
	 * @return map of command names and commands bound to current ActionMapping.
	 */
	public Map<String, ActionCommand> getCommands(){
		return new HashMap<String, ActionCommand>(commands);
	}
}
