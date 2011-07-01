package org.conversionsvg.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.conversionsvg.ConversionSvgApplication;
import org.conversionsvg.InkscapeCommandBuilder;
import org.conversionsvg.models.DomainModel;
import org.conversionsvg.models.SizeModel;

@SuppressWarnings("serial")
public class SizeView extends DomainView {
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.MainWindow", Locale.getDefault());

	/** Used to specify the height of the image. */
	private JTextField heightTextField;
	/** Used to specify the width of the image. */
	private JTextField widthTextField;

	/**
	 * Constructs a default <code>SizeView</code> initializing the components.
	 */
	public SizeView(ConversionSvgApplication application) {
		super(application);
		init();
	}

	@Override
	protected void init() {
		setLayout(new GridBagLayout());

		JLabel heightLabel = new JLabel(i18ln.getString("HeightTextField"));
		heightLabel.setToolTipText("Height of the exported image");
		JLabel widthLabel = new JLabel(i18ln.getString("WidthTextField"));
		widthLabel.setToolTipText("Width of the exported image");

		heightTextField = new JTextField(5);
		// heightTextField.addKeyListener(new NumberKeyAdapter(this));
		heightTextField.setBorder(BorderFactory.createLoweredBevelBorder());
		widthTextField = new JTextField(5);
		// widthTextField.addKeyListener(new NumberKeyAdapter(this));
		widthTextField.setBorder(BorderFactory.createLoweredBevelBorder());

		String[] units = { "px", "mm" };
		JComboBox unitComboBox = new JComboBox(units);
		unitComboBox.setEnabled(false);

		GridBagConstraints c;

		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0);
		add(heightLabel, c);
		c = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0);
		add(widthLabel, c);
		c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(heightTextField, c);
		c = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(widthTextField, c);
		c = new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(unitComboBox, c);
		// and a spacer to make everything anchor to the EAST
		c = new GridBagConstraints(3, 0, 1, 2, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(new JPanel(), c);

		setBorder(BorderFactory
				.createTitledBorder(i18ln.getString("SizePanel")));

	}

	@Override
	public void updateModel(DomainModel model) {
		try {
			int width = Math.abs(Integer.parseInt(widthTextField.getText()));
			int height = Math.abs(Integer.parseInt(heightTextField.getText()));
			InkscapeCommandBuilder command = ((SizeModel) model).getCommand();
			command.setPixelWidth(width);
			command.setPixelHeight(height);
		} catch (NumberFormatException e) {
			// do nothing or notify user via a banner
		}
	}

}
