package net.anotheria.maf.errorhandling;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Error handler.
 *
 * @author Illya Bogatyrchuk
 */
public interface ErrorHandler {
	/**
	 * Allows to handle the given error, which can be thrown in action.
	 *
	 * @param error    the error instance
	 * @param action   {@link Action}
	 * @param mapping  {@link ActionMapping}
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @return {@link ActionCommand}
	 */
	ActionCommand handleError(Throwable error, Action action, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws ServletException;
}
