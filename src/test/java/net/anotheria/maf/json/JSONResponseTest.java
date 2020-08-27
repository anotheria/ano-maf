package net.anotheria.maf.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

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
					Assert.fail("Name: " + name + " is missed in empty JSONResponse");

			}

			Assert.assertEquals("status in empty JSONResponse should be \"OK\"", "OK", jsonObject.getString("status"));
			Assert.assertEquals("commands in empty JSONResponse should be empty", 0, jsonObject.getJSONObject("commands").length());
			Assert.assertEquals("data in empty JSONResponse should be empty", 0, jsonObject.getJSONObject("data").length());

		} catch (JSONException e) {
			Assert.fail("Unexpected exception");
		}

	}

	@Test
	public void testExceptions() {
		JSONResponse response = new JSONResponse();

		//Test Errors
		try {
			response.addError("");
			Assert.fail("Exception should be thrown");
		} catch (Exception e) {
			Assert.assertTrue("IllegalArgumentException should be thrown", e instanceof IllegalArgumentException);
		}

		try {
			response.addError("error", "");
			Assert.fail("Exception should be thrown");
		} catch (Exception e) {
			Assert.assertTrue("IllegalArgumentException should be thrown", e instanceof IllegalArgumentException);
		}

		try {
			response.addError("", "error");
			Assert.fail("Exception should be thrown");
		} catch (Exception e) {
			Assert.assertTrue("IllegalArgumentException should be thrown", e instanceof IllegalArgumentException);
		}

		//Test commands
		try {
			response.addCommand("", "command");
			Assert.fail("Exception should be thrown");
		} catch (Exception e) {
			Assert.assertTrue("IllegalArgumentException should be thrown", e instanceof IllegalArgumentException);
		}
		try {
			response.addCommand("command", "");
			Assert.fail("Exception should be thrown");
		} catch (Exception e) {
			Assert.assertTrue("IllegalArgumentException should be thrown", e instanceof IllegalArgumentException);
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
					Assert.fail("Name: " + name + " is missed in empty JSONResponse");
			}


			//Test status section
			Assert.assertEquals("Errors section should contain \"ERROR\" if some errors where added", "ERROR", jsonObject.getString(names.get(0)));


			//Test error section
			jsonObject = response.toJSON().getJSONObject(names.get(3));
			Assert.assertEquals("Quantity of names in \"" + names.get(3) + "\" sections wrong", uniqueFieldsNamesQuantity + globalErrorSectionsQuantity, jsonObject.names().length());


			// Test commands section
			jsonObject = response.toJSON().getJSONObject(names.get(1));
			Assert.assertEquals("Quantity of names in \"" + names.get(1) + "\" sections wrong", commands.size(), jsonObject.names().length());


			//Test data section
			jsonObject = response.toJSON().getJSONObject(names.get(2));
			Assert.assertEquals("Quantity of names in \"" + names.get(1) + "\" sections wrong", data.size(), jsonObject.names().length());

			jsonArray = jsonObject.getJSONArray(data.get(0));
			Assert.assertEquals("Quantity of elements of array: \""+data.get(0)+"\" in \"" + names.get(1) + "\" sections wrong", 2, jsonArray.length());


		} catch (JSONException e) {
			Assert.fail("unexpected exception");
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
