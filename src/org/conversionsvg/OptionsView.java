package org.conversionsvg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.TreePath;

import net.sf.fstreem.FileSystemTreeModel;
import net.sf.fstreem.FileSystemTreeNode;

import org.conversionsvg.gui.filters.SVGFilter;
import org.javamvc.GUIApplication;
import org.javamvc.View;

import com.bric.swing.ColorPicker;
import com.jidesoft.hints.FileIntelliHints;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.SelectAllUtils;

/**
 * The <code>OptionsView</code> is a view in a
 * <em>Model View Controller (MVC)</em> framework. This view displays the
 * options available to the user to customize Inkscape arguments sent over the
 * command line.
 * 
 * @author Erich Schroeter
 */
@SuppressWarnings("serial")
public class OptionsView extends View {

	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.MainWindow", Locale.getDefault());

	/** The split pane to hold the components for this view. */
	private JSplitPane splitPane;

	/** Used to export the image drawing area */
	private JRadioButton drawingRadioButton;
	/** Used to export the image page area */
	private JRadioButton pageRadioButton;
	/** Contains the export color option Components */
	private ColorPicker colorPicker;
	/** Used to export image as a PNG */
	private JCheckBox pngCheckBox;
	/** Used to export image as a PS */
	private JCheckBox psCheckBox;
	/** Used to export image as a EPS */
	private JCheckBox epsCheckBox;
	/** Used to export image as a PDF */
	private JCheckBox pdfCheckBox;
	/** Displays available directories and files to select for conversion. */
	private CheckBoxTree fileHeirarchy;
	/** Opens a {@link JFileChooser} dialog to select the root directory. */
	private JButton changeRootButton;
	/** Used to export files in a single directory. */
	private JRadioButton singleOutputDirectoryRadio;
	/** Used to export files to the same directory as the source file. */
	private JRadioButton sameOutputDirectoryRadio;
	/** The root directory of the file system tree */
	private File rootDirectory;
	/** Displays the root directory for the file tree hierarchy. */
	private JTextField rootDirectoryTextField;
	/** Displays the output directory for converted files. */
	private JTextField outputDirectoryTextField;
	/** Opens a {@link JFileChooser} dialog to select the output directory */
	private JButton outputDirectoryEllipse;
	/** Used to initialize {@link #filters}. */
	private static final FileFilter[] heirarchy_filters = { new SVGFilter() };
	/**
	 * These filters filter what <i>WILL</i> be displayed (meaning if it doesn't
	 * match one of these filters, then it won't be shown)
	 */
	public static final Vector<FileFilter> filters = new Vector<FileFilter>(
			Arrays.asList(heirarchy_filters));

	/**
	 * Constructs a default <code>OptionsView</code>.
	 */
	public OptionsView(GUIApplication app) {
		super(app);
	}

	@Override
	protected void initializeView() {
		super.initializeView();
		setLayout(new BorderLayout());
		setName("views.options");

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel left = new JPanel(new GridBagLayout());
		splitPane.setLeftComponent(left);

		// install the different panels that make up this view
		// these are split up into separate methods for readability
		installFormat(left);
		installArea(left);
		installSize(left);
		installColor(left);
		installFileSelection();

		add(splitPane, BorderLayout.CENTER);
	}

