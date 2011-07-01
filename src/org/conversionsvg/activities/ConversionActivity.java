package org.conversionsvg.activities;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.conversionsvg.ConversionSvgApplication;
import org.conversionsvg.InkscapeCommandBuilder;

import com.jidesoft.app.framework.DataModelException;
import com.jidesoft.app.framework.activity.Activity;

/**
 * The <code>ConversionActivity</code> handles compiling the command actually
 * sent to Inkscape on the command line and then pushing the task to an
 * available thread in a thread pool.
 * 
 * @author Erich Schroeter
 */
public class ConversionActivity extends Activity {

	static final Logger logger = Logger.getLogger(ConversionActivity.class);

	@Override
	public void activityPerformed() throws Exception {

		ConversionSvgApplication application = (ConversionSvgApplication) getApplication();
		// we have to update the ColorModel since we can't set the Action
		// directly to the buttons. All other options are updated as they are
		// manipulated.
		try {
			application.getDataView(application.getDataModel("color options"))
					.updateModel(application.getDataModel("color options"));
		} catch (DataModelException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}

		// TODO convert and push to thread pool
		InkscapeCommandBuilder command = ((ConversionSvgApplication) getApplication())
				.getInkscapeCommand();

		if (command.getFiles() != null) {
			// display progress status
			// TODO output begin time stamp
			// TODO start ProgressBar
			// progressBar.setMaximum(files.size());

			for (File file : command.getFiles()) {
				logger.debug("convert <" + file.getAbsolutePath() + ">");
				List<String> commandline = command.getCommand(file);
				// TODO create inkscape thread
				Process process;
				try {
					// Execute the command
					process = new ProcessBuilder(commandline).start();
					process.waitFor();
				} catch (IOException e1) {
					logger.error(e1.getMessage());
				} catch (InterruptedException e1) {
					logger.error(e1.getMessage());
				}
				// application.getThreadPool().execute(new Converter());
			}
		}
	}
}
