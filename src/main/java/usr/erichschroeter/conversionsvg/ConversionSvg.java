package usr.erichschroeter.conversionsvg;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;

public class ConversionSvg {

	/** Whether to print verbose information. */
	public static boolean isVerbose;

	public static void main(String[] args) {
		// license to use JIDE software (JDAF, Grids, Components, etc)
		// com.jidesoft.utils.Lm.verifyLicense("Erich Schroeter",
		// "ConversionSVG", "3.99ekleZZE3EXVgbI0hck9kXuHYXJh2");
		Options o = new Options();
		o.addOption(new Option("v", "version", false, "display the version"));
		o.addOption(new Option("V", "verbose", false,
				"display verbose information"));
		o.addOption(new Option("h", "help", false, "display this menu"));
		o.addOption(new Option("I", "inkscape", true,
				"the location of Inkscape executable"));

		try {
			CommandLineParser parser = new GnuParser();
			CommandLine commandline = parser.parse(o, args);

			if (commandline.hasOption("h")) {
				HelpFormatter f = new HelpFormatter();
				f.printHelp("conversionsvg [Options]", o);
				System.exit(0);
			}
			if (commandline.hasOption("v")) {
				System.out.printf("%s%n",
						new ConversionSvgApplication().getVersion());
				System.exit(0);
			}
			isVerbose = commandline.hasOption("V");

			if (commandline.hasOption("I")) {
				Inkscape.setExecutable(new File(commandline.getOptionValue("I")));
			} else {
				Inkscape.setExecutable(Inkscape.findExecutable());
			}

			args = commandline.getArgs();
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		if (isVerbose) {
			File inkscape = Inkscape.getExecutable();
			if (inkscape == null) {
				System.out.println("could not find Inkscape installed");
			} else {
				System.out.printf("found Inkscape installed at <%s>%n",
						Inkscape.getExecutable().getAbsolutePath());
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				// // install the preference values
				// int width = getApplicationPreferences()
				// .getInt("window.size.width", 600);
				// int height =
				// getApplicationPreferences().getInt("window.size.height",
				// 350);
				// int x =
				// getApplicationPreferences().getInt("window.location.x", 100);
				// int y =
				// getApplicationPreferences().getInt("window.location.y", 100);
				//
				// getApplicationWindow().setPreferredSize(new Dimension(width,
				// height));
				// getApplicationWindow().setLocation(new Point(x, y));
				//
				// // initialize the thread pool
				// int corePoolSize = getApplicationPreferences().getInt(
				// "thread_pool.core_size", 10);
				// int maximumPoolSize = getApplicationPreferences().getInt(
				// "thread_pool.max_size", 20);
				// int keepAliveTime = getApplicationPreferences().getInt(
				// "thread_pool.keep_alive_time", 10);
				// // The thread pool used to start Inkscape processes
				// PriorityBlockingQueue<Runnable> queue = new
				// PriorityBlockingQueue<Runnable>(
				// corePoolSize);
				// setThreadPool(new ThreadPoolExecutor(corePoolSize,
				// maximumPoolSize,
				// keepAliveTime, TimeUnit.SECONDS, queue));
				//
				// getApplicationWindow().setVisible(true);

				ConversionSvgApplication app = new ConversionSvgApplication();
				JRibbon ribbon = app.createApplicationRibbon();
				ribbon.setApplicationMenu(app.createApplicationRibbonMenu());
				for (RibbonTask t : app.createApplicationRibbonTasks()) {
					ribbon.addTask(t);
				}
				app.installApplicationRibbon(ribbon);
				app.getApplicationWindow().pack();
				app.run();

			}
		});
	}

}
