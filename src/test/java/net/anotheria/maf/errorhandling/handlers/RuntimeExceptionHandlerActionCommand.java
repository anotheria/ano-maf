package net.anotheria.maf.errorhandling.handlers;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.errorhandling.ErrorHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link RuntimeException} handler which returns {@link ActionCommand}.
 *
 * @author Illya Bogatyrchuk
 */
public class RuntimeExceptionHandlerActionCommand implements ErrorHandler {
	@Override
	public ActionCommand handleError(Throwable error, Action action, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
		return new ActionCommand("ActionCommand");
	}
}
