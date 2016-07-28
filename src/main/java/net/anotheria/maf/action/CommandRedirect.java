package net.anotheria.maf.action;

/**
 * This type of action command is used for redirects.
 * @author lrosenberg
 *
 */
public class CommandRedirect extends ActionCommand implements Cloneable{
	/**
	 * Redirect target.
	 */
	private String target;
	/**
	 * Redirect http code (301,302).
	 */
	private int code;
	
	/**
	 * Creates a new redirect.
	 * @param name
	 * @param aTarget
	 * @param aCode
	 */
	public CommandRedirect(String name, String aTarget, int aCode){
		super(name);
		target = aTarget;
		code = aCode;
	}
	
	/**
	 * Creates a new 302 redirect.
	 * @param name
	 * @param target
	 */
	public CommandRedirect(String name, String target){
		this(name, target, 302);
	}
	
	@Override public String toString(){
        return getName()+" to "+ target +" via "+ code;
	}

	public String getTarget() {
		return target;
	}

	public int getCode() {
		return code;
	}
	
	@Override public CommandRedirect clone(){
		try{
			return (CommandRedirect)super.clone();
		}catch(CloneNotSupportedException e){
			throw new AssertionError("Can't happen");
		}
	}
	
	/**
	 * Adds a parameter to redirected url.
	 * @param name
	 * @param value
	 * @return
	 */
	public CommandRedirect addParameter(String name, String value){
		CommandRedirect ret = clone();
		if (ret.target.indexOf('?')==-1)
			ret.target += "?";
		else
			ret.target += "&";
		ret.target += name+ '=' +value;
		return ret;
	}
	
}
