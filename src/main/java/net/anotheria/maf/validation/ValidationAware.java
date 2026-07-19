package net.anotheria.maf.validation;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

/**
 * Custom error handler used to process validation errors,
 * action should implement this interface to customize error handling.
 *
 * @author vitaliy
 * @version 1.0
 *          Date: Jan 16, 2010
 *          Time: 9:23:55 PM
 */
public interface ValidationAware {

	/**
	 * Called instead of execute if the validation framework encountered an error.
	 * @param mapping action mapping.
	 * @param formBean form bean. This will probably be incompletely validated.
	 * @param errors Validation errors.
	 * @param req http servlet request.
	 * @param res http servlet response.
	 * @return ActionCommand to execute.
	 * @throws Exception if can't handle it.
	 */
	ActionCommand executeOnValidationError(ActionMapping mapping, FormBean formBean, List<ValidationError> errors, HttpServletRequest req, HttpServletResponse res) throws Exception;
}
