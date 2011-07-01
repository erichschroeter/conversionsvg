package org.conversionsvg.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import com.jidesoft.app.framework.gui.DataViewPane;

@SuppressWarnings("serial")
public class ConversionView extends DataViewPane {

	@Override
	protected void initializeComponents() {
		setLayout(new GridBagLayout());

		// initialize sub views
		FormatView formatView = new FormatView();
		formatView.initializeComponents();
		AreaView areaView = new AreaView();
		areaView.initializeComponents();
		SizeView sizeView = new SizeView();
		sizeView.initializeComponents();
		ColorView colorView = new ColorView();
		colorView.initializeComponents();

		GridBagConstraints c;
		c = new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0);
		add(formatView, c);
		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		add(areaView, c);
		c = new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		add(sizeView, c);
		c = new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0);
		add(colorView, c);
	}

}
