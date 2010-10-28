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

package conversion.ui;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

/**
 * The Converter class handles calling Inkscapes command line feature.
 */
public class Converter extends Thread
{
//    String[]                      lsFormat;
//    String[]                      lsOption;
//    String                        lsDosSortie;
//    JProgressBar                  pdbProgression;
//    boolean                       pbArbo;
//    String                        lsDosDepart;
//    JLabel                        nameOfImageLabel;
//    JLabel                        numberOfImageLabel;
//    int                           giImage;
//    boolean                       lbExist;
//    boolean                       gbAnnuler;
//    boolean                       gbMemeDos;
//    Vector                        vImage;
//    String                        gsInkscape;
//    String                        gsMOption;
//    String                        gsMTitre;
//    String                        gsMessage;
//    MainWindow                    mainWindow;
//    int                           giI = 1;
    protected Map<String, String> options;
    protected String              command;
    protected Process             process;

    public Converter(File executable, Map<String, String> options)
    {
        command = executable.getAbsolutePath();
        this.options = options;
    }

    @Override
    public void run()
    {
        StringBuilder builder = new StringBuilder(command);
        for (Map.Entry<String, String> pair : options.entrySet())
        {
            builder.append(pair.getKey());
            builder.append(pair.getValue());
        }

        try
        {
            // Execute the command
            process = new ProcessBuilder(builder.toString()).start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    // public Converter(Vector pvImage, String psInkscape, String[] psFormat,
    // String[] psOption, String psDosSortie,
    // JProgressBar ppdbProgression, boolean ppbArbo, String psDosDepart,
    // JLabel plblNomImage, JLabel plblNombreImage, boolean pMeme,
    // String psMOption, String psMTitre, String psMessage)
    // {
    // lsFormat = psFormat;
    // lsOption = psOption;
    // lsDosSortie = psDosSortie;
    // pdbProgression = ppdbProgression;
    // pbArbo = ppbArbo;
    // lsDosDepart = psDosDepart;
    // nameOfImageLabel = plblNomImage;
    // numberOfImageLabel = plblNombreImage;
    // giImage = 0;
    // lbExist = true;
    // gbAnnuler = false;
    // vImage = pvImage;
    // gsInkscape = psInkscape;
    // gbMemeDos = pMeme;
    // gsMOption = psMOption;
    // gsMTitre = psMTitre;
    // gsMessage = psMessage;
    // }

    // public void run()
    // {
    // String name = "";
    // // 0 = Replace Image 1 = Replace any images, 2 = do not replace, 3 =
    // // Replace any images, 4 = cancel
    // int liOK = 0;// 0 = Remplacer l'image; 1 = Remplacer toute les images; 2
    // // = ne pas remplacer; 3 = Remplacer aucune images; 4 =
    // // annuler
    // int liI = 0;
    // String[] options = gsMOption.split("#");
    // pdbProgression.setMaximum(vImage.size());
    // numberOfImageLabel.setText("0/" + vImage.size());
    // nameOfImageLabel.setText("");
    // while ((liI < vImage.size()) && (gbAnnuler == false))
    // {
    // if (pbArbo == false)
    // {
    // File lfTempo = new File(vImage.elementAt(liI).toString());
    // name = lfTempo.getName();
    // name = name.substring(0, name.indexOf("."));
    // if (gbMemeDos == true)
    // {
    // lsDosSortie = lfTempo.getParent();
    // }
    // } else
    // {
    // name = vImage.elementAt(liI).toString().substring(
    // lsDosDepart.length() + 1,
    // vImage.elementAt(liI).toString().length());
    // name = name.substring(0, name.indexOf("."));
    // }
    // try
    // {
    // File lfVerif = new File(lsDosSortie + "\\" + name + lsFormat[1]);
    // if ((lfVerif.exists()) && (liOK != 1) && (liOK != 3))
    // {
    // liOK = JOptionPane.showOptionDialog(null, name
    // + lsFormat[1] + " " + gsMessage, gsMTitre,
    // JOptionPane.DEFAULT_OPTION,
    // JOptionPane.INFORMATION_MESSAGE, null, options,
    // options[2]);
    // }
    // if (liOK == 4)
    // {
    // gbAnnuler = true;// cancel
    // }
    // if ((liOK == 0) || (liOK == 1)
    // || ((liOK == 3) && (!lfVerif.exists())))
    // {
    // // String tab =
    // // gsInkscape + " -f \"" +
    // // vImage.elementAt(liI) + "\"" + lsFormat[0] +
    // // " \"" + lsDosSortie +
    // // java.lang.System.getProperty("file.separator") + name +
    // // lsFormat[1] + "\"" +
    // // lsOption; // cr�� la commande pour le lancement
    // String[] tab = {
    // gsInkscape,
    // lsFormat[0],
    // lsDosSortie
    // + java.lang.System
    // .getProperty("file.separator")
    // + name + lsFormat[1], "-f ",
    // vImage.elementAt(liI).toString() };
    //
    // String[] cmd = new String[tab.length + lsOption.length];
    // System.arraycopy(tab, 0, cmd, 0, tab.length);
    // System.arraycopy(lsOption, 0, cmd, tab.length,
    // lsOption.length);
    //
    // // String tab =
    // //
    // "/usr/bin/inkscape -f \"/home/erich/Pictures/E.svg\" -e \"/home/erich/Downloads/E.png\" -D -w 100 -h 100";
    // // JOptionPane.showMessageDialog(mainWindow, tab);
    // // test = Runtime.getRuntime().exec(tab); // ex�cuter la
    // // commande
    // test = new ProcessBuilder(cmd).start();
    // // JOptionPane.showMessageDialog(mainWindow,
    // // test.exitValue());
    // // InputStream is = test.getInputStream();
    // // InputStreamReader isr = new InputStreamReader(is);
    // // BufferedReader br = new BufferedReader(isr);
    // // String line;
    // //
    // // while ((line = br.readLine()) != null) {
    // // System.out.println(line);
    // // }
    //
    // giI = 1;
    // test.waitFor();
    //
    // // test.getErrorStream().close();
    // // test.getInputStream().close();
    // // test.getOutputStream().close();
    // }
    // } catch (Exception ex)
    // { // si erreur, afficher message d'erreur
    // // if error, display error message
    // JOptionPane.showMessageDialog(mainWindow, ex.getMessage());
    // }
    // liI++;
    // pdbProgression.setValue(liI);
    // numberOfImageLabel.setText(String.valueOf(liI) + "/"
    // + vImage.size());
    // nameOfImageLabel.setText(lsDosSortie
    // + java.lang.System.getProperty("file.separator") + name
    // + lsFormat[1]);
    // }
    // }
}
