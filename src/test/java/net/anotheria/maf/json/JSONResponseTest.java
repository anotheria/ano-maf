package net.anotheria.maf.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test for JSONResponse class.
 *
 * @author dsilenko
 */
public class JSONResponseTest {

	@Test
	public void testEmptyResponse() {
		JSONResponse jsonResponse = new JSONResponse();
		JSONObject jsonObject = jsonResponse.toJSON();

		JSONArray jsonArray = jsonObject.names();
		List<String> names = Arrays.asList(new String[]{"status", "commands", "data"});

		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				String name = jsonArray.get(i).toString();
				if (!names.contains(name))
					Assertions.fail("Name: " + name + " is missed in empty JSONResponse");

			}

			Assertions.assertEquals("OK", jsonObject.getString("status"), "status in empty JSONResponse should be \"OK\"");
			Assertions.assertEquals(0, jsonObject.getJSONObject("commands").length(), "commands in empty JSONResponse should be empty");
			Assertions.assertEquals(0, jsonObject.getJSONObject("data").length(), "data in empty JSONResponse should be empty");

		} catch (JSONException e) {
			Assertions.fail("Unexpected exception");
		}

	}

	@Test
	public void testExceptions() {
		JSONResponse response = new JSONResponse();

		//Test Errors
		try {
			response.addError("");
			Assertions.fail("Exception should be thrown");
		} catch (Exception e) {
			Assertions.assertTrue(e instanceof IllegalArgumentException, "IllegalArgumentException should be thrown");
		}

		try {
			response.addError("error", "");
			Assertions.fail("Exception should be thrown");
		} catch (Exception e) {
			Assertions.assertTrue(e instanceof IllegalArgumentException, "IllegalArgumentException should be thrown");
		}

		try {
			response.addError("", "error");
			Assertions.fail("Exception should be thrown");
		} catch (Exception e) {
			Assertions.assertTrue(e instanceof IllegalArgumentException, "IllegalArgumentException should be thrown");
		}

		//Test commands
		try {
			response.addCommand("", "command");
			Assertions.fail("Exception should be thrown");
		} catch (Exception e) {
			Assertions.assertTrue(e instanceof IllegalArgumentException, "IllegalArgumentException should be thrown");
		}
		try {
			response.addCommand("command", "");
			Assertions.fail("Exception should be thrown");
		} catch (Exception e) {
			Assertions.assertTrue(e instanceof IllegalArgumentException, "IllegalArgumentException should be thrown");
		}


	}

	@Test
	public void testFilledResponse() {
		try {
			// Root response
			JSONResponse response = new JSONResponse();

			List<String> globalErrors = Arrays.asList(new String[]{"globalError1", "globalError2"});
			List<String> fieldsErrors = Arrays.asList(new String[]{"field1", "field2", "field2", "field3"});
			int uniqueFieldsNamesQuantity = getQuantityOfUniqueFields(fieldsErrors);
			int globalErrorSectionsQuantity = getQuantityOfGlobalErrorsSections(globalErrors);

			// Field errors
			for (String globalError : globalErrors)
				response.addError(globalError);

			int index = 0;
			for (String fieldsError : fieldsErrors)
				response.addError(fieldsError, "error" + index++);


			// Adding command
			List<String> commands = Arrays.asList(new String[]{"redirect", "refresh"});
			index = 0;
			for (String command : commands)
				response.addCommand(command, command + index++);


			// Adding data
			List<String> data = Arrays.asList(new String[]{"array", "data"});
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();

			Map<String, String> map = new HashMap<String, String>();
			map.put("key1", "value1");
			map.put("key2", "value2");
			map.put("key3", "value3");
			jsonArray.put(map);

			map = new HashMap<String, String>();
			map.put("key1", "value1");
			map.put("key2", "value2");
			map.put("key3", "value3");
			jsonArray.put(map);

			jsonObject.put(data.get(0), jsonArray);
			jsonObject.put(data.get(1), "test");
			response.setData(jsonObject);

			jsonObject = response.toJSON();


			//Test names existence
			jsonArray = jsonObject.names();
			List<String> names = Arrays.asList(new String[]{"status", "commands", "data", "errors"});
			for (int i = 0; i < jsonArray.length(); i++) {
				String name = jsonArray.get(i).toString();
				if (!names.contains(name))
					Assertions.fail("Name: " + name + " is missed in empty JSONResponse");
			}


			//Test status section
			Assertions.assertEquals("ERROR", jsonObject.getString(names.get(0)), "Errors section should contain \"ERROR\" if some errors where added");


			//Test error section
			jsonObject = response.toJSON().getJSONObject(names.get(3));
			Assertions.assertEquals(uniqueFieldsNamesQuantity + globalErrorSectionsQuantity, jsonObject.names().length(), "Quantity of names in \"" + names.get(3) + "\" sections wrong");


			// Test commands section
			jsonObject = response.toJSON().getJSONObject(names.get(1));
			Assertions.assertEquals(commands.size(), jsonObject.names().length(), "Quantity of names in \"" + names.get(1) + "\" sections wrong");


			//Test data section
			jsonObject = response.toJSON().getJSONObject(names.get(2));
			Assertions.assertEquals(data.size(), jsonObject.names().length(), "Quantity of names in \"" + names.get(1) + "\" sections wrong");

			jsonArray = jsonObject.getJSONArray(data.get(0));
			Assertions.assertEquals(2, jsonArray.length(), "Quantity of elements of array: \""+data.get(0)+"\" in \"" + names.get(1) + "\" sections wrong");


		} catch (JSONException e) {
			Assertions.fail("unexpected exception");
		}
	}


	/**
	 * Returns quantity of unique fields names from given list.
	 *
	 * @param fields list with fields names
	 * @return quantity of unique fields names
	 */
	private int getQuantityOfUniqueFields(List<String> fields) {
		List<String> result = new ArrayList<String>(fields.size());
		for (String field : fields)
			if (!result.contains(field))
				result.add(field);

		return result.size();
	}

	/**
	 * Returns quantity of global errors sections.
	 *
	 * @param globalErrors list with global errors
	 * @return quantity of global errors sections
	 */
	private int getQuantityOfGlobalErrorsSections(List<String> globalErrors) {
		if (globalErrors == null || globalErrors.isEmpty())
			return 0;

		return 1;
	}

}
