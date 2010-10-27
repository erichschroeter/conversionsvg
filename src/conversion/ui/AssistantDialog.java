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

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.Properties;

public class AssistantDialog extends JDialog
{
    private static final long serialVersionUID = -7920481672759341924L;
	
	JPanel pnlFond = new JPanel();
    Parameter parametre;
    //pnlFond
    JTextArea txaAssistant = new JTextArea();
    JPanel pnlBouton = new JPanel();
    JButton btnSuivant = new JButton();
    JButton btnPrecedent = new JButton();
    JButton btnAnnuler = new JButton();
    JLabel lblLogo = new JLabel();

    //Panel Q1
    JPanel pnlQ1 = new JPanel();
    JTextArea txaQ1 = new JTextArea();
    JRadioButton rdbFichier = new JRadioButton();
    JRadioButton rdbDossier = new JRadioButton();
    ButtonGroup groupQ1 = new ButtonGroup();
    JCheckBox ckbAssistant = new JCheckBox();

    //Panel Q2
    JPanel pnlQ2 = new JPanel();
    JTextArea txaQ2 = new JTextArea();
    JButton btnParcourir = new JButton(); //Panel Q3
    JPanel pnlQ3 = new JPanel();
    JTextArea txaQ3 = new JTextArea();
    JButton btnParcourirSortie = new JButton();
    JCheckBox ckbMeme = new JCheckBox();

