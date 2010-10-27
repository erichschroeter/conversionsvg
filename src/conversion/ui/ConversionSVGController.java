package conversion.ui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.MenuElement;

public class ConversionSVGController
{
    protected MainWindow mainwindow;
    protected Converter  converter;

    public ConversionSVGController(conversion.ui.MainWindow mainwindow)
    {
        this.mainwindow = mainwindow;
    }

    public List<File> getFiles()
    {
        return null;
    }

    public void setFiles(List<File> files)
    {

    }

    public void sameOutputDirectorySelected()
    {

    }

    public void sameOutputDirectoryUnselected()
    {

    }

    public void singleOutputDirectorySelected()
    {

    }

    public void singleOutputDirectoryUnselected()
    {

    }

    public void convert()
    {
        // TODO make use of ThreadPoolExecutor()
        
        // disable GUI input to prevent mistakes
        enableInput(false);
        // getOutputFormat() for each file and merge
        //      with result from getInkscapeCommandLineOptions()
        Map<String, String> options = getInkscapeCommandlineOptions();
        for (File file : getFiles())
        {
            Map<String, String> format = getOutputFormat(file);
            // TODO fix, this will be too many
            options.putAll(format);
        }
        
        // enable GUI input
        enableInput(true);
    }

    public void cancel()
    {

    }

    public void quit()
    {
        // TODO if processes are still going (Inkscape), modal dialog for
        // confirmation to quit
        System.exit(0);
    }
    
    /**
     * Disable all user inputs to prevent unwanted/unnecessary interactions while
     * Inkscape handles converting images.
     * @param enable
     */
    public void enableInput(boolean enable)
    {
        // enable/disable all Menu input
       for (MenuElement elem : mainwindow.menubar.getSubElements())
       {
           elem.getComponent().setEnabled(enable);
       }
       
       // enable/disable the necessary JPanels
       enablePanel(mainwindow.colorPicker, enable);
       enablePanel(mainwindow.sizePanel, enable);
       enablePanel(mainwindow.outputFormatPanel, enable);
       enablePanel(mainwindow.exportAreaPanel, enable);
       
    }
    
    /**
     * Disables all the components contained in the JPanel. This is intended
     * to be used by disableInput() prior to converting.
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
        options.put("-h", mainwindow.heightTextField.getText());
        options.putAll(getExportArea());
        
        return options;
    }

    /**
     * Inkscape requires the background color to be a string in a certain format. We
     * are choosing to use the rgb(255,255,255) format.
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
        if (mainwindow.pngCheckBox.isSelected())
        {
            option.put("-e", file.getAbsolutePath());
        }
        if (mainwindow.psCheckBox.isSelected())
        {
            option.put("-P", file.getAbsolutePath());
        }
        if (mainwindow.pdfCheckBox.isSelected())
        {
            option.put("-A", file.getAbsolutePath());
        }
        if (mainwindow.epsCheckBox.isSelected())
        {
            option.put("-E", file.getAbsolutePath());
        }
        return option;
    }
}
