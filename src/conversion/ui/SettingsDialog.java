package conversion.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

import com.bric.swing.ColorPicker;

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 3236457818625822473L;
	// Panel identifiers
	public static final String PANEL_DEFAULTS = "Defaults";
	public static final String PANEL_PREFERENCES = "Preferences";
	
	static Window parent;
	
	JPanel defaultsPanel;
	JPanel preferencesPanel;
	
	public SettingsDialog(Window parent, Properties props)
	{
		super(parent, ModalityType.APPLICATION_MODAL);
		SettingsDialog.parent = parent;
		setLocationRelativeTo(parent);
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

class DefaultsPanel extends JPanel
{
	private static final long serialVersionUID = 5069100333806373036L;
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

class PreferencesPanel extends JPanel
{
	private static final long serialVersionUID = 6958329148117270930L;
	private static PreferencesPanel instance = null;

	protected PreferencesPanel(Properties props) {
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
		
		// Core and Maximum Threads in ThreadPool
		constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(SettingsDialog.createLabeledField("Inkscape", "The location of the Inkscape executable on your system"), constraints);
		constraints = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(SettingsDialog.createLabeledField("Core Threads", "The number of threads guaranteed to be in the thread pool"), constraints);
		constraints = new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		add(SettingsDialog.createLabeledField("Max Threads", "The maximum number of threads allowed in the thread pool"), constraints);
		
	}
}