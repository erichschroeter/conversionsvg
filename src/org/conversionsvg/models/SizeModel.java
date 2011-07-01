package org.conversionsvg.models;

import org.conversionsvg.InkscapeCommandBuilder;
import org.conversionsvg.ModifiesInkscapeCommand;

import com.jidesoft.app.framework.BasicDataModel;
import com.jidesoft.app.framework.DataModelException;

public class SizeModel extends DomainModel implements ModifiesInkscapeCommand {

	/** A globally unique identifier for the size model */
	public static final String GLOBAL_NAME = "models.size";
	/**
	 * The command builder used to build the command to send to Inkscape on the
	 * command line
	 */
	private InkscapeCommandBuilder command;

	/**
	 * Constructs a <code>SizeModel</code> specifying the Inkscape command
	 * builder.
	 */
	public SizeModel(InkscapeCommandBuilder command) {
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
