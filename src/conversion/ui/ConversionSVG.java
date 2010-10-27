package conversion.ui;

import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

/**
 * <p>Titre : </p>
 *
 * <p>Description : </p>
 *
 * <p>Copyright : Copyright (c) 2007</p>
 *
 * <p>Soci�t� : </p>
 *
 * @author non attribuable
 * @version 1.0
 */
public class ConversionSVG {
    boolean packFrame = false;

    /**
     * Construire et afficher l'application.
     */
    public ConversionSVG() {
        MainWindow frame = new MainWindow();
        // Valider les cadres ayant des tailles pr�d�finies
        // Compacter les cadres ayant des infos de taille pr�f�r�es - ex. depuis leur disposition
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
     * Point d'entr�e de l'application.
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.
                                             getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                new ConversionSVG();
            }
        });
    }
}