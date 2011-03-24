package conversion.ui.settings;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bric.swing.ColorPicker;

public class DefaultsPanel extends JPanel {

	private static final long serialVersionUID = -3411002492776158974L;

	private static DefaultsPanel instance = null;
	
	JTextField selectedColor;
	// set a default
	Color color = Color.WHITE;

	protected DefaultsPanel(Properties props) {
		init(props);
	}

	public static DefaultsPanel getInstance(Properties props) {
		if (instance == null) {
			instance = new DefaultsPanel(props);
		}
		return instance;
	}
	
	private void init(Properties props) {
		setVisible(false);
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints;
		
		// Height and Width
		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(SettingsDialog.createLabeledField("Height"), constraints);
		constraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(SettingsDialog.createLabeledField("Width"), constraints);
		
		// Background Color and Opacity
		JPanel colorPanel = new JPanel(new GridBagLayout());
		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		JButton colorButton = new JButton("Color");
		colorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((color = ColorPicker.showDialog(SettingsDialog.parent, color != null ? color : Color.WHITE, true)) != null) { 
					selectedColor.setText(Integer.toHexString(color.getRGB() & 0xffffffff));
				}
			}
		});
		colorPanel.add(colorButton, constraints);
		constraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		colorPanel.add(selectedColor = new JTextField(7), constraints);
		constraints = new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(colorPanel, constraints);
		
	}
}
