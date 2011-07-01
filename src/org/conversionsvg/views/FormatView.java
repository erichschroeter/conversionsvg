package org.conversionsvg.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;

import org.conversionsvg.ConversionSvgApplication;
import org.conversionsvg.InkscapeCommandBuilder;
import org.conversionsvg.models.DomainModel;
import org.conversionsvg.models.FormatModel;

@SuppressWarnings("serial")
public class FormatView extends DomainView {
	static final ResourceBundle i18ln = ResourceBundle.getBundle(
			"org.conversionsvg.gui.MainWindow", Locale.getDefault());

	/** Used to export image as a PNG */
	private JCheckBox pngCheckBox;
	/** Used to export image as a PS */
	private JCheckBox psCheckBox;
	/** Used to export image as a EPS */
	private JCheckBox epsCheckBox;
	/** Used to export image as a PDF */
	private JCheckBox pdfCheckBox;

	/**
	 * Constructs a default <code>FormatView</code> initializing the components.
	 */
	public FormatView(ConversionSvgApplication application) {
		super(application);
		init();
	}

	@Override
	protected void init() {
		GroupLayout layout = new GroupLayout(this);

		pngCheckBox = new JCheckBox(".PNG", true);
		pngCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		psCheckBox = new JCheckBox(".PS");
		epsCheckBox = new JCheckBox(".EPS");
		pdfCheckBox = new JCheckBox(".PDF");

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pngCheckBox).addComponent(pdfCheckBox))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								epsCheckBox).addComponent(psCheckBox)));
		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pngCheckBox).addComponent(epsCheckBox))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING).addComponent(
								pdfCheckBox).addComponent(psCheckBox)));
		setLayout(layout);
		setBorder(BorderFactory.createTitledBorder(i18ln
				.getString("FormatPanel")));

		add(epsCheckBox);
		add(pngCheckBox);
		add(psCheckBox);
		add(pdfCheckBox);
	}

	/**
	 * Updates the {@link FormatModel} with the following Inkscape options:
	 * <ul>
	 * <li>{@link Inkscape.Option#EXPORT_PNG}</li>
	 * <li>{@link Inkscape.Option#EXPORT_PDF}</li>
	 * <li>{@link Inkscape.Option#EXPORT_PS}</li>
	 * <li>{@link Inkscape.Option#EXPORT_EPS}</li>
	 * </ul>
	 * <p>
	 * The following list shows the possible values stored in the options map
	 * for the option keys for the options above based on the selected
	 * checkboxes in the view:
	 * <ul>
	 * <li><code>true</code> if selected</li>
	 * <li><code>false</code> if not selected</li>
	 * </ul>
	 */
	@Override
	public void updateModel(DomainModel model) {
		InkscapeCommandBuilder command = ((FormatModel) model).getCommand();
		command.exportAsPng(pngCheckBox.isSelected());
		command.exportAsPdf(pdfCheckBox.isSelected());
		command.exportAsPs(psCheckBox.isSelected());
		command.exportAsEps(epsCheckBox.isSelected());
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public void updateModel(DataModel dataModel) throws DataModelException {
	// Map<Inkscape.Option, Object> options = (Map<Inkscape.Option, Object>)
	// ((BasicDataModel) dataModel)
	// .getData();
	// options.put(Inkscape.Option.EXPORT_PNG, pngCheckBox.isSelected());
	// options.put(Inkscape.Option.EXPORT_PDF, pdfCheckBox.isSelected());
	// options.put(Inkscape.Option.EXPORT_PS, psCheckBox.isSelected());
	// options.put(Inkscape.Option.EXPORT_EPS, epsCheckBox.isSelected());
	// }

}
