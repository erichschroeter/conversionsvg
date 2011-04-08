package conversion.ui.settings;

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

import org.prefs.IPreferencePageContainer;
import org.prefs.IPreferenceStore;
import org.prefs.PreferencePage;

import conversion.ui.MainWindowController;
import conversion.ui.filters.InkscapeFilter;

public class AdvancedPreferencePage extends PreferencePage {

	private static final long serialVersionUID = -5609495319656181951L;

	/**
	 * The page container holding this <code>PreferencePage</code>.
	 */
	private IPreferencePageContainer container;

	/**
	 * Represents whether any setting on this page has changed.
	 * <p>
	 * This value will be <code>True</code> if a value has changed, otherwise
	 * <code>False</code>.
	 * <p>
	 */
	private boolean dirty;

	JTextField inkscapeField;
	JTextField coreThreadsField;
	JTextField maxThreadsField;

	public AdvancedPreferencePage(String title, String description) {
		super(title, description);
		createContents();
	}

	/**
	 * Creates a <code>AdvancedPreferencePage</code> object specifying the
	 * <i>title</i>, and <i>description</i>.
	 * 
	 * @param container
	 *            The container in which this <code>PreferencePage</code> is
	 *            being held.
	 * @param title
	 *            The title. If value is <code>null</code> the <i>title</i>
	 *            attribute is set to empty string ( <code>""</code>)
	 * @param description
	 *            A description of the page. If value is <code>null</code> the
	 *            <i>description</i> attribute is set to empty string (
	 *            <code>""</code>)
	 */
	public AdvancedPreferencePage(IPreferencePageContainer container,
			String title, String description) {
		super(title, description);
		setPageContainer(container);
		createContents();
	}

	public void setInkscapePath() {
		JFileChooser inkscapeChooser = new JFileChooser(FileSystemView
				.getFileSystemView().getHomeDirectory());
		inkscapeChooser.setFileFilter(new InkscapeFilter());
		inkscapeChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (inkscapeChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			// container.getPreferenceStore().setV
		}
	}

	/**
	 * Populates the fields on the page with the existing values stored in the
	 * <i>container</i>'s <code>PreferenceStore</code>.
	 * <p>
	 * Calls <code>super.setVisible(visible)</code>
	 * </p>
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		inkscapeField.setText(container.getPreferenceStore().getValue(
				MainWindowController.KEY_INKSCAPE_PATH));
		coreThreadsField.setText(container.getPreferenceStore().getValue(
				MainWindowController.KEY_CORE_POOL_SIZE));
		maxThreadsField.setText(container.getPreferenceStore().getValue(
				MainWindowController.KEY_MAXIMUM_POOL_SIZE));
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
		inkscapeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setInkscapePath();
			}
		});
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
		// Inkscape end
		//

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
		// Threads end
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
	public void initialize(IPreferenceStore store) {
		String value;
		inkscapeField
				.setText((value = store
						.getValue(MainWindowController.KEY_INKSCAPE_PATH)) != null ? value
						: store.getDefault(MainWindowController.KEY_INKSCAPE_PATH));
		coreThreadsField
				.setText((value = store
						.getValue(MainWindowController.KEY_CORE_POOL_SIZE)) != null ? value
						: store.getDefault(MainWindowController.KEY_CORE_POOL_SIZE));
		maxThreadsField
				.setText((value = store
						.getDefault(MainWindowController.KEY_MAXIMUM_POOL_SIZE)) != null ? value
						: store.getDefault(MainWindowController.KEY_MAXIMUM_POOL_SIZE));
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
		container.getPreferenceStore()
				.setValue(MainWindowController.KEY_INKSCAPE_PATH,
						inkscapeField.getText());
		container.getPreferenceStore().setValue(
				MainWindowController.KEY_CORE_POOL_SIZE,
				coreThreadsField.getText());
		container.getPreferenceStore().setValue(
				MainWindowController.KEY_MAXIMUM_POOL_SIZE,
				maxThreadsField.getText());
		return true;
	}

	@Override
	public boolean performDefault() {
		inkscapeField.setText(container.getPreferenceStore().getDefault(
				MainWindowController.KEY_INKSCAPE_PATH));
		coreThreadsField.setText(container.getPreferenceStore().getDefault(
				MainWindowController.KEY_CORE_POOL_SIZE));
		maxThreadsField.setText(container.getPreferenceStore().getDefault(
				MainWindowController.KEY_MAXIMUM_POOL_SIZE));
		return true;
	}

	/**
	 * Sets the container in which this <code>PreferencePage</code> is being
	 * held.
	 * 
	 * @param container
	 *            The container in which this <code>PreferencePage</code> is
	 *            being held.
	 * @throws NullPointerException
	 *             if the container argument is <code>null</code>.
	 */
	public void setPageContainer(IPreferencePageContainer container)
			throws NullPointerException {
		if (container == null) {
			throw new NullPointerException("container cannot be set to null");
		}
		this.container = container;
	}

	/**
	 * Returns the container in which this <code>PreferencePage</code> is being
	 * held.
	 */
	public IPreferencePageContainer getPageContainer() {
		return container;
	}

	@Override
	public String getClassName() {
		return "AdvancedPreferencePage";
	}

}
