package net.anotheria.maf.errorhandling;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link Action} implementation, which throws {@link AssertionError} during execution.
 */
public class ActionWithAssertionError implements Action {
	@Override
	public void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

	}

	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
		throw new AssertionError();
	}

	@Override
	public void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

	}
}
