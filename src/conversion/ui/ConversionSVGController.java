package conversion.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.naming.spi.DirectoryManager;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.MenuElement;
import javax.swing.border.TitledBorder;
import javax.swing.text.html.ListView;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.pushingpixels.flamingo.api.common.JCommandButton;

import test.svg.transcoded.system_log_out;

import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;

public class ConversionSVGController
{
    protected MainWindow               mainwindow;
    ResourceBundle                     resourceBundle;
    EventListener                      eventListener;
    ProgressDialog                     progressDialog;

    protected File                     inkscapeExecutable;

    // ThreadPool
    int                                corePoolSize       = 2;
    int                                maximumPoolSize    = 2;
    long                               keepAliveTime      = 10;
    final ArrayBlockingQueue<Runnable> queue              = new ArrayBlockingQueue<Runnable>(5);
    private ThreadPoolExecutor         threadPool;
    int                                completedProcesses = 0;

    public ConversionSVGController(conversion.ui.MainWindow mainwindow,
            ResourceBundle resourceBundle)
    {
        this.mainwindow = mainwindow;
        this.resourceBundle = resourceBundle;
        eventListener = new EventListener();
        if ((inkscapeExecutable = findInkscapeExecutable()) == null)
        {
            JFileChooser inkscapeChooser = new JFileChooser();
            inkscapeChooser.setFileFilter(new InkscapeFilter());
            inkscapeChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (inkscapeChooser.showOpenDialog(mainwindow) == JFileChooser.APPROVE_OPTION)
            {
                inkscapeExecutable = inkscapeChooser.getSelectedFile();
            }
            else
            {
                System.exit(0);
            }
        }
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
        
        progressDialog = new ProgressDialog(mainwindow);
        progressDialog.addWindowListener(eventListener);

        
    }

    public List<File> getFiles()
    {
        List<File> files = new ArrayList<File>();
        TreePath[] selectedTreePaths = null;

        if ((selectedTreePaths = mainwindow.fileHeirarchy.getCheckBoxTreeSelectionModel().getSelectionPaths()) != null)
        {
            for (TreePath selection : selectedTreePaths)
            {
                File file = (File) selection.getLastPathComponent();
                files.add(file);
            }
        }

        return files;
    }

    /**
     * This is the method that handles creating the threads that call Inkscape
     * to convert the files selected by the user.
     */
    public void convert()
    {
        // disable GUI input to prevent mistakes
        enableInput(false);

        // getOutputFormat() for each file and merge
        // with result from getInkscapeCommandLineOptions()
        List<File> files = getFiles();
        boolean yesToAll = false;
        boolean noToAll = false;

        // display progress status
        if (!progressDialog.statusOutput.isEmpty()){
            progressDialog.statusOutput.addElement("-----------------------------");
        }
        progressDialog.progressBar.setMaximum(files.size());
        progressDialog.setVisible(true);

        Converter inkscapeProcess;

        for (File file : files)
        {
            Map<String, String> options = getInkscapeCommandlineOptions();
            options.put("-f", file.getAbsolutePath());
            Map<String, String> formats = getOutputFormat(file);

            // we have to call Inkscape each time to export each format
            // I don't believe Inkscape supports it's own batch mode (as of yet)
            for (Map.Entry<String, String> format : formats.entrySet())
            {
                options.put(format.getKey(), format.getValue());

                File exportFile = new File(format.getValue());

                if (exportFile.exists())
                {
                    int choice = -2; // nothing uses -2 (-1 is CLOSED_OPTION)
                    if (yesToAll)
                    {

                    } else if (noToAll)
                    {
                        break;
                    } else
                    { // only show the dialog if neither yesToAll or noToAll
                        // have been set to TRUE
                        String[] choices = { "yes", "yes to all", "no",
                                "no to all", "cancel" };
                        choice = JOptionPane.showOptionDialog(mainwindow, "create a message", "File exists", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[2]);
                    }

                    if (choice == 4 || choice == JOptionPane.CLOSED_OPTION)
                    { // Cancelled
                        enableInput(true);
                        return;
                    } else if (choice == 1)
                    {
                        yesToAll = true;
                    } else if (choice == 3)
                    {
                        noToAll = true;
                    }

                    inkscapeProcess = new Converter(inkscapeExecutable, options);
                    inkscapeProcess.addProcessCompletedListener(eventListener);

                    if (yesToAll)
                    {
                        threadPool.execute(inkscapeProcess);
                        break;
                    }

                    if (choice == 0)
                    {
                        threadPool.execute(inkscapeProcess);
                    }
                } else
                {
                    inkscapeProcess = new Converter(inkscapeExecutable, options);
                    inkscapeProcess.addProcessCompletedListener(eventListener);
                    threadPool.execute(inkscapeProcess);
                }
            }
        }

        // enable GUI input
        enableInput(true);
    }

