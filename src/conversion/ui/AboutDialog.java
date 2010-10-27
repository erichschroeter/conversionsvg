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
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class AboutDialog extends JDialog
{
    private static final long serialVersionUID = 8520033695347084393L;
	
    JPanel panel1 = new JPanel();
    JLabel lblZone = new JLabel();
    JLabel jLabel1 = new JLabel();
    JLabel lblGPL = new JLabel();
    JLabel jLabel2 = new JLabel();
    JButton btnFermer = new JButton();
    JLabel lblLogo = new JLabel();
    JLabel lblInkscape = new JLabel();
    JTextArea txaInkscape = new JTextArea();
    Module module;

    public AboutDialog(Frame owner, String title, boolean modal, Module pmodule) {
        super(owner, title, modal);
        try {
            module = pmodule;
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jbInit();
            pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public AboutDialog() {
        this(new Frame(), "A_Propos", false, new Module(new MainWindow()));
    }

    private void jbInit() throws Exception {
        panel1.setLayout(null);
        lblZone.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        lblZone.setForeground(Color.blue);
        lblZone.setHorizontalAlignment(SwingConstants.CENTER);
        lblZone.setHorizontalTextPosition(SwingConstants.CENTER);
        lblZone.setText("zonelibre.grics.qc.ca");
        lblZone.setBounds(new Rectangle(117, 166, 166, 15));
        lblZone.addMouseListener(new APropos_lblZone_mouseAdapter(this));
        jLabel1.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Copyright \u00A9 2007  Soci�t� Grics");
        jLabel1.setBounds(new Rectangle(51, 151, 298, 15));
        lblGPL.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        lblGPL.setHorizontalAlignment(SwingConstants.CENTER);
        lblGPL.setText("");
        lblGPL.setBounds(new Rectangle(114, 137, 173, 15));
        jLabel2.setFont(new java.awt.Font("SansSerif", Font.BOLD, 11));
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("Version 1.0");
        jLabel2.setBounds(new Rectangle(113, 122, 174, 15));
        btnFermer.setBounds(new Rectangle(313, 216, 71, 23));
        btnFermer.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        btnFermer.setText("Fermer");
        btnFermer.addActionListener(new APropos_btnFermer_actionAdapter(this));
        Properties prop = System.getProperties();
        lblLogo.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        lblLogo.setIcon(new ImageIcon (new java.net.URL ("file:///"+ prop.getProperty("user.dir") + "/Ressources/ConversionSVGm.png")));
        lblLogo.setBounds(new Rectangle(43, 3, 315, 135));
        panel1.setBackground(Color.white);
        panel1.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        lblInkscape.setBackground(Color.white);
        lblInkscape.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 11));
        lblInkscape.setBorder(null);
        lblInkscape.setText("");
        lblInkscape.setIcon(new ImageIcon (new java.net.URL ("file:///"+ prop.getProperty("user.dir") + "/Ressources/Inkscape.png")));
        lblInkscape.setBounds(new Rectangle(92, 188, 52, 52));
        lblInkscape.addMouseListener(new APropos_lblInkscape_mouseAdapter(this));
        txaInkscape.setEnabled(false);
        txaInkscape.setFont(new java.awt.Font("SansSerif", Font.PLAIN, 12));
        txaInkscape.setBorder(null);
        txaInkscape.setDisabledTextColor(Color.black);
        txaInkscape.setEditable(false);
        txaInkscape.setText("");
        txaInkscape.setLineWrap(true);
        txaInkscape.setWrapStyleWord(true);
        txaInkscape.setBounds(new Rectangle(146, 196, 158, 37));
        getContentPane().add(panel1);
        panel1.add(lblLogo);
        panel1.add(lblGPL);
        panel1.add(jLabel2);
        panel1.add(jLabel1);
        panel1.add(lblZone, null);
        panel1.add(btnFermer);
        panel1.add(txaInkscape);
        panel1.add(lblInkscape);
    }


    public void lblZone_mouseClicked(MouseEvent e) {
        Properties sys = System.getProperties();
        String os = sys.getProperty("os.name");
        Runtime r = Runtime.getRuntime();
        try {
            if (os.endsWith("NT")||os.endsWith("2000")||os.endsWith("XP")){
                r.exec("cmd /c start http://www.zonelibre.grics.qc.ca/");
            } else {
                r.exec("start http://www.zonelibre.grics.qc.ca/");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void lblZone_mouseEntered(MouseEvent e) {
        lblZone.setText("<html><u>"+"zonelibre.grics.qc.ca"+"</u></html>"); //souligner le texte
    }

    public void lblZone_mouseExited(MouseEvent e) {
        lblZone.setText("zonelibre.grics.qc.ca"); // enlever le soulign�
    }

    public void btnFermer_actionPerformed(ActionEvent e) {
        this.hide();
    }

    public void lblInkscape_mouseEntered(MouseEvent e) {
        lblInkscape.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public void lblInkscape_mouseExited(MouseEvent e) {
        lblInkscape.setBorder(null);
    }

    public void lblInkscape_mouseClicked(MouseEvent e) {
        Properties sys = System.getProperties();
        String os = sys.getProperty("os.name");
        Runtime r = Runtime.getRuntime();
        try {
            if (os.endsWith("NT")||os.endsWith("2000")||os.endsWith("XP")){
                r.exec("cmd /c start http://www.inkscape.org/");
            } else {
                r.exec("start http://www.inkscape.org/");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void lblInkscape_mouseReleased(MouseEvent e) {
        lblInkscape.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public void lblInkscape_mousePressed(MouseEvent e) {
        lblInkscape.setBorder(BorderFactory.createLoweredBevelBorder());
    }
}

/***********************************************************************************/
/************************************** class **************************************/
/***********************************************************************************/
class APropos_lblInkscape_mouseAdapter extends MouseAdapter {
    private AboutDialog adaptee;
    APropos_lblInkscape_mouseAdapter(AboutDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseEntered(MouseEvent e) {
        adaptee.lblInkscape_mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        adaptee.lblInkscape_mouseExited(e);
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.lblInkscape_mouseClicked(e);
    }

    public void mouseReleased(MouseEvent e) {
        adaptee.lblInkscape_mouseReleased(e);
    }

    public void mousePressed(MouseEvent e) {
        adaptee.lblInkscape_mousePressed(e);
    }

}

class APropos_btnFermer_actionAdapter implements ActionListener {
    private AboutDialog adaptee;
    APropos_btnFermer_actionAdapter(AboutDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnFermer_actionPerformed(e);
    }
}

class APropos_lblZone_mouseAdapter extends MouseAdapter {
    private AboutDialog adaptee;
    APropos_lblZone_mouseAdapter(AboutDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.lblZone_mouseClicked(e);
    }

    public void mouseEntered(MouseEvent e) {
        adaptee.lblZone_mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        adaptee.lblZone_mouseExited(e);
    }
}
