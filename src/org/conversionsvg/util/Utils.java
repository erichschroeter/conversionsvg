package org.conversionsvg.util;

import java.awt.Dimension;

import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

/**
 * Consists of <code>public static</code> methods that may be useful in several
 * classes, but do not necessarily need to be encapsulated in them.
 * 
 * @author Erich Schroeter
 */
public class Utils {

	/**
	 * Returns the icon resource specified at <code>resource</code> with a size
	 * of 48x48.
	 * <p>
	 * This is equivalent to
	 * <code>resizableIcon(resource, new Dimension(48, 48))</code>.
	 * 
	 * @see #resizableIcon(String, Dimension)
	 * @param resource
	 *            the icon resource
	 * @return the icon located at <code>resource</code>
	 */
	public static ResizableIcon resizableIcon(String resource) {
		return resizableIcon(resource, new Dimension(48, 48));
	}

	/**
	 * Returns the icon resource specified at <code>resource</code> with the
	 * specified <code>dimension</code>.
	 * <p>
	 * This is equivalent to
	 * <code>ImageWrapperResizableIcon.getIcon(MagExplorerApplication.class
				.getClassLoader().getResourceAsStream(resource), dimension)</code>.
	 * 
	 * @param resource
	 *            the icon resource
	 * @param dimension
	 *            the desired icon dimension
	 * @return the icon located at <code>resource</code> with the
	 *         <code>dimension</code>
	 */
	public static ResizableIcon resizableIcon(String resource,
			Dimension dimension) {
		return ImageWrapperResizableIcon.getIcon(Utils.class.getClassLoader()
				.getResourceAsStream(resource), dimension);
	}
	
}
