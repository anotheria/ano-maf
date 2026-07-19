package net.anotheria.maf.errorhandling.annotatedactions;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.annotation.ActionErrorHandler;
import net.anotheria.maf.annotation.ActionErrorHandlers;
import net.anotheria.maf.errorhandling.handlers.NullPointerExceptionHandler;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerActionCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNoOperationCommand;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * {@link Action} implementation with {@link ActionErrorHandler} annotations.
 */
@ActionErrorHandlers({
		@ActionErrorHandler(exception = RuntimeException.class, handler = RuntimeExceptionHandlerNoOperationCommand.class),
		@ActionErrorHandler(exception = RuntimeException.class, handler = RuntimeExceptionHandlerActionCommand.class),
		@ActionErrorHandler(exception = NullPointerException.class, handler = NullPointerExceptionHandler.class),
})
public class ActionWithActionErrorHandlerAnnotations implements Action {
	@Override
	public void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

	}

	@Override
	public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
		throw new RuntimeException();
	}

	@Override
	public void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

	}
}
