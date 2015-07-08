package net.anotheria.maf.json;

import net.anotheria.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * JSON error response.
 * 
 * @author Alexandr Bolbat
 */
public class JSONResponse implements JSONErrorScope {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 5890081951501938183L;

	/**
	 * Status json field name.
	 */
	public static final String STATUS_JSON_FIELD = "status";

	/**
	 * Data root scope.
	 */
	public static final String ROOT_DATA_SCOPE = "data";

	/**
	 * Commands root scope.
	 */
	public static final String ROOT_COMMANDS_SCOPE = "commands";

	/**
	 * Raw data root scope.
	 */
	public static final String ROOT_RAW_DATA_SCOPE = "rawData";

	/**
	 * Errors root scope.
	 */
	public static final String ROOT_ERRORS_SCOPE = "errors";

	/**
	 * Errors scope global errors.
	 */
	public static final String ERRORS_SCOPE_GLOBAL_ERRORS = "_global";

	/**
	 * Inner scope name.
	 */
	private final String innerScopeName;

	/**
	 * Response data.
	 */
	private JSONObject data;

	/**
	 * Raw data.
	 */
	private final Map<String, String> rawData = new HashMap<String, String>();

	/**
	 * Commands.
	 */
	private final Map<String, String> commands = new HashMap<String, String>();

	/**
	 * Global response errors.
	 */
	private final Set<String> globalErrors = new HashSet<String>();

	/**
	 * Field response errors.
	 */
	private final Map<String, Set<String>> fieldErrors = new HashMap<String, Set<String>>();

	/**
	 * Sub-object related errors.
	 */
	private final Map<String, JSONResponse> innerScopes = new HashMap<String, JSONResponse>();

	/**
	 * Default constructor.
	 */
	public JSONResponse() {
		this("ROOT");
	}

	/**
	 * Internal constructor.
	 * 
	 * @param aScopeName
	 *            - response scope name
	 */
	private JSONResponse(final String aScopeName) {
		this.innerScopeName = aScopeName;
	}

	/**
	 * Set response data.
	 * 
	 * @param aData
	 *            - response data
	 */
	public void setData(final JSONObject aData) {
		if (aData == null)
			throw new IllegalArgumentException("Null data.");

		this.data = aData;
	}

	/**
	 * Add some raw data to response.
	 * 
	 * @param key
	 *            - key
	 * @param value
	 *            - value
	 */
	public void addRawData(final String key, final String value) {
		if (StringUtils.isEmpty(key)) {
			throw new IllegalArgumentException("Key is empty or null.");
		}
		if (StringUtils.isEmpty(value)) {
			throw new IllegalArgumentException("Value is empty or null.");
		}

		rawData.put(key, value);
	}

	/**
	 * Get response raw data
	 * 
	 * @return {@link Map} with raw data
	 */
	public Map<String, String> getRawData() {
		return new HashMap<String, String>(rawData);
	}

	/**
	 * Add command to response.
	 * 
	 * @param name
	 *            - command name
	 * @param param
	 *            - command parameter
	 */
	public void addCommand(final String name, final String param) {
		if (StringUtils.isEmpty(name))
			throw new IllegalArgumentException("Name is empty or null.");

		if (StringUtils.isEmpty(param))
			throw new IllegalArgumentException("Param is empty or null.");

		commands.put(name, param);
	}

	@Override
	public void addError(final String error) {
		if (StringUtils.isEmpty(error))
			throw new IllegalArgumentException("Error is empty or null.");

		globalErrors.add(error);
	}

	@Override
	public void addError(final String fieldName, final String error) {
		if (StringUtils.isEmpty(fieldName))
			throw new IllegalArgumentException("FieldName is empty or null.");

		if (StringUtils.isEmpty(error))
			throw new IllegalArgumentException("Error is empty or null.");

		Set<String> fieldErrorsSet = fieldErrors.get(fieldName);
		if (fieldErrorsSet == null) {
			fieldErrorsSet = new HashSet<String>();
			fieldErrors.put(fieldName, fieldErrorsSet);
		}

		fieldErrorsSet.add(error);
	}

