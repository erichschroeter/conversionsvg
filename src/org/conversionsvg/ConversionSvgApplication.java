package org.conversionsvg;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.conversionsvg.util.R;
import org.conversionsvg.util.Utils;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntrySecondary;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;

import usr.erichschroeter.applib.GUIApplicationImpl;

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
public class ConversionSvgApplication extends GUIApplicationImpl<JFrame>
		implements ConversionSvgPreferences, ConversionSvgOptions {

	static final Logger logger = Logger
			.getLogger(ConversionSvgApplication.class);

	/** Handles starting the threads in the thread pool */
	private ThreadPoolExecutor threadPool;
	private OptionsView view;

	/**
	 * Constructs a default <code>ConversionSvgApplication</code>, disabling the
	 * menubars and toolbars in favor of using a {@link JRibbon}.
	 */
	public ConversionSvgApplication() {
		super();
		setSavePreferencesOnExit(true);
	}

	@Override
	protected void installApplicationPreferences(Preferences preferences) {
		super.installApplicationPreferences(preferences);
		Preferences p = preferences.node("Inkscape");
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
	protected void initializeWindow(JFrame window) {
		super.initializeWindow(window);
		window.setIconImages(Arrays.asList(
				usr.erichschroeter.applib.utils.Utils
						.iconToImage(usr.erichschroeter.applib.utils.Utils
								.imageIcon(R.png("conversionsvg-16x16.png"))),
				usr.erichschroeter.applib.utils.Utils
						.iconToImage(usr.erichschroeter.applib.utils.Utils
								.imageIcon(R.png("conversionsvg-24x24.png"))),
				usr.erichschroeter.applib.utils.Utils
						.iconToImage(usr.erichschroeter.applib.utils.Utils
								.imageIcon(R.png("conversionsvg-32x32.png"))),
				usr.erichschroeter.applib.utils.Utils
						.iconToImage(usr.erichschroeter.applib.utils.Utils
								.imageIcon(R.png("conversionsvg-48x48.png")))));

		view = new OptionsView(this);
		window.add(view, BorderLayout.CENTER);
	}

	/**
	 * Sets the ribbon used by this application.
	 * 
	 * @param applicationRibbon
	 *            the ribbon
	 */
	public void installApplicationRibbon(JRibbon applicationRibbon) {
		getApplicationWindow().getContentPane().add(applicationRibbon,
				BorderLayout.NORTH);
	}

	/**
	 * Creates and returns an ribbon to be used by this application.
	 * 
	 * @return the application ribbon
	 */
	public JRibbon createApplicationRibbon() {
		JRibbon r = new JRibbon(Utils.resizableIcon(R
				.png("conversion-48x48.png")));
		r.configureHelp(Utils.resizableIcon(R.png("conversion-48x48.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						usr.erichschroeter.applib.utils.Utils
								.openInWebBrowser("http://www.google.com");
					}
				});
		return r;
	}

	public RibbonApplicationMenu createApplicationRibbonMenu() {
		RibbonApplicationMenu menu = new RibbonApplicationMenu();

		RibbonApplicationMenuEntrySecondary github = new RibbonApplicationMenuEntrySecondary(
				Utils.resizableIcon(R.png("github.png")), "Github",
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						usr.erichschroeter.applib.utils.Utils
								.openInWebBrowser("https://github.com/erichschroeter/conversionsvg");
					}
				}, CommandButtonKind.POPUP_ONLY);
		RibbonApplicationMenuEntrySecondary jide = new RibbonApplicationMenuEntrySecondary(
				Utils.resizableIcon(R.png("websites-menu-item.png")), "JIDE",
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						usr.erichschroeter.applib.utils.Utils
								.openInWebBrowser("http://www.jidesoft.com/");
					}
				}, CommandButtonKind.POPUP_ONLY);
		RibbonApplicationMenuEntrySecondary sourceforge = new RibbonApplicationMenuEntrySecondary(
				Utils.resizableIcon(R.png("websites-menu-item.png")),
				"Sourceforge", new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						usr.erichschroeter.applib.utils.Utils
								.openInWebBrowser("http://sourceforge.net/projects/conversionsvg/");
					}
				}, CommandButtonKind.POPUP_ONLY);
		RibbonApplicationMenuEntryPrimary websites = new RibbonApplicationMenuEntryPrimary(
				Utils.resizableIcon(R.png("erichschroeter.png")), "Websites",
				null, CommandButtonKind.POPUP_ONLY);
		websites.addSecondaryMenuGroup("", github, jide, sourceforge);

		RibbonApplicationMenuEntryFooter exit = new RibbonApplicationMenuEntryFooter(
				Utils.resizableIcon(R.png("system-log-out.png")), "Exit",
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						exit(0);
					}
				});
		menu.addFooterEntry(exit);

		return menu;
	}

	public RibbonTask[] createApplicationRibbonTasks() {
		JCommandButton convert = new JCommandButton("Convert",
				Utils.resizableIcon(R.png("convert.png")));
		convert.setFlat(false);
		convert.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
		convert.setActionRichTooltip(new RichTooltip("Convert SVG Images",
				"This will begin the conversion activity to use "
						+ "Inkscape to convert the selected SVG "
						+ "images using the specified options."));
		convert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<InkscapeCommand> commands = view.getInkscapeCommands();

				// TODO convert and push to thread pool
				Process process;
				for (InkscapeCommand command : commands) {
					try {
						// Execute the command
						process = new ProcessBuilder(command.getCommand())
								.start();
						process.waitFor();
					} catch (IOException e1) {
						logger.error(e1.getMessage());
					} catch (InterruptedException e1) {
						logger.error(e1.getMessage());
					}
				}
			}
		});

		JRibbonBand control = new JRibbonBand("Controls", Utils.resizableIcon(R
				.png("control.png")));
		control.setResizePolicies(new Vector<RibbonBandResizePolicy>(Arrays
				.asList(new CoreRibbonResizePolicies.Mirror(control
						.getControlPanel()),
						new CoreRibbonResizePolicies.Mid2Low(control
								.getControlPanel()))));
		control.addCommandButton(convert, RibbonElementPriority.TOP);

		RibbonTask home = new RibbonTask("Home", control);

		return new RibbonTask[] { home };
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

	@Override
	protected JFrame createApplicationWindow() {
		return new JFrame("Conversion SVG (beta)");
	}

	@Override
	public String getVersion() {
		Properties p = new Properties();
		try {
			p.load(ConversionSvgApplication.class
					.getClassLoader()
					.getResourceAsStream(
							"usr/erichschroeter/conversionsvg/build.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return String.format("%s.%s.%s", p.getProperty("build.major", "0"),
				p.getProperty("build.minor", "0"),
				p.getProperty("build.revision", "0"));
	}

}
