package org.conversionsvg;

import java.util.List;
import java.util.Map;

public interface HasOptions {

	/**
	 * Returns the options this object has.
	 * 
	 * @return the options
	 */
	public List<String> getOptions();

	/**
	 * Sets the options this object has.
	 * 
	 * @param options
	 *            the map of options to their values
	 */
	public void setOptions(Map<Inkscape.Option, Object> options);
}
