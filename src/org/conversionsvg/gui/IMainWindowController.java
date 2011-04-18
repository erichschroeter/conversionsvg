package org.conversionsvg.gui;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.jidesoft.swing.CheckBoxTree;

/**
 * The IMainWindowController interface exists to define the essential functions
 * required to be implemented in order to have communication between a
 * <code>MainWindow</code> instance and its controller. This, of course, is
 * assuming the developer is attempting to follow the Model View Controller
 * (MVC) framework.
 */
public interface IMainWindowController {

	/**
	 * Adds the given <code>IProgressListener</code> to a container which keeps
	 * track of the listeners that will be notified of the progress made on a
	 * conversion.
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addProgressListener(IProgressListener listener);

	/**
	 * Converts the Scalable Vector Graphic (SVG) images selected by the user to
	 * the selected output file types also selected by the user.
	 * 
	 */
	public void handleConvert(List<File> files, Map<String, String> options);

	/**
	 * Cancels any conversions currently in progress and prevents conversions in
	 * a queue from being started.
	 */
	public void handleCancel();

	/**
	 * Changes the language of text on the user interface on the fly.
	 * 
	 * @param locale
	 *            The locale
	 */
	public void handleChangeLanguage(Locale locale);

	/**
	 * Changes the root of the file hierarchy.
	 * 
	 * <p>
	 * This is useful for reducing the number of directories the user needs to
	 * expand in order to select files.
	 * </p>
	 * 
	 * @param tree
	 *            the file system tree
	 * @param root
	 *            The new root
	 */
	public void handleChangeRoot(CheckBoxTree tree, File root);

	/**
	 * Saves the state of the settings the user has selected.
	 * <p>
	 * These settings are stored in the <code>PreferenceStore</code> and are
	 * loaded on startup.
	 * </p>
	 */
	public void handleSave();

	/**
	 * Stops existing threads currently running (e.g. threads still converting)
	 * and exits the application.
	 * 
	 * <p>
	 * Explicitly closing the application may not be necessary if the
	 * <code>Window</code> is set to EXIT_ON_CLOSE.
	 * </p>
	 */
	public void handleQuit();

	/**
	 * Displays the <i>About</i> dialog.
	 */
	public void handleAbout();

	/**
	 * Displays the preference settings dialog.
	 * 
	 * <p>
	 * If the dialog is not set to automatically save settings on a window
	 * closing event, this would be the place to save the settings.
	 * <p>
	 */
	public void handleSettings();
}
