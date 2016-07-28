package net.anotheria.maf.action;

/**
 * A command that is returned by the action and signalizes to the maf filter what to do next.
 * @author lrosenberg
 *
 */
public class ActionCommand {
	/**
	 * Name of the command.
	 */
	private String name;  
	
	public ActionCommand(String aName){
		name = aName;
	}
	
	public String getName(){
		return name;
	}
	
	@Override public String toString(){
        return "command "+ name;
	}
}
