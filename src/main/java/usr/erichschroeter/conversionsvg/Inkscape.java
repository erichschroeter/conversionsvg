package usr.erichschroeter.conversionsvg;

import java.io.File;

/**
 * Contains public static fields available throughout the application pertaining
 * to Inkscape.
 * 
 * @author Erich Schroeter
 */
public final class Inkscape {

	/** The available options Inkscape supports via the command line */
	public static enum Option {
		/**
		 * Option flag to tell Inkscape to export image to
		 * <em>Portable Network Graphics (PNG)</em> format
		 */
		EXPORT_PNG,
		/**
		 * Option flag to tell Inkscape to export image to
		 * <em>Printable Document Format (PDF)</em> format
		 */
		EXPORT_PDF,
		/** Option flag to tell Inkscape to export image to <em>(PS)</em> format */
		EXPORT_PS,
		/**
		 * Option flag to tell Inkscape to export image to <em>(EPS)</em> format
		 */
		EXPORT_EPS,
		/**
		 * Option to tell Inkscape to export image with a specified width (in
		 * pixels)
		 */
		PIXEL_WIDTH,
		/**
		 * Option to tell Inkscape to export image with a specified width (in
		 * pixels)
		 */
		PIXEL_HEIGHT,
		/**
		 * Option to tell Inkscape to export image with a specified background
		 * color
		 */
		BACKGROUND_COLOR,
		/**
		 * Option to tell Inkscape to export image with a specified background
		 * opacity
		 */
		BACKGROUND_OPACITY,
		/** Option to tell Inkscape to export the specified file */
		SVG_FILE,
		/** Option to tell Inkscape to export image's drawing area */
		AREA_DRAWING,
		/** Option to tell Inkscape to export image's page area */
		AREA_PAGE,
		/**
		 * Option to tell Inkscape to export image with the specified area (in
		 * pixels)
		 */
		AREA_CUSTOM
	}

	/**
	 * The command line value to pass to Inkscape to export image as
	 * <em>Portable Network Graphics (PNG)</em>
	 */
	public static final String OPTION_EXPORT_PNG = "-e";
	/**
	 * The command line value to pass to Inkscape to export image as
	 * <em>(PS)</em>
	 */
	public static final String OPTION_EXPORT_PS = "-P";
	/**
	 * The command line value to pass to Inkscape to export image as
	 * <em>Printable Document Format (PDF)</em>
	 */
	public static final String OPTION_EXPORT_PDF = "-A";
	/**
	 * The command line value to pass to Inkscape to export image as
	 * <em>(EPS)</em>
	 */
	public static final String OPTION_EXPORT_EPS = "-E";
	/**
	 * The command line value to pass to Inkscape to specify the background
	 * color of the image.
	 */
	public static final String OPTION_BACKGROUND_COLOR = "-b";
	/**
	 * The command line value to pass to Inkscape to specify the background
	 * opacity of the image.
	 */
	public static final String OPTION_BACKGROUND_OPACITY = "-y";
	/**
	 * The command line value to pass to Inkscape to specify the image width (in
	 * pixels)
	 */
	public static final String OPTION_PIXEL_WIDTH = "-w";
	/**
	 * The command line value to pass to Inkscape to specify the image height
	 * (in pixels)
	 */
	public static final String OPTION_PIXEL_HEIGHT = "-h";
	/** The command line value to pass to Inkscape to export the drawing area */
	public static final String OPTION_AREA_DRAWING = "-D";
	/** The command line value to pass to Inkscape to export the page area */
	public static final String OPTION_AREA_PAGE = "-C";
	/** The command line value to pass to Inkscape to export custom area */
	public static final String OPTION_AREA_CUSTOM = "-a";

	/**
	 * The full path to the Inkscape executable. This <code>null</code> by
	 * default.
	 * <p>
	 * The user is responsible for setting this value.
	 */
	private static File executable = null;

	/**
	 * Returns the full path to the Inkscape executable. This <code>null</code>
	 * by default and the user is responsible for setting this value.
	 * 
	 * @see #setExecutable(File)
	 * @return the executable, or <code>null</code> if not set
	 */
	public static File getExecutable() {
		return executable;
	}

	/**
	 * Sets the full path to the Inkscape executable.
	 * <p>
	 * If the location is unknown perhaps {@link #findExecutable()} may be of
	 * interest.
	 * 
	 * @param executable
	 *            the executable to set
	 */
	public static void setExecutable(File executable) {
		Inkscape.executable = executable;
	}

	/**
	 * Attempts to find the Inkscape executable based on the OS.
	 * <p>
	 * <ul>
	 * <li>For <em>Windows XP</em> it looks for
	 * <code>C:/Program Files/Inkscape/inkscape.exe</code></li>
	 * <li>
	 * For <em>Linux</em> it looks for <code>/usr/bin/inkscape</code></li>
	 * </ul>
	 * 
	 * @return the Inkscape executable as a <code>File</code>, or
	 *         <code>null</code> if not found
	 */
	public static File findExecutable() {
		File executable = null;
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("linux")) {
			executable = new File("/usr/bin/inkscape");
		} else if (os.contains("Windows") || os.contains("windows")) {
			executable = new File("C:/Program Files/Inkscape/inkscape.exe");
			if (!executable.exists()) {
				executable = new File(
						"C:/Program Files (x86)/Inkscape/inkscape.exe");
			}
		} else {
			// TODO handle Apple Inc. or platform independent
		}

		// only return the File object if the file actually exists on the system
		return executable.exists() ? executable : null;
	}

}
