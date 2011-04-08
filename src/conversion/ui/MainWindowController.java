package conversion.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreePath;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.prefs.DefaultPreferenceNode;
import org.prefs.PreferenceDialog;
import org.prefs.PreferenceManager;
import org.prefs.PreferencePage;
import org.prefs.PreferenceStore;

import net.sf.fstreem.FileSystemTreeModel;
import net.sf.fstreem.FileSystemTreeNode;

import com.jidesoft.swing.CheckBoxTree;

import conversion.ui.settings.AdvancedPreferencePage;
import conversion.ui.settings.DefaultsPreferencePage;

/**
 * The MainWindowController class is somewhat self-explanatory. It is the
 * Controller in the Model View Controller (MVC) framework. It handles
 * controlling what is done based on user interaction from the MainWindow (the
 * View in MVC).
 * 
 * In addition to the MainWindow, the MainWindowController also handles
 * displaying dialogs where needed. In instances where a file to be exported
 * already exists on the system, a dialog will popup asking the user if they
 * want to overwrite it or not. Another instance where a dialog will popup is if
 * the program cannot find the Inkscape executable on the system. A file chooser
 * dialog will popup that will filter anything except the Inkscape executable
 * (file named Inkscape.exe or inkscape).
 * 
 * @author Erich Schroeter
 */
public class MainWindowController implements IMainWindowController {

	protected MainWindow mainwindow;
	ResourceBundle languageBundle;
	EventListener eventListener;
	ProgressDialog progressDialog;

	Locale selectedLocale = Locale.getDefault();

	protected File inkscapeExecutable;

	// ThreadPool defaults
	private int corePoolSize = 2;
	private int maximumPoolSize = 2;
	private long keepAliveTime = 10;
	private PriorityBlockingQueue<Runnable> queue;
	private ThreadPoolExecutor threadPool;
	private int completedProcesses = 0;

	//
	// Preferences
	//
	private PreferenceStore prefs;
	private PreferenceManager prefsManager;
	private PreferenceDialog dlg;
	private static final String SETTINGS_FILE = "preferences.properties";
	private static File prefsFile;

	//
	// Properties KEYS
	//
	public static final String KEY_INKSCAPE_PATH = "inkscape_path";
	public static final String KEY_CORE_POOL_SIZE = "pool_size_core";
	public static final String KEY_MAXIMUM_POOL_SIZE = "pool_size_max";
	public static final String KEY_KEEP_ALIVE_TIME = "thread_keep_alive_time";
	public static final String KEY_INKSCAPE_EXPORT_WIDTH = "inkscape_export_width";
	public static final String KEY_INKSCAPE_EXPORT_HEIGHT = "inkscape_export_height";
	public static final String KEY_INKSCAPE_EXPORT_COLOR = "inkscape_export_color";

