package net.anotheria.maf.errorhandling;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.maf.action.CommandHandled;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerActionCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerCommandHandled;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNoOperationCommand;
import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

	@Before
	public void setup() {
		factory = new ErrorHandlerFactory();
		actionMappings = new ActionMappings();
	}

	@Test
	public void handlerReturnsNull() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNull.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getErrorHandlers(RuntimeException.class));

		Assert.assertTrue(command instanceof CommandHandled);
	}

	@Test
	public void handlerReturnsActionCommand() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getErrorHandlers(RuntimeException.class));

		Assert.assertNotNull(command);
		Assert.assertEquals("ActionCommand", command.getName());
	}

	@Test
	public void handlerReturnsCommandHandled() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getErrorHandlers(RuntimeException.class));

		Assert.assertTrue(command instanceof CommandHandled);
	}

	@Test
	public void handlerReturnsNoOperationCommand() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		final ActionCommand command = processor.process(actionMappings.getErrorHandlers(RuntimeException.class));

		Assert.assertTrue(command instanceof CommandHandled);
	}

	@Test
	public void processChainOfHandlers() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		processor.enableDebug();

		final ActionCommand command = processor.process(actionMappings.getErrorHandlers(RuntimeException.class));

		Assert.assertNotNull(command);
		Assert.assertEquals("ActionCommand", command.getName());

		Assert.assertEquals(2, processor.getExecutedErrorsHandlers().size());
		Assert.assertEquals(RuntimeExceptionHandlerNoOperationCommand.class.getName(), processor.getExecutedErrorsHandlers().get(0));
		Assert.assertEquals(RuntimeExceptionHandlerActionCommand.class.getName(), processor.getExecutedErrorsHandlers().get(1));
	}

	@Test
	public void processChainOfHandlers_2() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		processor.enableDebug();

		final ActionCommand command = processor.process(actionMappings.getErrorHandlers(RuntimeException.class));

		Assert.assertTrue(command instanceof CommandHandled);

		Assert.assertEquals(2, processor.getExecutedErrorsHandlers().size());
		Assert.assertEquals(RuntimeExceptionHandlerNoOperationCommand.class.getName(), processor.getExecutedErrorsHandlers().get(0));
		Assert.assertEquals(RuntimeExceptionHandlerCommandHandled.class.getName(), processor.getExecutedErrorsHandlers().get(1));
	}

	@Test
	public void processChainOfHandlers_3() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		processor.enableDebug();

		final ActionCommand command = processor.process(actionMappings.getErrorHandlers(RuntimeException.class));

		Assert.assertNotNull(command);
		Assert.assertEquals("ActionCommand", command.getName());

		Assert.assertEquals(1, processor.getExecutedErrorsHandlers().size());
		Assert.assertEquals(RuntimeExceptionHandlerActionCommand.class.getName(), processor.getExecutedErrorsHandlers().get(0));
	}

	@Test
	public void processChainOfHandlers_4() {
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerCommandHandled.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerNoOperationCommand.class);
		actionMappings.addErrorHandler(RuntimeException.class, RuntimeExceptionHandlerActionCommand.class);

		final ErrorHandlersProcessor processor = new ErrorHandlersProcessor(factory, new RuntimeException(), null, null, null, null);
		processor.enableDebug();

		final ActionCommand command = processor.process(actionMappings.getErrorHandlers(RuntimeException.class));

		Assert.assertTrue(command instanceof CommandHandled);

		Assert.assertEquals(1, processor.getExecutedErrorsHandlers().size());
		Assert.assertEquals(RuntimeExceptionHandlerCommandHandled.class.getName(), processor.getExecutedErrorsHandlers().get(0));
	}
}
