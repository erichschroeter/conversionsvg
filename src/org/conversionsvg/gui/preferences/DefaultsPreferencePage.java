package org.conversionsvg.gui.preferences;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bric.swing.ColorPicker;

import org.conversionsvg.gui.ConversionSVG;
import org.conversionsvg.util.Helpers;
import org.jpreferences.IPreferenceManager;
import org.jpreferences.storage.IPreferenceStore;
import org.jpreferences.ui.PreferencePage;

@SuppressWarnings("serial")
public class DefaultsPreferencePage extends PreferencePage {

	/**
	 * Represents whether any setting on this page has changed.
	 * <p>
	 * This value will be <code>True</code> if a value has changed, otherwise
	 * <code>False</code>.
	 * <p>
	 */
	private boolean dirty;

	JTextField widthTextField;
	JTextField heightTextField;
	JTextField colorTextField;
	JTextField opacityTextField;

	/**
	 * The color last selected from a <code>ColorPicker</code>.
	 * <p>
	 * The default is <code>Color.WHITE</code>
	 * </p>
	 */
	Color selectedColor = Color.WHITE;

	/**
	 * Creates a <code>DefaultsPreferencePage</code> object specifying the
	 * <i>title</i>, and <i>description</i>.
	 * 
	 * @param manager
	 *            The preference manager which manages the preferences being
	 *            displayed on this <code>PreferencePage</code>
	 * @param title
	 *            The title. If value is <code>null</code> the <i>title</i>
	 *            attribute is set to empty string ( <code>""</code>)
	 * @param description
	 *            A description of the page. If value is <code>null</code> the
	 *            <i>description</i> attribute is set to empty string (
	 *            <code>""</code>)
	 */
	public DefaultsPreferencePage(IPreferenceManager manager, String title,
			String description) {
		super(manager, title, description);
		createContents();
	}

	/**
	 * Initializes the fields on this page with the values in the given store.
	 * <p>
	 * The <code>PreferencePage</code> knows which preferences to look for. If a
	 * preference does not exist in the store, a default value is used.
	 * </p>
	 * 
	 * @param store
	 *            the store with the initial values
	 */
	public void setInitialValues(IPreferenceStore store) {
		String value;
		widthTextField
				.setText((value = store
						.read(ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH)) != null ? value
						: store
								.getDefault(ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH));
		heightTextField
				.setText((value = store
						.read(ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT)) != null ? value
						: store
								.getDefault(ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT));
		colorTextField
				.setText((value = store
						.read(ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR)) != null ? value
						: store
								.getDefault(ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR));
	}

	/**
	 * @return an <code>ActionListener</code> which handles displaying a
	 *         <code>ColorPicker</code>. The selected color is set as the value
	 *         of {@link ConversionSVG#KEY_BACKGROUND_COLOR} and
	 *         {@link ConversionSVG#KEY_BACKGROUND_OPACITY}.
	 */
	private ActionListener colorActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((selectedColor = ColorPicker.showDialog((Dialog) null,
						selectedColor != null ? selectedColor : Color.WHITE,
						true)) != null) {
					colorTextField.setText(Helpers.toRGB(selectedColor, false));
					opacityTextField.setText(Integer.toString(selectedColor
							.getAlpha()));
				}
			}
		};
	}

	//
	// PreferencePage members
	//

	@Override
	public void createContents() {
		setLayout(new GridBagLayout());

		GridBagConstraints c;

		//
		// Size Panel
		//
		JPanel sizePanel = new JPanel(new GridBagLayout());
		sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));

		// Height
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel heightLabel = new JLabel("Height");
		heightLabel.setToolTipText("The height in pixels");
		sizePanel.add(heightLabel, c);
		c = new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		heightTextField = new JTextField();
		sizePanel.add(heightTextField, c);

		// Width
		c = new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel widthLabel = new JLabel("Width");
		widthLabel.setToolTipText("The width in pixels");
		sizePanel.add(widthLabel, c);
		c = new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		widthTextField = new JTextField();
		sizePanel.add(widthTextField, c);

		//
		// Background
		//
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		backgroundPanel.setBorder(BorderFactory
				.createTitledBorder("Background"));

		// Background Color
		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel colorLabel = new JLabel("Color");
		colorLabel.setToolTipText("The background color (including opacity)");
		backgroundPanel.add(colorLabel, c);

		c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);
		JButton colorButton = new JButton(Helpers.getResizableIconFromURL(
				"res/images/colorpicker.png", new Dimension(16, 16)));
		colorButton.setMargin(new Insets(0, 0, 0, 0));
		colorButton.setMaximumSize(new Dimension(16, 16));
		colorButton.addActionListener(colorActionListener());
		backgroundPanel.add(colorButton, c);

		c = new GridBagConstraints(2, 0, 1, 1, 0.8, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		colorTextField = new JTextField();
		backgroundPanel.add(colorTextField, c);

		c = new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		opacityTextField = new JTextField();
		backgroundPanel.add(opacityTextField, c);

		//
		// add all Panels
		//
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(sizePanel, c);
		c = new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(backgroundPanel, c);
	}

	@Override
	public boolean okToLeave() {
		return !dirty;
	}

	@Override
	public boolean performCancel() {
		// TODO implement setting state back before modifications
		return true;
	}

	@Override
	public boolean performOk() {
		manager.getStore().create(ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH,
				widthTextField.getText());
		manager.getStore().create(ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT,
				heightTextField.getText());
		manager.getStore().create(ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR,
				colorTextField.getText());
		return true;
	}

	@Override
	public boolean performDefault() {
		widthTextField.setText(manager.getStore().getDefault(
				ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH));
		heightTextField.setText(manager.getStore().getDefault(
				ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT));
		colorTextField.setText(manager.getStore().getDefault(
				ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR));
		return true;
	}

}
