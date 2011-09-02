package org.conversionsvg.views;

import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;

import org.conversionsvg.ConversionSvgApplication;
import org.conversionsvg.InkscapeCommandBuilder;
import org.conversionsvg.models.AreaModel;
import org.conversionsvg.models.DomainModel;

import com.bric.swing.ColorPicker;

@SuppressWarnings("serial")
public class ColorView extends DomainView {
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.MainWindow", Locale.getDefault());

	/** Contains the export color option Components */
	private ColorPicker colorPicker;

	/**
	 * Constructs a default <code>ColorView</code> initializing the components.
	 */
	public ColorView(ConversionSvgApplication application) {
		super(application);
		init();
	}

	@Override
	protected void init() {
		colorPicker = new ColorPicker(true, true);
		// Color Picker
		colorPicker.setColor(Color.WHITE);
		colorPicker.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("BackgroundPanel")));
		colorPicker.setEnabled(false);
		add(colorPicker);
	}

	/**
	 * Updates the {@link ColorModel} with the following Inkscape options:
	 * <ul>
	 * <li>{@link Inkscape.Option#BACKGROUND_COLOR}</li>
	 * <li>{@link Inkscape.Option#BACKGROUND_OPACITY}</li>
	 * </ul>
	 * <p>
	 * The following list shows the possible values stored in the options map
	 * for the option keys for the options above based on the selected color
	 * components in the view:
	 * <ul>
	 * <li><code>Color</code> for color</li>
	 * <li><code>Integer</code> for the opacity</li>
	 * </ul>
	 */
	@Override
	public void updateModel(DomainModel model) {
		InkscapeCommandBuilder command = ((AreaModel) model).getCommand();
		command.setBackgroundColor(colorPicker.getColor());
		command.setBackgroundOpacity((int) colorPicker.getOpacity());
	}

}
