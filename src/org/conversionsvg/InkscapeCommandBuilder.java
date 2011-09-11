package org.conversionsvg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.conversionsvg.util.Helpers;

/**
 * The <code>InkscapeCommandBuilder</code> implements the <a
 * href="http://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>.
 * 
 * @author Erich Schroeter
 */
public class InkscapeCommandBuilder {

	/** The list of arguments to pass on the command line to Inkscape */
	private List<File> files;
	/**
	 * The directory to export all files to. If <code>null</code> all files will
	 * be exported to their existing directory.
	 */
	private File singleDirectory;
	/**
	 * Whether to export as PNG. Only used when the {@link #singleDirectory}
	 * feature is enabled.
	 */
	private boolean pngExportEnabled;
	/**
	 * Whether to export as PDF. Only used when the {@link #singleDirectory}
	 * feature is enabled.
	 */
	private boolean pdfExportEnabled;
	/**
	 * Whether to export as PS. Only used when the {@link #singleDirectory}
	 * feature is enabled.
	 */
	private boolean psExportEnabled;
	/**
	 * Whether to export as EPS. Only used when the {@link #singleDirectory}
	 * feature is enabled.
	 */
	private boolean epsExportEnabled;
	/** The Inkscape command to build. */
	private InkscapeCommand command;

	/**
	 * Constructs a default <code>InkscapeCommandBuilder</code> to handle
	 * building the command to send to Inkscape on the command line.
	 */
	public InkscapeCommandBuilder() {
		command = new InkscapeCommand();
		setFiles(new Vector<File>());
	}

	/**
	 * Returns the files currently on the command line to be converted.
	 * 
	 * @return the files to be converted
	 */
	// public List<File> getFiles() {
	// return files;
	// }

	/**
	 * Sets the files on the command line to be converted.
	 * <p>
	 * This will replace the container of files currently to be on the command
	 * line. To add file(s) to the command line use {@link #convert(File)} and
	 * {@link #convertAll(List)}.
	 * <p>
	 * This is equivalent to <code>setFiles(Arrays.asList(files))</code>.
	 * 
	 * @see #setFiles(List)
	 * @see #convert(File)
	 * @see #convertAll(List)
	 * @param files
	 *            the files to set
	 */
	public void setFiles(File... files) {
		setFiles(Arrays.asList(files));
	}

	/**
	 * Sets the files on the command line to be converted.
	 * <p>
	 * This will replace the container of files currently to be on the command
	 * line. To add file(s) to the command line use {@link #convert(File)} and
	 * {@link #convertAll(List)}.
	 * 
	 * @see #convert(File)
	 * @see #convertAll(List)
	 * @param files
	 *            the files to set
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}

	/**
	 * Adds the file's absolute path to the command line. If the file already
	 * exists in command it is removed from the command line.
	 * 
	 * @param file
	 *            the SVG file to convert
	 * @return the command builder
	 */
	public InkscapeCommandBuilder convert(File file) {
		boolean exists = files.contains(file.getAbsolutePath());
		if (!exists) {
			files.add(file);
		} else {
			int index = files.indexOf(file);
			files.remove(index);
		}
		return this;
	}

	/**
	 * Adds all the file absolute paths to the command line. If a file already
	 * exists in command it is removed from the command line.
	 * 
	 * @see #convert(File)
	 * @param files
	 *            the SVG files to convert
	 * @return the command builder
	 */
	public InkscapeCommandBuilder convertAll(List<File> files) {
		for (File svg : files) {
			convert(svg);
		}
		return this;
	}

