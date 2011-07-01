package org.conversionsvg;

/**
 * This interface provides methods for required in order to modify the Inkscape
 * command. The intention of this interface is to provide a common framework for
 * accessing the single {@link InkscapeCommandBuilder}. By having access to the
 * application command builder, different views can then make specific
 * modifications depending on the components available on the graphical
 * interface.
 * 
 * @author Erich Schroeter
 */
public interface ModifiesInkscapeCommand {

	/**
	 * Returns the command builder responsible for building the command sent on
	 * the command line to Inkscape.
	 * 
	 * @return the Inkscape command builder
	 */
	public InkscapeCommandBuilder getCommand();
}
