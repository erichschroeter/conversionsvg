package org.conversionsvg.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

public class ProgressDialog extends JDialog
{
    private static final long serialVersionUID = -3110458696300312879L;
    ResourceBundle languageBundle = ResourceBundle.getBundle("org.conversionsvg.gui.MainWindow", Locale.getDefault());

    JPanel                    panel            = new JPanel();
    JProgressBar              progressBar;
    DefaultListModel          statusOutput;
    private JList             completedTasksList;
    JButton                   closeButton;
    
    private EventListener eventListener;

    public ProgressDialog(JFrame window)
    {
        super(window);
        init();
    }

    private void init()
    {
        eventListener = new EventListener(this);
        statusOutput = new DefaultListModel();
        completedTasksList = new JList(statusOutput);
        JScrollPane completedTasksScrollPane = new JScrollPane(completedTasksList);
        progressBar = new JProgressBar();
        closeButton = new JButton(languageBundle.getString("Close"));
        closeButton.addActionListener(eventListener);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);
        panel.add(completedTasksScrollPane, c);
        c = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        panel.add(progressBar, c);
        c = new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);
        panel.add(closeButton, c);

        add(panel);
        setLocationRelativeTo(getParent());
        setMinimumSize(new Dimension(300, 300));
        setPreferredSize(new Dimension(300, 300));
    }
    
    private class EventListener implements ActionListener
    {
        ProgressDialog dialog;
        public EventListener(ProgressDialog dialog)
        {
            this.dialog = dialog;
        }
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object obj = e.getSource();
            if (obj.equals(closeButton))
            {
                dialog.setVisible(false);
            }
        }
        
    }
}
