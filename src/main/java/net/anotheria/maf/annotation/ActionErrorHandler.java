package net.anotheria.maf.annotation;

import net.anotheria.maf.errorhandling.ErrorHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation can be used only with class which implements {@link net.anotheria.maf.action.Action}.
 * It describes the exception type which can be thrown in the action and {@link ErrorHandler} which is able to handle this exception.
 *
 * @author Illya Bogatyrchuk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionErrorHandler {
	/**
	 * The class of the exception which should be handled by {@link #handler()}.
	 *
	 * @return the exception class
	 */
	Class<? extends Throwable> exception();

	/**
	 * The class of the error handler responsible for {@link #exception()} handling.
	 *
	 * @return the error handler class
	 */
	Class<? extends ErrorHandler> handler();
}