    //Panel Q4
    JPanel pnlQ4 = new JPanel();
    JTextArea txaQ4 = new JTextArea();
    JRadioButton rdbPNG = new JRadioButton();
    JRadioButton rdbPS = new JRadioButton();
    JRadioButton rdbPDF = new JRadioButton();
    JRadioButton rdbEPS = new JRadioButton();
    ButtonGroup groupQ4 = new ButtonGroup();
    JPanel pnlQ5 = new JPanel();
    JPanel pnlCouleur = new JPanel();
    JPanel pnlQ6 = new JPanel();
    JPanel pnlQ7 = new JPanel();
    JTextArea txaQ5 = new JTextArea();
    JTextArea txaCouleur = new JTextArea();
    JTextArea txaQ6 = new JTextArea();
    JTextArea txaQ7 = new JTextArea();
    JSlider slrRouge = new JSlider();
    JSlider slrVert = new JSlider();
    JSlider slrBleu = new JSlider();
    JSlider slrOpaque = new JSlider();
    JLabel lblCouleur = new JLabel();
    JLabel lblValeurRouge = new JLabel();
    JLabel lblValeurVert = new JLabel();
    JLabel lblValeurBleu = new JLabel();
    JLabel lblOpaque = new JLabel();
    JLabel lblTransparent = new JLabel();
    JLabel lblRouge = new JLabel();
    JLabel lblVert = new JLabel();
    JLabel lblBleu = new JLabel();
    JCheckBox ckbCouleurFond = new JCheckBox();
    JRadioButton rdbPage = new JRadioButton();
    JRadioButton rdbDessin = new JRadioButton();
    JTextArea txaDessin = new JTextArea();
    JTextArea txaPage = new JTextArea();
    ButtonGroup groupQ5 = new ButtonGroup();
    ButtonGroup groupQ6 = new ButtonGroup();
    JRadioButton rdbOriginal = new JRadioButton();
    JRadioButton rdbHauteur = new JRadioButton();
    JRadioButton rdbPerso = new JRadioButton();
    JRadioButton rdbLargeur = new JRadioButton();
    JTextField txtHauteur = new JTextField();
    JTextField txtLargeur = new JTextField();
    JLabel lblHauteur = new JLabel();
    JLabel lblLargeur = new JLabel();
    Module module;
    JLabel lblFicDos = new JLabel();
    JLabel lblDosSortie = new JLabel();
    JLabel lblPercent = new JLabel();
    public AssistantDialog(Frame owner, String title, boolean modal, Parameter pparametre, Module pmodule) {
        super(owner, title, modal);
        try {
            parametre = pparametre;
            module = pmodule;
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jbInit();
            pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public AssistantDialog() {
        this(new Frame(), "Assistant", false, new Parameter(), new Module(new MainWindow()));
    }

    private void jbInit() throws Exception {
        //pnlFond
        pnlFond.setLayout(null);
        pnlFond.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        txaAssistant.setFont(new java.awt.Font("SansSerif", Font.BOLD, 12));
        txaAssistant.setBorder(BorderFactory.createEtchedBorder());
        txaAssistant.setPreferredSize(new Dimension(70, 50));
        txaAssistant.setDisabledTextColor(Color.black);
        txaAssistant.setEditable(false);
        txaAssistant.setText("Bienvenue");
        txaAssistant.setLineWrap(true);
        txaAssistant.setWrapStyleWord(true);
        txaAssistant.setBounds(new Rectangle(0, 0, 470, 50));
        pnlBouton.setBackground(Color.white);
        pnlBouton.setBorder(BorderFactory.createEtchedBorder());
        pnlBouton.setPreferredSize(new Dimension(10, 50));
        pnlBouton.setBounds(new Rectangle(0, 250, 470, 61));
        pnlBouton.setLayout(null);
        btnSuivant.setBackground(Color.white);
        btnSuivant.setBounds(new Rectangle(255, 15, 85, 23));
        btnSuivant.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        btnSuivant.setText("jButton1");
        btnSuivant.addActionListener(new Assistant_btnSuivant_actionAdapter(this));
        btnPrecedent.setBackground(Color.white);
        btnPrecedent.setBounds(new Rectangle(167, 15, 85, 23));
        btnPrecedent.setEnabled(false);
        btnPrecedent.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        btnPrecedent.setText("jButton1");
        btnPrecedent.addActionListener(new Assistant_btnPrecedent_actionAdapter(this));
        btnAnnuler.setBackground(Color.white);
        btnAnnuler.setBounds(new Rectangle(378, 15, 80, 23));
        btnAnnuler.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        btnAnnuler.setText("jButton1");
        btnAnnuler.addActionListener(new Assistant_btnAnnuler_actionAdapter(this));
        lblLogo.setText("");
        lblLogo.setBounds(new Rectangle(5, 7, 92, 37));
        txaQ4.setText("jTextArea1");
        rdbPNG.setBackground(Color.white);
        rdbPNG.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbPNG.setSelected(true);
        rdbPNG.setText("PNG");
        rdbPNG.setBounds(new Rectangle(28, 78, 91, 23));
        rdbPNG.addActionListener(new Assistant_rdbPNG_actionAdapter(this));
        rdbPS.setBackground(Color.white);
        rdbPS.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbPS.setText("PS");
        rdbPS.setBounds(new Rectangle(133, 79, 91, 23));
        rdbPS.addActionListener(new Assistant_rdbPS_actionAdapter(this));
        rdbPDF.setBackground(Color.white);
        rdbPDF.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbPDF.setText("PDF");
        rdbPDF.setBounds(new Rectangle(346, 80, 91, 23));
        rdbPDF.addActionListener(new Assistant_rdbPDF_actionAdapter(this));
        rdbEPS.setBackground(Color.white);
        rdbEPS.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbEPS.setText("EPS");
        rdbEPS.setBounds(new Rectangle(242, 79, 91, 23));
        rdbEPS.addActionListener(new Assistant_rdbEPS_actionAdapter(this));
        rdbFichier.setSelected(true);
        this.setResizable(false);
        slrRouge.setMaximum(255);
        slrRouge.setPaintLabels(true);
        slrRouge.setPaintTicks(true);
        slrRouge.setBackground(Color.white);
        slrRouge.setEnabled(false);
        slrRouge.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        slrRouge.setBounds(new Rectangle(45, 65, 296, 24));
        slrRouge.setValue(0);
        slrRouge.addChangeListener(new Assistant_slrRouge_changeAdapter(this));
        slrVert.setMaximum(255);
        slrVert.setPaintLabels(true);
        slrVert.setPaintTicks(true);
        slrVert.setBackground(Color.white);
        slrVert.setEnabled(false);
        slrVert.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        slrVert.setBounds(new Rectangle(45, 92, 296, 24));
        slrVert.setValue(0);
        slrVert.addChangeListener(new Assistant_slrVert_changeAdapter(this));
        slrBleu.setMaximum(255);
        slrBleu.setPaintLabels(true);
        slrBleu.setPaintTicks(true);
        slrBleu.setBackground(Color.white);
        slrBleu.setEnabled(false);
        slrBleu.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        slrBleu.setBounds(new Rectangle(45, 119, 296, 24));
        slrBleu.setValue(0);
        slrBleu.addChangeListener(new Assistant_slrBleu_changeAdapter(this));
        slrOpaque.setInverted(true);
        slrOpaque.setPaintLabels(true);
        slrOpaque.setPaintTicks(true);
        slrOpaque.setBackground(Color.white);
        slrOpaque.setEnabled(false);
        slrOpaque.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        slrOpaque.setBounds(new Rectangle(87, 152, 213, 24));
        slrOpaque.setValue(100);
        slrOpaque.addChangeListener(new Assistant_slrOpaque_changeAdapter(this));
        lblCouleur.setBackground(Color.white);
        lblCouleur.setBorder(BorderFactory.createLineBorder(Color.black));
        lblCouleur.setOpaque(true);
        lblCouleur.setBounds(new Rectangle(381, 68, 75, 75));
        lblValeurRouge.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblValeurRouge.setText("0");
        lblValeurRouge.setBounds(new Rectangle(343, 70, 34, 15));
        lblValeurVert.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblValeurVert.setText("0");
        lblValeurVert.setBounds(new Rectangle(343, 97, 34, 15));
        lblValeurBleu.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblValeurBleu.setText("0");
        lblValeurBleu.setBounds(new Rectangle(343, 124, 34, 15));
        lblOpaque.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblOpaque.setHorizontalAlignment(SwingConstants.RIGHT);
        lblOpaque.setText("Opaque");
        lblOpaque.setBounds(new Rectangle(3, 157, 80, 15));
        lblTransparent.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblTransparent.setText("Transparent");
        lblTransparent.setBounds(new Rectangle(305, 157, 115, 15));
        lblRouge.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblRouge.setHorizontalAlignment(SwingConstants.RIGHT);
        lblRouge.setText("Rouge");
        lblRouge.setBounds(new Rectangle( -2, 70, 45, 15));
        lblVert.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblVert.setHorizontalAlignment(SwingConstants.RIGHT);
        lblVert.setText("Vert");
        lblVert.setBounds(new Rectangle( -2, 97, 45, 15));
        lblBleu.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblBleu.setHorizontalAlignment(SwingConstants.RIGHT);
        lblBleu.setText("Bleu");
        lblBleu.setBounds(new Rectangle( -2, 124, 45, 15));
        ckbCouleurFond.setBackground(Color.white);
        ckbCouleurFond.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        ckbCouleurFond.setText("jCheckBox1");
        ckbCouleurFond.setBounds(new Rectangle(11, 43, 337, 23));
        ckbCouleurFond.addActionListener(new
                                         Assistant_ckbCouleurFond_actionAdapter(this));
        rdbPage.setBackground(Color.white);
        rdbPage.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbPage.setText("jRadioButton1");
        rdbPage.setBounds(new Rectangle(260, 49, 144, 23));
        rdbPage.addActionListener(new Assistant_rdbPage_actionAdapter(this));
        rdbDessin.setBackground(Color.white);
        rdbDessin.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbDessin.setSelected(true);
        rdbDessin.setText("jRadioButton1");
        rdbDessin.setBounds(new Rectangle(51, 49, 144, 23));
        rdbDessin.addActionListener(new Assistant_rdbDessin_actionAdapter(this));
        txaDessin.setEnabled(false);
        txaDessin.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaDessin.setBorder(BorderFactory.createLineBorder(Color.black));
        txaDessin.setDisabledTextColor(Color.black);
        txaDessin.setEditable(false);
        txaDessin.setText("jTextArea1");
        txaDessin.setLineWrap(true);
        txaDessin.setWrapStyleWord(true);
        txaDessin.setBounds(new Rectangle(27, 72, 168, 114));
        txaPage.setEnabled(false);
        txaPage.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaPage.setBorder(BorderFactory.createLineBorder(Color.black));
        txaPage.setDisabledTextColor(Color.black);
        txaPage.setEditable(false);
        txaPage.setText("jTextArea1");
        txaPage.setLineWrap(true);
        txaPage.setWrapStyleWord(true);
        txaPage.setBounds(new Rectangle(260, 72, 168, 114));
        rdbOriginal.setBackground(Color.white);
        rdbOriginal.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbOriginal.setSelected(true);
        rdbOriginal.setText("jRadioButton1");
        rdbOriginal.setBounds(new Rectangle(17, 48, 272, 23));
        rdbOriginal.addActionListener(new Assistant_rdbOriginal_actionAdapter(this));
        rdbHauteur.setBackground(Color.white);
        rdbHauteur.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbHauteur.setSelected(true);
        rdbHauteur.setText("jRadioButton1");
        rdbHauteur.setBounds(new Rectangle(17, 106, 272, 23));
        rdbHauteur.addActionListener(new Assistant_rdbHauteur_actionAdapter(this));
        rdbPerso.setBackground(Color.white);
        rdbPerso.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbPerso.setSelected(true);
        rdbPerso.setText("jRadioButton1");
        rdbPerso.setBounds(new Rectangle(17, 135, 272, 23));
        rdbPerso.addActionListener(new Assistant_rdbPerso_actionAdapter(this));
        rdbLargeur.setBackground(Color.white);
        rdbLargeur.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbLargeur.setSelected(true);
        rdbLargeur.setText("jRadioButton1");
        rdbLargeur.setBounds(new Rectangle(17, 77, 272, 23));
        rdbLargeur.addActionListener(new Assistant_rdbLargeur_actionAdapter(this));
        txtHauteur.setEnabled(false);
        txtHauteur.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        txtHauteur.setText("");
        txtHauteur.setHorizontalAlignment(SwingConstants.RIGHT);
        txtHauteur.setBounds(new Rectangle(313, 128, 62, 20));
        txtHauteur.addKeyListener(new Assistant_txtHauteur_keyAdapter(this));
        txtLargeur.setEnabled(false);
        txtLargeur.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        txtLargeur.setText("");
        txtLargeur.setHorizontalAlignment(SwingConstants.RIGHT);
        txtLargeur.setBounds(new Rectangle(313, 78, 62, 20));
        txtLargeur.addKeyListener(new Assistant_txtLargeur_keyAdapter(this));
        lblHauteur.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblHauteur.setText("jLabel1");
        lblHauteur.setBounds(new Rectangle(313, 113, 62, 15));
        lblLargeur.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        lblLargeur.setText("jLabel1");
        lblLargeur.setBounds(new Rectangle(313, 64, 62, 15));
        pnlQ1.setBackground(Color.white);
        pnlQ1.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        pnlQ1.setBounds(new Rectangle(0, 50, 470, 200));
        pnlQ1.setLayout(null);
        txaQ1.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaQ1.setMinimumSize(new Dimension(70, 30));
        txaQ1.setPreferredSize(new Dimension(70, 40));
        txaQ1.setText("Q1");
        txaQ1.setEditable(false);
        txaQ1.setEnabled(false);
        txaQ1.setDisabledTextColor(Color.black);
        rdbFichier.setBackground(Color.white);
        rdbFichier.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbFichier.setText("Fichier");
        rdbDossier.setBackground(Color.white);
        rdbDossier.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        rdbDossier.setText("Dossier");
        rdbDossier.setBounds(new Rectangle(73, 117, 200, 21));
        rdbDossier.addActionListener(new Assistant_rdbDossier_actionAdapter(this));
        ckbAssistant.setBackground(Color.white);
        ckbAssistant.setFont(new java.awt.Font("SansSerif", Font.BOLD, 12));
        ckbAssistant.setText("jCheckBox1");
        ckbAssistant.setBounds(new Rectangle(0, 177, 470, 23));
        ckbAssistant.addActionListener(new Assistant_ckbAssistant_actionAdapter(this));
        rdbFichier.setBounds(new Rectangle(73, 90, 200, 21));
        rdbFichier.addActionListener(new Assistant_rdbFichier_actionAdapter(this));
        txaQ1.setBounds(new Rectangle(1, 1, 465, 35));
        pnlQ7.setBounds(new Rectangle(0, 50, 470, 200));
        pnlQ6.setBounds(new Rectangle(0, 50, 470, 200));
        pnlCouleur.setBounds(new Rectangle(0, 50, 470, 200));
        pnlQ5.setBounds(new Rectangle(0, 50, 470, 200));
        pnlQ3.setBounds(new Rectangle(0, 50, 470, 200));
        pnlQ2.setBounds(new Rectangle(0, 50, 470, 200));
        pnlQ4.setBounds(new Rectangle(0, 50, 470, 200));
        txaQ4.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaCouleur.setLineWrap(true);
        txaCouleur.setWrapStyleWord(true);
        btnParcourirSortie.addActionListener(new
                Assistant_btnParcourirSortie_actionAdapter(this));
        ckbMeme.addActionListener(new Assistant_ckbMeme_actionAdapter(this));
        btnParcourir.addActionListener(new Assistant_btnParcourir_actionAdapter(this));
        lblFicDos.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        lblFicDos.setBorder(BorderFactory.createLoweredBevelBorder());
        lblFicDos.setText("");
        lblFicDos.setBounds(new Rectangle(21, 123, 368, 20));
        lblDosSortie.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        lblDosSortie.setBorder(BorderFactory.createLoweredBevelBorder());
        lblDosSortie.setText("");
        lblDosSortie.setBounds(new Rectangle(21, 123, 368, 20));
        lblPercent.setFont(new java.awt.Font("SansSerif", Font.BOLD, 11));
        lblPercent.setHorizontalAlignment(SwingConstants.CENTER);
        lblPercent.setText("100%");
        lblPercent.setBounds(new Rectangle(89, 3, 34, 15));
        getContentPane().add(pnlFond);
        pnlBouton.add(lblLogo);
        pnlBouton.add(btnSuivant);
        pnlBouton.add(btnPrecedent);
        pnlBouton.add(btnAnnuler);
        groupQ1.add(rdbFichier);
        groupQ1.add(rdbDossier);
        pnlQ1.add(ckbAssistant, null);
        pnlQ1.add(txaQ1, null);
        pnlQ1.add(rdbFichier, null);
        pnlQ1.add(rdbDossier, null);
        pnlQ5.add(txaQ5);
        pnlQ5.add(txaPage);
        pnlQ5.add(rdbPage);
        pnlQ5.add(txaDessin);
        pnlQ5.add(rdbDessin);
        pnlCouleur.add(slrOpaque);
        slrOpaque.add(lblPercent);
        pnlCouleur.add(lblRouge);
        pnlCouleur.add(slrRouge);
        pnlCouleur.add(lblValeurRouge);
        pnlCouleur.add(lblCouleur);
        pnlCouleur.add(lblVert);
        pnlCouleur.add(slrVert);
        pnlCouleur.add(lblValeurVert);
        pnlCouleur.add(lblBleu);
        pnlCouleur.add(slrBleu);
        pnlCouleur.add(lblValeurBleu);
        pnlCouleur.add(lblOpaque);
        pnlCouleur.add(lblTransparent);
        pnlCouleur.add(ckbCouleurFond);
        pnlCouleur.add(txaCouleur);
        pnlQ6.add(txaQ6);
        pnlQ6.add(rdbPerso);
        pnlQ6.add(rdbHauteur);
        pnlQ6.add(rdbLargeur);
        pnlQ6.add(rdbOriginal);
        pnlQ6.add(lblHauteur);
        pnlQ6.add(txtHauteur);
        pnlQ6.add(txtLargeur);
        pnlQ6.add(lblLargeur);
        pnlQ7.add(txaQ7);
        pnlFond.add(pnlBouton, null);
        pnlQ4.add(txaQ4);
        pnlQ4.add(rdbPNG);
        pnlQ4.add(rdbPS);
        pnlQ4.add(rdbEPS);
        pnlQ4.add(rdbPDF);
        btnParcourir.setBackground(Color.white);
        btnParcourir.setBounds(new Rectangle(399, 123, 41, 20));
        btnParcourir.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        btnParcourir.setText("...");
        txaQ2.setBounds(new Rectangle(1, 1, 465, 35));
        txaQ2.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaQ2.setPreferredSize(new Dimension(57, 40));
        txaQ2.setDisabledTextColor(Color.black);
        txaQ2.setEditable(false);
        txaQ2.setEnabled(false);
        txaQ2.setDisabledTextColor(Color.black);
        txaQ2.setText("Q2");
        txaQ2.setLineWrap(true);
        txaQ2.setWrapStyleWord(true);
        pnlQ2.setLayout(null);
        pnlQ2.setBackground(Color.white);
        pnlQ2.add(txaQ2, null);
        pnlQ2.add(btnParcourir, null);
        pnlQ2.add(lblFicDos);
        pnlQ3.add(txaQ3, null);
        pnlQ3.add(btnParcourirSortie);
        pnlQ3.add(ckbMeme);
        pnlQ3.add(lblDosSortie);
        pnlQ3.setBackground(Color.white);
        pnlQ3.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        pnlQ3.setLayout(null);
        txaQ3.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaQ3.setPreferredSize(new Dimension(57, 40));
        txaQ3.setText("Q3");
        txaQ3.setBounds(new Rectangle(1, 1, 465, 35));
        txaQ3.setEnabled(false);
        txaQ3.setDisabledTextColor(Color.black);
        txaQ3.setEditable(false);
        btnParcourirSortie.setFont(new java.awt.Font("SansSerif", Font.PLAIN,
                11));
        btnParcourirSortie.setBounds(new Rectangle(399, 123, 41, 20));
        btnParcourirSortie.setText("...");
        btnParcourirSortie.setBackground(Color.white);
        ckbMeme.setBackground(Color.white);
        ckbMeme.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        ckbMeme.setText("jCheckBox1");
        ckbMeme.setBounds(new Rectangle(21, 96, 368, 23));
        pnlQ4.setBackground(Color.white);
        pnlQ4.setLayout(null);
        txaQ4.setBounds(new Rectangle(1, 1, 465, 35));
        txaQ4.setMinimumSize(new Dimension(70, 30));
        txaQ4.setPreferredSize(new Dimension(70, 40));
        txaQ4.setText("Q4");
        txaQ4.setEditable(false);
        txaQ4.setEnabled(false);
        txaQ4.setDisabledTextColor(Color.black);
        groupQ4.add(rdbPNG);
        groupQ4.add(rdbPS);
        groupQ4.add(rdbEPS);
        groupQ4.add(rdbPDF);
        pnlCouleur.setBackground(Color.white);
        pnlCouleur.setLayout(null);
        txaCouleur.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaCouleur.setBounds(new Rectangle(1, 1, 465, 35));
        txaCouleur.setMinimumSize(new Dimension(70, 30));
        txaCouleur.setPreferredSize(new Dimension(70, 40));
        txaCouleur.setText("Couleur");
        txaCouleur.setEditable(false);
        txaCouleur.setEnabled(false);
        txaCouleur.setDisabledTextColor(Color.black);
        pnlQ5.setBackground(Color.white);
        pnlQ5.setLayout(null);
        txaQ5.setBounds(new Rectangle(1, 1, 465, 35));
        txaQ5.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaQ5.setMinimumSize(new Dimension(70, 30));
        txaQ5.setPreferredSize(new Dimension(70, 40));
        txaQ5.setText("Q5");
        txaQ5.setEditable(false);
        txaQ5.setEnabled(false);
        txaQ5.setDisabledTextColor(Color.black);
        pnlQ6.setBackground(Color.white);
        pnlQ6.setLayout(null);
        txaQ6.setBounds(new Rectangle(1, 1, 465, 35));
        txaQ6.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaQ6.setMinimumSize(new Dimension(70, 30));
        txaQ6.setPreferredSize(new Dimension(70, 40));
        txaQ6.setText("Q6");
        txaQ6.setEditable(false);
        txaQ6.setEnabled(false);
        txaQ6.setDisabledTextColor(Color.black);
        pnlQ7.setBackground(Color.white);
        pnlQ7.setLayout(null);
        txaQ7.setBounds(new Rectangle(1, 1, 465, 35));
        txaQ7.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaQ7.setMinimumSize(new Dimension(70, 30));
        txaQ7.setPreferredSize(new Dimension(70, 40));
        txaQ7.setText("Q7");
        txaQ7.setEditable(false);
        txaQ7.setEnabled(false);
        txaQ7.setDisabledTextColor(Color.black);
        groupQ5.add(rdbDessin);
        groupQ5.add(rdbPage);
        groupQ6.add(rdbOriginal);
        groupQ6.add(rdbLargeur);
        groupQ6.add(rdbHauteur);
        groupQ6.add(rdbPerso);
        pnlFond.add(txaAssistant, null);
        pnlFond.add(pnlQ1, null);
        pnlFond.add(pnlQ4, null);
        pnlFond.add(pnlQ2, null);
        pnlFond.add(pnlQ3, null);
        pnlFond.add(pnlQ5, null);
        pnlFond.add(pnlCouleur, null);
        pnlFond.add(pnlQ6, null);
        pnlFond.add(pnlQ7, null);
        //pnlQ1.setVisible(false);
        pnlQ2.setVisible(false);
        pnlQ3.setVisible(false);
        pnlQ4.setVisible(false);
        pnlCouleur.setVisible(false);
        pnlQ5.setVisible(false);
        pnlQ6.setVisible(false);
        pnlQ7.setVisible(false);
        Properties prop = System.getProperties();
        lblLogo.setIcon(new ImageIcon (new java.net.URL ("file:///"+ prop.getProperty("user.dir") + "/Ressources/pConversionSVG.png")));
        ckbAssistant.setSelected(module.parameter.gbWizard);
    }

    public void btnAnnuler_actionPerformed(ActionEvent e) {
        this.hide();
    }

    public void btnSuivant_actionPerformed(ActionEvent e) {
        if (pnlQ1.isVisible()){
            pnlQ1.setVisible(false);
            if (rdbFichier.isSelected()){
                module.language.AssistantQ2(txaQ2,true);
            } else {
                module.language.AssistantQ2(txaQ2,false);
            }
            pnlQ2.setVisible(true);
            btnPrecedent.setEnabled(true);
        } else if (pnlQ2.isVisible()){
            pnlQ2.setVisible(false);
            pnlQ3.setVisible(true);
        } else if (pnlQ3.isVisible()){
            pnlQ3.setVisible(false);
            pnlQ4.setVisible(true);
        } else if (pnlQ4.isVisible()){
            pnlQ4.setVisible(false);
            if (rdbPNG.isSelected()){
                pnlCouleur.setVisible(true);
            }else{
                pnlQ5.setVisible(true);
            }
        } else if (pnlCouleur.isVisible()){
            pnlCouleur.setVisible(false);
            pnlQ5.setVisible(true);
        } else if (pnlQ5.isVisible()){
            pnlQ5.setVisible(false);
            pnlQ6.setVisible(true);
        } else if (pnlQ6.isVisible()){
            pnlQ6.setVisible(false);
            pnlQ7.setVisible(true);
            module.language.finishWizard(btnSuivant,true);
        } else if (pnlQ7.isVisible()){
            this.hide();
            module.launchConversion();
        }
        ActiverSuivant();
    }

    public void btnPrecedent_actionPerformed(ActionEvent e) {
        if (pnlQ7.isVisible()){
            pnlQ7.setVisible(false);
            pnlQ6.setVisible(true);
            module.language.finishWizard(btnSuivant,false);
        } else if (pnlQ6.isVisible()){
            pnlQ6.setVisible(false);
            pnlQ5.setVisible(true);
        }else if (pnlQ5.isVisible()){
            pnlQ5.setVisible(false);
            if (rdbPNG.isSelected()){
                pnlCouleur.setVisible(true);
            }else{
                pnlQ4.setVisible(true);
            }
        }else if (pnlCouleur.isVisible()){
            pnlCouleur.setVisible(false);
            pnlQ4.setVisible(true);
        }else if (pnlQ4.isVisible()){
            pnlQ4.setVisible(false);
            pnlQ3.setVisible(true);
        }else if (pnlQ3.isVisible()){
            pnlQ3.setVisible(false);
            pnlQ2.setVisible(true);
        }else if (pnlQ2.isVisible()){
            pnlQ2.setVisible(false);
            pnlQ1.setVisible(true);
            btnPrecedent.setEnabled(false);
        }
        ActiverSuivant();
    }

    public void slrOpaque_stateChanged(ChangeEvent e) {
        module.Transparence(slrOpaque.getValue());
        lblPercent.setText(String.valueOf(slrOpaque.getValue())+"%");
    }

    public void slrRouge_stateChanged(ChangeEvent e) {
        lblCouleur.setBackground(new Color(slrRouge.getValue(),slrVert.getValue(),slrBleu.getValue()));
        module.displayColor(slrRouge.getValue(),slrVert.getValue(),slrBleu.getValue());
        lblValeurRouge.setText(String.valueOf(slrRouge.getValue()));
    }

    public void slrVert_stateChanged(ChangeEvent e) {
        lblCouleur.setBackground(new Color(slrRouge.getValue(),slrVert.getValue(),slrBleu.getValue()));
        module.displayColor(slrRouge.getValue(),slrVert.getValue(),slrBleu.getValue());
        lblValeurVert.setText(String.valueOf(slrVert.getValue()));
    }

    public void slrBleu_stateChanged(ChangeEvent e) {
        lblCouleur.setBackground(new Color(slrRouge.getValue(),slrVert.getValue(),slrBleu.getValue()));
        module.displayColor(slrRouge.getValue(),slrVert.getValue(),slrBleu.getValue());
        lblValeurBleu.setText(String.valueOf(slrBleu.getValue()));
    }

    public void txtLargeur_keyTyped(KeyEvent e) {
        module.Numeric(e);
    }

    public void txtHauteur_keyTyped(KeyEvent e) {
        module.Numeric(e);
    }

    public void rdbPerso_actionPerformed(ActionEvent e) {
        txtHauteur.setEnabled(true);
        txtLargeur.setEnabled(true);
    }

    public void rdbHauteur_actionPerformed(ActionEvent e) {
        txtHauteur.setEnabled(true);
        txtLargeur.setEnabled(false);
        txtLargeur.setText("");
        module.Taille(2);
    }

    public void rdbLargeur_actionPerformed(ActionEvent e) {
        txtHauteur.setEnabled(false);
        txtLargeur.setEnabled(true);
        txtHauteur.setText("");
        module.Taille(3);
    }

    public void rdbOriginal_actionPerformed(ActionEvent e) {
        txtHauteur.setEnabled(false);
        txtLargeur.setEnabled(false);
        txtLargeur.setText("");
        txtHauteur.setText("");
        module.Taille(1);
    }

    public void btnParcourirSortie_actionPerformed(ActionEvent e) {
        module.folderOutput(lblDosSortie, ckbMeme);
        module.PrincipalDosSortie(this);
        ActiverSuivant();
    }

    public void ckbMeme_actionPerformed(ActionEvent e) {
        btnParcourirSortie.setEnabled(!ckbMeme.isSelected());
        if (ckbMeme.isSelected()){
            lblDosSortie.setText("");
        }
        module.PrincipalDosSortie(this);
        ActiverSuivant();
    }

    public void btnParcourir_actionPerformed(ActionEvent e) {
        module.fileFolder(rdbDossier.isSelected(),lblFicDos);
        module.mainFileFolder(rdbDossier.isSelected(),lblFicDos.getText());
        ActiverSuivant();
    }

    private void ActiverSuivant(){
        if (pnlQ1.isVisible()){
            btnSuivant.setEnabled(true);
        }else if ((pnlQ2.isVisible())&& (lblFicDos.getText().length() > 0)){
            btnSuivant.setEnabled(true);
        }else if ((pnlQ3.isVisible())&& ((lblDosSortie.getText().length() > 0)||(ckbMeme.isSelected()))){
            btnSuivant.setEnabled(true);
        }else if ((pnlQ4.isVisible())||(pnlCouleur.isVisible())||(pnlQ5.isVisible())||(pnlQ6.isVisible())||(pnlQ7.isVisible())){
            btnSuivant.setEnabled(true);
        }else{
            btnSuivant.setEnabled(false);
        }
    }

    public void rdbFichier_actionPerformed(ActionEvent e) {
        module.fileOrFolder(true);
        lblFicDos.setText("");
    }

    public void rdbDossier_actionPerformed(ActionEvent e) {
        module.fileOrFolder(false);
        lblFicDos.setText("");
    }

    public void rdbPNG_actionPerformed(ActionEvent e) {
        module.format(1,e);
    }

    public void rdbPS_actionPerformed(ActionEvent e) {
        module.format(2,e);
    }

    public void rdbEPS_actionPerformed(ActionEvent e) {
        module.format(3,e);
    }

    public void rdbPDF_actionPerformed(ActionEvent e) {
        module.format(4,e);
    }

    public void rdbPage_actionPerformed(ActionEvent e) {
        module.zone(false);
    }

    public void rdbDessin_actionPerformed(ActionEvent e) {
        module.zone(true);
    }

    public void ckbCouleurFond_actionPerformed(ActionEvent e) {
        slrRouge.setEnabled(ckbCouleurFond.isSelected());
        slrVert.setEnabled(ckbCouleurFond.isSelected());
        slrBleu.setEnabled(ckbCouleurFond.isSelected());
        slrOpaque.setEnabled(ckbCouleurFond.isSelected());
        module.Couleurfond(ckbCouleurFond.isSelected(),e);
        if (ckbCouleurFond.isSelected()){
            lblCouleur.setBackground(new Color(slrRouge.getValue(),
                                               slrVert.getValue(),
                                               slrBleu.getValue()));
        }else{
            lblCouleur.setBackground(new Color(255,255,255));
        }
    }

    public void txtHauteur_keyReleased(KeyEvent e) {
        module.height(txtHauteur.getText());
    }

    public void txtLargeur_keyReleased(KeyEvent e) {
        module.width(txtLargeur.getText());
    }

    public void ckbAssistant_actionPerformed(ActionEvent e) {
        module.parameter.gbWizard = ckbAssistant.isSelected();
    }
}

/***********************************************************************************/
/************************************** class **************************************/
/***********************************************************************************/

class Assistant_ckbAssistant_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_ckbAssistant_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ckbAssistant_actionPerformed(e);
    }
}

