package conversion.ui.settings;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.prefs.IPreferencePageContainer;
import org.prefs.PreferencePage;

import com.bric.swing.ColorPicker;

public class DefaultsPreferencePage extends PreferencePage {

	private static final long serialVersionUID = -8539690532878618137L;

	/**
	 * The page container holding this <code>PreferencePage</code>.
	 */
	private IPreferencePageContainer container;

	/**
	 * Represents whether any setting on this page has changed.
	 * <p>
	 * This value will be <code>True</code> if a value has changed, otherwise
	 * <code>False</code>.
	 * <p>
	 */
	private boolean dirty;

	JTextField colorTextField;

	/**
	 * The color last selected from a <code>ColorPicker</code>.
	 * <p>
	 * The default is <code>Color.WHITE</code>
	 * </p>
	 */
	Color selectedColor = Color.WHITE;

	public DefaultsPreferencePage(String title, String description) {
		super(title, description);
		createContents();
	}

	/**
	 * Creates a <code>DefaultsPreferencePage</code> object specifying the
	 * <i>title</i>, and <i>description</i>.
	 * 
	 * @param container
	 *            The container in which this
	 *            <code>DefaultsPreferencePage</code> is being held.
	 * @param title
	 *            The title. If value is <code>null</code> the <i>title</i>
	 *            attribute is set to empty string ( <code>""</code>)
	 * @param description
	 *            A description of the page. If value is <code>null</code> the
	 *            <i>description</i> attribute is set to empty string (
	 *            <code>""</code>)
	 */
	public DefaultsPreferencePage(IPreferencePageContainer container,
			String title, String description) {
		super(title, description);
		setPageContainer(container);
		createContents();
	}

	/**
	 * Sets the container in which this <code>PreferencePage</code> is being
	 * held.
	 * 
	 * @param container
	 *            The container in which this <code>PreferencePage</code> is
	 *            being held.
	 * @throws NullPointerException
	 *             if the container argument is <code>null</code>.
	 */
	public void setPageContainer(IPreferencePageContainer container)
			throws NullPointerException {
		if (container == null) {
			throw new NullPointerException("container cannot be set to null");
		}
		this.container = container;
	}

	/**
	 * Returns the container in which this <code>PreferencePage</code> is being
	 * held.
	 */
	public IPreferencePageContainer getPageContainer() {
		return container;
	}

	//
	// PreferencePage members
	//

	@Override
	protected void createContents() {
		setLayout(new GridBagLayout());

		GridBagConstraints c;

		//
		// Size Panel
		//
		JPanel sizePanel = new JPanel(new GridBagLayout());
		sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));

		// Height
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel heightLabel = new JLabel("Height");
		heightLabel.setToolTipText("The height in pixels");
		sizePanel.add(heightLabel, c);
		c = new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		// TODO set the current settings value
		JTextField heightTextField = new JTextField();
		sizePanel.add(heightTextField, c);

		// Width
		c = new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel widthLabel = new JLabel("Width");
		widthLabel.setToolTipText("The width in pixels");
		sizePanel.add(widthLabel, c);
		c = new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		// TODO set the current settings value
		JTextField widthTextField = new JTextField();
		sizePanel.add(widthTextField, c);
		//
		// Size Panel end
		//

		//
		// Background
		//
		JPanel backgroundPanel = new JPanel(new GridBagLayout());
		backgroundPanel.setBorder(BorderFactory
				.createTitledBorder("Background"));

		// Background Color
		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		JLabel colorLabel = new JLabel("Color");
		colorLabel.setToolTipText("The background color (including opacity)");
		backgroundPanel.add(colorLabel, c);
		c = new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		// TODO set the current settings value
		colorTextField = new JTextField();
		backgroundPanel.add(colorTextField, c);
		c = new GridBagConstraints(2, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		// TODO use the color picker logo from Dropbox
		JButton colorButton = new JButton("...");
		// TODO set selectedColor to the settings value
		// this action listener handles converting the selected color w/ opacity
		// to a
		// hex value which can then stored in the properties
		colorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((selectedColor = ColorPicker.showDialog(
						SettingsDialog.parent,
						selectedColor != null ? selectedColor : Color.WHITE,
						true)) != null) {
					colorTextField.setText(Integer.toHexString(selectedColor
							.getRGB() & 0xffffffff));
				}
			}
		});
		backgroundPanel.add(colorButton, c);
		//
		// Background end
		//

		c = new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(sizePanel, c);
		c = new GridBagConstraints(0, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0);
		add(backgroundPanel, c);
	}

	@Override
	public boolean okToLeave() {
		return dirty;
	}

	@Override
	public boolean performCancel() {
		// TODO implement setting state back before modifications
		return true;
	}

	@Override
	public boolean performOk() {
		try {
			container.getPreferenceStore().save();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean performDefault() {
		return true;
	}

	@Override
	public String getClassName() {
		return "DefaultsPreferencePage";
	}

}
