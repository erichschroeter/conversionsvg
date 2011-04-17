package org.conversionsvg.gui;

import java.util.List;

public class InkscapeProcessInfo {
	List<String> options;

	/**
	 * An object which serves to hold the information relevant to an Inkscape
	 * process.
	 * 
	 * @param options
	 *            the command line options used to start the Inkscape process
	 */
	public InkscapeProcessInfo(List<String> options) {
		this.options = options;
	}
}
