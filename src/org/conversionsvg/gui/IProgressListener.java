package org.conversionsvg.gui;

public interface IProgressListener {

	/**
	 * Updates a progress bar to notify the user of the progress made in a
	 * conversion.
	 * 
	 * @param info
	 *            information about the Inkscape process just finished
	 */
	public void updateProgressBar(InkscapeProcessInfo info);
}
