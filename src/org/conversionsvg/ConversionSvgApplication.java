package org.conversionsvg;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.conversionsvg.util.Helpers;
import org.javamvc.GUIApplication;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.icon.EmptyResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;

/**
 * The <code>ConversionSvgApplication</code> is a Java application that allows
 * users to convert <em>Scalable Vector Graphic (SVG)</em> images to other
 * formats.
 * <p>
 * This conversion application piggy backs on the open source application <a
 * href="http://inkscape.org/">Inkscape</a>. What this means is that Inkscape
 * must be installed in order to use this application. Inkscape is required
 * because this application spawns threads off which pass commands via the
 * command line to Inkscape to let it handle converting images.
 * 
 * @author Erich Schroeter
 */
public class ConversionSvgApplication extends GUIApplication implements
		ConversionSvgPreferences, ConversionSvgOptions {

	static final Logger logger = Logger
			.getLogger(ConversionSvgApplication.class);

	/** The ribbon used by this application */
	private JRibbon applicationRibbon;
	/** Handles starting the threads in the thread pool */
	private ThreadPoolExecutor threadPool;
	/**
	 * The command builder used to build the command to send to Inkscape on the
	 * command line
	 */
	private InkscapeCommandBuilder command;

	/**
	 * Constructs a default <code>ConversionSvgApplication</code>, disabling the
	 * menubars and toolbars in favor of using a {@link JRibbon}.
	 */
	public ConversionSvgApplication() {
		super(new JFrame());
		setApplicationIcon(new ImageIcon(ConversionSvgApplication.class
				.getClassLoader().getResource("images/logo.png")));
		// initialize the thread pool
		int corePoolSize = getApplicationPreferences().getInt(
				"thread_pool.core_size", 10);
		int maximumPoolSize = getApplicationPreferences().getInt(
				"thread_pool.max_size", 20);
		int keepAliveTime = getApplicationPreferences().getInt(
				"thread_pool.keep_alive_time", 10);
		// The thread pool used to start Inkscape processes
		PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(
				corePoolSize);
		setThreadPool(new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, TimeUnit.SECONDS, queue));
	}

	@Override
	protected void installApplicationPreferences() {
		super.installApplicationPreferences();
		Preferences p = getApplicationPreferences();
		p.putInt("window.size.width", 600);
		p.putInt("window.size.height", 350);

		p.put("inkscape.location", "");
		p.putInt("thread_pool.core_size", 10);
		p.putInt("thread_pool.max_size", 20);
		p.putInt("thread_pool.keep_alive_time", 10);
		p.putInt("inkscape.export_width", 16);
		p.putInt("inkscape.export_height", 16);
		p.put("inkscape.export_color", "#00FFFFFF");
		p.put("last_root", System.getProperty("user.home"));
	}

	@Override
	protected void initializeWindow() {
		super.initializeWindow();
		JFrame frame = (JFrame) getApplicationWindow();
		frame.setTitle("Conversion SVG \u0392");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// a window listener must be implemented to call
		// DesktopApplication.exit() in order for our overridden exit() method
		// to be called and save our preferences
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				exit();
			}
		});
		frame.pack();
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	/**
	 * Returns the ribbon used by this application.
	 * 
	 * @return the ribbon
	 */
	public JRibbon getApplicationRibbon() {
		return applicationRibbon;
	}

	/**
	 * Sets the ribbon used by this application.
	 * 
	 * @param applicationRibbon
	 *            the ribbon
	 */
	public void setApplicationRibbon(JRibbon applicationRibbon) {
		this.applicationRibbon = applicationRibbon;
	}

	/**
	 * Creates and returns an ribbon to be used by this application.
	 * 
	 * @return the application ribbon
	 */
	protected JRibbon createApplicationRibbon() {
		RibbonFactory factory = new RibbonFactory()
				.withHelp(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						openInBrowser("http://www.google.com");
					}
				});

		factory.addSubMenuItem("Erich Schroeter Github", Helpers
				.getResizableIconFromResource("images/erichschroeter.png"),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						openInBrowser("https://github.com/erichschroeter/conversionsvg");
					}
				});
		factory.addSubMenuItem("Powered by JIDE", new EmptyResizableIcon(16),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						openInBrowser("http://www.jidesoft.com/");
					}
				});
		factory.addSubMenuItem("Original SourceForge Project", Helpers
				.getResizableIconFromResource("images/sourceforge.png"),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						openInBrowser("http://sourceforge.net/projects/conversionsvg/");
					}
				});
		factory.addMenuItem("Websites", Helpers
				.getResizableIconFromResource("images/websites-menu-item.png"),
				null, CommandButtonKind.POPUP_ONLY);
		factory.addFooterMenuItem("Exit", Helpers
				.getResizableIconFromResource("images/system-log-out.png"),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						exit(0);
					}
				});
		factory.withMenu();

		// factory.addButtonTypeAction("Convert", Helpers
		// .getResizableIconFromResource("images/convert.png"),
		// getActionMap().get("convert"), new RichTooltip(
		// "Convert SVG Images",
		// "This will begin the conversion activity to use "
		// + "Inkscape to convert the selected SVG "
		// + "images using the specified options."));
		factory.addBand("Controls");
		factory.addTask("Home");

		return factory.getRibbon();
	}

	/**
	 * Handles opening the <code>url</code> in the default web browser.
	 * 
	 * @param url
	 *            a valid website address
	 * @return <code>false</code> if an IOException occurs when creating a
	 *         {@link URI} instance with <code>url</code>, else
	 *         <code>true</code>
	 */
	public static boolean openInBrowser(String url) {
		try {
			Desktop.getDesktop().browse(URI.create(url));
		} catch (IOException e1) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the command builder responsible for building the command sent on
	 * the command line to Inkscape.
	 * 
	 * @return the Inkscape command builder
	 */
	public InkscapeCommandBuilder getInkscapeCommand() {
		return command;
	}

	/**
	 * Sets the Inkscape command builder responsible for building the command
	 * sent on the command line to Inkscape.
	 * 
	 * @param command
	 *            the command to set
	 */
	public void setInkscapeCommand(InkscapeCommandBuilder command) {
		this.command = command;
	}

	/**
	 * Returns the object responsible for executing threads in the thread pool.
	 * 
	 * @return the threadPool
	 */
	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	/**
	 * Sets the object responsible for executing threads in the thread pool.
	 * 
	 * @param threadPool
	 *            the thread pool executor to set
	 */
	protected void setThreadPool(ThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
	}

	public InkscapeCommandBuilder getCommand() {
		return command;
	}

	@Override
	public void run(String[] args) {

		CommandLineParser parser = new GnuParser();
		Options options = new Options();
		options.addOption(OPTION_INKSCAPE_LOCATION);

		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption(SWITCH_INKSCAPE_LOCATION)) {
				Inkscape.setExecutable((File) cmd
						.getOptionObject(SWITCH_INKSCAPE_LOCATION));
			} else {
				Inkscape.setExecutable(Inkscape.findExecutable());
			}

			command = new InkscapeCommandBuilder();
		} catch (ParseException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		// install the preference values
		int width = getApplicationPreferences()
				.getInt("window.size.width", 600);
		int height = getApplicationPreferences().getInt("window.size.height",
				350);
		int x = getApplicationPreferences().getInt("window.location.x", 100);
		int y = getApplicationPreferences().getInt("window.location.y", 100);

		getApplicationWindow().setPreferredSize(new Dimension(width, height));
		getApplicationWindow().setLocation(new Point(x, y));

		// initialize the thread pool
		int corePoolSize = getApplicationPreferences().getInt(
				"thread_pool.core_size", 10);
		int maximumPoolSize = getApplicationPreferences().getInt(
				"thread_pool.max_size", 20);
		int keepAliveTime = getApplicationPreferences().getInt(
				"thread_pool.keep_alive_time", 10);
		// The thread pool used to start Inkscape processes
		PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(
				corePoolSize);
		setThreadPool(new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, TimeUnit.SECONDS, queue));

	}

	/**
	 * Handles verifying the <em>JIDE</em> license information, configuring
	 * <em>log4j</em>, and starting the {@link ConversionSvgApplication} passing
	 * the remaining command line arguments that have not been used.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws ParseException
	 *             if there is an error parsing the command line options
	 */
	public static void main(String[] args) throws ParseException {
		// license to use JIDE software (JDAF, Grids, Components, etc)
//		com.jidesoft.utils.Lm.verifyLicense("Erich Schroeter", "ConversionSVG",
//				"3.99ekleZZE3EXVgbI0hck9kXuHYXJh2");

		CommandLineParser parser = new GnuParser();
		Options options = new Options();
		options.addOption(OPTION_LOG4J);

		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption(SWITCH_LOG4J)) {
			DOMConfigurator.configure(cmd.getOptionValue(SWITCH_LOG4J));
		} else {
			BasicConfigurator.configure();
		}

		final String[] remainingArgs = cmd.getArgs();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				ConversionSvgApplication app = new ConversionSvgApplication();
				app.run(remainingArgs);

			}
		});
	}

}
