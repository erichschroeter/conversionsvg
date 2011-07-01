package org.conversionsvg.models;

import java.util.HashMap;
import java.util.Map;

import org.conversionsvg.InkscapeCommandBuilder;
import org.conversionsvg.ModifiesInkscapeCommand;
import org.conversionsvg.views.DomainView;

import com.jidesoft.app.framework.BasicDataModel;
import com.jidesoft.app.framework.DataModelException;

public class OptionsModel extends BasicDataModel implements
		ModifiesInkscapeCommand {

	/**
	 * A key to be used to map a <em>sub view</em> or <em>sub model</em>. Value
	 * is {@value #SUB_KEY_AREA}.
	 */
	public static final String SUB_KEY_AREA = "sub_view.area";
	/**
	 * A key to be used to map a <em>sub view</em> or <em>sub model</em>. Value
	 * is {@value #SUB_KEY_COLOR}.
	 */
	public static final String SUB_KEY_COLOR = "sub_view.color";
	/**
	 * A key to be used to map a <em>sub view</em> or <em>sub model</em>. Value
	 * is {@value #SUB_KEY_FILES}.
	 */
	public static final String SUB_KEY_FILES = "sub_view.file_selection";
	/**
	 * A key to be used to map a <em>sub view</em> or <em>sub model</em>. Value
	 * is {@value #SUB_KEY_FORMAT}.
	 */
	public static final String SUB_KEY_FORMAT = "sub_view.format";
	/**
	 * A key to be used to map a <em>sub view</em> or <em>sub model</em>. Value
	 * is {@value #SUB_KEY_SIZE}.
	 */
	public static final String SUB_KEY_SIZE = "sub_view.size";

	/** A globally unique identifier for the options model */
	public static final String GLOBAL_NAME = "models.options";
	/**
	 * A map between the available options supported by Inkscape and the
	 * specified value by the user
	 */
	// private Map<Inkscape.Option, Object> options;
	/**
	 * The command builder used to build the command to send to Inkscape on the
	 * command line
	 */
	private InkscapeCommandBuilder command;
	private Map<String, DomainModel> submodels;

	/**
	 * Constructs a default <code>OptionsModel</code> initializing the model
	 * name to <code>options</code>.
	 */
	public OptionsModel(InkscapeCommandBuilder commandBuilder) {
		super("options");
		setCommand(commandBuilder);
		submodels = new HashMap<String, DomainModel>();
	}

	/**
	 * Sets the Inkscape command builder responsible for building the command
	 * sent on the command line to Inkscape.
	 * 
	 * @param command
	 *            the command to set
	 */
	protected void setCommand(InkscapeCommandBuilder command) {
		this.command = command;
	}

	/**
	 * Registers the <code>model</code> with this <em>JDAF</em> model.
	 * Registering <em>sub models</em> are necessary in order to update the
	 * options represented by the <em>sub models</em> with this model.
	 * <p>
	 * The <code>key</code> follows a convention of using a
	 * <code>public static final String</code> value from {@link OptionsModel}.
	 * 
	 * @param key
	 *            the sub model key
	 * @param model
	 *            the sub model
	 * @return <code>model</code> to allow for additional configuration
	 */
	protected DomainModel registerSubModel(String key, DomainModel model) {
		submodels.put(key, model);
		return model;
	}

	/**
	 * Returns the registered <em>sub models</em>.
	 * 
	 * @return the registered sub models container
	 */
	public Map<String, DomainModel> getSubModels() {
		return submodels;
	}

	//
	// ModifiesInkscapeCommand members
	//

	@Override
	public InkscapeCommandBuilder getCommand() {
		return command;
	}

	//
	// BasicDataModel members
	//

	@Override
	public void newData() {
		// ((InkscapeCommandBuilder)getData()).
		registerSubModel(SUB_KEY_AREA, new AreaModel(getCommand()));
	}

	@Override
	public void openData() throws DataModelException {
		InkscapeCommandBuilder toLoad = (InkscapeCommandBuilder) getCriteria();
		command = toLoad;
	}

	@Override
	public void resetData() throws DataModelException {
		setData(null);
		newData();
	}

	@Override
	public void closeData() {
		super.closeData();
		// options.clear();
	}

	@Override
	public void saveData() throws DataModelException {
		// TODO save options
	}

}
