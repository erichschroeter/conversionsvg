package conversion.ui.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import conversion.ui.MainWindowController;
import conversion.ui.filters.InkscapeFilter;

public class PreferencesPanel extends JPanel {

	private static final long serialVersionUID = 4669623582499515980L;
	private static PreferencesPanel instance = null;
	private Properties props;
	
	JTextField inkscapeField;

	protected PreferencesPanel(Properties props) {
		this.props = props;
		init(props);
	}

	public static PreferencesPanel getInstance(Properties props) {
		if (instance == null) {
			instance = new PreferencesPanel(props);
		}
		return instance;
	}
	
	private void init(Properties props) {
		setVisible(false);
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints;

		// Inkscape binary location
		JButton inkscapeButton = new JButton("Inkscape");
		inkscapeButton.setToolTipText("The location of the Inkscape executable on your system");
		inkscapeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser inkscapeChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				inkscapeChooser.setFileFilter(new InkscapeFilter());
				inkscapeChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (inkscapeChooser.showOpenDialog(SettingsDialog.parent) == JFileChooser.APPROVE_OPTION)
				{
					setInkscapePath(inkscapeChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(inkscapeButton, constraints);
		constraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(inkscapeField = new JTextField(20), constraints);
		inkscapeField.setText(props.getProperty(MainWindowController.KEY_INKSCAPE_PATH) != null ? props.getProperty(MainWindowController.KEY_INKSCAPE_PATH) : null);
		
		// Core and Maximum Threads in ThreadPool
		constraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(SettingsDialog.createLabeledField("Core Threads", "The number of threads guaranteed to be in the thread pool"), constraints);
		constraints = new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(SettingsDialog.createLabeledField("Max Threads", "The maximum number of threads allowed in the thread pool"), constraints);
		
	}
	
	public void setInkscapePath(String path) {
		props.setProperty(MainWindowController.KEY_INKSCAPE_PATH, path);
	}
}
