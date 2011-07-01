package org.conversionsvg.views;

import javax.swing.JPanel;

import org.conversionsvg.models.DomainModel;

import com.jidesoft.app.framework.gui.DataViewPane;
import com.jidesoft.app.framework.gui.GUIApplication;

/**
 * A domain view is a view in a <em>Model View Controller (MVC)</em> framework.
 * This is different than the <em>Jide Application Desktop Framework (JDAF)</em>
 * {@link DataViewPane} since creating sub views within views is not easily
 * supported by the library.
 * <p>
 * Essentially a <code>DomainView</code> is a <code>JPanel</code> which presents
 * components to the user.
 * 
 * @author Erich Schroeter
 */
@SuppressWarnings("serial")
public abstract class DomainView extends JPanel {

	/** The application this view belongs to. */
	protected GUIApplication application;

	/**
	 * Constructs a <code>DomainView</code> specifying the application the view
	 * belongs to.
	 * 
	 * @param application
	 *            the application
	 */
	public DomainView(GUIApplication application) {
		setApplication(application);
	}

	/**
	 * Returns the application this view belongs to.
	 * 
	 * @return the application
	 */
	public GUIApplication getApplication() {
		return application;
	}

	/**
	 * Sets the application this view belongs to.
	 * 
	 * @param application
	 *            the application to set
	 */
	public void setApplication(GUIApplication application) {
		this.application = application;
	}

	/**
	 * Initializes the components for this view.
	 */
	protected abstract void init();
	
	public abstract void updateModel(DomainModel model);
}
