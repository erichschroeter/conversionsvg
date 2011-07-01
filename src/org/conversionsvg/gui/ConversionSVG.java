package org.conversionsvg.gui;

import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.conversionsvg.util.Helpers;

import org.conversionsvg.gui.preferences.AdvancedPreferencePage;
import org.conversionsvg.gui.preferences.DefaultsPreferencePage;
import org.jpreferences.DefaultPreferenceManager;
import org.jpreferences.IPreferenceManager;
import org.jpreferences.model.DefaultPreferenceNode;
import org.jpreferences.storage.ConflictingIdentifierException;
import org.jpreferences.storage.DefaultPreferenceStore;
import org.jpreferences.storage.IPreferenceStore;
import org.jpreferences.storage.XmlPreferenceStore;
import org.jpreferences.ui.IPreferencePage;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConversionSVG {
	boolean packFrame = false;
	static final Logger logger = Logger.getLogger(ConversionSVG.class);

	//
	// Preferences
	//
	private IPreferenceStore prefs;
	private IPreferenceManager prefsManager;
	private static final String SETTINGS_FILE = "preferences.properties";
	private static File prefsFile;

	//
	// Properties KEYS
	//
	public static final String KEY_INKSCAPE_PATH = "inkscape_path";
	public static final String KEY_CORE_POOL_SIZE = "pool_size_core";
	public static final String KEY_MAXIMUM_POOL_SIZE = "pool_size_max";
	public static final String KEY_KEEP_ALIVE_TIME = "thread_keep_alive_time";
	public static final String KEY_INKSCAPE_EXPORT_WIDTH = "inkscape_export_width";
	public static final String KEY_INKSCAPE_EXPORT_HEIGHT = "inkscape_export_height";
	public static final String KEY_INKSCAPE_EXPORT_COLOR = "inkscape_export_color";
	public static final String KEY_LAST_ROOT = "last_root";
	public static final String KEY_BACKGROUND_COLOR = "background_color";
	public static final String KEY_BACKGROUND_OPACITY = "background_opacity";

	/**
	 * Creates an instance of the ConversionSVG application.
	 * <p>
	 * This constructor does the following:
	 * <ul>
	 * <li>creates a <code>MainWindow</code> instance</li>
	 * <li>positions the application in the center of the screen</li>
	 * <li>sets the window visible</li>
	 * </ul>
	 */
	public ConversionSVG() {

		// BEGIN get user settings/preferences
		try {
			prefsFile = new File(SETTINGS_FILE);
			prefsFile.createNewFile();
			prefs = new DefaultPreferenceStore(prefsFile,
					"This file contains preference settings for ConversionSVG",
					getDefaults());

			prefs.load();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		// END get user settings/preferences

		prefsManager = new DefaultPreferenceManager(prefs);

		IPreferencePage defaultsPage = new DefaultsPreferencePage(prefsManager,
				"Defaults",
				"Specify the default preferences to be populated when ConversionSVG starts up");
		IPreferencePage advancedPage = new AdvancedPreferencePage(prefsManager,
				"Advanced", "Configure advanced features ConversionSVG uses.");

		try {
			prefsManager.add(new DefaultPreferenceNode("defaults",
					defaultsPage, "Defaults"));
			prefsManager.add(new DefaultPreferenceNode("advanced",
					advancedPage, "Advanced"));
		} catch (ConflictingIdentifierException e) {
			logger.error(e.getMessage());
		}

		IMainWindowController controller = new MainWindowController(
				prefsManager);
		MainWindow window = new MainWindow(controller);

		if (packFrame) {
			window.pack();
		} else {
			window.validate();
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = window.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		window.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		window.setVisible(true);
	}

	/**
	 * Returns a <code>Properties</code> instance populated with the default
	 * values <code>MainWindowController</code> uses.
	 * 
	 * @return the default properties used by <code>MainWindowController</code>
	 */
	private Properties getDefaults() {
		Properties defaults = new Properties();
		defaults.setProperty(ConversionSVG.KEY_CORE_POOL_SIZE, Integer
				.toString(5));
		defaults.setProperty(ConversionSVG.KEY_MAXIMUM_POOL_SIZE, Integer
				.toString(10));
		defaults.setProperty(ConversionSVG.KEY_KEEP_ALIVE_TIME, Long
				.toString(10));
		defaults.setProperty(ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR, Helpers
				.toRGB(Color.WHITE, false));
		defaults.setProperty(ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT, "");
		defaults.setProperty(ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH, "");
		return defaults;
		// Map<String, String> defaults = new HashMap<String, String>();
		//
		// defaults.put(ConversionSVG.KEY_CORE_POOL_SIZE, Integer.toString(5));
		// defaults.put(ConversionSVG.KEY_MAXIMUM_POOL_SIZE,
		// Integer.toString(10));
		// defaults.put(ConversionSVG.KEY_KEEP_ALIVE_TIME, Long.toString(10));
		// defaults.put(ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR, Helpers.toRGB(
		// Color.WHITE, false));
		// defaults.put(ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT, "");
		// defaults.put(ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH, "");
		//
		// return defaults;
	}

	/**
	 * Sets the <i>Look and Feel (LAF)</i> for the application. Creates an
	 * instance of <code>ConversionSVG</code>.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				new ConversionSVG();
			}
		});
	}
}