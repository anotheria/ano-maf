package net.anotheria.maf.errorhandling.handlers;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.annotation.ErrorHandlerAnnotation;
import net.anotheria.maf.errorhandling.ErrorHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Global {@link RuntimeException} handler.
 *
 * @author Illya Bogatyrchuk
 */
@ErrorHandlerAnnotation(exception = RuntimeException.class)
public class GlobalRuntimeExceptionHandler implements ErrorHandler {
	@Override
	public ActionCommand handleError(Throwable error, Action action, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
		return new ActionCommand("GlobalRuntimeExceptionHandlerActionCommand");
	}
}