class Assistant_rdbDessin_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbDessin_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbDessin_actionPerformed(e);
    }
}

class Assistant_ckbCouleurFond_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_ckbCouleurFond_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ckbCouleurFond_actionPerformed(e);
    }
}

class Assistant_rdbPDF_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbPDF_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbPDF_actionPerformed(e);
    }
}

class Assistant_rdbPage_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbPage_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbPage_actionPerformed(e);
    }
}

class Assistant_rdbEPS_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbEPS_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbEPS_actionPerformed(e);
    }
}

class Assistant_rdbPS_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbPS_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbPS_actionPerformed(e);
    }
}

class Assistant_rdbDossier_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbDossier_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbDossier_actionPerformed(e);
    }
}

class Assistant_rdbPNG_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbPNG_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbPNG_actionPerformed(e);
    }
}

class Assistant_rdbFichier_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbFichier_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbFichier_actionPerformed(e);
    }
}

class Assistant_btnParcourir_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_btnParcourir_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnParcourir_actionPerformed(e);
    }
}

class Assistant_ckbMeme_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_ckbMeme_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ckbMeme_actionPerformed(e);
    }
}

class Assistant_btnParcourirSortie_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_btnParcourirSortie_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnParcourirSortie_actionPerformed(e);
    }
}

class Assistant_rdbOriginal_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbOriginal_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbOriginal_actionPerformed(e);
    }
}

