package org.conversionsvg.models;

import org.conversionsvg.InkscapeCommandBuilder;
import org.conversionsvg.ModifiesInkscapeCommand;

public class ColorModel extends DomainModel implements ModifiesInkscapeCommand {

	/** A globally unique identifier for the color model */
	public static final String GLOBAL_NAME = "models.color";
	/**
	 * The command builder used to build the command to send to Inkscape on the
	 * command line
	 */
	private InkscapeCommandBuilder command;

	/**
	 * Constructs a <code>ColorModel</code> specifying the Inkscape command
	 * builder.
	 */
	public ColorModel(InkscapeCommandBuilder command) {
		setCommand(command);
	}

	/**
	 * Sets the Inkscape command builder responsible for building the command
	 * sent on the command line to Inkscape.
	 * 
	 * @param command
	 *            the command to set
	 */
	protected void setCommand(InkscapeCommandBuilder command) {
		this.command = command;
	}

	//
	// ModifiesInkscapeCommand members
	//

	@Override
	public InkscapeCommandBuilder getCommand() {
		return command;
	}

}
