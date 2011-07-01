package org.conversionsvg;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import com.jidesoft.icons.IconSet.File;

@SuppressWarnings("static-access")
public interface ConversionSvgOptions {

	/**
	 * The command line switch used to set the <em>log4j</em> configuration file
	 */
	public static final char SWITCH_LOG4J = 'l';
	/**
	 * The command line options for parsing the command line for the
	 * <em>log4j</em> configuration file
	 */
	public static Option OPTION_LOG4J = OptionBuilder.withArgName("file")
			.hasArg().withType(File.class).isRequired(false).withLongOpt(
					"log4j-config").create(SWITCH_LOG4J);

	/**
	 * The command line switch used to set the location of the Inkscape
	 * executable
	 */
	public static final char SWITCH_INKSCAPE_LOCATION = 'I';
	/**
	 * The command line option used for parsing the command line for the
	 * Inkscape executable
	 */
	public static Option OPTION_INKSCAPE_LOCATION = OptionBuilder.withArgName(
			"file").hasArg().withType(File.class).isRequired(false)
			.withLongOpt("inkscape-location").create(SWITCH_INKSCAPE_LOCATION);
}