    public void showStatus()
    {
        if (progressDialog != null)
        {
            progressDialog.setVisible(true);
        }
    }

    /**
     * Immediately shuts down the active Inkscape processes.
     */
    public void cancel()
    {
        int queued = threadPool.getQueue().size();
        if (queued > 0)
        {
            threadPool.shutdownNow();
            progressDialog.statusOutput.addElement("Cancelled with " + queued + " remaining.");
            enableInput(true);
        }
    }

    /**
     * Checks to make sure there are no active Inkscape processes in the thread
     * pool. If there are active processes, a modal dialog is presented to the
     * user asking to confirm that they want to shutdown those processes.
     */
    public void quit()
    {
        // if processes are still going (Inkscape), modal dialog for
        // confirmation to quit
        int proceed = JOptionPane.OK_OPTION;
        
        if (threadPool.getActiveCount() != 0)
        {
            proceed = JOptionPane.showConfirmDialog(mainwindow, resourceBundle.getString("ActiveProcessesModalMessage"));
        }

        if (proceed == JOptionPane.OK_OPTION)
        {
            threadPool.shutdownNow();
            System.exit(0);
        }
    }

    /**
     * Handles changing the text the user sees on the application's screen to
     * the selected language.
     * 
     * @param locale
     *            The Locale language to change to.
     */
    public void changeLanguage(Locale locale)
    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("conversion.resources.i18ln.MainWindow", locale);

        // Ribbon
        mainwindow.homeTask.setTitle(resourceBundle.getString("HomeRibbonTask"));
        mainwindow.controlsBand.setTitle(resourceBundle.getString("ControlsRibbonBand"));
        mainwindow.preferencesBand.setTitle(resourceBundle.getString("PreferencesRibbonBand"));

        mainwindow.convertButton.setText(resourceBundle.getString("ConvertButton"));
        mainwindow.cancelButton.setText(resourceBundle.getString("CancelButton"));
        mainwindow.languageButton.setText(resourceBundle.getString("LanguageButton"));
        mainwindow.accessibilityButton.setText(resourceBundle.getString("AccessibilityButton"));
        mainwindow.fontButton.setText(resourceBundle.getString("FontButton"));
        mainwindow.shortcutsButton.setText(resourceBundle.getString("ShortcutsButton"));

        // Option Panel
        changeBorderLanguage((TitledBorder) mainwindow.outputFormatPanel.getBorder(), resourceBundle.getString("FormatPanel"));
        changeBorderLanguage((TitledBorder) mainwindow.exportAreaPanel.getBorder(), resourceBundle.getString("ExportAreaPanel"));
        changeBorderLanguage((TitledBorder) mainwindow.sizePanel.getBorder(), resourceBundle.getString("SizePanel"));
        changeBorderLanguage((TitledBorder) mainwindow.colorPicker.getBorder(), resourceBundle.getString("BackgroundPanel"));

        mainwindow.drawingRadioButton.setText(resourceBundle.getString("DrawingRadioButton"));
        mainwindow.pageRadioButton.setText(resourceBundle.getString("PageRadioButton"));
        mainwindow.heightLabel.setText(resourceBundle.getString("HeightTextField"));
        mainwindow.widthLabel.setText(resourceBundle.getString("WidthTextField"));
        mainwindow.colorPicker.setLocale(locale);

