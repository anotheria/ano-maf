package net.anotheria.maf.action;

/**
 * The command indicates that {@link net.anotheria.maf.errorhandling.ErrorHandler} can't handle error.
 * If {@link net.anotheria.maf.errorhandling.ErrorHandlersProcessor} receives such command, it continue error handlers processing.
 *
 * @author Illya Bogatyrchuk
 */
public class NoOperationCommand extends ActionCommand {
	/**
	 * Constructor.
	 */
	public NoOperationCommand() {
		super("NoOperationCommand");
	}
}
