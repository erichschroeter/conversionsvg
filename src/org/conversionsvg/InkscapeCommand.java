package org.conversionsvg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.List;
import java.util.Vector;

import org.conversionsvg.util.Helpers;

/**
 * An <code>InkscapeCommand</code> encapsulates the options and arguments passed
 * on the command line to Inkscape. This class serves as a model that handles
 * special and boundary cases around which the Inkscape command line supports.
 * 
 * @author Erich Schroeter
 */
public class InkscapeCommand implements Cloneable {

	/** The SVG file to be exported. */
	private File svgFile;
	/**
	 * Includes the switch (and by association its argument) to export a PNG
	 * image.
	 */
	private File exportPNG;
	/**
	 * Include the switch (and by association its argument) to export a PDF
	 * image.
	 */
	private File exportPDF;
	/**
	 * Include the switch (and by association its argument) to export a PS
	 * image.
	 */
	private File exportPS;
	/**
	 * Include the switch (and by association its argument) to export a EPS
	 * image.
	 */
	private File exportEPS;
	/** The background color to export the image with. */
	private Color background;
	/** The background opacity to export the image with. */
	private double opacity;
	/**
	 * Wheter the Inkscape <code>--export-background-opacity</code> option is
	 * enabled.
	 */
	private boolean opacityEnabled;
	/** The number of pixels high to export the image. */
	private int pixelHeight;
	/** Whether the Inkscape <code>--export-height</code> option is enabled. */
	private boolean pixelHeightEnabled;
	/** The number of pixels wide to export the image. */
	private int pixelWidth;
	/** Whether the Inkscape <code>--export-width</code> option is enabled. */
	private boolean pixelWidthEnabled;

	/** The export area type. */
	private ExportArea areaType;
	/** Whether to export custom area. */
	private boolean exportCustomEnabled;
	/** The custom area width and height relative to the {@link #areaOrigin}. */
	private Dimension areaSize;
	/** The origin point for a custom area. */
	private Point areaOrigin;
	/** Whether to export the page area. */
	private boolean exportPageEnabled;
	/** Whether to export the drawing area. */
	private boolean exportDrawingEnabled;

	/** The supported areas to export the image. */
	public static enum ExportArea {
		/** Exports the entire page. */
		PAGE,
		/** Exports the entire drawing (which is not the page) */
		DRAWING,
		/** Exports a custom area specified by the user. */
		CUSTOM
	}

	/**
	 * Returns the SVG file to be exported.
	 * 
	 * @see #setSVG(File)
	 * @return the SVG file
	 */
	public File getSVG() {
		return svgFile;
	}

	/**
	 * Sets the SVG file to be used for exporting.
	 * 
	 * @param file
	 *            the SVG file
	 */
	public void setSVG(File file) {
		svgFile = file;
	}

	/**
	 * Returns whether the Inkscape <code>--export-height</code> option is
	 * enabled.
	 * <p>
	 * This is used to determine if the option should be included when getting
	 * the command line options.
	 * 
	 * @return <code>true</code> if enabled, <code>false</code> is disabled
	 */
	public boolean isHeightEnabled() {
		return pixelHeightEnabled;
	}

	/**
	 * Returns the pixel height of the image to be exported.
	 * 
	 * @see #setHeight(int)
	 * @see #setHeight(int, boolean)
	 * @return the pixel height
	 */
	public int getHeight() {
		return pixelHeight;
	}

	/**
	 * Sets the height of the exported image. If <code>pixels</code> is less
	 * than 0, the Inkscape <code>--export-height</code> option is not enabled.
	 * <p>
	 * This is equivalent to <code>setHeight(pixels, true)</code>.
	 * 
	 * @see #setHeight(int, boolean)
	 * @param pixels
	 *            the pixel height
	 */
	public void setHeight(int pixels) {
		setHeight(pixels, true);
	}

