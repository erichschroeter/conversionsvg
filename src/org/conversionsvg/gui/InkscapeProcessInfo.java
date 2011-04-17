package org.conversionsvg.gui;

import java.util.List;

public class InkscapeProcessInfo {
	List<String> command;

	/**
	 * An object which serves to hold the information relevant to an Inkscape
	 * process.
	 * 
	 * @param command
	 *            the command line used to start the Inkscape process
	 */
	public InkscapeProcessInfo(List<String> command) {
		this.command = command;
	}
}
