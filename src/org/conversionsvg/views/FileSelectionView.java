package org.conversionsvg.views;

import java.awt.Dimension;
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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.TreePath;

import net.sf.fstreem.FileSystemTreeModel;
import net.sf.fstreem.FileSystemTreeNode;

import org.conversionsvg.ConversionSvgApplication;
import org.conversionsvg.Inkscape;
import org.conversionsvg.InkscapeCommandBuilder;
import org.conversionsvg.gui.MainWindow;
import org.conversionsvg.gui.filters.SVGFilter;
import org.conversionsvg.models.DomainModel;
import org.conversionsvg.models.FileSelectionModel;

import com.jidesoft.hints.FileIntelliHints;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.SelectAllUtils;

/**
 * The file selection view presents the file system to the user to allow them to
 * select the SVG files to convert using the application.
 * 
 * @author Erich Schroeter
 */
@SuppressWarnings("serial")
public class FileSelectionView extends DomainView {
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.MainWindow", Locale.getDefault());

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

	/**
	 * Constructs a default <code>FileSelectionView</code> initializing the
	 * components.
	 */
	public FileSelectionView(ConversionSvgApplication application) {
		super(application);
		init();
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
			tree.setModel(new FileSystemTreeModel(root, MainWindow.filters));
			getApplication().getPreferences().put(
					ConversionSvgApplication.KEY_LAST_ROOT,
					rootDirectory.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void init() {
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

		setMinimumSize(new Dimension(200, 500));
		setPreferredSize(new Dimension(400, 600));
		setLayout(new GridBagLayout());

		// add the output directory options
		// - same directory as each file
		// - a single directory
		sameOutputDirectoryRadio = new JRadioButton(i18ln
				.getString("SameDirectoryRadioButton"));
		singleOutputDirectoryRadio = new JRadioButton(i18ln
				.getString("SingleDirectoryRadioButton"));
		ButtonGroup outputDirectoryGroup = new ButtonGroup();
		outputDirectoryGroup.add(singleOutputDirectoryRadio);
		outputDirectoryGroup.add(sameOutputDirectoryRadio);
		sameOutputDirectoryRadio.setSelected(true);
		// singleOutputDirectoryRadio.addChangeListener(singleDirChangeListener());
		changeRootButton = new JButton("...");
		// changeRootButton.addActionListener(changeRootActionListener());

		GridBagConstraints c;
		c = new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		add(changeRootButton, c);
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		add(rootDirectoryTextField, c);
		c = new GridBagConstraints(0, 1, 2, 1, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		add(fileSelectScrollPane, c);
		c = new GridBagConstraints(0, 2, 2, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		add(sameOutputDirectoryRadio, c);
		c = new GridBagConstraints(0, 3, 2, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		add(singleOutputDirectoryRadio, c);
		c = new GridBagConstraints(0, 4, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		outputDirectoryTextField = new JTextField();
		// outputDirectoryTextField.getDocument().addDocumentListener(
		// outputDirDocumentListener());
		add(outputDirectoryTextField, c);
		c = new GridBagConstraints(1, 4, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		outputDirectoryEllipse = new JButton("...");
		// outputDirectoryEllipse.addActionListener(outputDirActionListener());
		add(outputDirectoryEllipse, c);

		sameOutputDirectoryRadio.setEnabled(true);
		// setOutputDirectorEnabled(false);
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

	/**
	 * Updates the {@link FileSelectionModel} with the following Inkscape
	 * options:
	 * <ul>
	 * <li>{@link Inkscape.Option#SVG_FILE}</li>
	 * </ul>
	 * <p>
	 * The following list shows the possible values stored in the options map
	 * for the <code>Inkscape.Option.SVG_FILE</code> key based on the selected
	 * files in the <code>CheckboxTree</code>:
	 * <ul>
	 * <li>A <code>List&#60;File&#62;</code> with the selected files</li>
	 * </ul>
	 */
	@Override
	public void updateModel(DomainModel model) {
		InkscapeCommandBuilder command = ((FileSelectionModel) model)
				.getCommand();
		command.setFiles(getFiles());
	}
}