	/**
	 * Sets the height of the exported image. If <code>pixels</code> is less
	 * than 0, the Inkscape <code>--export-height</code> option is not enabled.
	 * 
	 * @param pixels
	 *            the pixel height
	 * @param enable
	 *            <code>true</code> to enable to option, <code>false</code> to
	 *            disable the option
	 */
	public void setHeight(int pixels, boolean enable) {
		if (pixels < 0) {
			pixels = 0;
			enable = false;
		}
		pixelHeight = pixels;
		pixelHeightEnabled = enable;
	}

	/**
	 * Returns whether the Inkscape <code>--export-width</code> option is
	 * enabled.
	 * <p>
	 * This is used to determine if the option should be included when getting
	 * the command line options.
	 * 
	 * @return <code>true</code> if enabled, <code>false</code> is disabled
	 */
	public boolean isWidthEnabled() {
		return pixelWidthEnabled;
	}

	/**
	 * Returns the pixel width of the image to be exported.
	 * 
	 * @see #setWidth(int)
	 * @see #setWidth(int, boolean)
	 * @return the pixel width
	 */
	public int getWidth() {
		return pixelWidth;
	}

	/**
	 * Sets the width of the exported image. If <code>pixels</code> is less than
	 * 0, the Inkscape <code>--export-width</code> option is not enabled.
	 * <p>
	 * This is equivalent to <code>setWidth(pixels, true)</code>.
	 * 
	 * @see #setWidth(int, boolean)
	 * @param pixels
	 *            the pixel width
	 */
	public void setWidth(int pixels) {
		setWidth(pixels, true);
	}

	/**
	 * Sets the width of the exported image. If <code>pixels</code> is less than
	 * 0, the Inkscape <code>--export-width</code> option is not enabled.
	 * 
	 * @param pixels
	 *            the pixel width
	 * @param enable
	 *            <code>true</code> to enable to option, <code>false</code> to
	 *            disable the option
	 */
	public void setWidth(int pixels, boolean enable) {
		if (pixels < 0) {
			pixels = 0;
			enable = false;
		}
		pixelWidth = pixels;
		pixelWidthEnabled = enable;
	}

	/**
	 * Returns whether the image page area will be exported.
	 * 
	 * @return <code>true</code> if enabled, else <code>false</code>
	 */
	public boolean isExportPageEnabled() {
		return exportPageEnabled;
	}

	/**
	 * Sets the area of the image to export to as the page.
	 * <p>
	 * This is equivalent to
	 * <code>setExportArea(ExportArea.PAGE, null, null)</code>.
	 */
	public void exportPage(boolean enable) {
		exportPageEnabled = enable;
		// only change the areaType if enabled
		if (exportPageEnabled) {
			areaType = ExportArea.PAGE;
		}
	}

	/**
	 * Returns whether the image drawing area will be exported.
	 * 
	 * @return <code>true</code> if enabled, else <code>false</code>
	 */
	public boolean isExportDrawingEnabled() {
		return exportDrawingEnabled;
	}

	/**
	 * Sets the area of the image to export to as the drawing.
	 * <p>
	 * This is equivalent to
	 * <code>setExportArea(ExportArea.DRAWING, null, null)</code>.
	 */
	public void exportDrawing(boolean enable) {
		exportDrawingEnabled = enable;
		// only change the areaType if enabled
		if (exportDrawingEnabled) {
			areaType = ExportArea.DRAWING;
		}
	}

	/**
	 * Returns whether the image custom area will be exported.
	 * 
	 * @return <code>true</code> if enabled, else <code>false</code>
	 */
	public boolean isExportCustomEnabled() {
		return exportCustomEnabled;
	}

