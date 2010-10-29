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

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class Language {

    String language;
    public Language() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setLanguage(String language){
        this.language = language;
    }

    public void languageAssistant(AssistantDialog assistant){
        ResourceBundle rAssistant = ResourceBundle.getBundle("Assistant");
        if (language.equals("fr")){
            rAssistant = ResourceBundle.getBundle("Assistant",Locale.FRENCH);
        } else {
            rAssistant = ResourceBundle.getBundle("Assistant",Locale.ENGLISH);
        }
        assistant.txaAssistant.setText(rAssistant.getString("txaAssistant"));
        assistant.btnAnnuler.setText(rAssistant.getString("btnAnnuler"));
        assistant.btnSuivant.setText(rAssistant.getString("btnSuivant"));
        assistant.btnPrecedent.setText(rAssistant.getString("btnPrecedent"));
        assistant.txaQ1.setText(rAssistant.getString("txaQ1"));
        assistant.txaQ3.setText(rAssistant.getString("txaQ3"));
        assistant.txaQ4.setText(rAssistant.getString("txaQ4"));
        assistant.txaCouleur.setText(rAssistant.getString("txaCouleur"));
        assistant.txaQ5.setText(rAssistant.getString("txaQ5"));
        assistant.txaQ6.setText(rAssistant.getString("txaQ6"));
        assistant.txaQ7.setText(rAssistant.getString("txaQ7"));
        assistant.ckbAssistant.setText(rAssistant.getString("ckbAssistant"));
        assistant.ckbCouleurFond.setText(rAssistant.getString("ckbCF"));
        assistant.ckbMeme.setText(rAssistant.getString("ckbMeme"));
        assistant.lblBleu.setText(rAssistant.getString("lblBleu"));
        assistant.lblRouge.setText(rAssistant.getString("lblRouge"));
        assistant.lblVert.setText(rAssistant.getString("lblVert"));
        assistant.lblHauteur.setText(rAssistant.getString("lblHauteur"));
        assistant.lblLargeur.setText(rAssistant.getString("lblLargeur"));
        assistant.lblOpaque.setText(rAssistant.getString("lblOpaque"));
        assistant.lblTransparent.setText(rAssistant.getString("lblTransparent"));
        assistant.rdbDessin.setText(rAssistant.getString("rdbDessin"));
        assistant.rdbPage.setText(rAssistant.getString("rdbPage"));
        assistant.rdbDossier.setText(rAssistant.getString("rdbDossier"));
        assistant.rdbFichier.setText(rAssistant.getString("rdbFichier"));
        assistant.rdbHauteur.setText(rAssistant.getString("rdbHauteur"));
        assistant.rdbLargeur.setText(rAssistant.getString("rdbLargeur"));
        assistant.rdbOriginal.setText(rAssistant.getString("rdbOriginal"));
        assistant.rdbPerso.setText(rAssistant.getString("rdbPerso"));
        assistant.txaDessin.setText(rAssistant.getString("txaDessin"));
        assistant.txaPage.setText(rAssistant.getString("txaPage"));
    }

    public void AssistantQ2(JTextArea Q2, boolean isFichier){
        ResourceBundle rAssistant = ResourceBundle.getBundle("Assistant");
        if (language.equals("fr")){
            rAssistant = ResourceBundle.getBundle("Assistant",Locale.FRENCH);
        } else {
            rAssistant = ResourceBundle.getBundle("Assistant",Locale.ENGLISH);
        }
        if (isFichier){
            Q2.setText(rAssistant.getString("txaQ2-1"));
        }else {
            Q2.setText(rAssistant.getString("txaQ2-2"));
        }
    }

    public void finishWizard(JButton finish, boolean isFin){
        ResourceBundle rAssistant = ResourceBundle.getBundle("Assistant");
        if (language.equals("fr")){
            rAssistant = ResourceBundle.getBundle("Assistant",Locale.FRENCH);
        } else {
            rAssistant = ResourceBundle.getBundle("Assistant",Locale.ENGLISH);
        }
        if (isFin){
            finish.setText(rAssistant.getString("Terminer"));
        }else {
            finish.setText(rAssistant.getString("btnSuivant"));
        }
    }

    public void aboutLanguage(AboutDialog apropos)
    {
        ResourceBundle rAPropos = ResourceBundle.getBundle("APropos");
        if (language.equals("fr")){
            rAPropos = ResourceBundle.getBundle("APropos",Locale.FRENCH);
        } else {
            rAPropos = ResourceBundle.getBundle("APropos",Locale.ENGLISH);
        }
        apropos.lblGPL.setText(rAPropos.getString("Licence"));
        apropos.txaInkscape.setText(rAPropos.getString("txaInkscape"));
    }

    public void mainLanguage(MainWindow principal){
        ResourceBundle rPrincipal = ResourceBundle.getBundle("Principal");
        if (language.equals("fr")){
            rPrincipal = ResourceBundle.getBundle("Principal",Locale.FRENCH);
            principal.frenchCheckboxMenuItem.setSelected(true);
            principal.englishCheckboxMenuItem.setSelected(false);
        } else {
            rPrincipal = ResourceBundle.getBundle("Principal",Locale.ENGLISH);
            principal.frenchCheckboxMenuItem.setSelected(false);
            principal.englishCheckboxMenuItem.setSelected(true);
        }
//        principal.ckbCouleurFond.setText(rPrincipal.getString("CouleurFond"));
        principal.cancelButton.setText(rPrincipal.getString("Annuler"));
        principal.convertButton.setText(rPrincipal.getString("Convertir"));
//        principal.btnParDossier.setText(rPrincipal.getString("Parcourir"));
//        principal.btnParDosSortie.setText(rPrincipal.getString("Parcourir"));
//        principal.btnParFichier.setText(rPrincipal.getString("Parcourir"));
        principal.quitButton.setText(rPrincipal.getString("Quitter"));
//        principal.sameOutputDirectoryCheckbox.setText(rPrincipal.getString("chbMeme"));
//        principal.treeCheckbox.setText(rPrincipal.getString("Arbo"));
//        principal.ckbCouleurFond.setText(rPrincipal.getString("CouleurFond"));
        principal.heightLabel.setText(rPrincipal.getString("Hauteur"));
        principal.widthLabel.setText(rPrincipal.getString("Largeur"));
//        principal.lblOpaque.setText(rPrincipal.getString("Opaque"));
//        principal.lblTransparent.setText(rPrincipal.getString("Transparence"));
        principal.aboutMenuItem.setText(rPrincipal.getString("MApropos"));
        principal.englishCheckboxMenuItem.setText(rPrincipal.getString("mniAnglais"));
        principal.wizardMenuItem.setText(rPrincipal.getString("MAssistant"));
        principal.frenchCheckboxMenuItem.setText(rPrincipal.getString("mniFrancais"));
        principal.inkscapeMenuItem.setText(rPrincipal.getString("mniInkscape"));
        principal.quitMenuItem.setText(rPrincipal.getString("MQuitter"));
        principal.helpMenu.setText(rPrincipal.getString("MAide"));
        principal.fileMenu.setText(rPrincipal.getString("Fichier"));
        principal.conversionMenu.setText(rPrincipal.getString("MConversion"));
        principal.languageMenu.setText(rPrincipal.getString("MLangue"));
        principal.parameterMenu.setText(rPrincipal.getString("MParametre"));
        principal.drawingRadioButton.setText(rPrincipal.getString("Dessin"));
//        principal.folderRadioButton.setText(rPrincipal.getString("Dossier"));
//        principal.fileRadioButton.setText(rPrincipal.getString("Fichier"));
        principal.pageRadioButton.setText(rPrincipal.getString("Page"));
        principal.inkscapeTitle = rPrincipal.getString("TitreInkscape");
    }

    public String errorMessage(String pMessage){
        ResourceBundle Erreur = ResourceBundle.getBundle("Message");
        if (language == null){
            language = "fr";
        }
        if (language.equals("fr")){
            Erreur = ResourceBundle.getBundle("Message",Locale.FRENCH);
        } else {
            Erreur = ResourceBundle.getBundle("Message",Locale.ENGLISH);
        }
        return Erreur.getString(pMessage);
    }

    private void jbInit() throws Exception {
    }
}
