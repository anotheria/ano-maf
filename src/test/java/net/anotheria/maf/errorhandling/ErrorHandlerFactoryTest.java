package net.anotheria.maf.errorhandling;

import net.anotheria.maf.errorhandling.handlers.RuntimeExceptionHandlerNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link ErrorHandlerFactory} test.
 *
 * @author Illya Bogatyrchuk
 */
public class ErrorHandlerFactoryTest {
	@Test
	public void testSingleton() {
		final ErrorHandlerFactory factory = new ErrorHandlerFactory();

		final RuntimeExceptionHandlerNull instance1 = factory.getInstance(RuntimeExceptionHandlerNull.class);
		final RuntimeExceptionHandlerNull instance2 = factory.getInstance(RuntimeExceptionHandlerNull.class);

		Assertions.assertSame(instance1, instance2);
	}
}