	public MainWindowController(conversion.ui.MainWindow mainwindow,
			ResourceBundle resourceBundle) {
		this.mainwindow = mainwindow;

		this.languageBundle = resourceBundle;
		eventListener = new EventListener();

		this.mainwindow.addWindowListener(eventListener);

		// BEGIN get user settings/preferences
		try {
			prefsFile = new File(SETTINGS_FILE);
			prefsFile.createNewFile();
			prefs = new PreferenceStore(prefsFile,
					"This file contains preference settings for ConversionSVG",
					getDefaults());

			prefs.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// END get user settings/preferences

		prefsManager = new PreferenceManager(prefs);

		PreferencePage defaultsPage = new DefaultsPreferencePage("Defaults",
				"Specify the default preferences to be populated when ConversionSVG starts up");
		defaultsPage.initialize(prefs);
		PreferencePage advancedPage = new AdvancedPreferencePage("Advanced",
				"Configure advanced features ConversionSVG uses.");
		advancedPage.initialize(prefs);

		prefsManager.add(new DefaultPreferenceNode(defaultsPage, "Defaults"));
		prefsManager.add(new DefaultPreferenceNode(advancedPage, "Advanced"));

		dlg = new PreferenceDialog(mainwindow, prefsManager);

		defaultsPage.setPageContainer(dlg);
		advancedPage.setPageContainer(dlg);

		// BEGIN get the path to Inkscape
		String inkscape_location = prefs.getValue(KEY_INKSCAPE_PATH);
		if (inkscape_location != null) {
			inkscapeExecutable = new File(inkscape_location);
		} else {
			inkscapeExecutable = findInkscapeExecutable();
		}

		if (!inkscapeExecutable.exists()) {
			JOptionPane
					.showMessageDialog(
							mainwindow,
							"The Inkscape executable location does not exist. Please enter the correct location in the Settings.");
		} else {
			prefs.setValue(KEY_INKSCAPE_PATH, inkscapeExecutable
					.getAbsolutePath());
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
		corePoolSize = (value = prefs.getValue(KEY_CORE_POOL_SIZE)) != null ? Integer
				.valueOf(value)
				: corePoolSize;
		maximumPoolSize = (value = prefs.getValue(KEY_MAXIMUM_POOL_SIZE)) != null ? Integer
				.valueOf(prefs.getValue(KEY_MAXIMUM_POOL_SIZE))
				: maximumPoolSize;

		queue = new PriorityBlockingQueue<Runnable>(corePoolSize);
		threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, TimeUnit.SECONDS, queue);
		// END configure thread pool

		progressDialog = new ProgressDialog(mainwindow);
		progressDialog.addWindowListener(eventListener);
	}

	/**
	 * Returns a <code>Properties</code> instance populated with the default
	 * values <code>MainWindowController</code> uses.
	 * 
	 * @return the default properties used by <code>MainWindowController</code>
	 */
	private Properties getDefaults() {
		Properties defaults = new Properties();
		defaults
				.setProperty(KEY_CORE_POOL_SIZE, Integer.toString(corePoolSize));
		defaults.setProperty(KEY_MAXIMUM_POOL_SIZE, Integer
				.toString(maximumPoolSize));
		defaults.setProperty(KEY_KEEP_ALIVE_TIME, Long.toString(keepAliveTime));
		defaults.setProperty(KEY_INKSCAPE_EXPORT_COLOR,
				sanitizeColor(Color.WHITE));
		defaults.setProperty(KEY_INKSCAPE_EXPORT_HEIGHT, "");
		defaults.setProperty(KEY_INKSCAPE_EXPORT_WIDTH, "");
		return defaults;
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
	public List<File> getFiles(CheckBoxTree tree) {
		List<File> files = new ArrayList<File>();
		TreePath[] selectedTreePaths = mainwindow.fileHeirarchy
				.getCheckBoxTreeSelectionModel().getSelectionPaths();

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

	private int showVerificationDialog(Locale locale) {
		ResourceBundle languageBundle = ResourceBundle.getBundle(
				"conversion.resources.i18ln.MainWindow", locale);
		String[] choices = { languageBundle.getString("Yes"),
				languageBundle.getString("YesToAll"),
				languageBundle.getString("No"),
				languageBundle.getString("NoToAll"),
				languageBundle.getString("Cancel") };
		return JOptionPane.showOptionDialog(mainwindow, languageBundle
				.getString("FileExistsModalMessage"), languageBundle
				.getString("FileExists"), JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, choices, choices[2]);
	}

	/**
	 * Displays the Progress dialog. If the progressDialog field has not been
	 * instantiated, then nothing occurs.
	 */
	public void showStatus() {
		if (progressDialog != null) {
			progressDialog.setVisible(true);
		}
	}

	/**
	 * Disable all user inputs to prevent unwanted/unnecessary interactions
	 * while Inkscape handles converting images.
	 * 
	 * @param enable
	 *            <code>true</code> to enable input, <code>false</code> to
	 *            disable input
	 */
	public void enableInput(boolean enable) {

		// enable/disable the necessary JPanels
		enablePanel(mainwindow.colorPicker, enable);
		enablePanel(mainwindow.sizePanel, enable);
		mainwindow.unitComboBox.setEnabled(false);// Special case until it is
		// actually implemented
		enablePanel(mainwindow.outputFormatPanel, enable);
		enablePanel(mainwindow.exportAreaPanel, enable);
	}

	/**
	 * Disables all the <code>Component</code>s contained in the
	 * <code>JPanel</code>. This is intended to be used by
	 * <code>enableInput(boolean)</code> prior to converting.
	 * 
	 * @param panel
	 *            the panel in which to disable the <code>Component</code>s
	 * @param enable
	 *            <code>true</code> to enable input, <code>false</code> to
	 *            disable input
	 */
	private void enablePanel(JPanel panel, boolean enable) {
		for (Component component : panel.getComponents()) {
			component.setEnabled(enable);
		}
	}

	/**
	 * Handles populating the command line switches and switch arguments needed
	 * for Inkscape's command line.
	 * 
	 * @return A map populated with switches and their arguments (also known as
	 *         options).
	 */
	private HashMap<String, String> getInkscapeCommandlineOptions() {
		HashMap<String, String> options = new HashMap<String, String>();

		options.put("-b", sanitizeColor(mainwindow.colorPicker.getColor()));
		options.put("-y", String.valueOf(mainwindow.colorPicker.getColor()
				.getAlpha()));
		setCommandlineOption(options, "-h", mainwindow.heightTextField);
		setCommandlineOption(options, "-w", mainwindow.widthTextField);
		options.putAll(getExportArea());

		return options;
	}

	/**
	 * Handles populating the map with the respective switch needed for the
	 * command line.
	 * 
	 * @return The map with the respective switch needed for the command line.
	 */
	private Map<String, String> getExportArea() {
		HashMap<String, String> option = new HashMap<String, String>();
		if (mainwindow.pageRadioButton.isSelected()) {
			option.put("-C", "");
		} else if (mainwindow.drawingRadioButton.isSelected()) {
			option.put("-D", "");
		} // TODO future -a --export-area=x0:y0:x1:y1
		return option;
	}

	/**
	 * Handles getting all the formats selected by the user in the Format panel
	 * and populating the options map with the respective switches and switch
	 * arguments needed for the command line.
	 * 
	 * @param file
	 *            Used to name the exported file with the selected export
	 *            formats (PNG, PS, PDF, etc)
	 * @return The map of options to be used on the command line.
	 */
	private Map<String, String> getOutputFormat(File file) {
		HashMap<String, String> option = new HashMap<String, String>();

		if (mainwindow.singleOutputDirectoryRadio.isSelected()) {
			addOutputFormats(option, changeDirectory(file,
					mainwindow.outputDirectoryTextField.getText()));
		} else if (mainwindow.sameOutputDirectoryRadio.isSelected()) {
			addOutputFormats(option, file.getAbsolutePath());
		}

		return option;
	}

	/**
	 * Handles adding the necessary options to the Map for exporting different
	 * file formats.
	 * 
	 * @param option
	 * @param absolutePath
	 */
	private void addOutputFormats(Map<String, String> option,
			String absolutePath) {
		if (mainwindow.pngCheckBox.isSelected()) {
			option.put("-e", changeExtension(absolutePath, "png"));
		}
		if (mainwindow.psCheckBox.isSelected()) {
			option.put("-P", changeExtension(absolutePath, "ps"));
		}
		if (mainwindow.pdfCheckBox.isSelected()) {
			option.put("-A", changeExtension(absolutePath, "pdf"));
		}
		if (mainwindow.epsCheckBox.isSelected()) {
			option.put("-E", changeExtension(absolutePath, "eps"));
		}
	}

	private String changeDirectory(File file, String path) {
		String name = file.getName();
		return path + System.getProperty("file.separator") + name;
	}

	private String changeExtension(String path, String extension) {
		path = path.substring(0, path.lastIndexOf(".") + 1);
		path += extension;
		return path;
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
	 * A helper function to get a formatted message using parameters for a
	 * specific <code>Locale</code>.
	 * 
	 * @return Returns a formatted string.
	 */
	public String sprintf(String pattern, Object[] args, Locale locale) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(locale);
		formatter.applyPattern(pattern);
		return formatter.format(args);
	}

	/**
	 * Inkscape requires the background color to be a string in a certain
	 * format. We are choosing to use the rgb(255,255,255) format.
	 * 
	 * @param color
	 * @return A sanitized String formatted as rgb(255,255,255).
	 */
	private String sanitizeColor(Color color) {
		return "rgb(" + color.getRed() + "," + color.getGreen() + ","
				+ color.getBlue() + ")";
	}

	/**
	 * Helper function to save code in <code>handleChangeLanguage()</code>. This
	 * saves lines of code for each border that needs the language changed.
	 * <p>
	 * We can write the following:
	 * </p>
	 * 
	 * <pre>
	 * {@code
	 * changeBorderLanguage(
	 *     (TitledBorder) mainwindow.outputFormatPanel.getBorder(),
	 *     resourceBundle.getString("FormatPanel"));
	 * }
	 * </pre>
	 * 
	 * <p>
	 * instead of:
	 * </p>
	 * 
	 * <pre>
	 * {
	 * 	&#064;code
	 * 	TitledBorder border = (TitledBorder) mainwindow.outputFormatPanel
	 * 			.getBorder();
	 * 	border.setTitle(resourceBundle.getString(&quot;FormatPanel&quot;));
	 * }
	 * </pre>
	 * 
	 * @param border
	 * @param text
	 */
	private void changeBorderLanguage(TitledBorder border, String text) {
		border.setTitle(text);
	}

	/**
	 * helper function used for checking if anything was entered into the text
	 * field.
	 * 
	 * @param options
	 * @param option
	 * @param field
	 */
	private void setCommandlineOption(HashMap<String, String> options,
			String option, JTextField field) {
		if (!field.getText().isEmpty()) {
			options.put(option, field.getText());
		}
	}

	private class EventListener implements InkscapeProcessCompletedListener,
			WindowListener {
		@Override
		public void processCompleted(InkscapeProcessInfo info) {
			completedProcesses++;
			progressDialog.statusOutput.addElement(getExportedFile(info)
					+ "... " + languageBundle.getString("Done"));
			progressDialog.progressBar.setValue(completedProcesses);

			// TODO log4j
			Logger logger = Logger.getRootLogger();
			logger.setLevel(Level.INFO);
			logger.info(getExportedFile(info));

			if (threadPool.getQueue().size() == 0) {
				// enable GUI input
				enableInput(true);
			}
		}

		// helper function to get the exported file
		private String getExportedFile(InkscapeProcessInfo info) {
			String file = null;
			if (info.options.containsKey("-e")) {
				file = info.options.get("-e");
			} else if (info.options.containsKey("-E")) {
				file = info.options.get("-E");
			} else if (info.options.containsKey("-A")) {
				file = info.options.get("-A");
			} else if (info.options.containsKey("-P")) {
				file = info.options.get("-P");
			}
			return file;
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent e) {
			if (e.getSource().equals(progressDialog)) {
				progressDialog.setVisible(false);
			}
		}

		@Override
		public void windowClosing(WindowEvent e) {
			if (e.getSource().equals(progressDialog)) {
				progressDialog.setVisible(false);
			} else if (e.getSource().equals(mainwindow)) {
				handleQuit();
			}
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			if (e.getSource().equals(progressDialog)) {
				mainwindow.statusMonitorButton.setEnabled(true);
			}
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}
	}

	//
	// IMainWindowController members
	//

	@Override
	public void handleConvert() {
		// disable GUI input to prevent mistakes
		enableInput(false);

		// getOutputFormat() for each file and merge
		// with result from getInkscapeCommandLineOptions()
		List<File> files = getFiles(mainwindow.fileHeirarchy);
		boolean yesToAll = false;
		boolean noToAll = false;

		// display progress status
		// only print a line separator if we've converted something previously
		// and there won't be >1 consecutively.
		String lineSeparator = "-----------------------------";
		if (!progressDialog.statusOutput.isEmpty()
				&& !progressDialog.statusOutput.get(
						progressDialog.statusOutput.getSize() - 1).equals(
						lineSeparator)) {
			progressDialog.statusOutput.addElement(lineSeparator);
		}
		progressDialog.progressBar.setMaximum(files.size());
		progressDialog.setVisible(true);

		Converter inkscapeProcess;

		for (File file : files) {
			Map<String, String> formats = getOutputFormat(file);

			// we have to call Inkscape each time to export each format
			// I don't believe Inkscape supports it's own batch mode (as of yet)
			for (Map.Entry<String, String> format : formats.entrySet()) {
				// create a new options map for each process, so that we can use
				// the specific commands used when the ProcessCompleted event
				// occurs.
				Map<String, String> options = getInkscapeCommandlineOptions();
				options.put("-f", file.getAbsolutePath());

				options.put(format.getKey(), format.getValue());

				File exportFile = new File(format.getValue());

				if (exportFile.exists()) {
					int choice = -2; // nothing uses -2 (-1 is CLOSED_OPTION)
					if (yesToAll) {

					} else if (noToAll) {
						break;
					} else { // only show the dialog if neither yesToAll or
						// noToAll
						// have been set to TRUE
						choice = showVerificationDialog(selectedLocale);
					}

					if (choice == 4 || choice == JOptionPane.CLOSED_OPTION) { // Cancelled
						enableInput(true);
						return;
					} else if (choice == 1) {
						yesToAll = true;
					} else if (choice == 3) {
						noToAll = true;
					}

					inkscapeProcess = new Converter(inkscapeExecutable, options);
					inkscapeProcess.addProcessCompletedListener(eventListener);

					if (yesToAll) {
						threadPool.execute(inkscapeProcess);
					}

					if (choice == 0) {
						threadPool.execute(inkscapeProcess);
					}
				} else {
					inkscapeProcess = new Converter(inkscapeExecutable, options);
					inkscapeProcess.addProcessCompletedListener(eventListener);
					threadPool.execute(inkscapeProcess);
				}
			}
		}
	}

	@Override
	public void handleCancel() {
		int queued = threadPool.getQueue().size();
		if (queued > 0) {
			threadPool.shutdown();
			Object[] args = { queued };
			progressDialog.statusOutput
					.addElement(sprintf(languageBundle
							.getString("CancelledRemainingQueue"), args,
							selectedLocale));
			enableInput(true);
		}
	}

	@Override
	public void handleChangeLanguage(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(
				"conversion.resources.i18ln.MainWindow", locale);
		selectedLocale = locale;
		// Ribbon
		mainwindow.saveMenuButton.setText(resourceBundle.getString("Save"));
		mainwindow.aboutMenuButton.setText(resourceBundle.getString("About"));
		mainwindow.inkscapeMenuButton.setText(resourceBundle
				.getString("Information"));
		mainwindow.quitMenuButton.setText(resourceBundle.getString("Quit"));

		mainwindow.homeTask
				.setTitle(resourceBundle.getString("HomeRibbonTask"));
		mainwindow.controlsBand.setTitle(resourceBundle
				.getString("ControlsRibbonBand"));
		mainwindow.preferencesBand.setTitle(resourceBundle
				.getString("PreferencesRibbonBand"));

		mainwindow.convertButton.setText(resourceBundle
				.getString("ConvertButton"));
		mainwindow.cancelButton.setText(resourceBundle.getString("Cancel"));
		mainwindow.languageButton.setText(resourceBundle
				.getString("LanguageButton"));
		mainwindow.accessibilityButton.setText(resourceBundle
				.getString("AccessibilityButton"));
		mainwindow.fontButton.setText(resourceBundle.getString("FontButton"));
		mainwindow.shortcutsButton.setText(resourceBundle
				.getString("ShortcutsButton"));

		// Option Panel
		changeBorderLanguage((TitledBorder) mainwindow.outputFormatPanel
				.getBorder(), resourceBundle.getString("FormatPanel"));
		changeBorderLanguage((TitledBorder) mainwindow.exportAreaPanel
				.getBorder(), resourceBundle.getString("ExportAreaPanel"));
		changeBorderLanguage((TitledBorder) mainwindow.sizePanel.getBorder(),
				resourceBundle.getString("SizePanel"));
		changeBorderLanguage((TitledBorder) mainwindow.colorPicker.getBorder(),
				resourceBundle.getString("BackgroundPanel"));

		mainwindow.drawingRadioButton.setText(resourceBundle
				.getString("DrawingRadioButton"));
		mainwindow.pageRadioButton.setText(resourceBundle
				.getString("PageRadioButton"));
		mainwindow.heightLabel.setText(resourceBundle
				.getString("HeightTextField"));
		mainwindow.widthLabel.setText(resourceBundle
				.getString("WidthTextField"));
		mainwindow.colorPicker.setLocale(locale);

		// File Select Panel
		mainwindow.singleOutputDirectoryRadio.setText(resourceBundle
				.getString("SingleDirectoryRadioButton"));
		mainwindow.sameOutputDirectoryRadio.setText(resourceBundle
				.getString("SameDirectoryRadioButton"));
	}

	@Override
	public void handleChangeRoot(File root) {
		try {
			mainwindow.fileHeirarchy.setModel(new FileSystemTreeModel(root,
					MainWindow.filters));
			prefs.setValue("last_root", root.getAbsolutePath());

		} catch (Exception e) {
			// TODO notify user
			e.printStackTrace();
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
			proceed = JOptionPane.showConfirmDialog(mainwindow, languageBundle
					.getString("ActiveProcessesModalMessage"));
		}

		if (proceed == JOptionPane.OK_OPTION) {
			threadPool.shutdownNow();
		}

		try {
			prefs.save();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	@Override
	public void handleAbout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleSettings() {
		dlg.setVisible(true);
	}
}
