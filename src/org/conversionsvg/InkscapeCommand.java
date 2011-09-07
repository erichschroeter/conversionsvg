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
public class InkscapeCommand {

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
	/** The custom area width and height relative to the {@link #areaOrigin}. */
	private Dimension areaSize;
	/** The origin point for a custom area. */
	private Point areaOrigin;

	/** The supported areas to export the image. */
	public static enum ExportArea {
		/** Exports the entire page. */
		PAGE,
		/** Exports the entire drawing (which is not the page). */
		DRAWING,
		/** Exports a custom area specified by the user. */
		CUSTOM
	}

	/**
	 * Sets the SVG file to be used for exporting.
	 * 
	 * @param file
	 *            the SVG file
	 */
	public void setSvgFile(File file) {
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
	 * Sets the area of the image to export to as the page.
	 * <p>
	 * This is equivalent to
	 * <code>setExportArea(ExportArea.PAGE, null, null)</code>.
	 */
	public void exportPage() {
		setExportArea(ExportArea.PAGE, null, null);
	}

	/**
	 * Sets the area of the image to export to as the drawing.
	 * <p>
	 * This is equivalent to
	 * <code>setExportArea(ExportArea.DRAWING, null, null)</code>.
	 */
	public void exportDrawing() {
		setExportArea(ExportArea.DRAWING, null, null);
	}

	/**
	 * Sets the custom area of the image to export.
	 * <p>
	 * This is equivalent to
	 * <code>setExportArea(ExportArea.CUSTOM, origin, size)</code>.
	 */
	public void exportCustom(Point origin, Dimension size) {
		setExportArea(ExportArea.CUSTOM, origin, size);
	}

	/**
	 * Sets the area of the image to export.
	 * <ul>
	 * <li>If <code>size</code> is <code>null</code> it will be equivalent to
	 * setting <code>area</code> to {@link ExportArea#PAGE}.</li>
	 * <li>If <code>lowerLeft</code> is <code>null</code> it is equivalent to
	 * (0,0)</li>
	 * </ul>
	 * If <code>area</code> is {@link ExportArea#PAGE} or
	 * {@link ExportArea#DRAWING} then <code>size</code> and
	 * <code>lowerleft</code> are ignored.
	 * 
	 * @param area
	 *            the area type to determine the command line option used
	 * @param origin
	 *            the lower left corner of the <code>size</code>
	 * @param size
	 *            the height and width in pixel area relative to
	 *            <code>point</code>
	 */
	public void setExportArea(ExportArea area, Point origin, Dimension size) {
		if (size == null) {
			area = ExportArea.PAGE;
		}
		if (origin == null) {
			origin = new Point(0, 0);
		}
		areaType = area;
		areaOrigin = origin;
		areaSize = size;
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
		if (Inkscape.getExecutable() == null) {
			Inkscape.setExecutable(Inkscape.findExecutable());
			if (Inkscape.getExecutable() == null) {
				throw new IllegalStateException(
						"Inkscape executable could not be found");
			}
		}
		List<String> options = new Vector<String>();

		options.add(Inkscape.getExecutable().getAbsolutePath());

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

		switch (areaType) {
		case PAGE:
			options.add(Inkscape.OPTION_AREA_PAGE);
			break;
		case DRAWING:
			options.add(Inkscape.OPTION_AREA_DRAWING);
			break;
		case CUSTOM:
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
			break;
		default:
			throw new IllegalStateException("unsupported area type <"
					+ areaType + ">");
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

	@Override
	public String toString() {
		List<String> options = getOptions();
		StringBuilder b = new StringBuilder();
		for (String option : options) {
			if (b.length() != 0) {
				b.append(" ");
			}
			b.append(option);
		}
		return b.toString();
	}
}
