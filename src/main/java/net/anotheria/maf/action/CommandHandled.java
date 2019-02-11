package net.anotheria.maf.action;

/**
 * The command indicates to {@link net.anotheria.maf.MAFFilter} not to do anything at all.
 *
 * @author Illya Bogatyrchuk
 */
public class CommandHandled extends ActionCommand {
	/**
	 * Constructor.
	 */
	public CommandHandled() {
		super("CommandHandled");
	}
}
