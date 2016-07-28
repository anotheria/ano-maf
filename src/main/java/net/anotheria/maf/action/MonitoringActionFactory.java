package net.anotheria.maf.action;

import net.anotheria.moskito.core.dynamic.ProxyUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This version of the action factory monitors all created instances.
 *
 * @author lrosenberg
 * @since 11.05.14 01:07
 */
public class MonitoringActionFactory implements ActionFactory{

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
			int index = actionType.lastIndexOf('.');
			String name = index == -1 ? actionType : actionType.substring(index+1);
			action = ProxyUtils.createServiceInstance(action, name, "action", "action", Action.class);
		}catch(Exception e){
			throw new ActionFactoryException(e);
		}
		Action old = instances.putIfAbsent(actionType, action);


		return old != null ? old : action;
	}

}
