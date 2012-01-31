package org.conversionsvg.util;

/**
 * The <code>R</code> class contains methods for specifying resources.
 * 
 * @author Erich Schroeter
 */
public class R {

	/**
	 * Returns the resource string that refers to the <code>resource</code> file
	 * in the <code>usr.erichschroeter.conversionsvg.res.png</code> package.
	 * 
	 * @param resource
	 *            the file in the <code>png</code> package
	 * @return the full resource string
	 */
	public static String png(String resource) {
		return "usr/erichschroeter/conversionsvg/res/png/" + resource;
	}

	/**
	 * Returns the resource string that refers to the <code>resource</code> file
	 * in the <code>usr.erichschroeter.conversionsvg.res.xsd</code> package.
	 * 
	 * @param resource
	 *            the file in the <code>xsd</code> package
	 * @return the full resource string
	 */
	public static String xsd(String resource) {
		return "usr/erichschroeter/conversionsvg/res/xsd/" + resource;
	}

}
