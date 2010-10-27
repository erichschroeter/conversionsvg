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

import java.io.File;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Module
{
    public Module()
    {
        try
        {
            jbInit();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    MainWindow     mainWindow;
    Parameter      parameter;
    Language       language;
    Converter      converter;
    Vector         vImage                = new Vector();
    Vector         vDossier              = new Vector();
    boolean        gbCancel              = false;
    ActionListener AttendreFinConversion = new ActionListener() {
                                             public void actionPerformed(
                                                     ActionEvent e)
                                             {
                                                 if (!converter.isAlive())
                                                 {
                                                     AfficherFinConversion();
                                                     enable();
                                                     Timer.stop();
                                                 }
                                             }
                                         };
    Timer          Timer                 = new Timer(500, AttendreFinConversion);

    // show end conversion
    private void AfficherFinConversion()
    {
        if (!gbCancel)
        {
            JOptionPane.showMessageDialog(mainWindow, language
                    .errorMessage("MessageTerminer"));
        } else
        {
            JOptionPane.showMessageDialog(mainWindow, language
                    .errorMessage("MessageAnnuler"));
            gbCancel = false;
        }
        mainWindow.progressBar.setValue(0);
    }

    public Module(MainWindow pprincipal)
    {
        mainWindow = pprincipal;
    }

    public void createLanguage()
    {
        language = new Language();
    }

    public void openHome()
    {
        HomeDialog frmAccueil = new HomeDialog();
        frmAccueil.setLocationRelativeTo(mainWindow);
        frmAccueil.show();
        parameter = frmAccueil.gaParametre;
        File inkscape = new File(parameter.gsInkscape);
        if ((!inkscape.exists()) || (parameter.gsInkscape.length() == 0))
        {
            parameter.gsInkscape = findInkscape();
            writeParameter();
        }
    }

    public void writeParameter()
    {
        try
        {
            FileOutputStream Fichier = new FileOutputStream(
                    "Ressources/Parametres.dat"); // ouvrir le fichier
            ObjectOutputStream Enregistrement = new ObjectOutputStream(Fichier); // ouvrir
                                                                                 // en
                                                                                 // �criture
            Enregistrement.writeObject(parameter);// �crire l'objet dans le
                                                  // fichier
            Enregistrement.flush(); // fermer le fichier
            Enregistrement.close(); // fermer le fichier
            Fichier.close(); // fermer le fichier
        } catch (Exception ioe)
        {
        }
    }

    /**
     * findInkscape()
     * 
     * @return
     */
    private String findInkscape()
    {
        // File lfPF = new File("C:\\Program Files");
        File lfPF = new File("/usr/bin/");
        int i;
        boolean lbOK = false;
        String[] listeDos = lfPF.list();
        // TODO find inkscape
        return "/usr/bin/inkscape";
        // for (i = 0; i < listeDos.length; i++) {
        // if (listeDos[i].indexOf("Inkscape")!=-1) {
        // File lfIM = new File(lfPF.toString() + "\\" + listeDos[i]);
        // String[] listeFichier = lfIM.list();
        // for (int liI = 0; liI < listeFichier.length; liI++) {
        // // list files
        // // if (listeFichier[liI].indexOf("inkscape.exe") != -1) {
        // if (listeFichier[liI].indexOf("inkscape") != -1) {
        // lbOK = true;
        // break;
        // }
        // }
        // if (lbOK == true) {
        // // return lfIM.toString()+"\\inkscape.exe";
        // return lfIM.toString()+"\\inkscape";
        // }
        // }
        // }
        // if (lbOK == false){
        // JOptionPane.showMessageDialog(principal,langue.MessageErreur("MessageNonTrouver"));
        // JFileChooser flcImageDossier = new JFileChooser();
        // flcImageDossier.setAcceptAllFileFilterUsed(false);
        // flcImageDossier.setFileFilter(new Filtre_Inkscape());
        // if (flcImageDossier.showOpenDialog(principal) ==
        // JFileChooser.APPROVE_OPTION){
        // return flcImageDossier.getSelectedFile().toString();
        // }else{
        // JOptionPane.showMessageDialog(principal,langue.MessageErreur("MessageNonIndique"));
        // System.exit(0);
        // }
        // }
        // return null;
    }

    public void setParameter(Parameter pparametre)
    {
        parameter = pparametre;
    }

    public void quit()
    {
        writeParameter();
        System.exit(0);
    }

    public void showWizard()
    {
        AssistantDialog assistant = new AssistantDialog(mainWindow,
                "Assistant", true, parameter, this);
        assistant.setSize(470, 335);
        assistant.setLocationRelativeTo(mainWindow);
        language.languageAssistant(assistant);
        assistant.show();
    }

    public void showWizard(ActionEvent e)
    {
        clearInfo(e);
        AssistantDialog assistant = new AssistantDialog(mainWindow,
                "Assistant", true, parameter, this);
        assistant.setSize(470, 335);
        assistant.setLocationRelativeTo(mainWindow);
        language.languageAssistant(assistant);
        assistant.show();
    }

    private void clearInfo(ActionEvent e)
    {
//        mainWindow.redSlider.setValue(0);
//        mainWindow.redSlider.setEnabled(false);
//        mainWindow.blueSlider.setValue(0);
//        mainWindow.blueSlider.setEnabled(false);
//        mainWindow.greenSlider.setValue(0);
//        mainWindow.greenSlider.setEnabled(false);
//        mainWindow.drawingRadioButton.setSelected(true);
//        mainWindow.pngCheckBox.setSelected(true);
//        mainWindow.heightTextField.setText("");
//        mainWindow.widthTextField.setText("");
//        mainWindow.ckbCouleurFond.setSelected(false);
//        mainWindow.opaqueSlider.setValue(100);
//        mainWindow.opaqueSlider.setEnabled(false);
//        mainWindow.rdbFichier_actionPerformed(e);
//        mainWindow.convertFileLabel.setText("");
//        mainWindow.lblDossierSortie.setText("");
//        mainWindow.treeCheckbox.setSelected(false);
//        mainWindow.sameOutputDirectoryCheckbox.setSelected(false);
//        mainWindow.convertButton.setEnabled(false);
//        mainWindow.lblCouleur.setBackground(new Color(224, 223, 227));
    }

    public void displayColor()
    {
//        mainWindow.lblTxtRouge.setText(String.valueOf(mainWindow.redSlider
//                .getValue()));
//        mainWindow.lbltxtVert.setText(String.valueOf(mainWindow.greenSlider
//                .getValue()));
//        mainWindow.lblTxtBleu.setText(String.valueOf(mainWindow.blueSlider
//                .getValue()));
//        mainWindow.lblCouleur.setBackground(new Color(mainWindow.redSlider
//                .getValue(), mainWindow.greenSlider.getValue(),
//                mainWindow.blueSlider.getValue()));
    }

    public void displayColor(int Rouge, int Vert, int Bleu)
    {
//        mainWindow.redSlider.setValue(Rouge);
//        mainWindow.greenSlider.setValue(Vert);
//        mainWindow.blueSlider.setValue(Bleu);
//        mainWindow.lblTxtRouge.setText(String.valueOf(mainWindow.redSlider
//                .getValue()));
//        mainWindow.lbltxtVert.setText(String.valueOf(mainWindow.greenSlider
//                .getValue()));
//        mainWindow.lblTxtBleu.setText(String.valueOf(mainWindow.blueSlider
//                .getValue()));
//        mainWindow.lblCouleur.setBackground(new Color(mainWindow.redSlider
//                .getValue(), mainWindow.greenSlider.getValue(),
//                mainWindow.blueSlider.getValue()));
    }

    public void Transparence(int Trans)
    {
//        mainWindow.opaqueSlider.setValue(Trans);
//        mainWindow.lblPercent.setText(String.valueOf(Trans) + "%");
    }

    public void Numeric(KeyEvent e)
    {
        if (e.getKeyChar() == '.')
        {
            e.consume();
        } else if ((e.getKeyChar() != '0') && (e.getKeyChar() != '1')
                && (e.getKeyChar() != '2') && (e.getKeyChar() != '3')
                && (e.getKeyChar() != '4') && (e.getKeyChar() != '5')
                && (e.getKeyChar() != '6') && (e.getKeyChar() != '7')
                && (e.getKeyChar() != '8') && (e.getKeyChar() != '9')
                && (e.getKeyChar() != '\b') && (e.getKeyChar() != '.'))
        {
            e.consume();
        }
    }

    // folder output
    public void folderOutput(JLabel test, JCheckBox test2)
    {
        JFileChooser flcDossierSortie = new JFileChooser();
        flcDossierSortie.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (flcDossierSortie.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION)
        {
            test.setText(flcDossierSortie.getSelectedFile().toString());
            test2.setSelected(false);
        }
    }

    // back main output
    public void PrincipalDosSortie(AssistantDialog assistant)
    {
//        mainWindow.sameOutputDirectoryCheckbox.setSelected(assistant.ckbMeme
//                .isSelected());
//        mainWindow.lblDossierSortie.setText(assistant.lblDosSortie.getText());
//        mainWindow.btnParDosSortie.setEnabled(assistant.btnParcourirSortie
//                .isEnabled());
    }

    public void fileFolder(boolean folder, JLabel Test)
    {
        JFileChooser flcImageDossier = new JFileChooser();
        if (folder)
        {
            flcImageDossier.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else
        {
            flcImageDossier.setAcceptAllFileFilterUsed(false);
            flcImageDossier.setFileFilter(new SVGFilter());
        }
        if (flcImageDossier.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION)
        {
            Test.setText(flcImageDossier.getSelectedFile().toString());
        }
    }

    public void mainFileFolder(boolean folder, String File)
    {
//        if (folder)
//        {
//            mainWindow.convertFolderLabel.setText(File);
//        } else
//        {
//            mainWindow.convertFileLabel.setText(File);
//        }
    }

    public void fileOrFolder(boolean file)
    {
//        mainWindow.fileRadioButton.setSelected(file);
//        mainWindow.btnParFichier.setEnabled(file);
//        mainWindow.folderRadioButton.setSelected(!file);
//        mainWindow.btnParDossier.setEnabled(!file);
//        mainWindow.treeCheckbox.setEnabled(!file);
//        if (file)
//        {
//            mainWindow.convertFolderLabel.setText("");
//        } else
//        {
//            mainWindow.convertFileLabel.setText("");
//        }
    }

    public void format(int liI, ActionEvent e)
    {
        switch (liI)
        {
        case 1:
            mainWindow.pngCheckBox.setSelected(true);
            // mainWindow.rdbPNG_actionPerformed(e);
            break;
        case 2:
            mainWindow.psCheckBox.setSelected(true);
            // mainWindow.rdbPS_actionPerformed(e);
            break;
        case 3:
            mainWindow.epsCheckBox.setSelected(true);
            // mainWindow.rdbEPS_actionPerformed(e);
            break;
        case 4:
            mainWindow.pdfCheckBox.setSelected(true);
            // mainWindow.rdbPDF_actionPerformed(e);
            break;
        }
    }

    public void zone(boolean drawing)
    {
        mainWindow.drawingRadioButton.setSelected(drawing);
        mainWindow.pageRadioButton.setSelected(!drawing);
    }

    public void height(String height)
    {
        mainWindow.heightTextField.setText(height);
    }

    public void width(String width)
    {
        mainWindow.widthTextField.setText(width);
    }

    public void Couleurfond(boolean CF, ActionEvent e)
    {
//        mainWindow.ckbCouleurFond.setSelected(CF);
        // mainWindow.ckbCouleurFond_actionPerformed(e);
    }

    public void Taille(int liI)
    {
        if ((liI == 1) || (liI == 2))
        {
            mainWindow.widthTextField.setText("");
        }
        if ((liI == 1) || (liI == 3))
        {
            mainWindow.heightTextField.setText("");
        }
    }

    /**
     * launchConversion()
     */
    public void launchConversion()
    {
        disable(); // disable
        prepareConversion();
    }

    private void disable()
    {
//        mainWindow.cancelButton.setEnabled(true);
//        mainWindow.convertButton.setEnabled(false);
//        mainWindow.btnParDossier.setEnabled(false);
//        mainWindow.btnParDosSortie.setEnabled(false);
//        mainWindow.btnParFichier.setEnabled(false);
//        mainWindow.quitButton.setEnabled(false);
//        mainWindow.sameOutputDirectoryCheckbox.setEnabled(false);
//        mainWindow.treeCheckbox.setEnabled(false);
//        mainWindow.ckbCouleurFond.setEnabled(false);
//        mainWindow.aboutMenuItem.setEnabled(false);
//        mainWindow.englishCheckboxMenuItem.setEnabled(false);
//        mainWindow.wizardMenuItem.setEnabled(false);
//        mainWindow.frenchCheckboxMenuItem.setEnabled(false);
//        mainWindow.inkscapeMenuItem.setEnabled(false);
//        mainWindow.quitMenuItem.setEnabled(false);
//        mainWindow.helpMenu.setEnabled(false);
//        mainWindow.conversionMenu.setEnabled(false);
//        mainWindow.fileMenu.setEnabled(false);
//        mainWindow.languageMenu.setEnabled(false);
//        mainWindow.parameterMenu.setEnabled(false);
//        mainWindow.drawingRadioButton.setEnabled(false);
//        mainWindow.folderRadioButton.setEnabled(false);
//        mainWindow.epsCheckBox.setEnabled(false);
//        mainWindow.fileRadioButton.setEnabled(false);
//        mainWindow.pageRadioButton.setEnabled(false);
//        mainWindow.pdfCheckBox.setEnabled(false);
//        mainWindow.pngCheckBox.setEnabled(false);
//        mainWindow.psCheckBox.setEnabled(false);
//        mainWindow.blueSlider.setEnabled(false);
//        mainWindow.opaqueSlider.setEnabled(false);
//        mainWindow.redSlider.setEnabled(false);
//        mainWindow.greenSlider.setEnabled(false);
//        mainWindow.heightTextField.setEnabled(false);
//        mainWindow.widthTextField.setEnabled(false);
    }

    public void enable()
    {
//        mainWindow.cancelButton.setEnabled(false);
//        mainWindow.convertButton.setEnabled(true);
//        mainWindow.btnParDossier.setEnabled(mainWindow.folderRadioButton
//                .isSelected());
//        mainWindow.btnParDosSortie
//                .setEnabled(!mainWindow.sameOutputDirectoryCheckbox
//                        .isSelected());
//        mainWindow.btnParFichier.setEnabled(mainWindow.fileRadioButton
//                .isSelected());
//        mainWindow.quitButton.setEnabled(true);
//        mainWindow.sameOutputDirectoryCheckbox.setEnabled(true);
//        mainWindow.treeCheckbox.setEnabled(mainWindow.folderRadioButton
//                .isSelected());
//        mainWindow.ckbCouleurFond.setEnabled(mainWindow.pngCheckBox
//                .isSelected());
//        mainWindow.aboutMenuItem.setEnabled(true);
//        mainWindow.englishCheckboxMenuItem.setEnabled(true);
//        mainWindow.wizardMenuItem.setEnabled(true);
//        mainWindow.frenchCheckboxMenuItem.setEnabled(true);
//        mainWindow.inkscapeMenuItem.setEnabled(true);
//        mainWindow.quitMenuItem.setEnabled(true);
//        mainWindow.helpMenu.setEnabled(true);
//        mainWindow.conversionMenu.setEnabled(true);
//        mainWindow.fileMenu.setEnabled(true);
//        mainWindow.languageMenu.setEnabled(true);
//        mainWindow.parameterMenu.setEnabled(true);
//        mainWindow.drawingRadioButton.setEnabled(true);
//        mainWindow.folderRadioButton.setEnabled(true);
//        mainWindow.epsCheckBox.setEnabled(true);
//        mainWindow.fileRadioButton.setEnabled(true);
//        mainWindow.pageRadioButton.setEnabled(true);
//        mainWindow.pdfCheckBox.setEnabled(true);
//        mainWindow.pngCheckBox.setEnabled(true);
//        mainWindow.psCheckBox.setEnabled(true);
//        mainWindow.blueSlider
//                .setEnabled(mainWindow.ckbCouleurFond.isSelected());
//        mainWindow.opaqueSlider.setEnabled(mainWindow.ckbCouleurFond
//                .isSelected());
//        mainWindow.redSlider.setEnabled(mainWindow.ckbCouleurFond.isSelected());
//        mainWindow.greenSlider.setEnabled(mainWindow.ckbCouleurFond
//                .isSelected());
//        mainWindow.heightTextField.setEnabled(true);
//        mainWindow.widthTextField.setEnabled(true);
    }

    public void enableConvertButton()
    {
//        if (((mainWindow.convertFolderLabel.getText().length() > 0) || (mainWindow.convertFileLabel
//                .getText().length() > 0))
//                && ((mainWindow.lblDossierSortie.getText().length() > 0) || (mainWindow.sameOutputDirectoryCheckbox
//                        .isSelected())))
//        {
//            mainWindow.convertButton.setEnabled(true);
//        } else
//        {
//            mainWindow.convertButton.setEnabled(false);
//        }
    }

    public void cancel()
    {
        gbCancel = true;
        converter.stop();
    }

    public void prepareConversion()
    {
//        // list of images to convert
//        vImage.removeAllElements();
//        // list of files
//        vDossier.removeAllElements();
//        String[] lsFormat = { "", "" };
//        String[] lsOption = {};
//        // if the File radio button is selected
//        if (mainWindow.fileRadioButton.isSelected())
//        {
//            vImage.addElement(mainWindow.convertFileLabel.getText());
//            File lfTempo = new File(mainWindow.convertFileLabel.getText());
//            vDossier.addElement(lfTempo.getParent().toString());
//        } else
//        {
//            File lfDos = new File(mainWindow.convertFolderLabel.getText());
//            vImage = Scanner(lfDos, ".svg", vImage, vDossier);
//        }
//        lsFormat = ObtenirFormat();
//        lsOption = getOptions();
//        if (mainWindow.treeCheckbox.isSelected())
//        {
//            createTree();
//        }
//        converter = new Converter(vImage, parameter.gsInkscape, lsFormat,
//                lsOption, mainWindow.lblDossierSortie.getText(),
//                mainWindow.progressBar, mainWindow.treeCheckbox.isSelected(),
//                mainWindow.convertFolderLabel.getText(),
//                mainWindow.nameOfImageLabel, mainWindow.numberOfImageLabel,
//                mainWindow.sameOutputDirectoryCheckbox.isSelected(), language
//                        .errorMessage("Option"), language
//                        .errorMessage("MTitre"), language
//                        .errorMessage("Message"));
//        converter.start();
//        Timer.start();
    }

    private Vector Scanner(File lfDos, String lsEXT, Vector vImage,
            Vector vDossier) // module r�cursif
    {
        String[] listeApp = lfDos.list(); // faire une liste du contenue du
                                          // dossier
        for (int i = 0; i < listeApp.length; i++)
        { // tant que la liste n'est pas toute parcouru
            File lfTempo = new File(lfDos.getPath() + "/" + listeApp[i]); // fichier
                                                                          // avec
                                                                          // son
                                                                          // chemin
                                                                          // d'acc�s
            if (listeApp[i].indexOf(lsEXT) != -1) // si le fichier se termine
                                                  // par .svg
            {
                // Ajouter a un Vector
                vImage.add(lfTempo);
            } else
            // sinon
            {
                if (lfTempo.isDirectory() == true) // si le fichier est un
                                                   // dossier
                {
                    vDossier.add(lfTempo.toString());
                    Scanner(lfTempo, lsEXT, vImage, vDossier); // appel du
                                                               // module Scanner
                                                               // en envoyant le
                                                               // chemin du
                                                               // dossier comme
                                                               // param�tre
                }
            }
        }
        return vImage;
    }

    private String[] ObtenirFormat()
    {
        String lsFormat[] = new String[2];
        if (mainWindow.pngCheckBox.isSelected())
        {
            lsFormat[0] = " -e";
            lsFormat[1] = ".png";
        } else if (mainWindow.psCheckBox.isSelected())
        {
            lsFormat[0] = " -P";
            lsFormat[1] = ".ps";
        } else if (mainWindow.epsCheckBox.isSelected())
        {
            lsFormat[0] = " -E";
            lsFormat[1] = ".eps";
        } else
        {
            lsFormat[0] = " -A";
            lsFormat[1] = ".pdf";
        }
        return lsFormat;
    }

    private String[] getOptions()
    {
        // String lsOption ="";
        ArrayList<String> options = new ArrayList<String>();

        if (mainWindow.drawingRadioButton.isSelected())
        {
            // lsOption = lsOption+"-D";
            options.add("-D");
        } else
        {
            // lsOption = lsOption+"-C";
            options.add("-C");
        }
        if (mainWindow.heightTextField.getText().compareTo("") != 0)
        {
            // lsOption = lsOption+"-h "+ mainWindow.heightTextField.getText();
            options.add("-h");
            options.add(mainWindow.heightTextField.getText());
        }
        if (mainWindow.widthTextField.getText().compareTo("") != 0)
        {
            // lsOption = lsOption+"-w "+ mainWindow.widthTextField.getText();
            options.add("-w");
            options.add(mainWindow.widthTextField.getText());
        }
//        if (mainWindow.ckbCouleurFond.isSelected())
//        {
//            String lsTransparent = "";
//            if (mainWindow.opaqueSlider.getValue() == 100)
//            {
//                lsTransparent = "1";
//            } else
//            {
//                lsTransparent = "0."
//                        + String.valueOf(mainWindow.opaqueSlider.getValue());
//            }
//            // lsOption =
//            // lsOption+" -b rgb("+String.valueOf(mainWindow.slrRouge.getValue())+","+String.valueOf(mainWindow.slrVert.getValue())+","+String.valueOf(mainWindow.slrBleu.getValue())+") -y "+lsTransparent;
//            options.add("-b");
//            options.add("rgb("
//                    + String.valueOf(mainWindow.redSlider.getValue()) + ","
//                    + String.valueOf(mainWindow.greenSlider.getValue()) + ","
//                    + String.valueOf(mainWindow.blueSlider.getValue()) + ")");
//            options.add("-y");
//            options.add(lsTransparent);
//        }
        // return lsOption;
        String[] ret = options.toArray(new String[0]);
        return ret;
    }

    private void createTree()
    {
//        for (int liI = 0; liI < vDossier.size(); liI++)
//        {
//            String lsDos = vDossier.elementAt(liI).toString().substring(
//                    mainWindow.convertFolderLabel.getText().length(),
//                    vDossier.elementAt(liI).toString().length());
//            File lfTempo = new File(mainWindow.lblDossierSortie.getText()
//                    + lsDos);
//            lfTempo.mkdirs();
//        }
    }

    public String SelectionnerInkscape(String Titre)
    {
        JFileChooser flcImageDossier = new JFileChooser();
        flcImageDossier.setAcceptAllFileFilterUsed(false);
        flcImageDossier.setFileFilter(new InkscapeFilter());
        flcImageDossier.setDialogTitle(Titre);
        flcImageDossier.setCurrentDirectory(new File("C:\\Program Files\\"));
        if (flcImageDossier.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION)
        {
            return flcImageDossier.getSelectedFile().toString();
        } else
        {
            return "";
        }
    }

    private void jbInit() throws Exception
    {
    }
}
