package org.conversionsvg.gui.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.prefs.IPreferenceStore;
import org.prefs.PreferenceManager;
import org.prefs.PreferencePage;

import org.conversionsvg.gui.ConversionSVG;
import org.conversionsvg.gui.filters.InkscapeFilter;

public class AdvancedPreferencePage extends PreferencePage {

	private static final long serialVersionUID = -5609495319656181951L;

	/**
	 * Represents whether any setting on this page has changed.
	 * <p>
	 * This value will be <code>True</code> if a value has changed, otherwise
	 * <code>False</code>.
	 * <p>
	 */
	private boolean dirty;

	/**
	 * The manager of the preferences being displayed on this page.
	 */
	private PreferenceManager manager;

	private JTextField inkscapeField;
	private JTextField coreThreadsField;
	private JTextField maxThreadsField;

	/**
	 * Creates a <code>AdvancedPreferencePage</code> object specifying the
	 * <i>manager</i>, <i>title</i>, and <i>description</i>.
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
	public AdvancedPreferencePage(PreferenceManager manager, String title,
			String description) {
		super(manager, title, description);
		createContents();
	}

	/**
	 * Populates the fields on the page with the existing values stored in the
	 * <i>manager</i>'s <code>PreferenceStore</code>.
	 * <p>
	 * Calls <code>super.setVisible(visible)</code>
	 * </p>
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		inkscapeField.setText(manager.getStore().getValue(
				ConversionSVG.KEY_INKSCAPE_PATH));
		coreThreadsField.setText(manager.getStore().getValue(
				ConversionSVG.KEY_CORE_POOL_SIZE));
		maxThreadsField.setText(manager.getStore().getValue(
				ConversionSVG.KEY_MAXIMUM_POOL_SIZE));
	}

	/**
	 * Returns the <code>PreferenceManager</code> managing the preferences that
	 * are displayed on this <code>PreferencePage</code>.
	 * 
	 * @return the manager
	 */
	public PreferenceManager getManager() {
		return manager;
	}

	/**
	 * Sets the <code>PreferenceManager</code> managing the preferences that are
	 * displayed on this <code>PreferencePage</code>.
	 * 
	 * @param manager
	 *            the manager to set
	 */
	public void setManager(PreferenceManager manager) {
		this.manager = manager;
	}

	/**
	 * Initializes the fields on this page with the values in the given store.
	 * <p>
	 * The <code>PreferencePage</code> knows which preferences to look
	 * for. If a preference does not exist in the store, a default value is
	 * used.
	 * </p>
	 * 
	 * @param store
	 *            the store with the initial values
	 */
	public void setInitialValues(IPreferenceStore store) {
		String value;
		inkscapeField.setText((value = store
				.getValue(ConversionSVG.KEY_INKSCAPE_PATH)) != null ? value
				: store.getDefault(ConversionSVG.KEY_INKSCAPE_PATH));
		coreThreadsField.setText((value = store
				.getValue(ConversionSVG.KEY_CORE_POOL_SIZE)) != null ? value
				: store.getDefault(ConversionSVG.KEY_CORE_POOL_SIZE));
		maxThreadsField
				.setText((value = store
						.getDefault(ConversionSVG.KEY_MAXIMUM_POOL_SIZE)) != null ? value
						: store.getDefault(ConversionSVG.KEY_MAXIMUM_POOL_SIZE));
	}

	/**
	 * Returns this <code>AdvancedPreferencePage</code> instance.
	 * <p>
	 * This is intended to be used by the <code>private</code> functions which
	 * return listeners for the graphical components.
	 * </p>
	 * 
	 * @return this instance
	 */
	private AdvancedPreferencePage getInstance() {
		return this;
	}

	/**
	 * @return an <code>ActionListener</code> which handles displaying a
	 *         <code>JFileChooser</code> with an
	 *         <code>{@link InkscapeFilter}</code>. The selected file is set as
	 *         the value of {@link ConversionSVG#KEY_INKSCAPE_PATH}.
	 */
	private ActionListener inkscapePathActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser inkscapeChooser = new JFileChooser(FileSystemView
						.getFileSystemView().getHomeDirectory());
				inkscapeChooser.setFileFilter(new InkscapeFilter());
				inkscapeChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (inkscapeChooser.showOpenDialog(getInstance()) == JFileChooser.APPROVE_OPTION) {
					manager.getStore().setValue(
							ConversionSVG.KEY_INKSCAPE_PATH,
							inkscapeChooser.getSelectedFile());
				}
			}
		};
	}

	//
	// PreferencePage members
	//

	@Override
	protected void createContents() {
		setLayout(new GridBagLayout());

		GridBagConstraints c;

		//
		// Inkscape
		//
		JPanel inkscapePanel = new JPanel(new GridBagLayout());
		inkscapePanel.setBorder(BorderFactory.createTitledBorder("Inkscape"));

		// Inkscape path
		JButton inkscapeButton = new JButton("Inkscape");
		inkscapeButton
				.setToolTipText("The absolute path of the Inkscape executable on your system");
		inkscapeButton.addActionListener(inkscapePathActionListener());
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		inkscapePanel.add(inkscapeButton, c);

		c = new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		inkscapeField = new JTextField(20);
		inkscapePanel.add(inkscapeField, c);

		//
		// Threads & ThreadPool
		//
		JPanel threadsPanel = new JPanel(new GridBagLayout());
		threadsPanel.setBorder(BorderFactory.createTitledBorder("Threading"));

		// Core Threads
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel coreThreadsLabel = new JLabel("Core Threads");
		coreThreadsLabel
				.setToolTipText("The number of threads guaranteed to be in the thread pool");
		threadsPanel.add(coreThreadsLabel, c);

		c = new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		coreThreadsField = new JTextField();
		threadsPanel.add(coreThreadsField, c);

		// Max Threads
		c = new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel maxThreadsLabel = new JLabel("Max Threads");
		maxThreadsLabel
				.setToolTipText("The maximum number of threads allowed in the thread pool");
		threadsPanel.add(maxThreadsLabel, c);

		c = new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		maxThreadsField = new JTextField();
		threadsPanel.add(maxThreadsField, c);

		//
		// add all Panels
		//
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(inkscapePanel, c);
		c = new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(threadsPanel, c);
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
		manager.getStore().setValue(ConversionSVG.KEY_INKSCAPE_PATH,
				inkscapeField.getText());
		manager.getStore().setValue(ConversionSVG.KEY_CORE_POOL_SIZE,
				coreThreadsField.getText());
		manager.getStore().setValue(ConversionSVG.KEY_MAXIMUM_POOL_SIZE,
				maxThreadsField.getText());
		return true;
	}

	@Override
	public boolean performDefault() {
		inkscapeField.setText(manager.getStore().getDefault(
				ConversionSVG.KEY_INKSCAPE_PATH));
		coreThreadsField.setText(manager.getStore().getDefault(
				ConversionSVG.KEY_CORE_POOL_SIZE));
		maxThreadsField.setText(manager.getStore().getDefault(
				ConversionSVG.KEY_MAXIMUM_POOL_SIZE));
		return true;
	}

}
