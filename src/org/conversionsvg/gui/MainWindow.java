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

package org.conversionsvg.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import net.sf.fstreem.FileSystemTreeModel;

import org.apache.log4j.Logger;
import org.conversionsvg.util.Helpers;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntrySecondary;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;

import com.bric.swing.ColorPicker;
import com.jidesoft.hints.FileIntelliHints;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.SelectAllUtils;

import org.conversionsvg.gui.filters.NonHiddenDirectoryFilter;
import org.conversionsvg.gui.filters.SVGFilter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class MainWindow extends JRibbonFrame implements IProgressListener {

	static final Logger logger = Logger.getLogger(MainWindow.class);
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.MainWindow", Locale.getDefault());

	/** The controller to handle business logic */
	private IMainWindowController controller;
	/** Used to export image as a PNG */
	private JCheckBox pngCheckBox;
	/** Used to export image as a PS */
	private JCheckBox psCheckBox;
	/** Used to export image as a EPS */
	private JCheckBox epsCheckBox;
	/** Used to export image as a PDF */
	private JCheckBox pdfCheckBox;
	/** Contains the export format option Components */
	private JPanel formatPanel;
	/** Used to specify the height of the image. */
	private JTextField heightTextField;
	/** Used to specify the width of the image. */
	private JTextField widthTextField;
	/** Contains the export size option Components */
	private JPanel sizePanel;
	/** Used to export the image drawing area */
	private JRadioButton drawingRadioButton;
	/** Used to export the image page area */
	private JRadioButton pageRadioButton;
	/** Contains the export area option Components */
	private JPanel areaPanel;
	/** Contains the file selection Components */
	private JPanel filePanel;
	/** Contains the export color option Components */
	private ColorPicker colorPicker;
	/** Used to initialize {@link #filters}. */
	private static final FileFilter[] heirarchy_filters = { new SVGFilter() };
	/**
	 * These filters filter what <i>WILL</i> be displayed (meaning if it doesn't
	 * match one of these filters, then it won't be shown)
	 */
	public static final Vector<FileFilter> filters = new Vector<FileFilter>(
			Arrays.asList(heirarchy_filters));
	/** The root directory for the {@link #fileHeirarchy}. */
	private File root = new File(System.getProperty("user.home"));
	/** Displays available directories and files to select for conversion. */
	private CheckBoxTree fileHeirarchy;
	/** Opens a {@link JFileChooser} dialog to select the root directory. */
	private JButton changeRootButton;
	/** Used to export files in a single directory. */
	private JRadioButton singleOutputDirectoryRadio;
	/** Used to export files to the same directory as the source file. */
	private JRadioButton sameOutputDirectoryRadio;
	/** Displays the root directory for the file tree hierarchy. */
	private JTextField rootDirectoryTextField;
	/** Displays the output directory for converted files. */
	private JTextField outputDirectoryTextField;
	/** Opens a {@link JFileChooser} dialog to select the output directory */
	private JButton outputDirectoryEllipse;
	/** The progress bar which displays the progress of the conversion. */
	private JProgressBar progressBar;
	/** Used as the progress indicator for the {@link #progressBar}. */
	private int completedProcesses = 0;

	public MainWindow(IMainWindowController controller) {
		this.controller = controller;
		try {
			init();
		} catch (Exception e) {
			logger.error(e.getMessage());

			e.printStackTrace();
		}
	}

	/**
	 * Initializes the <code>MainWindow</code> graphical components.
	 * 
	 * <pre>
	 * _____________________________________________________________
	 * |															|
	 * |						Ribbon								|
	 * |____________________________________________________________|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |		Options				|		File Hierarchy			|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |							|								|
	 * |____________________________|_______________________________|
	 * |______________________Progress Bar__________________________|
	 * 
	 * <pre>
	 * 
	 * @throws java.lang.Exception
	 */
	private void init() throws Exception {
		// Main Window
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("ConversionSVG");
		setMinimumSize(new Dimension(600, 350));
		setApplicationIcon(Helpers
				.getResizableIconFromURL("res/images/conversion-svg.png"));

		//
		// Ribbon
		//
		getRibbon().setApplicationMenu(getRibbonMenu());

		JRibbonBand controlsBand = new ControlsRibbonBand(this, "Controls",
				getApplicationIcon());

		// add all bands to task
		RibbonTask homeTask = new RibbonTask(i18ln.getString("HomeRibbonTask"),
				controlsBand);
		getRibbon().addTask(homeTask);

		// Option panels
		formatPanel = getFormatPanel();
		sizePanel = getSizePanel();
		areaPanel = getAreaPanel();
		colorPicker = getColorPicker();

		// File panel
		filePanel = getFilePanel();

		// All option panels
		JPanel optionsPanel = new JPanel(new GridBagLayout());
		optionsPanel.setMinimumSize(new Dimension(400, 600));
		optionsPanel.setPreferredSize(new Dimension(400, 600));

		GridBagConstraints c;
		c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0);
		optionsPanel.add(formatPanel, c);
		c = new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0);
		optionsPanel.add(areaPanel, c);
		c = new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0);
		optionsPanel.add(sizePanel, c);
		c = new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0);
		optionsPanel.add(colorPicker, c);

		// SplitPane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0);
		splitPane.setDividerSize(9);
		splitPane.setOneTouchExpandable(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		splitPane.setTopComponent(new JScrollPane(optionsPanel));
		splitPane.setRightComponent(filePanel);

		// Progress
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.BLACK);
		controller.addProgressListener(this);
		getContentPane().add(progressBar, BorderLayout.SOUTH);

		pack();
	}

	/**
	 * Returns a <code>JPanel</code> containing the file components for
	 * selecting the SVG files to convert..
	 * <p>
	 * If {@link #filePanel} is <code>null</code> a new <code>JPanel</code> is
	 * created.
	 * </p>
	 * 
	 * @return the panel with file select components
	 */
	public JPanel getFilePanel() {
		if (filePanel == null) {
			// Tree Hierarchy
			FileSystemTreeModel model = new FileSystemTreeModel(root, filters);
			fileHeirarchy = new CheckBoxTree(model);
			JScrollPane fileSelectScrollPane = new JScrollPane(fileHeirarchy);

			// implement FileIntelliHints
			rootDirectoryTextField = new JTextField();
			rootDirectoryTextField.getDocument().addDocumentListener(
					rootDirectoryDocumentListener());
			FileIntelliHints rootIntellisense = new FileIntelliHints(
					rootDirectoryTextField);
			rootIntellisense.setFolderOnly(true);
			SelectAllUtils.install(rootDirectoryTextField);

			JPanel panel = new JPanel();
			panel.setMinimumSize(new Dimension(200, 500));
			panel.setPreferredSize(new Dimension(400, 600));
			panel.setLayout(new GridBagLayout());

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
			singleOutputDirectoryRadio
					.addChangeListener(singleDirChangeListener());
			changeRootButton = new JButton("...");
			changeRootButton.addActionListener(changeRootActionListener());

			GridBagConstraints c;
			c = new GridBagConstraints(1, 0, 1, 1, 0, 0,
					GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
			panel.add(changeRootButton, c);
			c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
					GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
			panel.add(rootDirectoryTextField, c);
			c = new GridBagConstraints(0, 1, 2, 1, 1, 1,
					GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0);
			panel.add(fileSelectScrollPane, c);
			c = new GridBagConstraints(0, 2, 2, 1, 0, 0,
					GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
			panel.add(sameOutputDirectoryRadio, c);
			c = new GridBagConstraints(0, 3, 2, 1, 0, 0,
					GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
			panel.add(singleOutputDirectoryRadio, c);
			c = new GridBagConstraints(0, 4, 1, 1, 1, 0,
					GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
			outputDirectoryTextField = new JTextField();
			outputDirectoryTextField.getDocument().addDocumentListener(
					outputDirDocumentListener());
			panel.add(outputDirectoryTextField, c);
			c = new GridBagConstraints(1, 4, 1, 1, 0, 0,
					GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
			outputDirectoryEllipse = new JButton("...");
			outputDirectoryEllipse.addActionListener(outputDirActionListener());
			panel.add(outputDirectoryEllipse, c);

			sameOutputDirectoryRadio.setEnabled(true);
			setOutputDirectorEnabled(false);
			return panel;
		} else {
			return filePanel;
		}
	}

	/**
	 * Returns the application menu
	 * 
	 * @return
	 */
	public RibbonApplicationMenu getRibbonMenu() {
		RibbonApplicationMenu menu = new RibbonApplicationMenu();

		RibbonApplicationMenuEntryPrimary saveMenuButton = new RibbonApplicationMenuEntryPrimary(
				Helpers.getResizableIconFromURL("res/images/media-floppy.png"),
				i18ln.getString("Save"), saveActionListener(),
				CommandButtonKind.ACTION_ONLY);
		RibbonApplicationMenuEntryPrimary aboutMenuButton = new RibbonApplicationMenuEntryPrimary(
				Helpers.getResizableIconFromURL("res/images/help-browser.png"),
				i18ln.getString("About"), aboutActionListener(),
				CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		RibbonApplicationMenuEntryPrimary settingsMenuButton = new RibbonApplicationMenuEntryPrimary(
				Helpers
						.getResizableIconFromURL("res/images/applications-system.png"),
				i18ln.getString("Settings"), settingsActionListener(),
				CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		RibbonApplicationMenuEntrySecondary inkscapeMenuButton = new RibbonApplicationMenuEntrySecondary(
				Helpers.getResizableIconFromURL("res/images/inkscape.png"),
				"Inkscape", inkscapeActionListener(),
				CommandButtonKind.ACTION_ONLY);
		aboutMenuButton
				.addSecondaryMenuGroup("Information", inkscapeMenuButton);

		RibbonApplicationMenuEntryFooter quitMenuButton = new RibbonApplicationMenuEntryFooter(
				Helpers
						.getResizableIconFromURL("res/images/system-log-out.png"),
				i18ln.getString("Quit"), quitActionListener());

		menu.addMenuEntry(saveMenuButton);
		menu.addMenuSeparator();
		menu.addMenuEntry(settingsMenuButton);
		menu.addMenuEntry(aboutMenuButton);
		menu.addFooterEntry(quitMenuButton);

		return menu;
	}

	/**
	 * Returns a <code>ColorPicker</code> components for selecting the
	 * background color and opacity supported by Inkscape.
	 * <p>
	 * If {@link #colorPicker} is <code>null</code> a new <code>JPanel</code> is
	 * created.
	 * </p>
	 * 
	 * @return the color picker
	 */
	public ColorPicker getColorPicker() {
		if (colorPicker == null) {
			ColorPicker picker = new ColorPicker(true, true);
			// Color Picker
			picker.setColor(Color.WHITE);
			picker.setBorder(BorderFactory.createTitledBorder(i18ln
					.getString("BackgroundPanel")));
			picker.setEnabled(false);

			return picker;
		} else {
			return colorPicker;
		}
	}

	/**
	 * Returns a <code>JPanel</code> containing the export area options
	 * components (i.e. <code>JTextField</code>'s) for export area options
	 * supported by Inkscape.
	 * <p>
	 * If {@link #areaPanel} is <code>null</code> a new <code>JPanel</code> is
	 * created.
	 * </p>
	 * 
	 * @return the panel with export area options
	 */
	public JPanel getAreaPanel() {
		if (areaPanel == null) {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

			drawingRadioButton = new JRadioButton(i18ln
					.getString("DrawingRadioButton"), true);
			drawingRadioButton
					.setToolTipText("Exported area is the entire drawing (not page)");
			pageRadioButton = new JRadioButton(i18ln
					.getString("PageRadioButton"));
			pageRadioButton.setToolTipText("Exported area is the entire page");

			ButtonGroup group = new ButtonGroup();
			group.add(pageRadioButton);
			group.add(drawingRadioButton);

			panel.add(drawingRadioButton);
			panel.add(pageRadioButton);
			panel.setBorder(BorderFactory.createTitledBorder(i18ln
					.getString("ExportAreaPanel")));

			return panel;
		} else {
			return areaPanel;
		}
	}

	/**
	 * Returns a <code>JPanel</code> containing the size options components
	 * (i.e. <code>JTextField</code>'s) for size options supported by Inkscape.
	 * <p>
	 * If {@link #sizePanel} is <code>null</code> a new <code>JPanel</code> is
	 * created.
	 * </p>
	 * 
	 * @return the panel with export size options
	 */
	public JPanel getSizePanel() {
		if (sizePanel == null) {
			JPanel panel = new JPanel(new GridBagLayout());

			JLabel heightLabel = new JLabel(i18ln.getString("HeightTextField"));
			heightLabel.setToolTipText("Height of the exported image");
			JLabel widthLabel = new JLabel(i18ln.getString("WidthTextField"));
			widthLabel.setToolTipText("Width of the exported image");
			
			heightTextField = new JTextField(5);
			heightTextField.addKeyListener(new NumberKeyAdapter(this));
			heightTextField.setBorder(BorderFactory.createLoweredBevelBorder());
			widthTextField = new JTextField(5);
			widthTextField.addKeyListener(new NumberKeyAdapter(this));
			widthTextField.setBorder(BorderFactory.createLoweredBevelBorder());

			String[] units = { "px", "mm" };
			JComboBox unitComboBox = new JComboBox(units);
			unitComboBox.setEnabled(false);

			GridBagConstraints c;

			c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0);
			panel.add(heightLabel, c);
			c = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0);
			panel.add(widthLabel, c);
			c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
			panel.add(heightTextField, c);
			c = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHWEST,
					GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
			panel.add(widthTextField, c);
			c = new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(5, 5, 5, 5), 0, 0);
			panel.add(unitComboBox, c);
			// and a spacer to make everything anchor to the EAST
			c = new GridBagConstraints(3, 0, 1, 2, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(5, 5, 5, 5), 0, 0);
			panel.add(new JPanel(), c);

			panel.setBorder(BorderFactory.createTitledBorder(i18ln
					.getString("SizePanel")));

			return panel;
		} else {
			return sizePanel;
		}
	}

	/**
	 * Returns a <code>JPanel</code> containing the format options components
	 * (i.e. <code>JCheckBox</code>'s) for each output format supported by
	 * Inkscape.
	 * <p>
	 * If {@link #formatPanel} is <code>null</code> a new <code>JPanel</code> is
	 * created.
	 * </p>
	 * 
	 * @return the panel with export format options
	 */
	public JPanel getFormatPanel() {
		if (formatPanel == null) {
			JPanel panel = new JPanel();
			GroupLayout layout = new GroupLayout(panel);

			pngCheckBox = new JCheckBox(".PNG", true);
			psCheckBox = new JCheckBox(".PS");
			epsCheckBox = new JCheckBox(".EPS");
			pdfCheckBox = new JCheckBox(".PDF");

			layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(pngCheckBox)
							.addComponent(pdfCheckBox))
					.addGroup(
							layout.createParallelGroup(
									GroupLayout.Alignment.LEADING)
									.addComponent(epsCheckBox).addComponent(
											psCheckBox)));
			layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(pngCheckBox)
							.addComponent(epsCheckBox))
					.addGroup(
							layout.createParallelGroup(
									GroupLayout.Alignment.LEADING)
									.addComponent(pdfCheckBox).addComponent(
											psCheckBox)));
			panel.setLayout(layout);
			panel.setBorder(BorderFactory.createTitledBorder(i18ln
					.getString("FormatPanel")));

			panel.add(epsCheckBox);
			panel.add(pngCheckBox);
			panel.add(psCheckBox);
			panel.add(pdfCheckBox);

			return panel;
		} else {
			return formatPanel;
		}
	}

	/**
	 * Sets the {@link #completedProcesses} to <code>0</code>.
	 */
	public void resetProgressBar() {
		completedProcesses = 0;
	}

	@Override
	public void updateProgressBar(InkscapeProcessInfo info) {
		completedProcesses++;

		File file;
		if (info.command.contains("-e")) {
			file = new File(info.command.get(info.command.indexOf("-e") + 1));
			logger.info(file.getAbsoluteFile());
		}
		if (info.command.contains("-E")) {
			file = new File(info.command.get(info.command.indexOf("-E") + 1));
			logger.info(file.getAbsoluteFile());
		}
		if (info.command.contains("-A")) {
			file = new File(info.command.get(info.command.indexOf("-A") + 1));
			logger.info(file.getAbsoluteFile());
		}
		if (info.command.contains("-P")) {
			file = new File(info.command.get(info.command.indexOf("-P") + 1));
			logger.info(file.getAbsoluteFile());
		}
		progressBar.setString(completedProcesses + " / "
				+ progressBar.getMaximum());
		progressBar.setValue(completedProcesses);
	}

	/**
	 * Either enables or disables {@link #outputDirectoryTextField} and
	 * {@link #outputDirectoryEllipse} based on <code>enable</code>.
	 * 
	 * @param enable
	 *            boolean to enable or disable
	 */
	public void setOutputDirectorEnabled(boolean enable) {
		outputDirectoryEllipse.setEnabled(enable);
		outputDirectoryTextField.setEnabled(enable);
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
		for (Component c : getComponents()) {
			if (c instanceof JPanel) {
				JPanel panel = (JPanel) c;
				enablePanel(panel, enable);
			}
		}
		// enable/disable the necessary JPanels
		// enablePanel(colorPicker, enable);
		// enablePanel(sizePanel, enable);
		// unitComboBox.setEnabled(false);
		// // Special case until it is actually implemented
		// enablePanel(outputFormatPanel, enable);
		// enablePanel(exportAreaPanel, enable);
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
	 * Returns this <code>MainWindow</code> instance.
	 * <p>
	 * This is intended to be used by the <code>private</code> functions which
	 * return listeners for the graphical components.
	 * </p>
	 * 
	 * @return this instance
	 */
	private MainWindow getInstance() {
		return this;
	}

	//
	// Action Listeners
	//

	public JCheckBox getPngCheckBox() {
		return pngCheckBox;
	}

	public IMainWindowController getController() {
		return controller;
	}

	public JCheckBox getPsCheckBox() {
		return psCheckBox;
	}

	public JCheckBox getEpsCheckBox() {
		return epsCheckBox;
	}

	public JCheckBox getPdfCheckBox() {
		return pdfCheckBox;
	}

	public JTextField getHeightTextField() {
		return heightTextField;
	}

	public JTextField getWidthTextField() {
		return widthTextField;
	}

	public JRadioButton getDrawingRadioButton() {
		return drawingRadioButton;
	}

	public JRadioButton getPageRadioButton() {
		return pageRadioButton;
	}

	public CheckBoxTree getFileHeirarchy() {
		return fileHeirarchy;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @return a <code>DocumentListener</code> to handle changing the
	 *         {@link #root} directory and calling
	 *         {@link IMainWindowController#handleChangeRoot(CheckBoxTree, File)}
	 *         .
	 */
	private DocumentListener rootDirectoryDocumentListener() {
		return new DocumentListener() {

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
						root = newRoot;
						controller.handleChangeRoot(fileHeirarchy, root);
					}
				} catch (BadLocationException e1) {
					logger.error(e1.getMessage());
				}
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which handles calling the
	 *         <i>controller</i>'s <code>handleSave()</code> function.
	 */
	private ActionListener saveActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.handleSave();
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which handles calling the
	 *         <i>controller</i>'s <code>handleQuit()</code> function.
	 */
	private ActionListener quitActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.handleQuit();
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which handles calling the
	 *         <i>controller</i>'s <code>handleSettings()</code> function.
	 */
	private ActionListener settingsActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.handleSettings();
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which handles calling the
	 *         <i>controller</i>'s <code>handleAbout()</code> function.
	 */
	private ActionListener aboutActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.handleAbout();
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which handles calling the
	 *         <i>controller</i>'s <code>openInkscapeWebsite()</code> function.
	 */
	private ActionListener inkscapeActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				logger.debug("TODO\t" + "open browser to inkscape webpage");
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which handles showing a
	 *         <code>JFileChooser</code> and setting the
	 *         <i>outputDirectoryTextField</i> text to the selected directory.
	 */
	private ActionListener outputDirActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showSaveDialog(getInstance()) == JFileChooser.APPROVE_OPTION) {
					outputDirectoryTextField.setText(chooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		};
	}

	/**
	 * @return a <code>DocumentListener</code> which set the single output
	 *         directory in the {@link MainWindowController}.
	 */
	private DocumentListener outputDirDocumentListener() {
		return new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateSingleOutputDir();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateSingleOutputDir();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateSingleOutputDir();
			}

			private void updateSingleOutputDir() {
				File newOutputDir = new File(outputDirectoryTextField.getText());
				if (newOutputDir.exists() && newOutputDir.isDirectory()) {
					MainWindowController.setSingleOutputDirectory(newOutputDir);
				}
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which handles showing a
	 *         <code>JFileChooser</code> and calling the <i>controller</i>'s
	 *         <code>handleChangeRoot()</code> function passing the selected
	 *         directory as the argument.
	 */
	private ActionListener changeRootActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showSaveDialog(getInstance()) == JFileChooser.APPROVE_OPTION) {
					root = chooser.getSelectedFile();
					controller.handleChangeRoot(fileHeirarchy, root);
				}
			}
		};
	}

	/**
	 * @return an <code>ChangeListener</code> which handles calling
	 *         {@link #setOutputDirectorEnabled(boolean)}
	 */
	private ChangeListener singleDirChangeListener() {
		return new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Object obj = e.getSource();
				if (obj.equals(singleOutputDirectoryRadio)) {
					setOutputDirectorEnabled(singleOutputDirectoryRadio
							.isSelected());
				}
			}
		};
	}

}

class NumberKeyAdapter extends KeyAdapter {
	// private MainWindow adaptee;

	NumberKeyAdapter(MainWindow adaptee) {
		// this.adaptee = adaptee;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Pattern p = Pattern.compile("\\p{Digit}");
		Matcher m = p.matcher(String.valueOf(e.getKeyChar()));
		if (!m.matches()) {
			e.consume();
		}
	}

	public void handleNumeric(KeyEvent e) {
		if (e.getKeyChar() == '.') {
			e.consume();
		} else if ((e.getKeyChar() != '0') && (e.getKeyChar() != '1')
				&& (e.getKeyChar() != '2') && (e.getKeyChar() != '3')
				&& (e.getKeyChar() != '4') && (e.getKeyChar() != '5')
				&& (e.getKeyChar() != '6') && (e.getKeyChar() != '7')
				&& (e.getKeyChar() != '8') && (e.getKeyChar() != '9')
				&& (e.getKeyChar() != '\b') && (e.getKeyChar() != '.')) {
			e.consume();
		}
	}
}