	/**
	 * Sets the custom area of the image to export.
	 * <p>
	 * If <code>size</code> is <code>null</code> it is equivalent to setting
	 * <code>enable</code> to <code>false</code>.
	 * 
	 * @param origin
	 *            the lower left corner of the <code>size</code>
	 * @param size
	 *            the height and width in pixel area relative to
	 *            <code>point</code>
	 * @param enable
	 *            <code>true</code> to enable, <code>false</code> to disable
	 */
	public void exportCustom(Point origin, Dimension size, boolean enable) {
		if (size == null) {
			exportCustomEnabled = false;
		}
		if (origin == null) {
			origin = new Point(0, 0);
		}
		exportCustomEnabled = enable;
		areaOrigin = origin;
		areaSize = size;
		// only change the areaType if enabled
		if (exportCustomEnabled) {
			areaType = ExportArea.CUSTOM;
		}
	}

	/**
	 * Returns whether the Inkscape <code>--export-background</code> options is
	 * enabled or disabled.
	 * <p>
	 * This is equivalent to <code>background != null</code>.
	 * 
	 * @see #setBackground(double)
	 * @return <code>true</code> if enabled, <code>false</code> is disabled
	 */
	public boolean isBackgroundEnabled() {
		return background != null;
	}

	/**
	 * Returns the background color to be exported.
	 * 
	 * @see #setBackgroundColor(Color)
	 * @return the background color
	 */
	public Color getBackgroundColor() {
		return background;
	}

	/**
	 * Sets the background color of the exported image. This does not set the
	 * opacity.
	 * <p>
	 * If <code>color</code> is <code>null</code> the Inkscape
	 * <code>--export-background</code> option will not be included on the
	 * command line. This effectively disables the option.
	 * 
	 * @see #setBackgroundOpacity(double)
	 * @see #isBackgroundEnabled()
	 * @param color
	 *            the background color
	 */
	public void setBackgroundColor(Color color) {
		background = color;
	}

	/**
	 * Returns whether the Inkscape <code>--export-background-opacity</code>
	 * options is enabled or disabled.
	 * 
	 * @see #setBackgroundOpacity(double, boolean)
	 * @return <code>true</code> if enabled, <code>false</code> is disabled
	 */
	public boolean isBackgroundOpacityEnabled() {
		return opacityEnabled;
	}

	/**
	 * Returns the background opacity of the image to be exported.
	 * 
	 * @see #setBackgroundOpacity(double)
	 * @see #setBackgroundOpacity(double, boolean)
	 * @return the background opacity
	 */
	public double getBackgroundOpacity() {
		return opacity;
	}

	/**
	 * Sets the background color opacity of the exported image. If
	 * <code>opacity</code> is less than 0.0 it is forced to 0.0, and if greater
	 * than 1.0 it is forced to 1.0.
	 * <p>
	 * This is equivalent to <code>setBackgroundOpacity(opacity)</code>.
	 * 
	 * @see #setBackgroundColor(Color)
	 * @see #isBackgroundOpacityEnabled()
	 * @param opacity
	 *            the background color opacity
	 */
	public void setBackgroundOpacity(double opacity) {
		setBackgroundOpacity(opacity, true);
	}

	/**
	 * Sets the background color opacity of the exported image. If
	 * <code>opacity</code> is less than 0.0 it is forced to 0.0, and if greater
	 * than 1.0 it is forced to 1.0.
	 * <p>
	 * If <code>enable</code> is <code>false</code> the Inkscape
	 * <code>--export-background-opacity</code> option will not be included on
	 * the command line.
	 * 
	 * @see #setBackgroundColor(Color)
	 * @see #isBackgroundOpacityEnabled()
	 * @param opacity
	 *            the background color opacity
	 * @param enable
	 *            <code>true</code> to enable to option, <code>false</code> to
	 *            disable the option
	 */
	public void setBackgroundOpacity(double opacity, boolean enable) {
		if (opacity < 0.0) {
			opacity = 0.0;
		}
		if (opacity > 1.0) {
			opacity = 1.0;
		}
		this.opacity = opacity;
		this.opacityEnabled = enable;
	}

	/**
	 * Returns the PNG file to be exported.
	 * 
	 * @see #isPNG()
	 * @see #setPNG(File)
	 * @return the PNG file
	 */
	public File getPNG() {
		return exportPNG;
	}

