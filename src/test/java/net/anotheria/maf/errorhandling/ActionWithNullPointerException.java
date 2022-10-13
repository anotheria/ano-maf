package net.anotheria.maf.errorhandling;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link Action} implementation, which throws {@link NullPointerException} during execution.
 */
public class ActionWithNullPointerException implements Action {
	@Override
	public void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

	}

	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
		throw new NullPointerException();
	}

	@Override
	public void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

	}
}
