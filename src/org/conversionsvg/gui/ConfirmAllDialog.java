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

public class ConfirmAllDialog extends JDialog {

	private static final long serialVersionUID = 1478574901983349013L;
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"res/i18ln/ConfirmAllDialog", Locale.getDefault());

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
		GridBagConstraints c;

		c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		JLabel messageLabel = new JLabel(message);
		add(messageLabel, c);

		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton yesButton = new JButton(i18ln.getString("Yes"));
		yesButton.addActionListener(yesActionListener());
		add(yesButton, c);

		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton yesToAllButton = new JButton(i18ln.getString("YesToAll"));
		yesToAllButton.addActionListener(yesToAllActionListener());
		add(yesToAllButton, c);

		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton noButton = new JButton(i18ln.getString("No"));
		noButton.addActionListener(noActionListener());
		add(noButton, c);

		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton noToAllButton = new JButton(i18ln.getString("NoToAll"));
		noToAllButton.addActionListener(noToAllActionListener());
		add(noToAllButton, c);

		c = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
		JButton cancelButton = new JButton(i18ln.getString("Cancel"));
		cancelButton.addActionListener(cancelActionListener());
		add(cancelButton, c);

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
			}
		};
	}

}
