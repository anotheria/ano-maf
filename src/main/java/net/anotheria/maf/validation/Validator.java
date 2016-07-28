package net.anotheria.maf.validation;

/**
 * Custom validator used to customize and define own checks.
 * <p/>
 *
 * @author vitaliy
 * @version 1.0
 *          Date: Jan 16, 2010
 *          Time: 9:23:55 PM
 */
public interface Validator<T> {

	/**
	 * Check field value.
	 *
	 * @param field value to check
	 * @return true if valid
	 */
    boolean validate(T field);
}
