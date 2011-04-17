package org.conversionsvg.util;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

public class Helpers {

	static final Logger logger = Logger.getLogger(Helpers.class);

	public static ResizableIcon getResizableIconFromResource(String resource) {
		URL url = null;
		try {
			url = new URL(resource);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		return ImageWrapperResizableIcon.getIcon(url, new Dimension(48, 48));
	}

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
				+ color.getBlue() + ", " + (alpha ? color.getAlpha() : "")
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
	 * @param file the base file
	 * @param extension the new extension
	 * @return a new <code>File</code>
	 */
	public static File changeExtension(File file, String extension) {
		String path = file.getAbsolutePath();
		path = path.substring(0, path.lastIndexOf(".") + 1);
		path += extension;
		return new File(path);
	}

}
