package net.anotheria.maf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation to mark the class which implements {@link ErrorHandlerAnnotation} as global handler of the exception
 * specified in {@link #exception()} which can be thrown in actions.
 *
 * @author Illya Bogatyrchuk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorHandlerAnnotation {
	/**
	 * The class of the exception which should be handled by current handler.
	 *
	 * @return the exception class
	 */
	Class<? extends Throwable> exception();
}
