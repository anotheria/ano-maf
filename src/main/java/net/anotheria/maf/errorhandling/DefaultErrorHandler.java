package net.anotheria.maf.errorhandling;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Default {@link net.anotheria.maf.MAFFilter} error handler in case if it was not specified
 * via {@link net.anotheria.maf.action.ActionMappings#setDefaultErrorHandler(Class)}.
 *
 * @author Illya Bogatyrchuk
 */
public class DefaultErrorHandler implements ErrorHandler {
	/**
	 * {@link ActionCommand}.
	 */
	private ActionCommand actionCommand;

	/**
	 * Default constructor.
	 */
	public DefaultErrorHandler() {
	}

	/**
	 * Constructor that allows to specify return action command.
	 *
	 * @param actionCommand {@link ActionCommand}
	 */
	public DefaultErrorHandler(ActionCommand actionCommand) {
		this.actionCommand = actionCommand;
	}

	@Override
	public ActionCommand handleError(Throwable error, Action action, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		if (actionCommand == null) {
			throw new ServletException("Error in processing: " + error.getMessage(), error);
		}

		return actionCommand;
	}
}
