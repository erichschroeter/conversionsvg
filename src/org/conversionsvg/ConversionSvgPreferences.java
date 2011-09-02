package org.conversionsvg;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

public interface ConversionSvgPreferences {

	/** The preference key associated with the Inkscape binary location */
	public static final String KEY_INKSCAPE_LOCATION = "inkscape.location";
	/**
	 * The preference key associated with the core number of threads used to
	 * delegate Inkscape processes
	 */
	public static final String KEY_CORE_POOL_SIZE = "thread_pool.core_size";
	/** The default amount of core threads in the thread pool */
	public static int DEFAULT_CORE_POOL_SIZE = 10;
	/**
	 * The preference key associated with the maximum number of threads used to
	 * delegate Inkscape processes
	 */
	public static final String KEY_MAXIMUM_POOL_SIZE = "thread_pool.max_size";
	/** The default maximum amount of threads in the thread pool */
	public static int DEFAULT_MAXIMUM_POOL_SIZE = 20;
	/**
	 * The preference key associated with the number of seconds to keep
	 * delegated Inkscape process threads alive
	 */
	public static final String KEY_KEEP_ALIVE_TIME = "thread_pool.keep_alive_time";
	/** The default number of seconds to keep threads alive */
	public static int DEFAULT_KEEP_ALIVE_TIME = 10;
	/** The preference key associated with the last known width used to export */
	public static final String KEY_INKSCAPE_EXPORT_WIDTH = "inkscape.export_width";
	/** The preference key associated with the last known height used to export */
	public static final String KEY_INKSCAPE_EXPORT_HEIGHT = "inkscape.export_height";
	/** The preference key associated with the last known color used to export */
	public static final String KEY_INKSCAPE_EXPORT_COLOR = "inkscape.export_color";
	/**
	 * The preference key associated with the last known directory used in the
	 * file selection tree hierarchy
	 */
	public static final String KEY_LAST_ROOT = "last root";
	/** The default location for the file tree hierarchy */
	public static File DEFAULT_LAST_ROOT = new File(System
			.getProperty("user.home"));

	// Basic application preferences

	/** The preference key associated with the window's last known width */
	public static final String KEY_WINDOW_WIDTH = "window.width";
	/** The preference key associated with the window's last known height */
	public static final String KEY_WINDOW_HEIGHT = "window.height";
	/** The default application window size */
	public static Dimension DEFAULT_WINDOW_SIZE = new Dimension(600, 350);
	/** The preference key associated with the window's last known x position */
	public static final String KEY_WINDOW_POSITION_X = "window.x";
	/** The preference key associated with the window's last known y position */
	public static final String KEY_WINDOW_POSITION_Y = "window.y";
	/** The default application window point */
	public static Point DEFAULT_WINDOW_POSITION = new Point(100, 100);

}