	protected void installFormat(JPanel panel) {
		JPanel p = new JPanel();
		GroupLayout layout = new GroupLayout(p);

		pngCheckBox = new JCheckBox(".PNG", true);
		psCheckBox = new JCheckBox(".PS");
		epsCheckBox = new JCheckBox(".EPS");
		pdfCheckBox = new JCheckBox(".PDF");

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pngCheckBox).addComponent(pdfCheckBox))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								epsCheckBox).addComponent(psCheckBox)));
		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pngCheckBox).addComponent(epsCheckBox))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								pdfCheckBox).addComponent(psCheckBox)));
		p.setLayout(layout);
		p.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("FormatPanel")));

		p.add(epsCheckBox);
		p.add(pngCheckBox);
		p.add(psCheckBox);
		p.add(pdfCheckBox);

		GridBagConstraints c;
		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(p, c);
	}

	protected void installArea(JPanel panel) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

		drawingRadioButton = new JRadioButton(i18ln
				.getString("DrawingRadioButton"), true);
		drawingRadioButton
				.setToolTipText("Exported area is the entire drawing (not page)");
		drawingRadioButton.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				((ConversionSvgApplication) getApplication())
						.getInkscapeCommand().useDrawingArea(
								drawingRadioButton.isSelected());
			}
		});
		pageRadioButton = new JRadioButton(i18ln.getString("PageRadioButton"));
		pageRadioButton.setToolTipText("Exported area is the entire page");
		pageRadioButton.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				((ConversionSvgApplication) getApplication())
						.getInkscapeCommand().useDrawingArea(
								pageRadioButton.isSelected());
			}
		});
		pageRadioButton.setSelected(true);

		ButtonGroup group = new ButtonGroup();
		group.add(pageRadioButton);
		group.add(drawingRadioButton);

		p.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("ExportAreaPanel")));

		p.add(drawingRadioButton);
		p.add(pageRadioButton);

		GridBagConstraints c;
		c = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(p, c);
	}

	protected void installSize(JPanel panel) {
		JPanel p = new JPanel(new GridBagLayout());

		JLabel heightLabel = new JLabel(i18ln.getString("HeightTextField"));
		heightLabel.setToolTipText("Height of the exported image");
		JLabel widthLabel = new JLabel(i18ln.getString("WidthTextField"));
		widthLabel.setToolTipText("Width of the exported image");

		JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(16, 1,
				Integer.MAX_VALUE, 1));
		heightSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				((ConversionSvgApplication) getApplication())
						.getInkscapeCommand()
						.setPixelHeight(
								(Integer) ((JSpinner) e.getSource()).getValue());
			}
		});
		JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(16, 1,
				Integer.MAX_VALUE, 1));
		widthSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				((ConversionSvgApplication) getApplication())
						.getInkscapeCommand()
						.setPixelWidth(
								(Integer) ((JSpinner) e.getSource()).getValue());
			}
		});

		String[] units = { "px", "mm" };
		JComboBox unitComboBox = new JComboBox(units);
		unitComboBox.setEnabled(false);

		p.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("SizePanel")));
		GridBagConstraints c;
		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0);
		p.add(heightLabel, c);
		c = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0);
		p.add(widthLabel, c);
		c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		p.add(heightSpinner, c);
		c = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		p.add(widthSpinner, c);
		c = new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		p.add(unitComboBox, c);
		// and a spacer to make everything anchor to the EAST
		c = new GridBagConstraints(3, 0, 1, 2, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		p.add(new JPanel(), c);

		c = new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(p, c);
	}

	protected void installColor(JPanel panel) {
		GridBagConstraints c;
		c = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		colorPicker = new ColorPicker(true, true);
		// Color Picker
		colorPicker.setColor(Color.WHITE);
		colorPicker.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("BackgroundPanel")));
		colorPicker.setEnabled(false);

		panel.add(colorPicker, c);
	}

	protected void installFileSelection() {

		/** Used to initialize {@link #filters}. */
		final FileFilter[] heirarchy_filters = { new SVGFilter() };
		/**
		 * These filters filter what <i>WILL</i> be displayed (meaning if it
		 * doesn't match one of these filters, then it won't be shown)
		 */
		final Vector<FileFilter> filters = new Vector<FileFilter>(Arrays
				.asList(heirarchy_filters));
		/** The root directory for the {@link #fileHeirarchy}. */
		rootDirectory = new File(System.getProperty("user.home"));

		// Tree Hierarchy
		FileSystemTreeModel model = new FileSystemTreeModel(rootDirectory,
				filters);
		fileHeirarchy = new CheckBoxTree(model);
		JScrollPane fileSelectScrollPane = new JScrollPane(fileHeirarchy);

		// implement FileIntelliHints
		rootDirectoryTextField = new JTextField();
		rootDirectoryTextField.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
						updateRoot(e);
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						updateRoot(e);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						updateRoot(e);
					}

					private void updateRoot(DocumentEvent e) {
						try {
							String path = e.getDocument().getText(0,
									e.getDocument().getLength());
							File newRoot = new File(path);
							if (newRoot.exists() && newRoot.isDirectory()) {
								rootDirectory = newRoot;
								changeRootDirectory(fileHeirarchy,
										rootDirectory);
							}
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				});
		FileIntelliHints rootIntellisense = new FileIntelliHints(
				rootDirectoryTextField);
		rootIntellisense.setFolderOnly(true);
		SelectAllUtils.install(rootDirectoryTextField);

		// add the output directory options
		// - same directory as each file
		// - a single directory
		sameOutputDirectoryRadio = new JRadioButton(i18ln
				.getString("SameDirectoryRadioButton"), true);
		singleOutputDirectoryRadio = new JRadioButton(i18ln
				.getString("SingleDirectoryRadioButton"));
		ButtonGroup outputDirectoryGroup = new ButtonGroup();
		outputDirectoryGroup.add(singleOutputDirectoryRadio);
		outputDirectoryGroup.add(sameOutputDirectoryRadio);
		// singleOutputDirectoryRadio.addChangeListener(singleDirChangeListener());
		changeRootButton = new JButton("...");
		// changeRootButton.addActionListener(changeRootActionListener());
		outputDirectoryTextField = new JTextField();
		outputDirectoryTextField.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
						updateRoot(e);
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						updateRoot(e);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						updateRoot(e);
					}

					private void updateRoot(DocumentEvent e) {
						try {
							String path = e.getDocument().getText(0,
									e.getDocument().getLength());
							File newLocation = new File(path);
							if (newLocation.exists()
									&& newLocation.isDirectory()) {
								outputDirectoryTextField.setText(newLocation
										.getAbsolutePath());
							}
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				});
		FileIntelliHints outputDirectoryIntellisense = new FileIntelliHints(
				outputDirectoryTextField);
		outputDirectoryIntellisense.setFolderOnly(true);
		outputDirectoryEllipse = new JButton("...");
		// outputDirectoryEllipse.addActionListener(outputDirActionListener());

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setMinimumSize(new Dimension(200, 500));
		panel.setPreferredSize(new Dimension(400, 600));
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c;
		c = new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(changeRootButton, c);
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(rootDirectoryTextField, c);
		c = new GridBagConstraints(0, 1, 2, 1, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(fileSelectScrollPane, c);
		c = new GridBagConstraints(0, 2, 2, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(sameOutputDirectoryRadio, c);
		c = new GridBagConstraints(0, 3, 2, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(singleOutputDirectoryRadio, c);
		c = new GridBagConstraints(0, 4, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(outputDirectoryTextField, c);
		c = new GridBagConstraints(1, 4, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		panel.add(outputDirectoryEllipse, c);

		// add to split pane
		c = new GridBagConstraints(1, 0, 1, 4, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		splitPane.setRightComponent(panel);
	}

	/**
	 * Changes the root of the file hierarchy.
	 * <p>
	 * This is useful for reducing the number of directories the user needs to
	 * expand in order to select files.
	 * 
	 * @param tree
	 *            the file system tree
	 * @param root
	 *            the new root
	 */
	public void changeRootDirectory(CheckBoxTree tree, File root) {
		try {
			tree.setModel(new FileSystemTreeModel(root, filters));
			getApplication().getApplicationPreferences().put(
					ConversionSvgApplication.KEY_LAST_ROOT,
					rootDirectory.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aggregates all selected files in the {@link #fileHeirarchy} and returns a
	 * list of selected files.
	 * 
	 * @return list of selected files
	 */
	public List<File> getFiles() {
		List<File> files = new Vector<File>();
		TreePath[] selectedTreePaths = fileHeirarchy
				.getCheckBoxTreeSelectionModel().getSelectionPaths();

		if (selectedTreePaths != null) {
			for (TreePath selection : selectedTreePaths) {
				FileSystemTreeNode node = (FileSystemTreeNode) selection
						.getLastPathComponent();
				File file = node.getFile();

				files.addAll(recursivelyGetFiles(file, new SVGFilter()));
			}
		}
		return files;
	}

	/**
	 * Returns a list of files that satisfy the <code>filter</code>. This
	 * recursively traverses the <code>directory</code> adding files to the list
	 * to be returned that satisfy the <code>filter</code>.
	 * 
	 * @param directory
	 *            the directory to recursively traverse
	 * @param filter
	 *            the file filter to use
	 * @return a list of files that satisfy the <code>filter</code> under the
	 *         <code>directory</code>
	 */
	public List<File> recursivelyGetFiles(File directory, FileFilter filter) {
		List<File> files = new Vector<File>();
		if (directory.isDirectory()) {
			File[] list = directory.listFiles(filter);
			for (File f : list) {
				if (f.isDirectory()) {
					files.addAll(recursivelyGetFiles(f, filter));
				} else {
					files.add(f);
				}
			}
		} else {
			if (filter.accept(directory)) {
				files.add(directory);
			}
		}
		return files;
	}

}
