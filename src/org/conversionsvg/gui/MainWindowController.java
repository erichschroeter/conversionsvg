package org.conversionsvg.gui;

import java.awt.Dialog;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.conversionsvg.util.Helpers;
import org.prefs.PreferenceDialog;
import org.prefs.PreferenceManager;

import net.sf.fstreem.FileSystemTreeModel;
import net.sf.fstreem.FileSystemTreeNode;

import com.bric.swing.ColorPicker;
import com.jidesoft.swing.CheckBoxTree;

/**
 * The MainWindowController class is somewhat self-explanatory. It is the
 * Controller in the Model View Controller (MVC) framework. It handles
 * controlling what is done based on user interaction from the MainWindow (the
 * <i>View</i> in MVC).
 * <p>
 * In addition to the MainWindow, the MainWindowController also handles
 * displaying dialogs where needed. In instances where a file to be exported
 * already exists on the system, a dialog will popup asking the user if they
 * want to overwrite it or not. Another instance where a dialog will popup is if
 * the program cannot find the Inkscape executable on the system. A file chooser
 * dialog will popup that will filter anything except the Inkscape executable
 * (file named Inkscape.exe or inkscape).
 * </p>
 * 
 * @author Erich Schroeter
 */
public class MainWindowController implements IMainWindowController {