	/**
	 * Adds the option and specified argument value to the command line. If the
	 * custom area argument already exists in command it and its value are
	 * removed from the command line.
	 * <p>
	 * (0, 0) is the lower-left corner of the image.
	 * <p>
	 * This is equivalent to calling
	 * <code>useCustomArea(new Point(x, y), new Point(x + width, y + height))</code>.
	 * 
	 * @see #exportCustom(Point, Dimension)
	 * @param x
	 *            originating x coordinate
	 * @param y
	 *            originating y coordinate
	 * @param width
	 *            number of width pixels from the originating point
	 * @param height
	 *            number of height pixels from the originating point
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportCustom(int x, int y, int width,
			int height) {
		return exportCustom(new Point(x, y), new Dimension(width, height));
	}

	public InkscapeCommandBuilder exportPage() {
		return exportPage(true);
	}

	public InkscapeCommandBuilder exportPage(boolean enable) {
		command.exportPage(enable);
		return this;
	}

	public InkscapeCommandBuilder exportDrawing() {
		return exportDrawing(true);
	}

	public InkscapeCommandBuilder exportDrawing(boolean enable) {
		command.exportDrawing(enable);
		return this;
	}

	public InkscapeCommandBuilder exportCustom(Point origin, Dimension size) {
		return exportCustom(origin, size, true);
	}

	public InkscapeCommandBuilder exportCustom(Point origin, Dimension size,
			boolean enable) {
		command.exportCustom(origin, size, true);
		return this;
	}

	/**
	 * Adds the option and specified argument value to the command line. If the
	 * width argument already exists in command it and its value are removed
	 * from the command line.
	 * <p>
	 * If <code>pixels</code> is less than zero (0) the absolute value of
	 * <code>pixels</code> is used.
	 * 
	 * @param pixels
	 *            the width of exported bitmap pixels
	 * @return the command builder
	 */
	public InkscapeCommandBuilder setWidth(int pixels) {
		command.setWidth(pixels);
		return this;
	}

	/**
	 * Adds the option and specified argument value to the command line. If the
	 * height argument already exists in command it and its value are removed
	 * from the command line.
	 * <p>
	 * If <code>pixels</code> is less than zero (0) the absolute value of
	 * <code>pixels</code> is used.
	 * 
	 * @param pixels
	 *            the height of exported bitmap pixels
	 * @return the command builder
	 */
	public InkscapeCommandBuilder setHeight(int pixels) {
		command.setHeight(pixels);
		return this;
	}

	/**
	 * Adds the option and specified argument value to the command line. If
	 * <code>color</code> is <code>null</code> the argument and its value is
	 * removed from the command line.
	 * 
	 * @param color
	 *            the background color
	 * @return the command builder
	 */
	public InkscapeCommandBuilder setBackgroundColor(Color color) {
		command.setBackgroundColor(color);
		return this;
	}

	/**
	 * Adds the option and specified argument value to the command line. If the
	 * opacity argument already exists in command it and its value are removed
	 * from the command line.
	 * <p>
	 * If <code>opacity</code> is less than zero (0.0) then <code>opacity</code>
	 * is set to zero (0.0). If <code>opacity</code> is greater than one (1.0)
	 * then <code>opacity</code> is set to one (1.0).
	 * 
	 * @param opacity
	 *            a value between 0.0 and 1.0
	 * @return the command builder
	 */
	public InkscapeCommandBuilder setBackgroundOpacity(double opacity) {
		command.setBackgroundOpacity(opacity);
		return this;
	}

	/**
	 * Returns whether the single directory feature is enabled in this command
	 * builder.
	 * <p>
	 * This feature is enabled if {@link #singleDirectory} is not
	 * <code>null</code> and exists.
	 * 
	 * @see File#exists()
	 * @return <code>true</code> if enabled, else <code>false</code>
	 */
	protected boolean isSingleDirectoryEnabled() {
		return singleDirectory != null && singleDirectory.exists();
	}

	/**
	 * Sets the directory to which all the files will be exported to. If
	 * <code>directory</code> is <code>null</code> files will be exported to the
	 * directory they are located.
	 * 
	 * @param directory
	 *            the directory to export all files to (can be <code>null</code>
	 *            )
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportToSingleDirectory(File directory) {
		return enableSingleDirectory(directory, directory != null);
	}

	/**
	 * Sets and enables the single directory feature of the
	 * <code>InkscapeCommandBuilder</code>. This feature allows you to configure
	 * an Inkscape command's options omitting the SVG file and export files
	 * (i.e. PNG, PDF, PS, EPS) allowing the builder to export files to a single
	 * directory.
	 * <p>
	 * If <code>directory</code> is <code>null</code> or does not exist, this
	 * feature will be disabled.
	 * 
	 * @param directory
	 *            the directory to export all files to
	 * @param enable
	 *            <code>true</code> to enable, <code>false</code> to disable
	 * @return the command builder
	 */
	public InkscapeCommandBuilder enableSingleDirectory(File directory,
			boolean enable) {
		if (enable) {
			singleDirectory = directory;
		} else {
			singleDirectory = null;
		}
		return this;
	}

