package net.anotheria.maf.errorhandling.handlers;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.errorhandling.ErrorHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler which throws {@link IllegalStateException}.
 *
 * @author Illya Bogatyrchuk
 */
public class ThrowsExceptionErrorHandler implements ErrorHandler {
	@Override
	public ActionCommand handleError(Throwable error, Action action, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		throw new IllegalStateException();
	}
}
