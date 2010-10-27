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

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.*;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;

public class HomeDialog extends JDialog
{
    private static final long serialVersionUID = -3542763460260510695L;
	
    JPanel panel1 = new JPanel();
    JLabel lblLogo = new JLabel();
    String currentDirectory;
    JLabel jLabel1 = new JLabel();
    int liI = 0;
    ActionListener showSplash = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            liI++;
            if (liI == 5)
            {
                TimerSplash.stop();
                close();
            }
        }
    };
    Timer TimerSplash = new Timer(1000,showSplash);
    Parameter gaParametre = new Parameter("fr","",true);
    BorderLayout borderLayout1 = new BorderLayout();
    public HomeDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        try {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jbInit();
            pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public HomeDialog() {
        this(new Frame(), "Accueil", false);
    }

    private void jbInit() throws Exception {
        panel1.setLayout(borderLayout1);
        lblLogo.setText("");
        this.getContentPane().setBackground(Color.white);
        this.setModal(true);
        this.addWindowListener(new Accueil_this_windowAdapter(this));
        this.setSize(607,275);
        panel1.setBackground(Color.white);
        panel1.setOpaque(false);
        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel1.setText("Version 1.0     ");
        getContentPane().add(panel1);
        panel1.add(lblLogo, java.awt.BorderLayout.CENTER);
        panel1.add(jLabel1, java.awt.BorderLayout.SOUTH);
        setUndecorated(true);
        Properties prop = System.getProperties();
        currentDirectory = prop.getProperty("user.dir");
        lblLogo.setBackground(Color.white);
        lblLogo.setIcon(new ImageIcon (new java.net.URL ("file:///"+ currentDirectory + "/Ressources/ConversionSVG.png")));
        readParameters();
        TimerSplash.start();
    }

    private void readParameters(){
        try{
            FileInputStream Lecture = new FileInputStream("Ressources/Parametres.dat");
            ObjectInputStream EnregistrementLu = new ObjectInputStream(Lecture);
            try {
                gaParametre = (Parameter)EnregistrementLu.readObject();
            } catch (ClassNotFoundException io){
            }
            Lecture.close();
            EnregistrementLu.close();
        }
        catch (IOException io){
        }
    }

    private void close(){
        this.hide();
    }

}

/***********************************************************************************/
/************************************** class **************************************/
/***********************************************************************************/

class Accueil_this_windowAdapter extends WindowAdapter {
    private HomeDialog adaptee;
    Accueil_this_windowAdapter(HomeDialog adaptee) {
        this.adaptee = adaptee;
    }

}
