package conversion.ui;

import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

public class ConversionSVG {
	boolean packFrame = false;

	//
	// Properties KEYS
	//
	public static final String KEY_INKSCAPE_PATH = "inkscape_path";
	public static final String KEY_CORE_POOL_SIZE = "pool_size_core";
	public static final String KEY_MAXIMUM_POOL_SIZE = "pool_size_max";
	public static final String KEY_KEEP_ALIVE_TIME = "thread_keep_alive_time";
	public static final String KEY_INKSCAPE_EXPORT_WIDTH = "inkscape_export_width";
	public static final String KEY_INKSCAPE_EXPORT_HEIGHT = "inkscape_export_height";
	public static final String KEY_INKSCAPE_EXPORT_COLOR = "inkscape_export_color";

	/**
	 * Creates an instance of the ConversionSVG application.
	 * <p>
	 * This constructor does the following:
	 * <ul>
	 * <li>creates a <code>MainWindow</code> instance</li>
	 * <li>positions the application in the center of the screen</li>
	 * <li>sets the window visible</li>
	 * </ul>
	 */
	public ConversionSVG() {
		MainWindow frame = new MainWindow();
		// Valider les cadres ayant des tailles pr�d�finies
		// Compacter les cadres ayant des infos de taille pr�f�r�es - ex. depuis
		// leur disposition
		if (packFrame) {
			frame.pack();
		} else {
			frame.validate();
		}

		// Centrer la fen�tre
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}

	/**
	 * Sets the <i>Look and Feel (LAF)</i> for the application. Creates an
	 * instance of <code>ConversionSVG</code>.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				new ConversionSVG();
			}
		});
	}
}