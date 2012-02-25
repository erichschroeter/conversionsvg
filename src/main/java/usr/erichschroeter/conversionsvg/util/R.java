package usr.erichschroeter.conversionsvg.util;

/**
 * The <code>R</code> class contains methods for specifying resources in the
 * <code>usr.erichschroeter.conversionsvg.res</code> package.
 * 
 * @author Erich Schroeter
 */
public class R {

	/** The root package resources are located in. */
	public static final String resourcePackage = "usr/erichschroeter/conversionsvg/res";

	/**
	 * Returns the resource string that refers to the <code>resource</code> file
	 * in the <code>usr.erichschroeter.conversionsvg.res.png</code> package.
	 * 
	 * @param resource
	 *            the file in the <code>png</code> package
	 * @return the full resource string
	 */
	public static String png(String resource) {
		return String.format("%s/png/%s", resourcePackage, resource);
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
		return String.format("%s/xsd/%s", resourcePackage, resource);
	}

}