	/**
	 * Adds the option flag and argument to the command line to export image as
	 * <em>Portable Network Graphics (PNG)</em>.
	 * 
	 * @param file
	 *            the PNG file to be exported
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPng(File file) {
		command.setPNG(file);
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Portable Network Graphics (PNG)</em>. This is only taken into effect
	 * if the {@link #singleDirectory} feature is enabled.
	 * 
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPng(boolean enable) {
		pngExportEnabled = enable;
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Post Script (PS)</em>
	 * 
	 * @param file
	 *            the PS file to be exported
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPs(File file) {
		command.setPS(file);
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Post Script (PS)</em>. This is only taken into effect if the
	 * {@link #singleDirectory} feature is enabled.
	 * 
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPs(boolean enable) {
		psExportEnabled = enable;
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Printable Document Format (PDF)</em>
	 * 
	 * @param file
	 *            the PDF file to be exported
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPdf(File file) {
		command.setPDF(file);
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Printable Document Format (PDF)</em>. This is only taken into effect
	 * if the {@link #singleDirectory} feature is enabled.
	 * 
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPdf(boolean enable) {
		pdfExportEnabled = enable;
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>(EPS)</em>
	 * 
	 * @param file
	 *            the EPS file to be exported
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsEps(File file) {
		command.setEPS(file);
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>(EPS)</em>. This is only taken into effect if the
	 * {@link #singleDirectory} feature is enabled.
	 * 
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsEps(boolean enable) {
		epsExportEnabled = enable;
		return this;
	}

	/**
	 * Returns the customized Inkscape command. This is the full command that is
	 * sent on the command line which includes:
	 * <ol>
	 * <li>the program (result of <code>Inkscape.getExecutable()</code>)</li>
	 * <li>the options (result of <code>getArgs()</code>)</li>
	 * <li>the SVG file (result of <code>file.getAbsolutePath()</code>)</li>
	 * </ol>
	 * <p>
	 * If the result of <code>Inkscape.getExecutable()</code> is
	 * <code>null</code> it attempts to find the inkscape binary by issuing
	 * <code>Inkscape.setExecutable(Inkscape.findExecutable())</code>.
	 * 
	 * @see #getArgs(File)
	 * @see Inkscape#getExecutable()
	 * @param file
	 *            the <em>Scalable Vector Graphics (SVG)</em> file
	 * @return the Inkscape command
	 */
	public InkscapeCommand getCommandFor(File file) {
		InkscapeCommand clone = null;
		try {
			clone = command.clone();
			File newFile = file;
			// change the path once instead of for each export type
			if (isSingleDirectoryEnabled()) {
				newFile = Helpers.changePath(file, singleDirectory);
			}
			if (clone.isPNG() || pngExportEnabled) {
				clone.setPNG(Helpers.changeExtension(newFile, "png"));
			}
			if (clone.isPDF() || pdfExportEnabled) {
				clone.setPDF(Helpers.changeExtension(newFile, "pdf"));
			}
			if (clone.isPS() || psExportEnabled) {
				clone.setPS(Helpers.changeExtension(newFile, "ps"));
			}
			if (clone.isEPS() || epsExportEnabled) {
				clone.setEPS(Helpers.changeExtension(newFile, "eps"));
			}
			// only add the SVG file if at least one export was selected
			if (clone.isPNG() || clone.isPDF() || clone.isPS() || clone.isEPS()) {
				clone.setSVG(file);
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

	/**
	 * Returns a list of Inkscape commands consisting of one command per
	 * <code>File</code>. This iterates over the builder's <code>File</code>
	 * container calling {@link #getCommandFor(File)}.
	 * 
	 * @see #getCommandFor(File)
	 * @return a list of commands
	 */
	public List<InkscapeCommand> getCommands() {
		List<InkscapeCommand> commands = new Vector<InkscapeCommand>();
		for (File file : files) {
			if (file.isFile()) {
				commands.add(getCommandFor(file));
			}
		}
		return commands;
	}

	/**
	 * Returns a string representation of the Inkscape command.
	 * 
	 * @return the Inkscape command
	 */
	@Override
	public String toString() {
		return command.toString();
	}

}
