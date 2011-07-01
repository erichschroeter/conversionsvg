package org.conversionsvg.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conversionsvg.ConversionSvgApplication;
import org.conversionsvg.models.DomainModel;
import org.conversionsvg.models.OptionsModel;

import com.jidesoft.app.framework.DataModel;
import com.jidesoft.app.framework.DataModelException;
import com.jidesoft.app.framework.gui.DataViewPane;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSplitPane;

/**
 * The options view is the <em>JIDE Desktop Application Framework</em> view that
 * presents the user with the supported options for converting SVG images to
 * other formats. The <code>OptionsView</code> consists of derived
 * {@link DomainView}s which break up options into manageable user interface
 * components.
 * 
 * @see AreaView
 * @see ColorView
 * @see FileSelectionView
 * @see FormatView
 * @see SizeView
 * @author Erich Schroeter
 */
@SuppressWarnings("serial")
public class OptionsView extends DataViewPane {

	private JideSplitPane splitPane;
	/** The component to be displayed in the left side of the split pane */
	private JPanel leftPane;
	/** The component to be displayed in the right side of the split pane */
	private JPanel rightPane;
	private Map<String, DomainView> subviews;

	public OptionsView() {
		subviews = new HashMap<String, DomainView>();
	}

	/**
	 * Returns the left pane component. This is an aggregation of the following
	 * views:
	 * <ul>
	 * <li>{@link FormatView}</li>
	 * <li>{@link SizeView}</li>
	 * <li>{@link AreaView}</li>
	 * <li>{@link ColorView}</li>
	 * </ul>
	 * 
	 * @return the left pane
	 */
	public JPanel getLeftPane() {
		return leftPane;
	}

	/**
	 * Sets the left pane of this view.
	 * 
	 * @param leftPane
	 *            the left pane component
	 */
	public void setLeftPane(JPanel leftPane) {
		this.leftPane = leftPane;
	}

	/**
	 * Returns the right pane component. This is a {@link FileSelectionView}.
	 * 
	 * @return the right pane
	 */
	public JPanel getRightPane() {
		return rightPane;
	}

	/**
	 * Sets the right pane of this view.
	 * 
	 * @param rightPane
	 *            the right pane component
	 */
	public void setRightPane(JPanel rightPane) {
		this.rightPane = rightPane;
	}

	/**
	 * Registers the <code>view</code> with this <em>JDAF</em> view. Registering
	 * <em>sub views</em> are necessary in order to update the options
	 * represented by the <em>sub views</em> with this view's model.
	 * <p>
	 * The <code>key</code> follows a convention of using a
	 * <code>public static final String</code> value from {@link OptionsModel}.
	 * 
	 * @param key
	 *            the sub view key
	 * @param view
	 *            the sub view
	 * @return <code>view</code> to allow for additional configuration
	 */
	protected DomainView registerSubView(String key, DomainView view) {
		subviews.put(key, view);
		return view;
	}

	/**
	 * Creates and returns the left pane of this view.
	 * 
	 * @return the left pane component
	 */
	protected JPanel createLeftPane() {
		JPanel left = new JPanel();
		left.setLayout(new GridBagLayout());

		// initialize sub views
		// FormatView formatView = new FormatView(
		// (ConversionSvgApplication) getApplication());
		AreaView areaView = new AreaView(
				(ConversionSvgApplication) getApplication());
		SizeView sizeView = new SizeView(
				(ConversionSvgApplication) getApplication());
		ColorView colorView = new ColorView(
				(ConversionSvgApplication) getApplication());

		GridBagConstraints c;
		c = new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		left.add(
				registerSubView(OptionsModel.SUB_KEY_FORMAT, new FormatView(
						(ConversionSvgApplication) getApplication())), c);
		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		left.add(areaView, c);
		c = new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		left.add(sizeView, c);
		c = new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		left.add(colorView, c);
		return left;
	}

	/**
	 * Creates and returns the right pane of this view.
	 * 
	 * @return the right pane component
	 */
	protected JPanel createRightPane() {
		return new FileSelectionView(
				(ConversionSvgApplication) getApplication());
	}

	//
	// DataViewPane members
	//

	@Override
	protected void initializeComponents() {
		splitPane = new JideSplitPane();
		setLeftPane(createLeftPane());
		setRightPane(createRightPane());
		splitPane.add(new JScrollPane(getLeftPane()), JideBoxLayout.FLEXIBLE);
		splitPane.add(new JScrollPane(getRightPane()), JideBoxLayout.FLEXIBLE);
		add(splitPane);
	}

	/**
	 * Updates the model based on the user input from the graphical interface.
	 * <p>
	 * This iterates over the registered <em>sub views</em> in
	 * <code>OptionsView</code> and the <em>sub models</em> in
	 * <code>OptionsModel</code>. A <em>sub view</em> and a <em>sub model</em>
	 * are required to have the same <em>key</em> mapped to them in order to
	 * successfully update the options.
	 * 
	 * @param dataModel
	 *            the data model
	 */
	@Override
	public void updateModel(DataModel dataModel) throws DataModelException {
		OptionsModel model = (OptionsModel) dataModel;
		Map<String, DomainModel> domainModels = model.getSubModels();
		for (String key : domainModels.keySet()) {
			DomainView subview = subviews.get(key);
			DomainModel submodel = domainModels.get(key);

			if (subview != null && submodel != null) {
				subview.updateModel(submodel);
			}
		}
		super.updateModel(dataModel);
	}
}
