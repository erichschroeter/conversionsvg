package conversion.ui.settings;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bric.swing.ColorPicker;

public class DefaultsPanel extends JPanel implements ISettingsPanel {

	private static final long serialVersionUID = -3411002492776158974L;

	private static DefaultsPanel instance = null;
	
	Properties properties;
	
	JTextField colorTextField;
	// default color
	Color selectedColor = Color.WHITE;

	private DefaultsPanel(Properties props) {
		init(props);
	}

	/**
	 * This method provides access to the singleton instance of the DefaultsPanel.
	 * @param props The properties associated with this panel 
	 * @return The singleton instance
	 */
	public static DefaultsPanel getInstance(Properties props) {
		if (instance == null) {
			instance = new DefaultsPanel(props);
		}
		return instance;
	}
	
	/**
	 * Handles setting up the graphical interface for this panel. The Properties given
	 * are used to set the initial values for the fields.
	 * @param props The properties to use for initial values.
	 */
	private void init(Properties props) {
		setVisible(false);
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints;
		
		//
		// Size Panel
		//
		JPanel sizePanel = new JPanel(new GridBagLayout());
		sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
		
		// Height
		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		JLabel heightLabel = new JLabel("Height");
		heightLabel.setToolTipText("The height in pixels");
		sizePanel.add(heightLabel, constraints);
		constraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		// TODO set the current settings value
		JTextField heightTextField = new JTextField();
		sizePanel.add(heightTextField, constraints);
		
		// Width
		constraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		JLabel widthLabel = new JLabel("Width");
		widthLabel.setToolTipText("The width in pixels");
		sizePanel.add(widthLabel, constraints);
		constraints = new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		// TODO set the current settings value
		JTextField widthTextField = new JTextField();
		sizePanel.add(widthTextField, constraints);
		//
		// Size Panel end
		//
		
		//
		// Background
		//
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		backgroundPanel.setBorder(BorderFactory.createTitledBorder("Background"));
		
		// Background Color
		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		JLabel colorLabel = new JLabel("Color");
		colorLabel.setToolTipText("The background color (including opacity)");
		backgroundPanel.add(colorLabel, constraints);
		constraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		// TODO set the current settings value
		colorTextField = new JTextField();
		backgroundPanel.add(colorTextField, constraints);
		constraints = new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		// TODO use the color picker logo from Dropbox
		JButton colorButton = new JButton("...");
		// TODO set selectedColor to the settings value
		// this action listener handles converting the selected color w/ opacity to a
		// hex value which can then stored in the properties
		colorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((selectedColor = ColorPicker.showDialog(SettingsDialog.parent, selectedColor != null ? selectedColor : Color.WHITE, true)) != null) { 
					colorTextField.setText(Integer.toHexString(selectedColor.getRGB() & 0xffffffff));
				}
			}
		});
		backgroundPanel.add(colorButton, constraints);
		//
		// Background end
		//

		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(sizePanel, constraints);
		constraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(backgroundPanel, constraints);
	}

	//
	// ISettingsPanel interface
	//
	
	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public void setProperties(Properties props) {
		properties = props;
	}
}