	@Override
	public JSONErrorScope getErrorObject(final String objectName) {
		if (StringUtils.isEmpty(objectName))
			throw new IllegalArgumentException("ObjectName is empty or null.");

		JSONResponse innerScope = innerScopes.get(objectName);
		if (innerScope == null) {
			innerScope = new JSONResponse(objectName);
			innerScopes.put(objectName, innerScope);
		}

		return innerScope;
	}

	/**
	 * Is response have errors.
	 * 
	 * @return <code>true</code> if have or <code>false</code>
	 */
	public boolean hasErrors() {
		if (!globalErrors.isEmpty() || !fieldErrors.isEmpty())
			return true;

		for (JSONResponse innerResponse : innerScopes.values())
			if (innerResponse.hasErrors())
				return true;

		return false;
	}

	/**
	 * Convert response to {@link JSONObject}.
	 * 
	 * @return {@link JSONObject}
	 */
	public JSONObject toJSON() {
		try {
			JSONObject result = new JSONObject();

			// prepare status
			result.put(STATUS_JSON_FIELD, (hasErrors() ? "ERROR" : "OK"));

			// prepare data scope
			result.put(ROOT_DATA_SCOPE, getDataJSON());

			// prepare commands scope
			result.put(ROOT_COMMANDS_SCOPE, getCommandsJSON());

			// prepare raw data
			if (!rawData.isEmpty()) {
				result.put(ROOT_RAW_DATA_SCOPE, getRawDataJSON());
			}

			// prepare errors scope
			if (hasErrors()) // showing errors section only if response have errors
				result.put(ROOT_ERRORS_SCOPE, getErrorsJSON());

			return result;
		} catch (JSONException e) {
			throw new RuntimeException("Preparing JSON Response fail.", e);
		}
	}

	/**
	 * Internal method for preparing data json.
	 * 
	 * @return {@link JSONObject}
	 * @throws JSONException
	 */
	private JSONObject getDataJSON() throws JSONException {
		if (data == null)
			return new JSONObject();

		return data;
	}

	/**
	 * Internal method for preparing raw data json.
	 *
	 * @return {@link JSONObject}
	 * @throws JSONException
	 */
	private JSONObject getRawDataJSON() throws JSONException {
		JSONObject rawDataJsonObject = new JSONObject();

		for (String dataName : rawData.keySet())
			rawDataJsonObject.put(dataName, rawData.get(dataName));

		return rawDataJsonObject;
	}

	/**
	 * Internal method for preparing commands json.
	 * 
	 * @return {@link JSONObject}
	 * @throws JSONException
	 */
	private JSONObject getCommandsJSON() throws JSONException {
		JSONObject commandsScope = new JSONObject();

		// prepare field-scope errors
		for (String commandName : commands.keySet())
			commandsScope.put(commandName, commands.get(commandName));

		return commandsScope;
	}

	/**
	 * Internal method for preparing errors json.
	 * 
	 * @return {@link JSONObject}
	 * @throws JSONException
	 */
	private JSONObject getErrorsJSON() throws JSONException {
		JSONObject errorsScope = new JSONObject();

		// prepare global-scope errors
		if (!globalErrors.isEmpty())
			errorsScope.put(ERRORS_SCOPE_GLOBAL_ERRORS, new JSONArray(globalErrors));

		// prepare field-scope errors
		for (String fieldName : fieldErrors.keySet())
			errorsScope.put(fieldName, new JSONArray(fieldErrors.get(fieldName)));

		// prepare inner-scopes
		for (String myInnerScopeName : innerScopes.keySet())
			errorsScope.put(myInnerScopeName, innerScopes.get(myInnerScopeName).getErrorsJSON());

		return errorsScope;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((innerScopeName == null) ? 0 : innerScopeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSONResponse other = (JSONResponse) obj;
		if (innerScopeName == null) {
			if (other.innerScopeName != null)
				return false;
		} else if (!innerScopeName.equals(other.innerScopeName))
			return false;
		return true;
	}

}
