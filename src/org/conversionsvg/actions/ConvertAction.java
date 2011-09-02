package org.conversionsvg.actions;

import java.awt.event.ActionEvent;

import org.conversionsvg.ConversionSvgApplication;
import org.conversionsvg.activities.ConversionActivity;
import org.conversionsvg.models.OptionsModel;
import org.conversionsvg.views.OptionsView;

import com.jidesoft.app.framework.DataModelException;
import com.jidesoft.app.framework.gui.GUIApplicationAction;

/**
 * The <code>ConvertAction</code> handles running the {@link ConversionActivity}
 * .
 * 
 * @author Erich Schroeter
 */
@SuppressWarnings("serial")
public class ConvertAction extends GUIApplicationAction {

	@Override
	public void actionPerformedDetached(ActionEvent e) {
		OptionsModel model = (OptionsModel) getApplication().getDataModelFor(
				((ConversionSvgApplication) getApplication()).getCommand());
		OptionsView view = (OptionsView) getApplication().getDataView(model);
		try {
			view.updateModel(model);
		} catch (DataModelException e1) {
			e1.printStackTrace();
		}
		getApplication().getActivityManager().run(new ConversionActivity());
	}
}
