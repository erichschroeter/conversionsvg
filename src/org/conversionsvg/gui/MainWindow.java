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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	IMainWindowController controller;

	//
	// Options
	//

	// Options Panel
	JPanel optionsPanel = new JPanel();

	// Output Format
	JPanel outputFormatPanel = new JPanel();
	JCheckBox pngCheckBox = new JCheckBox();
	JCheckBox psCheckBox = new JCheckBox();
	JCheckBox epsCheckBox = new JCheckBox();
	JCheckBox pdfCheckBox = new JCheckBox();
	ButtonGroup outputFormatGroup = new ButtonGroup();

	// Export Area
	JPanel exportAreaPanel = new JPanel();
	ButtonGroup exportAreaGroup = new ButtonGroup();
	JRadioButton drawingRadioButton = new JRadioButton();
	JRadioButton pageRadioButton = new JRadioButton();

	// Size
	JPanel sizePanel = new JPanel();
	JLabel heightLabel = new JLabel();
	JLabel widthLabel = new JLabel();
	JTextField heightTextField = new JTextField();
	JTextField widthTextField = new JTextField();
	JComboBox unitComboBox = new JComboBox();
	String[] units = { "px", "mm" };

	// Background Color Picker
	ColorPicker colorPicker = new ColorPicker(true, true);

	//
	// File Hierarchy
	//

	// CheckBoxTree
	/**
	 * Used to initialize {@link #filters}.
	 */
	static final FileFilter[] heirarchy_filters = { new SVGFilter(),
			new NonHiddenDirectoryFilter() };
	/**
	 * These filters filter what <i>WILL</i> be displayed (meaning if it doesn't
	 * match one of these filters, then it won't be shown)
	 */
	static final Vector<FileFilter> filters = new Vector<FileFilter>(Arrays
			.asList(heirarchy_filters));
	File root = new File(System.getProperty("user.home"));
	JPanel fileSelectPanel = new JPanel();
	CheckBoxTree fileHeirarchy;
	JButton changeRootButton = new JButton();
	JRadioButton singleOutputDirectoryRadio = new JRadioButton();
	JRadioButton sameOutputDirectoryRadio = new JRadioButton();
	JTextField directoryTextField = new JTextField();
	JTextField outputDirectoryTextField = new JTextField();
	JButton outputDirectoryEllipse = new JButton("...");

	/**
	 * The progress bar which displays the progress of the conversion.
	 */
	JProgressBar progressBar;

	/**
	 * Used as the progress indicator for the {@link #progressBar}.
	 */
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

		GridBagConstraints c;

		//
		// ContentPane
		//

		// SplitPane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0);
		splitPane.setDividerSize(9);
		splitPane.setOneTouchExpandable(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JScrollPane optionsPanelScrollPane = new JScrollPane(optionsPanel);
		splitPane.setTopComponent(optionsPanelScrollPane);
		splitPane.setRightComponent(fileSelectPanel);

		// Progress
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.BLACK);
		controller.addProgressListener(this);
		getContentPane().add(progressBar, BorderLayout.SOUTH);

		// Options Panel
		optionsPanel.setLayout(new GridBagLayout());
		optionsPanel.setMinimumSize(new Dimension(400, 600));
		optionsPanel.setPreferredSize(new Dimension(400, 600));

		c = new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		optionsPanel.add(outputFormatPanel, c);
		c = new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		optionsPanel.add(exportAreaPanel, c);
		c = new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		optionsPanel.add(sizePanel, c);
		c = new GridBagConstraints(0, 3, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0);
		optionsPanel.add(colorPicker, c);

		//
		// Ribbon
		//
		RibbonApplicationMenu menuApplicationButton = new RibbonApplicationMenu();

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

		menuApplicationButton.addMenuEntry(saveMenuButton);
		menuApplicationButton.addMenuSeparator();
		menuApplicationButton.addMenuEntry(settingsMenuButton);
		menuApplicationButton.addMenuEntry(aboutMenuButton);
		menuApplicationButton.addFooterEntry(quitMenuButton);

		getRibbon().setApplicationMenu(menuApplicationButton);

		//
		// ControlsRibbonBand
		//
		JRibbonBand controlsBand = new ControlsRibbonBand(this, "Controls",
				getApplicationIcon());

		//
		// add all bands to task
		//
		RibbonTask homeTask = new RibbonTask(i18ln.getString("HomeRibbonTask"),
				controlsBand);
		getRibbon().addTask(homeTask);

		//
		// Options Panel
		//

		// Output Format
		GroupLayout layout = new GroupLayout(outputFormatPanel);
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
		outputFormatPanel.setLayout(layout);
		outputFormatPanel.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("FormatPanel")));

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
		pageRadioButton.setText(i18ln.getString("PageRadioButton"));
		drawingRadioButton.setText(i18ln.getString("DrawingRadioButton"));
		exportAreaGroup.add(pageRadioButton);
		exportAreaGroup.add(drawingRadioButton);

		drawingRadioButton.setSelected(true);
		exportAreaPanel.add(drawingRadioButton);
		exportAreaPanel.add(pageRadioButton);
		exportAreaPanel.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("ExportAreaPanel")));

		// Size
		heightLabel.setText(i18ln.getString("HeightTextField"));
		widthLabel.setText(i18ln.getString("WidthTextField"));
		heightTextField.setBorder(BorderFactory.createLoweredBevelBorder());
		widthTextField.setBorder(BorderFactory.createLoweredBevelBorder());
		heightTextField.setColumns(10);

		widthTextField.addKeyListener(new NumberKeyAdapter(this));
		heightTextField.addKeyListener(new NumberKeyAdapter(this));

		ComboBoxModel unitModel = new DefaultComboBoxModel(units);
		unitComboBox.setModel(unitModel);
		unitComboBox.setEnabled(false);

		sizePanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
		sizePanel.add(heightLabel, c);
		c = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
		sizePanel.add(widthLabel, c);
		c = new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		sizePanel.add(heightTextField, c);
		c = new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		sizePanel.add(widthTextField, c);
		c = new GridBagConstraints(2, 0, 1, 2, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		sizePanel.add(unitComboBox, c);
		// and a spacer to make everything anchor to the EAST
		c = new GridBagConstraints(3, 0, 1, 2, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0);
		sizePanel.add(new JPanel(), c);

		sizePanel.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("SizePanel")));

		// Color Picker
		colorPicker.setColor(Color.WHITE);
		colorPicker.setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("BackgroundPanel")));
		colorPicker.setEnabled(false);

		//
		// File Select Panel
		//

		// Tree Hierarchy
		FileSystemTreeModel model = new FileSystemTreeModel(root, filters);
		fileHeirarchy = new CheckBoxTree(model);
		JScrollPane fileSelectScrollPane = new JScrollPane(fileHeirarchy);

		// implement FileIntelliHints
		directoryTextField.getDocument().addDocumentListener(
				rootDirectoryDocumentListener());
		FileIntelliHints rootIntellisense = new FileIntelliHints(
				directoryTextField);
		rootIntellisense.setFolderOnly(true);
		SelectAllUtils.install(directoryTextField);

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
		singleOutputDirectoryRadio.addChangeListener(singleDirChangeListener());
		changeRootButton.setText("...");
		changeRootButton.addActionListener(changeRootActionListener());

		c = new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		fileSelectPanel.add(changeRootButton, c);
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		fileSelectPanel.add(directoryTextField, c);
		c = new GridBagConstraints(0, 1, 2, 1, 1, 1,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		fileSelectPanel.add(fileSelectScrollPane, c);
		c = new GridBagConstraints(0, 2, 2, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		fileSelectPanel.add(sameOutputDirectoryRadio, c);
		c = new GridBagConstraints(0, 3, 2, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		fileSelectPanel.add(singleOutputDirectoryRadio, c);
		c = new GridBagConstraints(0, 4, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		outputDirectoryTextField.getDocument().addDocumentListener(
				outputDirDocumentListener());
		fileSelectPanel.add(outputDirectoryTextField, c);
		c = new GridBagConstraints(1, 4, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		outputDirectoryEllipse.addActionListener(outputDirActionListener());
		fileSelectPanel.add(outputDirectoryEllipse, c);

		sameOutputDirectoryRadio.setText(i18ln
				.getString("SameDirectoryRadioButton"));
		sameOutputDirectoryRadio.setEnabled(true);
		singleOutputDirectoryRadio.setText(i18ln
				.getString("SingleDirectoryRadioButton"));
		checkSingleOutputDirectoryOption(false);

		pack();
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

	public void checkSingleOutputDirectoryOption(boolean enable) {
		if (singleOutputDirectoryRadio.isSelected()) {
			outputDirectoryEllipse.setEnabled(true);
			outputDirectoryTextField.setEnabled(true);
		} else {
			outputDirectoryEllipse.setEnabled(false);
			outputDirectoryTextField.setEnabled(false);
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
		enablePanel(colorPicker, enable);
		enablePanel(sizePanel, enable);
		unitComboBox.setEnabled(false);
		// Special case until it is actually implemented
		enablePanel(outputFormatPanel, enable);
		enablePanel(exportAreaPanel, enable);
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
					if (newRoot.exists()) {
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
				MainWindowController.setSingleOutputDirectory(new File(
						outputDirectoryTextField.getText()));
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
	 *         <code>{@link #checkSingleOutputDirectoryOption(boolean)</code>
	 */
	private ChangeListener singleDirChangeListener() {
		return new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Object obj = e.getSource();
				if (obj.equals(singleOutputDirectoryRadio)) {
					checkSingleOutputDirectoryOption(singleOutputDirectoryRadio
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
