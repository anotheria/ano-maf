package net.anotheria.maf.errorhandling;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.CommandHandled;
import net.anotheria.maf.action.NoOperationCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerActionCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerCommandHandled;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNoOperationCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNull;
import net.anotheria.maf.errorhandling.handlers.ThrowsExceptionErrorHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * {@link ErrorHandlersProcessor} test.
 *
 * @author Illya Bogatyrchuk
 */
public class ErrorHandlersProcessorTest {
	/**
	 * {@link ErrorHandlerFactory} instance.
	 */
	private ErrorHandlerFactory factory;

	/**
	 * {@link ActionMappings} instance.
	 */
	private ActionMappings actionMappings;

	@BeforeEach
	public void setup() {
		factory = new ErrorHandlerFactory();
		actionMappings = new ActionMappings();
	}

	@Test
	public void handlerReturnsNull() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNull.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertTrue(command instanceof CommandHandled);
	}

	@Test
	public void handlerReturnsActionCommand() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertNotNull(command);
		Assertions.assertEquals("ActionCommand", command.getName());
	}

	@Test
	public void handlerReturnsCommandHandled() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertTrue(command instanceof CommandHandled);
	}


	@Test
	public void handlerReturnsNoOperationCommand() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertTrue(command instanceof NoOperationCommand);
	}

	@Test
	public void handlerThrowsNotExpectedException() {
		actionMappings.addErrorHandler(RuntimeException.class, ThrowsExceptionErrorHandler.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertTrue(command instanceof NoOperationCommand);
	}

	@Test
	public void processListOfHandlers() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		processor.enableDebug();

		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertNotNull(command);
		Assertions.assertEquals("ActionCommand", command.getName());

		Assertions.assertEquals(2, processor.getExecutedErrorsHandlers().size());
		Assertions.assertEquals(RuntimeExceptionHandlerNoOperationCommand.class.getName(), processor.getExecutedErrorsHandlers().get(0));
		Assertions.assertEquals(RuntimeExceptionHandlerActionCommand.class.getName(), processor.getExecutedErrorsHandlers().get(1));
	}

	@Test
	public void processListOfHandlers_2() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		processor.enableDebug();

		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertTrue(command instanceof CommandHandled);

		Assertions.assertEquals(2, processor.getExecutedErrorsHandlers().size());
		Assertions.assertEquals(RuntimeExceptionHandlerNoOperationCommand.class.getName(), processor.getExecutedErrorsHandlers().get(0));
		Assertions.assertEquals(RuntimeExceptionHandlerCommandHandled.class.getName(), processor.getExecutedErrorsHandlers().get(1));
	}

	@Test
	public void processListOfHandlers_3() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		processor.enableDebug();

		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertNotNull(command);
		Assertions.assertEquals("ActionCommand", command.getName());

		Assertions.assertEquals(1, processor.getExecutedErrorsHandlers().size());
		Assertions.assertEquals(RuntimeExceptionHandlerActionCommand.class.getName(), processor.getExecutedErrorsHandlers().get(0));
	}

	@Test
	public void processListOfHandlers_4() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		processor.enableDebug();

		final ActionCommand command = processor.process(actionMappings.getGlobalErrorHandlers(RuntimeException.class));

		Assertions.assertTrue(command instanceof CommandHandled);

		Assertions.assertEquals(1, processor.getExecutedErrorsHandlers().size());
		Assertions.assertEquals(RuntimeExceptionHandlerCommandHandled.class.getName(), processor.getExecutedErrorsHandlers().get(0));
	}
}
