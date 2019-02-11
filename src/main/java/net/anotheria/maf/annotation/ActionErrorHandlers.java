package net.anotheria.maf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation allows to have multiple {@link ActionErrorHandler} at once.
 * It can be used only with class which implements {@link net.anotheria.maf.action.Action}.
 *
 * @author Illya Bogatyrchuk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionErrorHandlers {
	ActionErrorHandler[] value();
}