        // File Select Panel
        mainwindow.singleOutputDirectoryRadio.setText(resourceBundle.getString("SingleDirectoryRadioButton"));
        mainwindow.sameOutputDirectoryRadio.setText(resourceBundle.getString("SameDirectoryRadioButton"));
    }

    // simple helper function to save code in changeLanguage()
    private void changeBorderLanguage(TitledBorder border, String text)
    {
        border.setTitle(text);
    }

    /**
     * Disable all user inputs to prevent unwanted/unnecessary interactions
     * while Inkscape handles converting images.
     * 
     * @param enable
     */
    public void enableInput(boolean enable)
    {

        // enable/disable the necessary JPanels
        enablePanel(mainwindow.colorPicker, enable);
        enablePanel(mainwindow.sizePanel, enable);
        mainwindow.unitComboBox.setEnabled(false);// Special case until it is
        // actually implemented
        enablePanel(mainwindow.outputFormatPanel, enable);
        enablePanel(mainwindow.exportAreaPanel, enable);
    }

    /**
     * Disables all the components contained in the JPanel. This is intended to
     * be used by disableInput() prior to converting.
     * 
     * @param panel
     * @param enable
     */
    private void enablePanel(JPanel panel, boolean enable)
    {
        for (Component component : panel.getComponents())
        {
            component.setEnabled(enable);
        }
    }

    public HashMap<String, String> getInkscapeCommandlineOptions()
    {
        HashMap<String, String> options = new HashMap<String, String>();

        options.put("-b", sanitizeColor(mainwindow.colorPicker.getColor()));
        options.put("-y", String.valueOf(mainwindow.colorPicker.getColor().getAlpha()));
        setCommandlineOption(options, "-h", mainwindow.heightTextField);
        setCommandlineOption(options, "-w", mainwindow.widthTextField);
        options.putAll(getExportArea());

        return options;
    }

    private void setCommandlineOption(HashMap<String, String> options,
            String option, JTextField field)
    {
        if (!field.getText().isEmpty())
        {
            options.put(option, field.getText());
        }
    }

    /**
     * Inkscape requires the background color to be a string in a certain
     * format. We are choosing to use the rgb(255,255,255) format.
     * 
     * @param color
     * @return A sanitized String formatted as rgb(255,255,255).
     */
    private String sanitizeColor(Color color)
    {
        return "rgb(" + color.getRed() + "," + color.getGreen() + ","
                + color.getBlue() + ")";
    }

    private Map<String, String> getExportArea()
    {
        HashMap<String, String> option = new HashMap<String, String>();
        if (mainwindow.pageRadioButton.isSelected())
        {
            option.put("-C", "");
        } else if (mainwindow.drawingRadioButton.isSelected())
        {
            option.put("-D", "");
        } // TODO future -a --export-area=x0:y0:x1:y1
        return option;
    }

    private Map<String, String> getOutputFormat(File file)
    {
        HashMap<String, String> option = new HashMap<String, String>();

        if (mainwindow.singleOutputDirectoryRadio.isSelected())
        {
            addOutputFormats(option, changeDirectory(file, mainwindow.outputDirectoryTextField.getText()));
        } else if (mainwindow.sameOutputDirectoryRadio.isSelected())
        {
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
            String absolutePath)
    {
        if (mainwindow.pngCheckBox.isSelected())
        {
            option.put("-e", changeExtension(absolutePath, "png"));
        }
        if (mainwindow.psCheckBox.isSelected())
        {
            option.put("-P", changeExtension(absolutePath, "ps"));
        }
        if (mainwindow.pdfCheckBox.isSelected())
        {
            option.put("-A", changeExtension(absolutePath, "pdf"));
        }
        if (mainwindow.epsCheckBox.isSelected())
        {
            option.put("-E", changeExtension(absolutePath, "eps"));
        }
    }

    private String changeDirectory(File file, String path)
    {
        String name = file.getName();
        return path + System.getProperty("file.separator") + name;
    }

    private String changeExtension(String path, String extension)
    {
        path = path.substring(0, path.lastIndexOf(".") + 1);
        path += extension;
        return path;
    }

    /**
     * Attempts to find the Inkscape executable in a device independent manner.
     * 
     * @return
     */
    private File findInkscapeExecutable()
    {
        File executable = null;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.equals("linux"))
        {
            executable = new File("/usr/bin/inkscape");
        } else if (os.contains("windows"))
        {
            executable = new File("C:/Program Files/Inkscape/inkscape.exe");
        } else
        {
            // TODO handle Apple Inc. or platform independent
        }

        return executable;
    }

    private class EventListener implements InkscapeProcessCompletedListener,
            WindowListener
    {
        @Override
        public void processCompleted(InkscapeProcessInfo info)
        {
            completedProcesses++;
            progressDialog.statusOutput.addElement(getExportedFile(info) + "... done");
            progressDialog.progressBar.setValue(completedProcesses);
        }
        // helper function to get the exported file
        private String getExportedFile(InkscapeProcessInfo info)
        {
            String file = null;
            if (info.options.containsKey("-e"))
            {
                file = info.options.get("-e");
            } else if (info.options.containsKey("-E"))
            {
                file = info.options.get("-E");
            } else if (info.options.containsKey("-A"))
            {
                file = info.options.get("-A");
            } else if (info.options.containsKey("-P"))
            {
                file = info.options.get("-P");
            }
            return file;
        }

        @Override
        public void windowActivated(WindowEvent arg0)
        {
        }

        @Override
        public void windowClosed(WindowEvent e)
        {
            if (e.getSource().equals(progressDialog))
            {
                progressDialog.setVisible(false);
            }
        }

        @Override
        public void windowClosing(WindowEvent e)
        {
            if (e.getSource().equals(progressDialog))
            {
                progressDialog.setVisible(false);
            }
        }

        @Override
        public void windowDeactivated(WindowEvent arg0)
        {
        }

        @Override
        public void windowDeiconified(WindowEvent arg0)
        {
        }

        @Override
        public void windowIconified(WindowEvent arg0)
        {
        }

        @Override
        public void windowOpened(WindowEvent arg0)
        {
        }
    }
}
