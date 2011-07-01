package org.conversionsvg.actions;

import java.awt.event.ActionEvent;

import org.conversionsvg.activities.ConversionActivity;
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
		getApplication().getActivityManager().run(new ConversionActivity());
	}
}
