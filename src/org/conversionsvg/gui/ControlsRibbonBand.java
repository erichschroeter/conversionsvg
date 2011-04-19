package org.conversionsvg.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.conversionsvg.util.Helpers;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;

public class ControlsRibbonBand extends JRibbonBand {

	private static final long serialVersionUID = -1492139421952545446L;
	static final Logger logger = Logger.getLogger(ControlsRibbonBand.class);
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.ControlsRibbonBand", Locale.getDefault());

	/**
	 * The image displayed before the convert action is started.
	 */
	static final ResizableIcon CONVERT_IMAGE = Helpers
			.getResizableIconFromURL("res/images/go-next.png");
	/**
	 * The image displayed after the convert action has been started.
	 */
	static final ResizableIcon CANCEL_IMAGE = Helpers
			.getResizableIconFromURL("res/images/process-stop.png");

	/**
	 * The <code>MainWindow</code> this <code>ControlsRibbonBand</code> is
	 * coupled to.
	 */
	private MainWindow window;

	/**
	 * The button which performs the convert action.
	 * <p>
	 * When pressed, and the convert action has began, this button is disabled
	 * in order to prevent the user from duplicating the conversion action.
	 * </p>
	 */
	JCommandButton convertButton;

	/**
	 * The button which performs the cancel action.
	 * <p>
	 * This button is not visible until the {@link #convertButton} is pressed.
	 * When this button is pressed the
	 * <code>{@link IMainWindowController#handleCancel()}</code> function is
	 * called and then this is hidden again.
	 * </p>
	 */
	JCommandButton cancelButton;

	/**
	 * Used to keep track of the state of the Convert and Cancel buttons.
	 * <p>
	 * <ul>
	 * <li>If a conversion is in progress, the {@link #convertButton} is
	 * disabled and the {@link #cancelButton} is set visible.</li>
	 * <li>If no conversion is in progress, the {@link #cancelButton} is hidden</li>
	 * </ul>
	 * </p>
	 */
	private boolean converting = false;

	/**
	 * Creates a <code>ControlsRibbonBand</code> with the given <i>title</i> and
	 * <i>icon</i>.
	 * <p>
	 * This <code>JRibbonBand</code> is highly coupled with
	 * <code>MainWindow</code>, as it uses attributes in the
	 * <code>MainWindow</code> to gather values. These values are aggregated and
	 * passed to the <code>MainWindow</code>'s
	 * <code>IMainWindowController</code>.
	 * </p>
	 * 
	 * @param window
	 *            the <code>MainWindow</code> this band belongs to
	 * @param title
	 *            the title of the band
	 * @param icon
	 *            the icon of the band
	 */
	public ControlsRibbonBand(MainWindow window, String title,
			ResizableIcon icon) {
		super(title, icon);
		this.window = window;
		init();
	}

	private void init() {

		List<RibbonBandResizePolicy> resizePolicies = new ArrayList<RibbonBandResizePolicy>();
		resizePolicies.add(new CoreRibbonResizePolicies.Mirror(
				getControlPanel()));
		setResizePolicies(resizePolicies);

		convertButton = new JCommandButton(i18ln.getString("ConvertButton"),
				CONVERT_IMAGE);
		convertButton.addActionListener(convertActionListener());

		cancelButton = new JCommandButton(i18ln.getString("CancelButton"),
				CANCEL_IMAGE);
		cancelButton.setVisible(false);
		cancelButton.addActionListener(cancelActionListener());

		addCommandButton(convertButton, RibbonElementPriority.TOP);
		addCommandButton(cancelButton, RibbonElementPriority.TOP);
	}

	/**
	 * @return an <code>ActionListener</code> which handles calling the
	 *         <i>controller</i>'s
	 *         <code>{@link IMainWindowController#handleCancel()}</code>
	 *         function.
	 */
	private ActionListener cancelActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (converting) {
					window.controller.handleCancel();
				}
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which handles calling the
	 *         <i>controller</i>'s
	 *         <code>{@link IMainWindowController#handleConvert(List, Map)}</code>
	 *         function.
	 */
	private ActionListener convertActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!converting) {
					List<File> files = MainWindowController
							.getFiles(window.fileHeirarchy);
					// only attempt to convert if there are 1 or more files
					if (files.size() > 0) {
						Map<String, String> options = MainWindowController
								.getInkscapeCommandlineOptions(
										window.pngCheckBox, window.psCheckBox,
										window.epsCheckBox, window.pdfCheckBox,
										window.colorPicker,
										window.heightTextField,
										window.widthTextField,
										window.pageRadioButton,
										window.drawingRadioButton);
						// initialize the ProgressBar
						// int exportTypes = 0;
						// exportTypes = options.containsKey("-e") ?
						// exportTypes++ : exportTypes;
						// exportTypes = options.containsKey("-P") ?
						// exportTypes++ : exportTypes;
						// exportTypes = options.containsKey("-E") ?
						// exportTypes++ : exportTypes;
						// exportTypes = options.containsKey("-A") ?
						// exportTypes++ : exportTypes;
						window.resetProgressBar();
						window.progressBar.setMaximum(files.size());

						// TODO disable input (buttons ...)
						window.enableInput(false);
						cancelButton.setVisible(true);

						converting = true;

						window.controller.handleConvert(files, options);
						// TODO enable input
						window.enableInput(true);
						converting = false;
						cancelButton.setVisible(false);
					}
				}
			}
		};
	}
}
