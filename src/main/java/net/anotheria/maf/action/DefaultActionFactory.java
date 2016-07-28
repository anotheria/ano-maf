package net.anotheria.maf.action;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A factory which creates action instances on the fly and manages created instances. 
 * @author lrosenberg
 *
 */
public final class DefaultActionFactory implements ActionFactory{
	
	/**
	 * Stored created factory instances.
	 */
	private static final ConcurrentMap<String, Action> instances = new ConcurrentHashMap<>();
	/**
	 * Returns an instance of defined action type.
	 * @param actionType
	 * @return
	 * @throws ActionFactoryException
	 */
	@Override public Action getInstanceOf(String actionType) throws ActionFactoryException{
		Action action = instances.get(actionType);
		if (action!=null)
			return action;
		try{
			action = (Action) Class.forName(actionType).newInstance();
		}catch(Exception e){
			throw new ActionFactoryException(e);
		}
		Action old = instances.putIfAbsent(actionType, action);
		return old != null ? old : action;
	}
	
}
