package net.anotheria.maf.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Simple action that allows to forward to a previously defined jsp.
 */
public class ForwardAction extends AbstractAction{
	@Override
	public ActionCommand execute(ActionMapping mapping,  HttpServletRequest req, HttpServletResponse res) {
		return mapping.findCommand("forward");
	}
}
