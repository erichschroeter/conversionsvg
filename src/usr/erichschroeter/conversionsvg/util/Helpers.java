package usr.erichschroeter.conversionsvg.util;

import java.awt.Color;
import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;

public class Helpers {

	/**
	 * Returns a <code>String</code> representing the <code>Color</code>.
	 * <p>
	 * Inkscape requires the background color to be a string in a certain
	 * format. We are choosing to use the rgb(255,255,255) format (without the
	 * opacity). Opacity is a separate argument.
	 * </p>
	 * 
	 * @param color
	 *            the color
	 * @param alpha
	 *            include the opacity value
	 * @return A sanitized String formatted as rgb(255,255,255).
	 */
	public static String toRGB(Color color, boolean alpha) {
		return "rgb(" + color.getRed() + ", " + color.getGreen() + ", "
				+ color.getBlue() + (alpha ? ", " + color.getAlpha() : "")
				+ ")";
	}

	/**
	 * A helper function to get a formatted message using parameters for a
	 * specific <code>Locale</code>.
	 * 
	 * @return Returns a formatted string.
	 */
	public static String sprintf(String pattern, Object[] args, Locale locale) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(locale);
		formatter.applyPattern(pattern);
		return formatter.format(args);
	}

	/**
	 * Returns a new <code>File</code> with the same path as the given
	 * <i>file</i>, but with the file extension changed to the given
	 * <i>extension</i>.
	 * 
	 * @param file
	 *            the base file
	 * @param extension
	 *            the new extension
	 * @return a new <code>File</code>
	 */
	public static File changeExtension(File file, String extension) {
		String path = file.getAbsolutePath();
		path = path.substring(0, path.lastIndexOf(".") + 1);
		path += extension;
		return new File(path);
	}

	/**
	 * Returns a new <code>File</code> with the path changed to the <i>path</i>
	 * specified, but the same file name.
	 * 
	 * @param file
	 *            the file to change
	 * @param path
	 *            the path to move to
	 * @return the changed <code>File</code>
	 */
	public static File changePath(File file, File path) {
		return new File(path.getPath() + System.getProperty("file.separator")
				+ file.getName());
	}
}
