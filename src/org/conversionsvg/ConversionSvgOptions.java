package org.conversionsvg;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import com.jidesoft.icons.IconSet.File;

@SuppressWarnings("static-access")
public interface ConversionSvgOptions {

	/**
	 * The command line switch used to set the <em>log4j</em> configuration file
	 */
	public static final char SWITCH_LOG4J = 'L';
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

	/** The unique switch used for enabling batch mode. */
	public static final String SWITCH_BATCH_MODE = "batch";
	/** The command line options used to execute the program in batch mode. */
	public static Option OPTION_BATCH_MODE = OptionBuilder.withLongOpt(SWITCH_BATCH_MODE)
			.withDescription("execute in batch mode (no GUI)").create();

}
