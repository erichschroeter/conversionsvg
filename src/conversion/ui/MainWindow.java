/*
    ConversionSVG 1.0
    Programm� par Kevin Albert
    Copyright (C) 2007-2008  Soci�t� Grics

    This file is part of ConversionSVG 1.0

    ConversionSVG 1.0 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ConversionSVG 1.0 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ConversionSVG 1.0.  If not, see <http://www.gnu.org/licenses/>.
 */

package conversion.ui;

import java.awt.BorderLayout;
import java.awt.Dimension; //import java.awt.List;
import java.awt.GridBagConstraints;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.sf.fstreem.FileSystemTreeModel;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntrySecondary;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;

import com.bric.swing.ColorPicker;
import com.jidesoft.swing.CheckBoxTree;

import conversion.ui.filters.NonHiddenDirectoryFilter;
import conversion.ui.filters.SVGFilter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainWindow extends JRibbonFrame
{
    private static final long serialVersionUID           = 6987289183119465870L;

    ResourceBundle            languageBundle;
    ResourceBundle            imageBundle;
    JSplitPane                splitPane;
    Container                 contentPane;

    // ------------------------------------------------
    // Ribbon
    // ------------------------------------------------
    RibbonApplicationMenu menuApplicationButton;
    RibbonApplicationMenuEntryPrimary saveMenuButton;
    RibbonApplicationMenuEntryPrimary aboutMenuButton;
    RibbonApplicationMenuEntryPrimary settingsMenuButton;
    RibbonApplicationMenuEntrySecondary inkscapeMenuButton;
    RibbonApplicationMenuEntryFooter quitMenuButton;
    
    RibbonTask                homeTask;
    
    JRibbonBand               controlsBand;
    JRibbonBand               preferencesBand;
    JRibbonBand               monitorBand;
    
    JCommandButton            convertButton;
    JCommandButton            cancelButton;
    JCommandButton            languageButton;
    JCommandButton            accessibilityButton;
    JCommandButton            fontButton;
    JCommandButton            shortcutsButton;
    JCommandButton            statusMonitorButton;

    // ------------------------------------------------
    // Options Panel
    // ------------------------------------------------
    JPanel                    optionsPanel               = new JPanel();

    // Output Format
    JPanel                    outputFormatPanel          = new JPanel();
    JCheckBox                 pngCheckBox                = new JCheckBox();
    JCheckBox                 psCheckBox                 = new JCheckBox();
    JCheckBox                 epsCheckBox                = new JCheckBox();
    JCheckBox                 pdfCheckBox                = new JCheckBox();
    ButtonGroup               outputFormatGroup          = new ButtonGroup();

    // Export Area
    JPanel                    exportAreaPanel            = new JPanel();
    ButtonGroup               exportAreaGroup            = new ButtonGroup();
    JRadioButton              drawingRadioButton         = new JRadioButton();
    JRadioButton              pageRadioButton            = new JRadioButton();

    // Size
    JPanel                    sizePanel                  = new JPanel();
    JLabel                    heightLabel                = new JLabel();
    JLabel                    widthLabel                 = new JLabel();
    JTextField                heightTextField            = new JTextField();
    JTextField                widthTextField             = new JTextField();
    JComboBox                 unitComboBox               = new JComboBox();
    String[]                  units                      = { "px", "mm" };

    // Background Color Picker
    ColorPicker               colorPicker                = new ColorPicker(true, true);

    // ------------------------------------------------
    // END Options Panel
    // ------------------------------------------------

    // File Select
    File					  root						 = new File(System.getProperty("user.home"));
    // these filters filter what WILL be displayed (meaning if it doesn't match one
    // of these filters, then it won't be shown)
    static final FileFilter[] heirarchy_filters			 = { new SVGFilter(), new NonHiddenDirectoryFilter() };
    static final Vector<FileFilter>	filters		 		 = new Vector<FileFilter>(Arrays.asList(heirarchy_filters));
    JPanel                    fileSelectPanel            = new JPanel();
    CheckBoxTree              fileHeirarchy;
    JButton                   changeRootButton           = new JButton();
    JRadioButton              singleOutputDirectoryRadio = new JRadioButton();
    JRadioButton              sameOutputDirectoryRadio   = new JRadioButton();
    JTextField                directoryTextField         = new JTextField();
    JTextField                outputDirectoryTextField   = new JTextField();
    JButton                   outputDirectoryEllipse     = new JButton("...");

    JLabel                    lblPercent                 = new JLabel();

    // Controller
    MainWindowController   controller;
    // Event Listener
    EventListener             eventListener              = new EventListener(this);

    public MainWindow()
    {
        try
        {
            init();
        } catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Initialisation du composant.
     * 
     * @throws java.lang.Exception
     */
    private void init() throws Exception
    {
        languageBundle = ResourceBundle.getBundle("conversion.resources.i18ln.MainWindow", Locale.getDefault());
        controller = new MainWindowController(this, languageBundle);
     
        // Main Window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("ConversionSVG");
        setMinimumSize(new Dimension(600, 350));
        setApplicationIcon(getResizableIconFromResource("conversion-svg.png"));

        GridBagConstraints constraints = new GridBagConstraints();

        // ContentPane
        contentPane = getContentPane();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0);
        splitPane.setDividerSize(9);
        splitPane.setOneTouchExpandable(true);
        contentPane.add(splitPane, BorderLayout.CENTER);

        JScrollPane optionsPanelScrollPane = new JScrollPane(optionsPanel);
        splitPane.setTopComponent(optionsPanelScrollPane);
        splitPane.setRightComponent(fileSelectPanel);

        // Options Panel
        optionsPanel.setLayout(new GridBagLayout());
        optionsPanel.setMinimumSize(new Dimension(400, 600));
        optionsPanel.setPreferredSize(new Dimension(400, 600));
        
        // (gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill,
        // insets, ipadx, ipady)
        constraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
        optionsPanel.add(outputFormatPanel, constraints);
        constraints = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
        optionsPanel.add(exportAreaPanel, constraints);
        constraints = new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
        optionsPanel.add(sizePanel, constraints);
        constraints = new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
        optionsPanel.add(colorPicker, constraints);

        // ------------------------------------------------
        // Ribbon
        // ------------------------------------------------
        menuApplicationButton = new RibbonApplicationMenu();
        
        saveMenuButton = new RibbonApplicationMenuEntryPrimary(getResizableIconFromResource("media-floppy.png"), languageBundle.getString("Save"), eventListener, CommandButtonKind.ACTION_ONLY);
        aboutMenuButton = new RibbonApplicationMenuEntryPrimary(getResizableIconFromResource("help-browser.png"), languageBundle.getString("About"), eventListener, CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        settingsMenuButton = new RibbonApplicationMenuEntryPrimary(getResizableIconFromResource("applications-system.png"), languageBundle.getString("Settings"), eventListener, CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
        inkscapeMenuButton = new RibbonApplicationMenuEntrySecondary(getResizableIconFromResource("inkscape.png"), "Inkscape", eventListener, CommandButtonKind.ACTION_ONLY);
        aboutMenuButton.addSecondaryMenuGroup("Information", inkscapeMenuButton);
        
        quitMenuButton = new RibbonApplicationMenuEntryFooter(getResizableIconFromResource("system-log-out.png"), languageBundle.getString("Quit"), eventListener);
        
        menuApplicationButton.addMenuEntry(saveMenuButton);
        menuApplicationButton.addMenuSeparator();
        menuApplicationButton.addMenuEntry(settingsMenuButton);
        menuApplicationButton.addMenuEntry(aboutMenuButton);
        menuApplicationButton.addFooterEntry(quitMenuButton);
        
        getRibbon().setApplicationMenu(menuApplicationButton);

        // Controls Band ----------------------------------
        convertButton = new JCommandButton(languageBundle.getString("ConvertButton"), getResizableIconFromResource("go-next.png"));
        cancelButton = new JCommandButton(languageBundle.getString("Cancel"), getResizableIconFromResource("process-stop.png"));

        controlsBand = new JRibbonBand(languageBundle.getString("ControlsRibbonBand"), getApplicationIcon());
        controlsBand.addCommandButton(convertButton, RibbonElementPriority.TOP);
        controlsBand.addCommandButton(cancelButton, RibbonElementPriority.TOP);

        List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
        resizePolicies.add(new CoreRibbonResizePolicies.Mirror(controlsBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.High2Mid(controlsBand.getControlPanel()));
        controlsBand.setResizePolicies(resizePolicies);
        // END --------------------------------------------
        
        // Preferences Band -------------------------------
        languageButton = new JCommandButton(languageBundle.getString("LanguageButton"), getResizableIconFromResource("preferences-desktop-locale.png"));
        languageButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
        languageButton.setPopupCallback(new PopupPanelCallback() {

            @Override
            public JPopupPanel getPopupPanel(JCommandButton commandButton)
            {
                JCommandPopupMenu menu = new JCommandPopupMenu();
                menu.addMenuButton(createLanguageButton(languageBundle.getString("en"), Locale.US));
                menu.addMenuButton(createLanguageButton(languageBundle.getString("fr"), Locale.FRANCE));
                return menu;
            }

            private JCommandMenuButton createLanguageButton(
                    String languageText, Locale locale)
            {
                final Locale finalLocale = locale; // needed for the inner class
                // NOTE this method depends on a standard naming convention of
                // the flag image
                JCommandMenuButton button = new JCommandMenuButton(languageText, getResizableIconFromResource("flag-" + locale.getCountry().toLowerCase() + ".png"));
                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        controller.changeLanguage(finalLocale);
                    }
                });
                return button;
            }
        });
        accessibilityButton = new JCommandButton(languageBundle.getString("AccessibilityButton"), getResizableIconFromResource("preferences-desktop-accessibility.png"));
        fontButton = new JCommandButton(languageBundle.getString("FontButton"), getResizableIconFromResource("preferences-desktop-font.png"));
        shortcutsButton = new JCommandButton(languageBundle.getString("ShortcutsButton"), getResizableIconFromResource("preferences-desktop-keyboard-shortcuts.png"));
        
        // -----------------------------------------------
        // Remove button disabling once functionality is implemented
        // -----------------------------------------------
        accessibilityButton.setEnabled(false);
        fontButton.setEnabled(false);
        shortcutsButton.setEnabled(false);

        preferencesBand = new JRibbonBand(languageBundle.getString("PreferencesRibbonBand"), getApplicationIcon());
        preferencesBand.addCommandButton(languageButton, RibbonElementPriority.TOP);
        preferencesBand.addCommandButton(accessibilityButton, RibbonElementPriority.MEDIUM);
        preferencesBand.addCommandButton(fontButton, RibbonElementPriority.MEDIUM);
        preferencesBand.addCommandButton(shortcutsButton, RibbonElementPriority.MEDIUM);

        resizePolicies = new ArrayList<RibbonBandResizePolicy>();
        resizePolicies.add(new CoreRibbonResizePolicies.Mirror(preferencesBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.High2Mid(preferencesBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(preferencesBand.getControlPanel()));
        preferencesBand.setResizePolicies(resizePolicies);
        // END --------------------------------------------

        // Monitor Band -----------------------------------
        statusMonitorButton = new JCommandButton(languageBundle.getString("StatusMonitorButton"), getResizableIconFromResource("utilities-system-monitor.png"));

        monitorBand = new JRibbonBand(languageBundle.getString("MonitorRibbonBand"), getApplicationIcon());
        monitorBand.addCommandButton(statusMonitorButton, RibbonElementPriority.TOP);

        // -----------------------------------------------
        // Remove button disabling once functionality is implemented
        // -----------------------------------------------
        statusMonitorButton.setEnabled(false);
        
        resizePolicies = new ArrayList<RibbonBandResizePolicy>();
        resizePolicies.add(new CoreRibbonResizePolicies.Mirror(monitorBand.getControlPanel()));
        resizePolicies.add(new CoreRibbonResizePolicies.Mid2Low(monitorBand.getControlPanel()));
        monitorBand.setResizePolicies(resizePolicies);        
        // END --------------------------------------------
        
        homeTask = new RibbonTask(languageBundle.getString("HomeRibbonTask"), controlsBand, preferencesBand, monitorBand);
        getRibbon().addTask(homeTask);
        // ------------------------------------------------
        // END Ribbon
        // ------------------------------------------------

        // Output Format
        GroupLayout layout = new GroupLayout(outputFormatPanel);
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pngCheckBox).addComponent(pdfCheckBox)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(epsCheckBox).addComponent(psCheckBox)));
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pngCheckBox).addComponent(epsCheckBox)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pdfCheckBox).addComponent(psCheckBox)));
        outputFormatPanel.setLayout(layout);
        outputFormatPanel.setBorder(BorderFactory.createTitledBorder(languageBundle.getString("FormatPanel")));

        pngCheckBox.setSelected(true);
        pngCheckBox.setText(".PNG");
        psCheckBox.setText(".PS");
        epsCheckBox.setText(".EPS");
        pdfCheckBox.setText(".PDF");

        outputFormatPanel.add(epsCheckBox);
        outputFormatPanel.add(pngCheckBox);
        outputFormatPanel.add(psCheckBox);
        outputFormatPanel.add(pdfCheckBox);

        // Export Area
        exportAreaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        pageRadioButton.setText(languageBundle.getString("PageRadioButton"));
        drawingRadioButton.setText(languageBundle.getString("DrawingRadioButton"));
        exportAreaGroup.add(pageRadioButton);
        exportAreaGroup.add(drawingRadioButton);

        drawingRadioButton.setSelected(true);
        exportAreaPanel.add(drawingRadioButton);
        exportAreaPanel.add(pageRadioButton);
        exportAreaPanel.setBorder(BorderFactory.createTitledBorder(languageBundle.getString("ExportAreaPanel")));

        // Size
        heightLabel.setText(languageBundle.getString("HeightTextField"));
        widthLabel.setText(languageBundle.getString("WidthTextField"));
        heightTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        widthTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        heightTextField.setColumns(10);

        ComboBoxModel unitModel = new DefaultComboBoxModel(units);
        unitComboBox.setModel(unitModel);
        unitComboBox.setEnabled(false);

        // (gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill,
        // insets, ipadx, ipady)
        sizePanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
        sizePanel.add(heightLabel, constraints);
        constraints = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
        sizePanel.add(widthLabel, constraints);
        constraints = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
        sizePanel.add(heightTextField, constraints);
        constraints = new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
        sizePanel.add(widthTextField, constraints);
        constraints = new GridBagConstraints(2, 0, 1, 2, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
        sizePanel.add(unitComboBox, constraints);
        // and a spacer to make everything anchor to the EAST
        constraints = new GridBagConstraints(3, 0, 1, 2, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
        sizePanel.add(new JPanel(), constraints);

        sizePanel.setBorder(BorderFactory.createTitledBorder(languageBundle.getString("SizePanel")));

        // Color Picker
        colorPicker.setColor(Color.WHITE);
        colorPicker.setBorder(BorderFactory.createTitledBorder(languageBundle.getString("BackgroundPanel")));
        colorPicker.setEnabled(false);

        // ------------------------------------------------
        // File Select
        // ------------------------------------------------
        FileSystemTreeModel model = new FileSystemTreeModel(root, filters);
        fileHeirarchy = new CheckBoxTree(model);
        JScrollPane fileSelectScrollPane = new JScrollPane(fileHeirarchy);

        fileSelectPanel.setMinimumSize(new Dimension(200, 500));
        fileSelectPanel.setPreferredSize(new Dimension(400, 600));
        fileSelectPanel.setLayout(new GridBagLayout());

        // add the output directory options
        // - same directory as each file
        // - a single directory
        ButtonGroup outputDirectoryGroup = new ButtonGroup();
        outputDirectoryGroup.add(singleOutputDirectoryRadio);
        outputDirectoryGroup.add(sameOutputDirectoryRadio);
        sameOutputDirectoryRadio.setSelected(true);
        singleOutputDirectoryRadio.addChangeListener(eventListener);
        changeRootButton.setText("...");
        changeRootButton.addActionListener(eventListener);

        // (gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady)
        constraints = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        fileSelectPanel.add(changeRootButton, constraints);
        constraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        fileSelectPanel.add(directoryTextField, constraints);
        constraints = new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);
        fileSelectPanel.add(fileSelectScrollPane, constraints);
        constraints = new GridBagConstraints(0, 2, 2, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        fileSelectPanel.add(sameOutputDirectoryRadio, constraints);
        constraints = new GridBagConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        fileSelectPanel.add(singleOutputDirectoryRadio, constraints);
        constraints = new GridBagConstraints(0, 4, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        fileSelectPanel.add(outputDirectoryTextField, constraints);
        constraints = new GridBagConstraints(1, 4, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        fileSelectPanel.add(outputDirectoryEllipse, constraints);

        sameOutputDirectoryRadio.setText(languageBundle.getString("SameDirectoryRadioButton"));
        sameOutputDirectoryRadio.setEnabled(true);
        singleOutputDirectoryRadio.setText(languageBundle.getString("SingleDirectoryRadioButton"));
        checkSingleOutputDirectoryOption(false);
        // END File Select --------------------------------

        // Status Information
//        lblPercent.setHorizontalAlignment(SwingConstants.CENTER);
//        lblPercent.setText("100%");
//        numberOfImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
//
//        statusBarPanel.setLayout(new GridBagLayout());
//
//        numberOfImageLabel.setBorder(BorderFactory.createLoweredBevelBorder());
//        numberOfImageLabel.setText("0/100");
//        nameOfImageLabel.setBorder(BorderFactory.createLoweredBevelBorder());
//        nameOfImageLabel.setText("/home/erich/Public/ConversionSVG/characature.svg");
//        constraints = new GridBagConstraints(0, 0, 1, 1, 0.1, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
//        statusBarPanel.add(numberOfImageLabel, constraints);
//        constraints = new GridBagConstraints(1, 0, 1, 1, 0.9, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
//        statusBarPanel.add(nameOfImageLabel, constraints);

        // Event Listeners
        convertButton.addActionListener(eventListener);
        cancelButton.addActionListener(eventListener);
        statusMonitorButton.addActionListener(eventListener);

        // File Select
        // fileHeirarchy.addTreeSelectionListener(eventListener);
        // fileHeirarchy.addTreeSelectionListener(eventListener);
        fileHeirarchy.getCheckBoxTreeSelectionModel().addTreeSelectionListener(eventListener);
        widthTextField.addKeyListener(new NumberKeyAdapter(this));
        heightTextField.addKeyListener(new NumberKeyAdapter(this));
        singleOutputDirectoryRadio.addActionListener(eventListener);
        sameOutputDirectoryRadio.addActionListener(eventListener);
        outputDirectoryEllipse.addActionListener(eventListener);

        pack();
    }

    public void checkSingleOutputDirectoryOption(boolean enable)
    {
        if (singleOutputDirectoryRadio.isSelected())
        {
            outputDirectoryEllipse.setEnabled(true);
            outputDirectoryTextField.setEnabled(true);
        } else
        {
            outputDirectoryEllipse.setEnabled(false);
            outputDirectoryTextField.setEnabled(false);
        }
    }

    public ResizableIcon getResizableIconFromResource(String resource)
    {
        return ImageWrapperResizableIcon.getIcon(MainWindow.class.getClassLoader().getResource("conversion/resources/"
                + resource), new Dimension(48, 48));
    }

    /**
     * Private class to handle the MainWindow events
     * 
     * @author Erich Schroeter
     */
    private class EventListener implements ActionListener,
            TreeSelectionListener, ChangeListener
    {
        MainWindow mainwindow;

        public EventListener(MainWindow mainwindow)
        {
            this.mainwindow = mainwindow;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object obj = e.getSource();

            // There must be a bug in Flamingo's comparison implementation. Comparing obj.getSource()
            // to a JCommandMenuButton does not work, so this is the only way I can get it to work
            if (obj.getClass() == JCommandMenuButton.class) {
            	JCommandMenuButton btn = (JCommandMenuButton) obj;
            	if (btn.getText().equals(settingsMenuButton.getText())) {
            		controller.showSettingsDialog();
            	}
            }
            // File Select
            if (obj.equals(singleOutputDirectoryRadio))
            {
                // handle set flag to only allow one output directory

            } else if (obj.equals(sameOutputDirectoryRadio))
            {
                // handle exporting all images to their respective same
                // directory
                // sameOutputDir_actionPerformed()

            } else if (obj.equals(outputDirectoryEllipse))
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showSaveDialog(mainwindow) == JFileChooser.APPROVE_OPTION)
                {
                    outputDirectoryTextField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            } else if (obj.equals(changeRootButton))
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showSaveDialog(mainwindow) == JFileChooser.APPROVE_OPTION)
                {
                    controller.changeRoot(chooser.getSelectedFile());
                }
            }

            // Output Format
            else if (obj.equals(pngCheckBox))
            {
                // TODO handle updating whoever needs to know what else to
                // export
            } else if (obj.equals(pdfCheckBox))
            {
                // TODO handle updating whoever needs to know what else to
                // export
            } else if (obj.equals(epsCheckBox))
            {
                // TODO handle updating whoever needs to know what else to
                // export
            } else if (obj.equals(psCheckBox))
            {
                // TODO handle updating whoever needs to know what else to
                // export
            }

            // Export Area
            else if (obj.equals(drawingRadioButton))
            {
                // TODO handle updating converter with the switch
            } else if (obj.equals(pageRadioButton))
            {
                // TODO handle updating converter with the switch
            }

            // Size
            else if (obj.equals(unitComboBox))
            {
                // TODO handle updating converter with the switch(es)
            }

            // Ribbon
            else if (obj.equals(convertButton))
            {
                controller.convert();
            } else if (obj.equals(cancelButton))
            {
                controller.cancel();
            } else if (obj.equals(statusMonitorButton))
            {
                controller.showStatus();
            }
        }

        @Override
        public void valueChanged(TreeSelectionEvent e)
        {
            // TODO if singleDirectoryCheckbox isSelected() then set output
            // directory to e.getPath();
            // controller.setFiles(files)
//            for (TreePath path : e.getPaths())
//            {
//                System.out.println(path);
//            }
        }

        @Override
        public void stateChanged(ChangeEvent e)
        {
            Object obj = e.getSource();
            if (obj.equals(singleOutputDirectoryRadio))
            {
                mainwindow.checkSingleOutputDirectoryOption(singleOutputDirectoryRadio.isSelected());
            }
        }
    }
}

class NumberKeyAdapter extends KeyAdapter
{
//    private MainWindow adaptee;

    NumberKeyAdapter(MainWindow adaptee)
    {
//        this.adaptee = adaptee;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        Pattern p = Pattern.compile("\\p{Digit}");
        Matcher m = p.matcher(String.valueOf(e.getKeyChar()));
        if (!m.matches())
        {
            e.consume();
        }
    }

    public void handleNumeric(KeyEvent e)
    {
        if (e.getKeyChar() == '.')
        {
            e.consume();
        } else if ((e.getKeyChar() != '0') && (e.getKeyChar() != '1')
                && (e.getKeyChar() != '2') && (e.getKeyChar() != '3')
                && (e.getKeyChar() != '4') && (e.getKeyChar() != '5')
                && (e.getKeyChar() != '6') && (e.getKeyChar() != '7')
                && (e.getKeyChar() != '8') && (e.getKeyChar() != '9')
                && (e.getKeyChar() != '\b') && (e.getKeyChar() != '.'))
        {
            e.consume();
        }
    }
}
