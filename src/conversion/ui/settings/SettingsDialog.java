package conversion.ui.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import conversion.ui.MainWindowController;

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 3236457818625822473L;
	static String APPLICATION_DIR = ".conversion-svg";
	
	// Panel identifiers
	public static final String PANEL_DEFAULTS = "Defaults";
	public static final String PANEL_PREFERENCES = "Preferences";
	
	static Window parent;
	Properties props;
	
	JPanel defaultsPanel;
	JPanel preferencesPanel;
	
	public SettingsDialog(Window parent, Properties props)
	{
		super(parent, ModalityType.APPLICATION_MODAL);
		SettingsDialog.parent = parent;
		setLocationRelativeTo(parent);
		this.props = props;
		init(props);
	}
	
	public SettingsDialog(Window parent)
	{
		this(parent, null);
	}

	public SettingsDialog(Properties props)
	{
		this(null, props);
	}
	
	private void init(Properties props)
	{
		setModal(true);
		setTitle("Settings Dialog");
		setLayout(new GridBagLayout());
		
		String[] categories_list = {PANEL_DEFAULTS, PANEL_PREFERENCES};
		JList categories = new JList(categories_list);
		categories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		categories.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		categories.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				show((String) list.getSelectedValue());
			}
		});
		JScrollPane categoriesScrollPane = new JScrollPane(categories);
		
		// default values (i.e. opacity level, background color, width, height, etc)
		defaultsPanel = DefaultsPanel.getInstance(props);
		// user preferences (i.e. root directory, default save directory, # of threads, etc)
		preferencesPanel = PreferencesPanel.getInstance(props);
		
		// pile all the settings panels on top of each other and only display one
		JPanel rightPanel = new JPanel();
		rightPanel.add(defaultsPanel);
		rightPanel.add(preferencesPanel);
		
		// create the dialog
		GridBagConstraints constraints;
		constraints = new GridBagConstraints(0, 0, 1, 1, 0, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0);
		add(categoriesScrollPane, constraints);
		constraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
		add(rightPanel, constraints);

		// perform the selection after everything has been created to avoid NullPointerException
		categories.setSelectedIndex(0);
		
		// set up listener to save settings when the window is closed
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) { }
			
			@Override
			public void windowIconified(WindowEvent e) { }
			
			@Override
			public void windowDeiconified(WindowEvent e) { }
			
			@Override
			public void windowDeactivated(WindowEvent e) { }
			
			@Override
			public void windowClosing(WindowEvent e) {
				saveSettings();
			}
			
			@Override
			public void windowClosed(WindowEvent e) { }
			
			@Override
			public void windowActivated(WindowEvent e) { }
		});
		
		pack();
	}
	
	public void show(String panel)
	{
		if (panel.equals(PANEL_DEFAULTS)) {
			defaultsPanel.setVisible(true);
			preferencesPanel.setVisible(false);
		} else if (panel.equals(PANEL_PREFERENCES)) {
			defaultsPanel.setVisible(false);
			preferencesPanel.setVisible(true);
		}
		pack();
	}
	
	public void saveSettings() {
		MainWindowController.saveSettings(props);
	}
	
    /**
     * Returns the user's settings directory located in their home directory.
     * If the directory does not exist, it is created.
     * @author McDowell from http://stackoverflow.com/questions/193474/how-to-create-an-ini-file-to-store-some-settings-in-java
     * @return The settings directory
     */
    public static File getSettingsDirectory() {
        String userHome = System.getProperty("user.home");
        if(userHome == null) {
            throw new IllegalStateException("user.home == null");
        }
        File home = new File(userHome);
        File settingsDirectory = new File(home, APPLICATION_DIR);
        if(!settingsDirectory.exists()) {
            if(!settingsDirectory.mkdir()) {
                throw new IllegalStateException(settingsDirectory.toString());
            }
        }
        return settingsDirectory;
    }

	// Helper function for creating a labeled text field
	public static JPanel createLabeledField(String title)
	{
		return createLabeledField(title, null);
	}
	
	// Helper function for creating a labeled text field	
	public static JPanel createLabeledField(String title, String tooltip)
	{
		JPanel container = new JPanel(new GridBagLayout());
		JTextField textField = new JTextField();
		textField.setColumns(5);
		JLabel label = new JLabel(title);
		label.setToolTipText(tooltip != null ? tooltip : "");
		
		GridBagConstraints constraints;
		constraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		container.add(label, constraints);
		constraints = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		container.add(textField, constraints);
		
		return container;	
	}
}
