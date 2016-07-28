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
	
	public ActionMapping(String aPath, String aType, ActionCommand... someCommands){
		path = aPath;
		type = aType;
		commands = new HashMap<>();
		if (someCommands!=null)
			for (ActionCommand c : someCommands)
				commands.put(c.getName(), c);
		
	}
	
	/**
	 * Returns a stored forward for the given forward name.
	 * @param name
	 * @return
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
	
	public ActionCommand findCommand(String name){
		return commands.get(name);
	}
	
	public String getPath(){
		return path;
	}
	
	public String getType(){
		return type;  
	}
	
	public ActionCommand success(){
		return findCommand("success");
	}
	public ActionCommand error(){
		return findCommand("error");
	}
	public ActionCommand dialog(){
		return findCommand("dialog");
	}
	public CommandRedirect redirect(){
		return (CommandRedirect)findCommand("redirect");
	}
	
	@Override public String toString(){
        return path +" - "+ type +" - "+commands;
	}
	
	public Map<String, ActionCommand> getCommands(){
		Map<String, ActionCommand> ret = new HashMap<>(commands);
		return ret;
	}
}
