package org.conversionsvg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.List;
import java.util.Vector;

import org.conversionsvg.InkscapeCommand.ExportArea;
import org.conversionsvg.util.Helpers;
import org.conversionsvg.util.SVGUtils;

public class InkscapeCommandBuilder {

	/** The list of options to pass on the command line to Inkscape */
	private List<String> options;
	/** The list of arguments to pass on the command line to Inkscape */
	private List<File> files;
	/**
	 * The directory to export all files to. If <code>null</code> all files will
	 * be exported to their existing directory.
	 */
	private File singleDirectory;
	/** The Inkscape command to build. */
	private InkscapeCommand command;

	/**
	 * Constructs a default <code>InkscapeCommandBuilder</code> to handle
	 * building the command to send to Inkscape on the command line.
	 */
	public InkscapeCommandBuilder() {
		setOptions(new Vector<String>());
		setFiles(new Vector<File>());
	}

	/**
	 * Returns the files currently on the command line to be converted.
	 * 
	 * @return the files to be converted
	 */
	public List<File> getFiles() {
		return files;
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
	 * Returns the options currently on the command line.
	 * <p>
	 * The exported file option values are set in this method. If the
	 * {@link #singleDirectory} is <code>null</code> then files are exported to
	 * their existing directory, otherwise they are exported to the
	 * <code>singleDirectory</code>.
	 * 
	 * @return the Inkscape options
	 */
	public List<String> getOptions(File file) {
		// set the option values for the export file types
		// if the singleDirectory is null then export to same directory,
		// otherwise export to the singleDirectory
		if (options.contains(Inkscape.OPTION_EXPORT_PNG)) {
			options.add(options.indexOf(Inkscape.OPTION_EXPORT_PNG) + 1,
					Helpers.changeExtension(
							singleDirectory != null ? Helpers.changePath(file,
									singleDirectory) : file, "png")
							.getAbsolutePath());
		}
		if (options.contains(Inkscape.OPTION_EXPORT_PDF)) {
			options.add(options.indexOf(Inkscape.OPTION_EXPORT_PDF) + 1,
					Helpers.changeExtension(
							singleDirectory != null ? Helpers.changePath(file,
									singleDirectory) : file, "pdf")
							.getAbsolutePath());
		}
		if (options.contains(Inkscape.OPTION_EXPORT_PS)) {
			options.add(options.indexOf(Inkscape.OPTION_EXPORT_PS) + 1, Helpers
					.changeExtension(
							singleDirectory != null ? Helpers.changePath(file,
									singleDirectory) : file, "ps")
					.getAbsolutePath());
		}
		if (options.contains(Inkscape.OPTION_EXPORT_EPS)) {
			options.add(options.indexOf(Inkscape.OPTION_EXPORT_EPS) + 1,
					Helpers.changeExtension(
							singleDirectory != null ? Helpers.changePath(file,
									singleDirectory) : file, "eps")
							.getAbsolutePath());
		}
		return options;
	}

	/**
	 * Sets the options to pass on the command line to Inkscape.
	 * <p>
	 * This will replace the container of options currently configured to be
	 * passed to Inkscape. In order to add options use the various methods
	 * available in this builder.
	 * 
	 * @param options
	 *            the options to set
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}

	/**
	 * Returns the arguments to pass on the command line to Inkscape. This
	 * constructs a new list adding the options and file in that order.
	 * <p>
	 * Inkscape only allows one file to be converted at a given time. This
	 * simply creates a <code>List&#60;String&#62;</code> and adding all
	 * <code>getOptions()</code> and the <code>file.getAbsolutePath()</code>.
	 * 
	 * @return the command arguments
	 */
	public List<String> getArgs(File file) {
		List<String> list = new Vector<String>(getOptions(file));
		list.add(file.getAbsolutePath());
		return list;
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
		boolean exists = getFiles().contains(file.getAbsolutePath());
		if (!exists) {
			getFiles().add(file);
		} else {
			int index = getFiles().indexOf(file);
			getFiles().remove(index);
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

	public InkscapeCommandBuilder exportArea(ExportArea area, Point origin,
			Dimension size) {
		command.setExportArea(area, origin, size);
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
	 * @param x
	 *            originating x coordinate
	 * @param y
	 *            originating y coordinate
	 * @param width
	 *            number of width pixels from the originating point
	 * @param height
	 *            number of height pixels from the originating point
	 * @return
	 */
	public InkscapeCommandBuilder useCustomArea(int x, int y, int width,
			int height) {
		return useCustomArea(new Point(x, y), new Point(x + width, y + height));
	}

	/**
	 * Adds the option and specified argument value to the command line. If the
	 * custom area argument already exists in command it and its value are
	 * removed from the command line.
	 * <p>
	 * (0, 0) is the lower-left corner of the image.
	 * 
	 * @param first
	 *            beginning point coordinate
	 * @param second
	 *            ending point coordinate
	 * @return the command builder
	 */
	public InkscapeCommandBuilder useCustomArea(Point first, Point second) {
		boolean exists = options.contains(Inkscape.OPTION_AREA_CUSTOM);
		if (!exists) {
			StringBuilder area = new StringBuilder();
			area.append(first.x).append(':');
			area.append(first.y).append(':');
			area.append(second.x).append(':');
			area.append(second.y);
			options.add(Inkscape.OPTION_AREA_CUSTOM);
			options.add(area.toString());
		} else {
			int index = options.indexOf(Inkscape.OPTION_AREA_CUSTOM);
			options.remove(index + 1);
			options.remove(index);
		}
		return this;
	}

	/**
	 * Adds the option flag to the command line to use the page area when
	 * exporting the image.
	 * <p>
	 * This is equivalent to calling <code>usePageArea(true)</code>
	 * 
	 * @see #usePageArea(boolean)
	 * @return the command builder
	 */
	public InkscapeCommandBuilder usePageArea() {
		return usePageArea(true);
	}

	/**
	 * Adds the option flag to the command line to use the page area when
	 * exporting the image.
	 * 
	 * @param enable
	 *            <code>true</code> to add to command line, or
	 *            <code>false</code> to remove from command line
	 * @return the command builder
	 */
	public InkscapeCommandBuilder usePageArea(boolean enable) {
		boolean exists = options.contains(Inkscape.OPTION_AREA_PAGE);
		if (enable && !exists) {
			options.add(Inkscape.OPTION_AREA_PAGE);
		} else if (!enable && exists) {
			options.remove(Inkscape.OPTION_AREA_PAGE);
		}
		return this;
	}

	/**
	 * Adds the option flag to the command line to use the drawing area when
	 * exporting the image.
	 * <p>
	 * This is equivalent to calling <code>useDrawingArea(true)</code>
	 * 
	 * @see #useDrawingArea(boolean)
	 * @return the command builder
	 */
	public InkscapeCommandBuilder useDrawingArea() {
		return useDrawingArea(true);
	}

	/**
	 * Adds the option flag to the command line to use the drawing area when
	 * exporting the image.
	 * 
	 * @param enable
	 *            <code>true</code> to add to command line, or
	 *            <code>false</code> to remove from command line
	 * @return the command builder
	 */
	public InkscapeCommandBuilder useDrawingArea(boolean enable) {
		boolean exists = options.contains(Inkscape.OPTION_AREA_DRAWING);
		if (enable && !exists) {
			options.add(Inkscape.OPTION_AREA_DRAWING);
		} else if (!enable && exists) {
			options.remove(Inkscape.OPTION_AREA_DRAWING);
		}
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
	public InkscapeCommandBuilder setPixelWidth(int pixels) {
		if (pixels < 0) {
			pixels = Math.abs(pixels);
		}
		boolean exists = options.contains(Inkscape.OPTION_PIXEL_WIDTH);
		if (!exists) {
			options.add(Inkscape.OPTION_PIXEL_WIDTH);
			options.add(Integer.toString(pixels));
		} else {
			int index = options.indexOf(Inkscape.OPTION_PIXEL_WIDTH);
			options.remove(index + 1);
			options.remove(index);
		}
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
	public InkscapeCommandBuilder setPixelHeight(int pixels) {
		if (pixels < 0) {
			pixels = Math.abs(pixels);
		}
		boolean exists = options.contains(Inkscape.OPTION_PIXEL_HEIGHT);
		if (!exists) {
			options.add(Inkscape.OPTION_PIXEL_HEIGHT);
			options.add(Integer.toString(pixels));
		} else {
			int index = options.indexOf(Inkscape.OPTION_PIXEL_HEIGHT);
			options.remove(index + 1);
			options.remove(index);
		}
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
		boolean exists = options.contains(Inkscape.OPTION_BACKGROUND_COLOR);
		if (color != null && !exists) {
			options.add(Inkscape.OPTION_BACKGROUND_COLOR);
			options.add(SVGUtils.toSvgFormatString(color));
		} else if (color == null && exists) {
			int index = options.indexOf(Inkscape.OPTION_BACKGROUND_COLOR);
			options.remove(index + 1);
			options.remove(index);
		}
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
		if (opacity < 0.0) {
			opacity = 0.0;
		}
		if (opacity > 1.0) {
			opacity = 1.0;
		}
		int value = (int) (255 * opacity);
		boolean exists = options.contains(Inkscape.OPTION_BACKGROUND_OPACITY);
		if (!exists) {
			options.add(Inkscape.OPTION_BACKGROUND_OPACITY);
			options.add(Double.toString(value));
		} else {
			int index = options.indexOf(Inkscape.OPTION_BACKGROUND_OPACITY);
			options.remove(index + 1);
			options.remove(index);
		}
		return this;
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
		singleDirectory = directory;
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Portable Network Graphics (PNG)</em>
	 * <p>
	 * This is equivalent to calling <code>exportAsPng(true)</code>.
	 * 
	 * @see #exportAsPng(boolean)
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPng() {
		return exportAsPng(true);
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Portable Network Graphics (PNG)</em>
	 * 
	 * @param enable
	 *            <code>true</code> to add to command line, or
	 *            <code>false</code> to remove from command line
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPng(boolean enable) {
		boolean exists = options.contains(Inkscape.OPTION_EXPORT_PNG);
		if (enable && !exists) {
			options.add(Inkscape.OPTION_EXPORT_PNG);
		} else if (!enable && exists) {
			options.remove(Inkscape.OPTION_EXPORT_PNG);
		}
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as <em>(PS)</em>
	 * <p>
	 * This is equivalent to calling <code>exportAsPs(true)</code>.
	 * 
	 * @see #exportAsPs(boolean)
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPs() {
		return exportAsPs(true);
	}

	/**
	 * Adds the option flag to the command line to export image as <em>(PS)</em>
	 * 
	 * @param enable
	 *            <code>true</code> to add to command line, or
	 *            <code>false</code> to remove from command line
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPs(boolean enable) {
		boolean exists = options.contains(Inkscape.OPTION_EXPORT_PS);
		if (enable && !exists) {
			options.add(Inkscape.OPTION_EXPORT_PS);
		} else if (!enable && exists) {
			options.remove(Inkscape.OPTION_EXPORT_PS);
		}
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Printable Document Format (PDF)</em>
	 * <p>
	 * This is equivalent to calling <code>exportAsPdf(true)</code>.
	 * 
	 * @see #exportAsPdf(boolean)
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPdf() {
		return exportAsPdf(true);
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>Printable Document Format (PDF)</em>
	 * 
	 * @param enable
	 *            <code>true</code> to add to command line, or
	 *            <code>false</code> to remove from command line
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsPdf(boolean enable) {
		boolean exists = options.contains(Inkscape.OPTION_EXPORT_PDF);
		if (enable && !exists) {
			options.add(Inkscape.OPTION_EXPORT_PDF);
		} else if (!enable && exists) {
			options.remove(Inkscape.OPTION_EXPORT_PDF);
		}
		return this;
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>(EPS)</em>
	 * <p>
	 * This is equivalent to calling <code>exportAsEps(true)</code>.
	 * 
	 * @see #exportAsEps(boolean)
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsEps() {
		return exportAsEps(true);
	}

	/**
	 * Adds the option flag to the command line to export image as
	 * <em>(EPS)</em>
	 * 
	 * @param enable
	 *            <code>true</code> to add to command line, or
	 *            <code>false</code> to remove from command line
	 * @return the command builder
	 */
	public InkscapeCommandBuilder exportAsEps(boolean enable) {
		boolean exists = options.contains(Inkscape.OPTION_EXPORT_EPS);
		if (enable && !exists) {
			options.add(Inkscape.OPTION_EXPORT_EPS);
		} else if (!enable && exists) {
			options.remove(Inkscape.OPTION_EXPORT_EPS);
		}
		return this;
	}

	/**
	 * Returns the customized Inkscape command as a list of strings. This is the
	 * full command that is sent on the command line which includes:
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
	 * @return list of strings
	 */
	public List<String> getCommand(File file) {
		if (Inkscape.getExecutable() == null) {
			Inkscape.setExecutable(Inkscape.findExecutable());
		}
		List<String> command = getArgs(file);
		((Vector<String>) command).insertElementAt(Inkscape.getExecutable()
				.getAbsolutePath(), 0);
		return command;
	}

	/**
	 * Returns a list of commands consisting of one command per
	 * <code>File</code>. This iterates over the builder's <code>File</code>
	 * container calling {@link #getCommand(File)}.
	 * 
	 * @see #getCommand(File)
	 * @return a list of commands
	 */
	public List<List<String>> getCommands() {
		List<List<String>> commands = new Vector<List<String>>();
		for (File file : getFiles()) {
			if (file.isFile()) {
				commands.add(getCommand(file));
			}
		}
		return commands;
	}

	public String toString(File file) {
		StringBuilder builder = new StringBuilder();
		for (String arg : getCommand(file)) {
			builder.append(arg);
			builder.append(" ");
		}
		return builder.toString();
	}

	/**
	 * Returns a string representation of the Inkscape command. The files may
	 * not be represented by the result string as they are not added until the
	 * last minute.
	 * 
	 * @return the Inkscape command
	 */
	@Override
	public String toString() {
		return command.toString();
	}

}