	/**
	 * Sets the file to be exported as a PNG. By setting <code>file</code> to a
	 * non-<code>null</code> value the switch for exporting a PNG image is
	 * automatically included.
	 * 
	 * @param file
	 *            the PNG file to create
	 */
	public void setPNG(File file) {
		exportPNG = file;
	}

	/**
	 * Returns whether a PNG image will be exported.
	 * <p>
	 * This is equivalent to <code>exportPNG != null</code>.
	 * 
	 * @return <code>true</code> if PNG file will be exported, else
	 *         <code>false</code>
	 */
	public boolean isPNG() {
		return exportPNG != null;
	}

	/**
	 * Returns the PDF file to be exported.
	 * 
	 * @see #isPDF()
	 * @see #setPDF(File)
	 * @return the PDF file
	 */
	public File getPDF() {
		return exportPDF;
	}

	/**
	 * Sets the file to be exported as a PDF. By setting <code>file</code> to a
	 * non-<code>null</code> value the switch for exporting a PDF image is
	 * automatically included.
	 * 
	 * @param file
	 *            the PDF file to create
	 */
	public void setPDF(File file) {
		exportPDF = file;
	}

	/**
	 * Returns whether a PDF image will be exported.
	 * <p>
	 * This is equivalent to <code>exportPDF != null</code>.
	 * 
	 * @return <code>true</code> if PDF file will be exported, else
	 *         <code>false</code>
	 */
	public boolean isPDF() {
		return exportPDF != null;
	}

	/**
	 * Returns the PS file to be exported.
	 * 
	 * @see #isPS()
	 * @see #setPS(File)
	 * @return the PS file
	 */
	public File getPS() {
		return exportPS;
	}

	/**
	 * Sets the file to be exported as a PS. By setting <code>file</code> to a
	 * non-<code>null</code> value the switch for exporting a PS image is
	 * automatically included.
	 * 
	 * @param file
	 *            the PS file to create
	 */
	public void setPS(File file) {
		exportPS = file;
	}

	/**
	 * Returns whether a PS image will be exported.
	 * <p>
	 * This is equivalent to <code>exportPS != null</code>.
	 * 
	 * @return <code>true</code> if PS file will be exported, else
	 *         <code>false</code>
	 */
	public boolean isPS() {
		return exportPS != null;
	}

	/**
	 * Returns the EPS file to be exported.
	 * 
	 * @see #isEPS()
	 * @see #setEPS(File)
	 * @return the EPS file
	 */
	public File getEPS() {
		return exportEPS;
	}

	/**
	 * Sets the file to be exported as a EPS. By setting <code>file</code> to a
	 * non-<code>null</code> value the switch for exporting a EPS image is
	 * automatically included.
	 * 
	 * @param file
	 *            the EPS file to create
	 */
	public void setEPS(File file) {
		exportEPS = file;
	}

	/**
	 * Returns whether a EPS image will be exported.
	 * <p>
	 * This is equivalent to <code>exportEPS != null</code>.
	 * 
	 * @return <code>true</code> if EPS file will be exported, else
	 *         <code>false</code>
	 */
	public boolean isEPS() {
		return exportEPS != null;
	}

