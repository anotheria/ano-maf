package net.anotheria.maf.errorhandling;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.action.CommandHandled;
import net.anotheria.maf.action.NoOperationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ErrorHandler} processor.
 *
 * @author Illya Bogatyrchuk
 */
public class ErrorHandlersProcessor {
	/**
	 * {@link Logger} instance.
	 */
	private static Logger LOGGER = LoggerFactory.getLogger(ErrorHandlersProcessor.class);

	/**
	 * {@link ErrorHandlerFactory} instance.
	 */
	private ErrorHandlerFactory errorHandlerFactory;

	/**
	 * {@link Throwable} instance.
	 */
	private Throwable error;

	/**
	 * {@link Action} instance.
	 */
	private Action action;

	/**
	 * {@link ActionMapping} instance.
	 */
	private ActionMapping mapping;

	/**
	 * {@link HttpServletRequest} instance.
	 */
	private HttpServletRequest request;

	/**
	 * {@link HttpServletResponse} instance.
	 */
	private HttpServletResponse response;

	/**
	 * Enables possibility to save the names of executed errors handlers in {@link #executedErrorsHandlers}.
	 */
	private boolean debug;

	/**
	 * The list of executed error handlers.
	 * Can be used only with {@link #debug} = true.
	 */
	private List<String> executedErrorsHandlers;

	/**
	 * Constructor.
	 *
	 * @param errorHandlerFactory {@link ErrorHandlerFactory}
	 * @param error               {@link Throwable}
	 * @param action              {@link Action}
	 * @param mapping             {@link ActionMapping}
	 * @param request             {@link HttpServletRequest}
	 * @param response            {@link HttpServletResponse}
	 */
	public ErrorHandlersProcessor(ErrorHandlerFactory errorHandlerFactory, Throwable error, Action action, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
		this.errorHandlerFactory = errorHandlerFactory;
		this.error = error;
		this.action = action;
		this.mapping = mapping;
		this.request = request;
		this.response = response;
	}

	/**
	 * Allows to process the given list of {@link ErrorHandler}.
	 * The first non {@link NoOperationCommand} will be considered as handled.
	 *
	 * @param handlersClasses the list of {@link ErrorHandler} classes to be processed
	 * @return {@link ActionCommand}
	 */
	public ActionCommand process(final List<Class<? extends ErrorHandler>> handlersClasses) {
		for (Class<? extends ErrorHandler> handlerClazz : handlersClasses) {
			final ActionCommand actionCommand = process(handlerClazz);

			if (actionCommand instanceof NoOperationCommand) {
				continue;
			}

			return actionCommand;
		}

		return new NoOperationCommand();
	}

	/**
	 * Process single error handler.
	 * If error handler returns {@code null} it will be considered as {@link CommandHandled}.
	 * If one of handlers returns something else then a NoOperationCommand the error is considered handled.
	 * {@link NoOperationCommand} will be returned too in case if unexpected exception was thrown during handler processing.
	 *
	 * @param handlerClazz the {@link ErrorHandler} class to be processed
	 * @return {@link ActionCommand}
	 */
	public ActionCommand process(final Class<? extends ErrorHandler> handlerClazz) {
		try {
			final ErrorHandler handler = errorHandlerFactory.getInstance(handlerClazz);
			final ActionCommand actionCommand = handler.handleError(error, action, mapping, request, response);

			if (debug) {
				getExecutedErrorsHandlers().add(handlerClazz.getName());
			}

			if (actionCommand == null) {
				return new CommandHandled();
			}

			return actionCommand;
		} catch (Throwable t) {
			LOGGER.warn(String.format("Unexpected error during %s processing", handlerClazz.getName()), t);
			return new NoOperationCommand();
		}
	}

	/**
	 * Marks {@link #debug} as {@code true}.
	 */
	void enableDebug() {
		this.debug = true;
	}

	/**
	 * For debug purposes.
	 *
	 * @return the list of executed error handlers
	 */
	List<String> getExecutedErrorsHandlers() {
		if (!debug) {
			throw new AssertionError("Only for debug purposes!");
		}

		if (executedErrorsHandlers == null) {
			executedErrorsHandlers = new ArrayList<>();
		}

		return executedErrorsHandlers;
	}
}
