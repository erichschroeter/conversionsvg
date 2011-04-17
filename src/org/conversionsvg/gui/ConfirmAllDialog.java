package org.conversionsvg.gui;

import java.awt.Dialog;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfirmAllDialog extends JDialog {

	private static final long serialVersionUID = 1478574901983349013L;
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.ConfirmAllDialog", Locale.getDefault());

	/**
	 * Return value from class method if no action is taken.
	 * <p>
	 * This is the default value returned if the dialog is never set visible.
	 * </p>
	 */
	public static final int NONE = -1;

	/**
	 * Return value from class method if YES is chosen.
	 */
	public static final int YES = 0;

	/**
	 * Return value from class method if YES TO ALL is chosen.
	 */
	public static final int YES_TO_ALL = 1;

	/**
	 * Return value from class method if NO is chosen.
	 */
	public static final int NO = 2;

	/**
	 * Return value from class method if NO TO ALL is chosen.
	 */
	public static final int NO_TO_ALL = 3;

	/**
	 * Return value from class method if CANCEL is chosen.
	 */
	public static final int CANCEL = 4;

	/**
	 * The message that is displayed to the user requesting a response.
	 */
	private String message;

	/**
	 * The choice selected.
	 * <p>
	 * Default is CANCEL
	 * </p>
	 */
	private static int choice = NONE;

	public ConfirmAllDialog(String message) {
		this(message, "");
	}

	public ConfirmAllDialog(String message, String title) {
		super((Dialog) null, true);
		this.message = message;
		setTitle(title);
		init();
	}

	private void init() {
		setLayout(new GridBagLayout());
		setResizable(false);
		GridBagConstraints c;

		c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		JLabel messageLabel = new JLabel(message);
		messageLabel.setToolTipText(message);
		add(messageLabel, c);
		
		//
		// Buttons
		//
		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		add(buttonPanel, c);
		
		// Yes
		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton yesButton = new JButton(i18ln.getString("Yes"));
		yesButton.addActionListener(yesActionListener());
		buttonPanel.add(yesButton, c);

		// Yes To All
		c = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton yesToAllButton = new JButton(i18ln.getString("YesToAll"));
		yesToAllButton.addActionListener(yesToAllActionListener());
		buttonPanel.add(yesToAllButton, c);

		// No
		c = new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton noButton = new JButton(i18ln.getString("No"));
		noButton.addActionListener(noActionListener());
		buttonPanel.add(noButton, c);

		// No To All
		c = new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton noToAllButton = new JButton(i18ln.getString("NoToAll"));
		noToAllButton.addActionListener(noToAllActionListener());
		buttonPanel.add(noToAllButton, c);

		// Cancel
		c = new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton cancelButton = new JButton(i18ln.getString("Cancel"));
		cancelButton.addActionListener(cancelActionListener());
		buttonPanel.add(cancelButton, c);
		
		pack();

		// Center the dialog relative to the application
		setLocationRelativeTo(getParent());
//		Dimension appSize = getParent().getSize();
//		Dimension dialogSize = getSize();
//		if (dialogSize.height > appSize.height) {
//			dialogSize.height = appSize.height;
//		}
//		if (dialogSize.width > appSize.width) {
//			dialogSize.width = appSize.width;
//		}
//		setLocation((appSize.width - dialogSize.width) / 2,
//				(appSize.height - dialogSize.height) / 2);
	}

	public static int showDialog(String message) throws HeadlessException {
		return showDialog(message, "");
	}

	public static int showDialog(String message, String title)
			throws HeadlessException {
		if (GraphicsEnvironment.isHeadless()) {
			throw new HeadlessException("A dialog needs to be exitable");
		}
		ConfirmAllDialog dlg = new ConfirmAllDialog(message, title);
		dlg.setVisible(true);

		return choice;
	}

	//
	// Action Listeners
	//

	/**
	 * @return an <code>ActionListener</code> which sets the <i>choice</i>
	 *         attribute to YES.
	 */
	private ActionListener yesActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				choice = YES;
				setVisible(false);
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which sets the <i>choice</i>
	 *         attribute to YES_TO_ALL.
	 */
	private ActionListener yesToAllActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				choice = YES_TO_ALL;
				setVisible(false);
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which sets the <i>choice</i>
	 *         attribute to NO.
	 */
	private ActionListener noActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				choice = NO;
				setVisible(false);
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which sets the <i>choice</i>
	 *         attribute to NO_TO_ALL.
	 */
	private ActionListener noToAllActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				choice = NO_TO_ALL;
				setVisible(false);
			}
		};
	}

	/**
	 * @return an <code>ActionListener</code> which sets the <i>choice</i>
	 *         attribute to CANCEL.
	 */
	private ActionListener cancelActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				choice = CANCEL;
				setVisible(false);
			}
		};
	}

}
