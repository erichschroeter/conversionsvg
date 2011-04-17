package org.conversionsvg.gui.preferences;

import java.awt.Color;
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

import org.prefs.IPreferenceStore;
import org.prefs.PreferenceManager;
import org.prefs.PreferencePage;

import com.bric.swing.ColorPicker;

import org.conversionsvg.gui.ConversionSVG;

public class DefaultsPreferencePage extends PreferencePage {

	private static final long serialVersionUID = -8539690532878618137L;

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
	public DefaultsPreferencePage(PreferenceManager manager, String title,
			String description) {
		super(manager, title, description);
		createContents();
	}

	public void showColorChooser() {
		// ColorChooser ch = new ColorChooser();
		// ch.setVisible(true);
		// if ((selectedColor = ColorPicker.showDialog(container.getWindow(),
		// selectedColor != null ? selectedColor : Color.WHITE)) != null) {
		// colorTextField
		// .setText(Integer.toHexString(selectedColor.getRGB() & 0xffffffff));
		// }
	}

	public void initialize(IPreferenceStore store) {
		String value;
		widthTextField
				.setText((value = store
						.getValue(ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH)) != null ? value
						: store
								.getDefault(ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH));
		heightTextField
				.setText((value = store
						.getValue(ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT)) != null ? value
						: store
								.getDefault(ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT));
		colorTextField
				.setText((value = store
						.getValue(ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR)) != null ? value
						: store
								.getDefault(ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR));
	}

	//
	// PreferencePage members
	//

	@Override
	protected void createContents() {
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
		// TODO set the current settings value
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
		// TODO set the current settings value
		widthTextField = new JTextField();
		sizePanel.add(widthTextField, c);
		//
		// Size Panel end
		//

		//
		// Background
		//
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		backgroundPanel.setBorder(BorderFactory
				.createTitledBorder("Background"));

		// Background Color
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel colorLabel = new JLabel("Color");
		colorLabel.setToolTipText("The background color (including opacity)");
		backgroundPanel.add(colorLabel, c);
		c = new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		// TODO set the current settings value
		colorTextField = new JTextField();
		backgroundPanel.add(colorTextField, c);
		c = new GridBagConstraints(2, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		// TODO use the color picker logo from Dropbox
		JButton colorButton = new JButton("...");
		// TODO set selectedColor to the settings value
		// this action listener handles converting the selected color w/ opacity
		// to a
		// hex value which can then stored in the properties
		colorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showColorChooser();
			}
		});
		backgroundPanel.add(colorButton, c);
		//
		// Background end
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
		manager.getStore().setValue(ConversionSVG.KEY_INKSCAPE_EXPORT_WIDTH,
				widthTextField.getText());
		manager.getStore().setValue(ConversionSVG.KEY_INKSCAPE_EXPORT_HEIGHT,
				heightTextField.getText());
		manager.getStore().setValue(ConversionSVG.KEY_INKSCAPE_EXPORT_COLOR,
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