	/**
	 * Returns the Inkscape options that may be passed on the command line.
	 * 
	 * @return the Inkscape options and values
	 * @exception IllegalStateException
	 *                <ul>
	 *                <li>if the Inkscape executable is <code>null</code> and
	 *                could not be found</li>
	 *                <li>{@link #areaType} is <code>null</code> or an
	 *                unsupported value (which may just need to be implemented)</li>
	 *                </ul>
	 */
	public List<String> getOptions() {
		List<String> options = new Vector<String>();

		if (isPNG()) {
			options.add(Inkscape.OPTION_EXPORT_PNG);
			options.add(exportPNG.getAbsolutePath());
		}
		if (isPDF()) {
			options.add(Inkscape.OPTION_EXPORT_PDF);
			options.add(exportPDF.getAbsolutePath());
		}
		if (isPS()) {
			options.add(Inkscape.OPTION_EXPORT_PS);
			options.add(exportPS.getAbsolutePath());
		}
		if (isEPS()) {
			options.add(Inkscape.OPTION_EXPORT_EPS);
			options.add(exportEPS.getAbsolutePath());
		}

		if (areaType != null) {
			switch (areaType) {
			case PAGE:
				if (isExportPageEnabled()) {
					options.add(Inkscape.OPTION_AREA_PAGE);
				}
				break;
			case DRAWING:
				if (isExportDrawingEnabled()) {
					options.add(Inkscape.OPTION_AREA_DRAWING);
				}
				break;
			case CUSTOM:
				if (isExportCustomEnabled()) {
					options.add(Inkscape.OPTION_AREA_CUSTOM);
					StringBuilder custom = new StringBuilder();
					custom.append(areaOrigin.x);
					custom.append(':');
					custom.append(areaOrigin.y);
					custom.append(':');
					custom.append(areaOrigin.x + areaSize.width);
					custom.append(':');
					custom.append(areaOrigin.y + areaSize.height);
					options.add(custom.toString());
				}
				break;
			default:
				throw new UnsupportedOperationException(
						"export area not supported");
			}
		}
		if (isHeightEnabled()) {
			options.add(Inkscape.OPTION_PIXEL_HEIGHT);
			options.add(Integer.toString(pixelHeight));
		}
		if (isWidthEnabled()) {
			options.add(Inkscape.OPTION_PIXEL_WIDTH);
			options.add(Integer.toString(pixelWidth));
		}

		if (isBackgroundEnabled()) {
			options.add(Inkscape.OPTION_BACKGROUND_COLOR);
			options.add(Helpers.toRGB(background, false));
		}
		if (isBackgroundOpacityEnabled()) {
			options.add(Inkscape.OPTION_BACKGROUND_OPACITY);
			options.add(Double.toString(opacity));
		}

		if (svgFile != null) {
			options.add(svgFile.getAbsolutePath());
		}

		return options;
	}

	public List<String> getCommand() {
		List<String> command = getOptions();
		if (Inkscape.getExecutable() == null) {
			Inkscape.setExecutable(Inkscape.findExecutable());
			if (Inkscape.getExecutable() == null) {
				throw new IllegalStateException(
						"Inkscape executable could not be found");
			}
		}
		command.add(0, Inkscape.getExecutable().getAbsolutePath());
		return command;
	}

	@Override
	public String toString() {
		List<String> options = getCommand();
		StringBuilder b = new StringBuilder();
		for (String option : options) {
			if (b.length() != 0) {
				b.append(" ");
			}
			b.append(option);
		}
		return b.toString();
	}

	@Override
	public InkscapeCommand clone() throws CloneNotSupportedException {
		InkscapeCommand clone = new InkscapeCommand();
		clone.setPNG(getPNG());
		clone.setPDF(getPDF());
		clone.setPS(getPS());
		clone.setEPS(getEPS());

		if (areaType != null) {
			switch (areaType) {
			case PAGE:
				clone.exportPage(isExportPageEnabled());
				break;
			case DRAWING:
				clone.exportDrawing(isExportDrawingEnabled());
				break;
			case CUSTOM:
				clone.exportCustom(new Point(areaOrigin), new Dimension(
						areaSize), isExportCustomEnabled());
				break;
			default:
				break;
			}
		}
		clone.setHeight(getHeight(), isHeightEnabled());
		clone.setWidth(getWidth(), isWidthEnabled());
		clone.setBackgroundColor(getBackgroundColor());
		clone.setBackgroundOpacity(getBackgroundOpacity(),
				isBackgroundOpacityEnabled());
		clone.setSVG(getSVG());
		return clone;
	}

}