	static final Logger logger = Logger.getLogger(MainWindowController.class);
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"res.i18ln.MainWindow", Locale.getDefault());

	private PreferenceManager manager;

	protected File inkscapeExecutable;

	private PreferenceDialog preferenceDialog;

	/**
	 * Used by <code>{@link #handleFileFormat(Map, String, List)}</code> to
	 * avoid showing the <code>ConfirmAllDialog</code>.
	 */
	private boolean yesToAll = false;

	// ThreadPool defaults
	private int corePoolSize = 2;
	private int maximumPoolSize = 2;
	private long keepAliveTime = 10;
	private PriorityBlockingQueue<Runnable> queue;
	private ThreadPoolExecutor threadPool;
	private int completedProcesses = 0;

	public MainWindowController(PreferenceManager manager) {
		this.manager = manager;
		preferenceDialog = new PreferenceDialog(null, manager);

		// BEGIN get the path to Inkscape
		String inkscape_location = manager.getStore().getValue(
				ConversionSVG.KEY_INKSCAPE_PATH);
		if (inkscape_location != null) {
			inkscapeExecutable = new File(inkscape_location);
		} else {
			inkscapeExecutable = findInkscapeExecutable();
		}

		if (!inkscapeExecutable.exists()) {
			JOptionPane
					.showMessageDialog(
							(Dialog) null,
							"The Inkscape executable location does not exist. Please enter the correct location in the Settings.");
		} else {
			manager.getStore().setValue(ConversionSVG.KEY_INKSCAPE_PATH,
					inkscapeExecutable.getAbsolutePath());
		}
		// END get the path to Inkscape

		// if ((inkscapeExecutable = findInkscapeExecutable()) == null)
		// {
		// JFileChooser inkscapeChooser = new
		// JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		// inkscapeChooser.setFileFilter(new InkscapeFilter());
		// inkscapeChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// if (inkscapeChooser.showOpenDialog(mainwindow) ==
		// JFileChooser.APPROVE_OPTION)
		// {
		// inkscapeExecutable = inkscapeChooser.getSelectedFile();
		// settings.setProperty("inkscape_location",
		// inkscapeExecutable.getAbsolutePath());
		// } else
		// {
		// System.exit(0);
		// }
		// }

		// BEGIN configure thread pool
		String value;
		corePoolSize = (value = manager.getStore().getValue(
				ConversionSVG.KEY_CORE_POOL_SIZE)) != null ? Integer
				.valueOf(value) : corePoolSize;
		maximumPoolSize = (value = manager.getStore().getValue(
				ConversionSVG.KEY_MAXIMUM_POOL_SIZE)) != null ? Integer
				.valueOf(manager.getStore().getValue(
						ConversionSVG.KEY_MAXIMUM_POOL_SIZE)) : maximumPoolSize;

		queue = new PriorityBlockingQueue<Runnable>(corePoolSize);
		threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, TimeUnit.SECONDS, queue);
		// END configure thread pool
	}

	/**
	 * Returns a list of <code>File</code>s that the user has checked in the
	 * <code>CheckBoxTree</code> on the <code>MainWindow</code>'s file select
	 * panel.
	 * 
	 * @param The
	 *            <code>CheckBoxTree</code> to retrieve a list of checked files
	 *            from
	 * @return list of <code>File</code>'s checked in the
	 *         <code>MainWindow</code>'s file select panel
	 */
	public static List<File> getFiles(CheckBoxTree tree) {
		List<File> files = new ArrayList<File>();
		TreePath[] selectedTreePaths = tree.getCheckBoxTreeSelectionModel()
				.getSelectionPaths();

		if (selectedTreePaths != null) {
			for (TreePath selection : selectedTreePaths) {
				FileSystemTreeNode node = (FileSystemTreeNode) selection
						.getLastPathComponent();
				File file = node.getFile();

				if (file.isDirectory()) {
					// If a directory is checked, all the children are
					// unselected (this is a performance feature according to
					// Jide) so we have to iterate over all the files in that
					// directory
					for (int i = 0; i < node.getChildCount(); i++) {
						FileSystemTreeNode child = node.getChildAt(i);
						files.add(child.getFile());
					}
				} else {
					files.add(file);
				}
			}
		}

		return files;
	}

	/**
	 * Handles populating the command line switches and switch arguments needed
	 * for Inkscape's command line.
	 * 
	 * @return A map populated with switches and their arguments (also known as
	 *         options).
	 */
	public static Map<String, String> getInkscapeCommandlineOptions(
			JCheckBox png, JCheckBox ps, JCheckBox eps, JCheckBox pdf,
			ColorPicker picker, JTextField height, JTextField width,
			JRadioButton page, JRadioButton drawing) {
		HashMap<String, String> options = new HashMap<String, String>();

		if (png.isSelected()) {
			options.put("-e", "-e");
		}
		if (ps.isSelected()) {
			options.put("-P", "-P");
		}
		if (eps.isSelected()) {
			options.put("-E", "-E");
		}
		if (pdf.isSelected()) {
			options.put("-A", "-A");
		}
		options.put("-b", Helpers.toRGB(picker.getColor(), false));
		options.put("-y", String.valueOf(picker.getColor().getAlpha()));
		setCommandlineOption(options, "-h", height);
		setCommandlineOption(options, "-w", width);
		options.putAll(getExportArea(page, drawing));

		return options;
	}

	/**
	 * Handles populating the map with the respective switch needed for the
	 * command line.
	 * 
	 * @return The map with the respective switch needed for the command line.
	 */
	private static Map<String, String> getExportArea(JRadioButton page,
			JRadioButton drawing) {
		HashMap<String, String> option = new HashMap<String, String>();
		if (page.isSelected()) {
			option.put("-C", "");
		} else if (drawing.isSelected()) {
			option.put("-D", "");
		} // TODO future -a --export-area=x0:y0:x1:y1
		return option;
	}

	/**
	 * Attempts to find the Inkscape executable in a device independent manner.
	 * 
	 * @return
	 */
	private File findInkscapeExecutable() {
		File executable = null;
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("linux")) {
			executable = new File("/usr/bin/inkscape");
		} else if (os.contains("windows")) {
			executable = new File("C:/Program Files/Inkscape/inkscape.exe");
		} else {
			// TODO handle Apple Inc. or platform independent
		}

		// only return the File object if the file actually exists on the system
		return executable.exists() ? executable : null;
	}

	//
	// Helper functions
	//

	/**
	 * helper function used for checking if anything was entered into the text
	 * field.
	 * 
	 * @param options
	 * @param option
	 * @param field
	 */
	private static void setCommandlineOption(Map<String, String> options,
			String option, JTextField field) {
		if (!field.getText().isEmpty()) {
			options.put(option, field.getText());
		}
	}

	private InkscapeProcessCompletedListener progressListener() {
		return new InkscapeProcessCompletedListener() {

			@Override
			public void processCompleted(InkscapeProcessInfo info) {
				completedProcesses++;
				// TODO update the ProgressBar
				// progressBar.setValue(completedProcesses);
				logger.debug("TODO\t" + "update the ProgressBar");
				File file;
				if (info.options.contains("-e")) {
					file = new File(info.options
							.get(info.options.indexOf("-e")));
					logger.info(file.getAbsoluteFile());
				}
				if (info.options.contains("-E")) {
					file = new File(info.options
							.get(info.options.indexOf("-E")));
					logger.info(file.getAbsoluteFile());
				}
				if (info.options.contains("-A")) {
					file = new File(info.options
							.get(info.options.indexOf("-A")));
					logger.info(file.getAbsoluteFile());
				}
				if (info.options.contains("-P")) {
					file = new File(info.options
							.get(info.options.indexOf("-P")));
					logger.info(file.getAbsoluteFile());
				}
			}
		};
	}

	//
	// IMainWindowController members
	//

	/**
	 * Handles creating the command to pass to an Inkscape process.
	 * <p>
	 * For each export file type selected the following check is performed.
	 * </p>
	 * 
	 * <pre>
	 * {@code
	 * if (choice == ConfirmAllDialog.NO) {
	 *     continue;
	 * } else if (choice == ConfirmAllDialog.NO_TO_ALL) {
	 *     return;
	 * } else if (choice == ConfirmAllDialog.YES_TO_ALL) {
	 *     yesToAll = true;
	 * } else if (choice == ConfirmAllDialog.CANCEL) {
	 *     return;
	 * }
	 * </pre>
	 * <p>
	 * The following explain what the code actually does:
	 * <ul>
	 * <li>If NO is selected <code>continue</code> will proceed to the next
	 * file, and the user will be prompted for the next file.</li>
	 * <li>If NO_TO_ALL is selected <code>return</code> will simply exit the
	 * <code>handleConvert</code> function.</li>
	 * <li>If YES_TO_ALL is selected the user will not be prompted any further
	 * about files being overwritten.</li>
	 * <li>If CANCEL is selected <code>return</code> will simply exit the
	 * <code>handleConvert</code> function.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param files
	 *            the list of SVG files to be converted
	 * @param options
	 *            the options to pass to Inkscape
	 */
	@Override
	public void handleConvert(List<File> files, Map<String, String> options) {
		// display progress status
		// TODO output begin time stamp
		// TODO start ProgressBar
		// progressBar.setMaximum(files.size());

		List<String> command;
		Converter inkscapeProcess;

		for (File file : files) {
			command = new ArrayList<String>();

			int choice = ConfirmAllDialog.NONE;
			File exportFile;

			// PNG
			if (options.containsKey("-e")) {
				exportFile = Helpers.changeExtension(file, "png");

				if (!yesToAll) {
					choice = ConfirmAllDialog.showDialog(file.getAbsolutePath()
							+ i18ln.getString("FileExistsModalMessage"), i18ln
							.getString("FileExists"));
					if (choice == ConfirmAllDialog.NO) {
						continue;
					} else if (choice == ConfirmAllDialog.NO_TO_ALL) {
						return;
					} else if (choice == ConfirmAllDialog.YES_TO_ALL) {
						yesToAll = true;
					} else if (choice == ConfirmAllDialog.CANCEL) {
						return;
					}
				}

				command.add("-e");
				command.add(exportFile.getAbsolutePath());
			}

			// PS
			if (options.containsKey("-P")) {
				exportFile = Helpers.changeExtension(file, "ps");

				if (!yesToAll) {
					choice = ConfirmAllDialog.showDialog(file.getAbsolutePath()
							+ i18ln.getString("FileExistsModalMessage"), i18ln
							.getString("FileExists"));
					if (choice == ConfirmAllDialog.NO) {
						continue;
					} else if (choice == ConfirmAllDialog.NO_TO_ALL) {
						return;
					} else if (choice == ConfirmAllDialog.YES_TO_ALL) {
						yesToAll = true;
					} else if (choice == ConfirmAllDialog.CANCEL) {
						return;
					}
				}

				command.add("-P");
				command.add(exportFile.getAbsolutePath());
			}

			// PDF
			if (options.containsKey("-A")) {
				exportFile = Helpers.changeExtension(file, "pdf");

				if (!yesToAll) {
					choice = ConfirmAllDialog.showDialog(file.getAbsolutePath()
							+ i18ln.getString("FileExistsModalMessage"), i18ln
							.getString("FileExists"));
					if (choice == ConfirmAllDialog.NO) {
						continue;
					} else if (choice == ConfirmAllDialog.NO_TO_ALL) {
						return;
					} else if (choice == ConfirmAllDialog.YES_TO_ALL) {
						yesToAll = true;
					} else if (choice == ConfirmAllDialog.CANCEL) {
						return;
					}
				}

				command.add("-A");
				command.add(exportFile.getAbsolutePath());
			}

			// EPS
			if (options.containsKey("-E")) {
				exportFile = Helpers.changeExtension(file, "eps");

				if (!yesToAll) {
					choice = ConfirmAllDialog.showDialog(file.getAbsolutePath()
							+ i18ln.getString("FileExistsModalMessage"), i18ln
							.getString("FileExists"));
					if (choice == ConfirmAllDialog.NO) {
						continue;
					} else if (choice == ConfirmAllDialog.NO_TO_ALL) {
						return;
					} else if (choice == ConfirmAllDialog.YES_TO_ALL) {
						yesToAll = true;
					} else if (choice == ConfirmAllDialog.CANCEL) {
						return;
					}
				}

				command.add("-E");
				command.add(exportFile.getAbsolutePath());
			}

			command.add("-f");
			command.add(file.getAbsolutePath());

			inkscapeProcess = new Converter(inkscapeExecutable, command);
			inkscapeProcess.addProcessCompletedListener(progressListener());
			threadPool.execute(inkscapeProcess);
		}
		// TODO output completed time stamp
	}

	@Override
	public void handleCancel() {
		int queued = threadPool.getQueue().size();
		if (queued > 0) {
			threadPool.shutdown();
			// TODO output number of remaining threads
		}
	}

	@Override
	public void handleChangeLanguage(Locale locale) {
		// ResourceBundle resourceBundle = ResourceBundle.getBundle(
		// "conversion.resources.i18ln.MainWindow", locale);
		// selectedLocale = locale;
		// // Ribbon
		// mainwindow.saveMenuButton.setText(resourceBundle.getString("Save"));
		// mainwindow.aboutMenuButton.setText(resourceBundle.getString("About"));
		// mainwindow.inkscapeMenuButton.setText(resourceBundle
		// .getString("Information"));
		// mainwindow.quitMenuButton.setText(resourceBundle.getString("Quit"));
		//
		// mainwindow.homeTask
		// .setTitle(resourceBundle.getString("HomeRibbonTask"));
		// mainwindow.controlsBand.setTitle(resourceBundle
		// .getString("ControlsRibbonBand"));
		// mainwindow.preferencesBand.setTitle(resourceBundle
		// .getString("PreferencesRibbonBand"));
		//
		// mainwindow.convertButton.setText(resourceBundle
		// .getString("ConvertButton"));
		// mainwindow.cancelButton.setText(resourceBundle.getString("Cancel"));
		// mainwindow.languageButton.setText(resourceBundle
		// .getString("LanguageButton"));
		// mainwindow.accessibilityButton.setText(resourceBundle
		// .getString("AccessibilityButton"));
		// mainwindow.fontButton.setText(resourceBundle.getString("FontButton"));
		// mainwindow.shortcutsButton.setText(resourceBundle
		// .getString("ShortcutsButton"));
		//
		// // Option Panel
		// changeBorderLanguage((TitledBorder) mainwindow.outputFormatPanel
		// .getBorder(), resourceBundle.getString("FormatPanel"));
		// changeBorderLanguage((TitledBorder) mainwindow.exportAreaPanel
		// .getBorder(), resourceBundle.getString("ExportAreaPanel"));
		// changeBorderLanguage((TitledBorder) mainwindow.sizePanel.getBorder(),
		// resourceBundle.getString("SizePanel"));
		// changeBorderLanguage((TitledBorder)
		// mainwindow.colorPicker.getBorder(),
		// resourceBundle.getString("BackgroundPanel"));
		//
		// mainwindow.drawingRadioButton.setText(resourceBundle
		// .getString("DrawingRadioButton"));
		// mainwindow.pageRadioButton.setText(resourceBundle
		// .getString("PageRadioButton"));
		// mainwindow.heightLabel.setText(resourceBundle
		// .getString("HeightTextField"));
		// mainwindow.widthLabel.setText(resourceBundle
		// .getString("WidthTextField"));
		// mainwindow.colorPicker.setLocale(locale);
		//
		// // File Select Panel
		// mainwindow.singleOutputDirectoryRadio.setText(resourceBundle
		// .getString("SingleDirectoryRadioButton"));
		// mainwindow.sameOutputDirectoryRadio.setText(resourceBundle
		// .getString("SameDirectoryRadioButton"));
	}

	@Override
	public void handleChangeRoot(CheckBoxTree tree, File root) {
		try {
			tree.setModel(new FileSystemTreeModel(root, MainWindow.filters));
			manager.getStore().setValue(ConversionSVG.KEY_LAST_ROOT,
					root.getAbsolutePath());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void handleSave() {
		// TODO Auto-generated method stub

	}

	/**
	 * Checks to make sure there are no active Inkscape processes in the thread
	 * pool. If there are active processes, a modal dialog is presented to the
	 * user asking to confirm that they want to shutdown those processes.
	 */
	@Override
	public void handleQuit() {
		// if processes are still going (Inkscape), modal dialog for
		// confirmation to quit
		int proceed = JOptionPane.OK_OPTION;

		if (threadPool.getActiveCount() != 0) {
			proceed = JOptionPane.showConfirmDialog((Dialog) null, i18ln
					.getString("ActiveProcessesModalMessage"));
		}

		if (proceed == JOptionPane.OK_OPTION) {
			threadPool.shutdownNow();
		}

		try {
			manager.getStore().save();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	@Override
	public void handleAbout() {
		// TODO Auto-generated method stub
		logger.debug("TODO\t" + "create About dialog");
		logger.debug("TODO\t" + "display About dialog");
	}

	@Override
	public void handleSettings() {
		preferenceDialog.setVisible(true);
	}
}
