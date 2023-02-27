package net.anotheria.maf.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Abstract action class which can be used as parent of all action classes.
 * @author another
 *
 */
public abstract class AbstractAction implements Action{

	@Override
	public void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) {
		
	}

	@Override
	public void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) {
		
	}

}