class Assistant_rdbLargeur_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbLargeur_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbLargeur_actionPerformed(e);
    }
}

class Assistant_rdbHauteur_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbHauteur_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbHauteur_actionPerformed(e);
    }
}

class Assistant_rdbPerso_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_rdbPerso_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.rdbPerso_actionPerformed(e);
    }
}

class Assistant_txtHauteur_keyAdapter extends KeyAdapter {
    private AssistantDialog adaptee;
    Assistant_txtHauteur_keyAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void keyTyped(KeyEvent e) {
        adaptee.txtHauteur_keyTyped(e);
    }

    public void keyReleased(KeyEvent e){
        adaptee.txtHauteur_keyReleased(e);
    }
}

class Assistant_slrBleu_changeAdapter implements ChangeListener {
    private AssistantDialog adaptee;
    Assistant_slrBleu_changeAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void stateChanged(ChangeEvent e) {
        adaptee.slrBleu_stateChanged(e);
    }
}

class Assistant_txtLargeur_keyAdapter extends KeyAdapter {
    private AssistantDialog adaptee;
    Assistant_txtLargeur_keyAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void keyTyped(KeyEvent e) {
        adaptee.txtLargeur_keyTyped(e);
    }

    public void keyReleased(KeyEvent e){
        adaptee.txtLargeur_keyReleased(e);
    }
}

class Assistant_slrVert_changeAdapter implements ChangeListener {
    private AssistantDialog adaptee;
    Assistant_slrVert_changeAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void stateChanged(ChangeEvent e) {
        adaptee.slrVert_stateChanged(e);
    }
}

class Assistant_slrRouge_changeAdapter implements ChangeListener {
    private AssistantDialog adaptee;
    Assistant_slrRouge_changeAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void stateChanged(ChangeEvent e) {
        adaptee.slrRouge_stateChanged(e);
    }
}

class Assistant_slrOpaque_changeAdapter implements ChangeListener {
    private AssistantDialog adaptee;
    Assistant_slrOpaque_changeAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void stateChanged(ChangeEvent e) {
        adaptee.slrOpaque_stateChanged(e);
    }
}

class Assistant_btnPrecedent_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_btnPrecedent_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnPrecedent_actionPerformed(e);
    }
}

class Assistant_btnSuivant_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_btnSuivant_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnSuivant_actionPerformed(e);
    }
}

class Assistant_btnAnnuler_actionAdapter implements ActionListener {
    private AssistantDialog adaptee;
    Assistant_btnAnnuler_actionAdapter(AssistantDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnAnnuler_actionPerformed(e);
    }
}
