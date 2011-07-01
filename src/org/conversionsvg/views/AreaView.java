package org.conversionsvg.views;

import java.awt.FlowLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.conversionsvg.ConversionSvgApplication;
import org.conversionsvg.InkscapeCommandBuilder;
import org.conversionsvg.models.AreaModel;
import org.conversionsvg.models.DomainModel;

import com.jidesoft.app.framework.DataModel;
import com.jidesoft.app.framework.DataModelException;
import com.jidesoft.app.framework.gui.DataViewPane;

@SuppressWarnings("serial")
public class AreaView extends DomainView {
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.MainWindow", Locale.getDefault());

	/** Used to export the image drawing area */
	private JRadioButton drawingRadioButton;
	/** Used to export the image page area */
	private JRadioButton pageRadioButton;

	/**
	 * Constructs a default <code>AreaView</code> initializing the components.
	 */
	public AreaView(ConversionSvgApplication application) {
		super(application);
		init();
	}

	@Override
	protected void init() {
		setLayout(new FlowLayout(FlowLayout.LEFT));

		drawingRadioButton = new JRadioButton(i18ln
				.getString("DrawingRadioButton"), true);
		drawingRadioButton
				.setToolTipText("Exported area is the entire drawing (not page)");
		pageRadioButton = new JRadioButton(i18ln.getString("PageRadioButton"));
		pageRadioButton.setToolTipText("Exported area is the entire page");

		ButtonGroup group = new ButtonGroup();
		group.add(pageRadioButton);
		group.add(drawingRadioButton);

		add(drawingRadioButton);
		add(pageRadioButton);
		setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("ExportAreaPanel")));
	}

	/**
	 * Updates the {@link AreaModel} with the following Inkscape options:
	 * <ul>
	 * <li>{@link Inkscape.Option#AREA_DRAWING}</li>
	 * <li>{@link Inkscape.Option#AREA_PAGE}</li>
	 * <li>{@link Inkscape.Option#AREA_CUSTOM}</li>
	 * </ul>
	 * <p>
	 * The following list shows the possible values stored in the options map
	 * for the option keys for the options above based on the selected radio
	 * button in the view:
	 * <ul>
	 * <li><code>true</code> for the selected area</li>
	 * <li><code>false</code> for all unselected areas</li>
	 * </ul>
	 */
	@Override
	public void updateModel(DomainModel model) {
		InkscapeCommandBuilder command = ((AreaModel) model).getCommand();
		if (drawingRadioButton.isSelected()) {
			command.useDrawingArea();
		} else if (pageRadioButton.isSelected()) {
			command.usePageArea();
		} else {
			// default use page
			command.usePageArea();
		}
	}

}
