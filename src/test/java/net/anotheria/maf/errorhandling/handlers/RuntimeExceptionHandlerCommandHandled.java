package net.anotheria.maf.errorhandling.handlers;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.action.CommandHandled;
import net.anotheria.maf.errorhandling.ErrorHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * {@link RuntimeException} handler which returns {@link CommandHandled}.
 *
 * @author Illya Bogatyrchuk
 */
public class RuntimeExceptionHandlerCommandHandled implements ErrorHandler {
	@Override
	public ActionCommand handleError(Throwable error, Action action, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
		return new CommandHandled();
	}
}
