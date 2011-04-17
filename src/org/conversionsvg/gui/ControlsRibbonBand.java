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
import org.pushingpixels.flamingo.api.common.JCommandToggleButton;
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
	 * When pressed, and the convert action has began, this button switches
	 * images to an image which notifies the user that they may cancel the
	 * conversion process.
	 * </p>
	 */
	JCommandToggleButton convertButton;

	/**
	 * Used to keep track of the state of the Convert toggle button.
	 */
	private boolean isPressed = false;

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

		convertButton = new JCommandToggleButton(i18ln
				.getString("ConvertButton"), CONVERT_IMAGE);
		convertButton.addActionListener(convertActionListener());

		addCommandButton(convertButton, RibbonElementPriority.TOP);
	}

	/**
	 * @return an <code>ActionListener</code> which handles calling the
	 *         <i>controller</i>'s <code>handleConvert()</code> function.
	 */
	private ActionListener convertActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isPressed) {
					window.controller.handleCancel();
					convertButton.setIcon(CONVERT_IMAGE);
				} else {
					
					List<File> files = MainWindowController
							.getFiles(window.fileHeirarchy);
					Map<String, String> options = MainWindowController
							.getInkscapeCommandlineOptions(window.pngCheckBox,
									window.psCheckBox, window.epsCheckBox,
									window.pdfCheckBox, window.colorPicker,
									window.heightTextField,
									window.widthTextField,
									window.pageRadioButton,
									window.drawingRadioButton);
					// TODO disable input (buttons ...)
					convertButton.setIcon(CANCEL_IMAGE);
					
					window.controller.handleConvert(files, options);

					convertButton.setIcon(CONVERT_IMAGE);
					// TODO enable input
				}
				// for some reason DefaultButtonModel.isPressed() is not visible
				// to us, so we have to keep the state of the convert toggle
				// button updated
				isPressed = isPressed ? false : true;
			}
		};
	}
	//
	// private ActionListener convertActionListener() {
	// return new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// logger.debug("TODO\t" + "handle convert");
	// logger.debug("TODO\t" + "handle cancel");
	// }
	// };
	// }
}
