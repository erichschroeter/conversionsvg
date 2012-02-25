package usr.erichschroeter.conversionsvg.util;

import java.awt.Color;

/**
 * Contains public static methods for working with
 * <em>Scalable Vector Graphics (SVG)</em>.
 * 
 * @author Erich Schroeter
 */
public final class SVGUtils {

	/**
	 * Returns a <em>Scalable Vector Graphics (SVG)</em>-supported color string
	 * based on the <code>color</code>.
	 * 
	 * @param color
	 *            the color
	 * @return SVG-supported color string
	 */
	public static String toSvgFormatString(Color color) {
		return toRGB(color, false);
	}

	/**
	 * Returns a <code>String</code> representing the <code>Color</code>.
	 * <p>
	 * Inkscape requires the background color to be a string in a certain
	 * format. We are choosing to use the <code>rgb(255,255,255)</code> format
	 * (without the opacity). Opacity is a separate argument for Inkscape.
	 * 
	 * @param color
	 *            the color
	 * @param alpha
	 *            include the opacity value
	 * @return A string formatted as
	 *         <code>rgb(<em>red</em>, <em>green</em>, <em>blue</em>, <em>alpha</em>)</code>
	 *         .
	 */
	public static String toRGB(Color color, boolean alpha) {
		return "rgb(" + color.getRed() + ", " + color.getGreen() + ", "
				+ color.getBlue() + (alpha ? ", " + color.getAlpha() : "")
				+ ")";
	}

}
