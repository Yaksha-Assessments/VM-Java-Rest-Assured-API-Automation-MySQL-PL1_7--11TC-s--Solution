package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtil {
	private static String BASE_URL;
	Properties prop;

	/**
	 * Retrieves the base URL from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and extracts the
	 * value associated with the key <code>base.url</code>. The value is stored in
	 * the static variable <code>BASE_URL</code> and returned.
	 *
	 * @return the base URL string if successfully read from the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getBaseUrl() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			BASE_URL = prop.getProperty("base.url");
			return BASE_URL;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the username from the configuration properties file.
	 *
	 * <p>
	 * This method reads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>username</code>.
	 *
	 * @return the username as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getUsername() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("username");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPassword() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Test1 - This method sends a GET request to fetch a list of employment status
	 *        entries from the system.
	 * 
	 * @description This method constructs and executes a GET API call to the
	 *              provided endpoint. It uses a session cookie ("orangehrm") for
	 *              authentication and optionally includes a request body (though
	 *              GET typically does not require one). The method extracts the
	 *              HTTP status, response content, and specifically parses the "id"
	 *              and "name" fields from the "data" array in the response. These
	 *              values are returned encapsulated in a CustomResponse object.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - Optional request body as a Map (can be null since GET
	 *                    does not typically include a body).
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of extracted IDs and
	 *         names from the response "data" array.
	 */

	public CustomResponse GetEmpStatus(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> holiday : data) {
				ids.add(holiday.get("id"));
				names.add(holiday.get("name"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names);
	}

	/**
	 * @Test2 - This method sends a GET request to retrieve job title information
	 *        from the system.
	 * 
	 * @description This method constructs and executes a GET API call to the
	 *              specified endpoint. It uses a session cookie ("orangehrm") for
	 *              authentication and can optionally include a request body (though
	 *              not typical for GET). The response is parsed to extract the HTTP
	 *              status, and from the "data" array, it collects "id" and "title"
	 *              fields. These are returned encapsulated in a CustomResponse
	 *              object for further validation or use.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - Optional request body as a Map (can be null since GET
	 *                    does not typically include a body).
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of extracted job title
	 *         IDs and titles from the response "data" array.
	 */
	public CustomResponse GetJobTitle(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> titles = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> holiday : data) {
				ids.add(holiday.get("id"));
				titles.add(holiday.get("title"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, titles);
	}

	/**
	 * @Test3 - This method sends a GET request to fetch administrative subunit data
	 *        from the system.
	 * 
	 * @description This method constructs and executes a GET API call to the
	 *              specified endpoint. It uses a session cookie ("orangehrm") for
	 *              authentication and optionally includes a request body (though
	 *              GET typically does not require one). The method parses the
	 *              response to extract HTTP status details and subunit-related
	 *              fields such as "id", "title", "unitId", "description", "level",
	 *              "left", and "right" from the "data" array. These are returned in
	 *              a CustomResponse object for validation or further processing.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - Optional request body as a Map (can be null since GET
	 *                    does not typically include a body).
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of extracted subunit
	 *         attributes including IDs, titles, unit IDs, descriptions, levels,
	 *         left, and right values.
	 */
	public CustomResponse GetAdminSubunit(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> unitIds = new ArrayList<>();
		List<Object> descriptions = new ArrayList<>();
		List<Object> level = new ArrayList<>();
		List<Object> left = new ArrayList<>();
		List<Object> right = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> holiday : data) {
				ids.add(holiday.get("id"));
				names.add(holiday.get("title"));
				unitIds.add(holiday.get("unitId"));
				descriptions.add(holiday.get("description"));
				level.add(holiday.get("level"));
				left.add(holiday.get("left"));
				right.add(holiday.get("right"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, unitIds, descriptions, level, left, right);
	}

	/**
	 * @Test4 - This method sends a GET request to retrieve employee data from the
	 *        PIM (Personal Information Management) module.
	 * 
	 * @description This method constructs and executes a GET API call to the
	 *              specified endpoint using a session cookie ("orangehrm") for
	 *              authentication. It can optionally include a request body,
	 *              although GET requests typically do not require one. The response
	 *              is parsed to extract the HTTP status and employee-related fields
	 *              from the "data" array, including "empNumber", "lastName",
	 *              "firstName", and "employeeId". These extracted values are
	 *              returned in a CustomResponse object for further validation or
	 *              usage.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - Optional request body as a Map (can be null since GET
	 *                    does not typically include a body).
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of employee numbers,
	 *         last names, first names, and employee IDs extracted from the "data"
	 *         array.
	 */
	public CustomResponse GetPimEmp(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> employeeNumbers = new ArrayList<>();
		List<Object> lastNames = new ArrayList<>();
		List<Object> firstNames = new ArrayList<>();
		List<Object> employeeId = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> holiday : data) {
				employeeNumbers.add(holiday.get("empNumber"));
				lastNames.add(holiday.get("lastName"));
				firstNames.add(holiday.get("firstName"));
				employeeId.add(holiday.get("employeeId"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, employeeNumbers, lastNames, firstNames, employeeId);
	}

	/**
	 * @Test5 - This method sends a GET request to retrieve report data in ascending
	 *        order from the system.
	 * 
	 * @description This method constructs and executes a GET API call to the
	 *              specified endpoint using a session cookie ("orangehrm") for
	 *              authentication. It optionally includes a request body, although
	 *              this is uncommon for GET requests. The response is parsed to
	 *              extract the HTTP status and report-related fields from the
	 *              "data" array, specifically the "id" and "name" of each item.
	 *              These values are returned in a CustomResponse object for
	 *              validation or further use.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - Optional request body as a Map (can be null since GET
	 *                    does not typically include a body).
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of IDs and names
	 *         extracted from the response "data" array.
	 */
	public CustomResponse GetReportASC(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> holiday : data) {
				ids.add(holiday.get("id"));
				names.add(holiday.get("name"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names);
	}

	/**
	 * @Test6 - This method sends a GET request to retrieve leave eligibility
	 *        information from the system.
	 * 
	 * @description This method constructs and executes a GET API call to the
	 *              specified endpoint using a session cookie ("orangehrm") for
	 *              authentication. While it allows for an optional request body, it
	 *              is typically not required for GET requests. The response is
	 *              parsed to extract the HTTP status and leave eligibility
	 *              attributes from the "data" array, including "id", "name",
	 *              "deleted", and "situational". These values are returned
	 *              encapsulated in a CustomResponse object for validation or
	 *              further processing.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - Optional request body as a Map (can be null since GET
	 *                    does not typically include a body).
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of leave eligibility
	 *         IDs, names, deletion flags, and situational flags extracted from the
	 *         "data" array.
	 */
	public CustomResponse GetLeaveEligibility(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> isDeleted = new ArrayList<>();
		List<Object> isSituational = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> holiday : data) {
				ids.add(holiday.get("id"));
				names.add(holiday.get("name"));
				isDeleted.add(holiday.get("deleted"));
				isSituational.add(holiday.get("situational"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, isDeleted, isSituational);
	}

	/**
	 * @Test7 - This method sends a PUT request to update administrative LDAP
	 *        configuration settings in the system.
	 * 
	 * @description This method constructs and executes a PUT API call to the
	 *              specified endpoint using a session cookie ("orangehrm") for
	 *              authentication. It accepts a request body as a JSON string
	 *              containing configuration data. The response is parsed to extract
	 *              the HTTP status and various configuration attributes from the
	 *              "data" object, including flags and parameters such as "enable",
	 *              "hostname", "port", "encryption", "ldapImplementation",
	 *              "bindAnonymously", "bindUserDN", "hasBindUserPassword",
	 *              "userLookupSettings", "dataMapping",
	 *              "mergeLDAPUsersWithExistingSystemUsers", and "syncInterval".
	 *              These are collected and returned in a CustomResponse object.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - The request body as a JSON-formatted String containing
	 *                    configuration details.
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists containing extracted
	 *         LDAP configuration parameters from the response "data" object.
	 */
	public CustomResponse PutAdminConfig(String endpoint, String cookieValue, String body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.put(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> enable = new ArrayList<>();
		List<Object> hostname = new ArrayList<>();
		List<Object> port = new ArrayList<>();
		List<Object> encryption = new ArrayList<>();
		List<Object> ldapImplementation = new ArrayList<>();
		List<Object> bindAnonymously = new ArrayList<>();
		List<Object> bindUserDN = new ArrayList<>();
		List<Object> hasBindUserPassword = new ArrayList<>();
		List<Object> userLookupSettings = new ArrayList<>();
		List<Object> dataMapping = new ArrayList<>();
		List<Object> mergeLDAPUsersWithExistingSystemUsers = new ArrayList<>();
		List<Object> syncInterval = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		if (data != null) {
			enable.add(data.get("enable"));
			hostname.add(data.get("hostname"));
			port.add(data.get("port"));
			encryption.add(data.get("encryption"));
			ldapImplementation.add(data.get("ldapImplementation"));
			bindAnonymously.add(data.get("bindAnonymously"));
			bindUserDN.add(data.get("bindUserDN"));
			hasBindUserPassword.add(data.get("hasBindUserPassword"));
			userLookupSettings.add(data.get("userLookupSettings"));
			dataMapping.add(data.get("dataMapping"));
			mergeLDAPUsersWithExistingSystemUsers.add(data.get("mergeLDAPUsersWithExistingSystemUsers"));
			syncInterval.add(data.get("syncInterval"));
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, enable, hostname, port, encryption, ldapImplementation,
				bindAnonymously, bindUserDN, hasBindUserPassword, userLookupSettings, dataMapping,
				mergeLDAPUsersWithExistingSystemUsers, syncInterval);
	}

	/**
	 * @Test8 - This method sends a PUT request to update optional field
	 *        configuration settings in the system.
	 * 
	 * @description This method constructs and executes a PUT API call to the
	 *              specified endpoint using a session cookie ("orangehrm") for
	 *              authentication. It accepts a request body as an object
	 *              containing optional field configurations. The response is parsed
	 *              to extract the HTTP status and values from the "data" object,
	 *              specifically "pimShowDeprecatedFields", "showSIN", "showSSN",
	 *              and "showTaxExemptions". These values are returned encapsulated
	 *              in a CustomResponse object for further validation or usage.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - The request body as an Object containing the optional
	 *                    field configuration details.
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of extracted
	 *         configuration flags from the response "data" object.
	 */
	public CustomResponse PutOptionalField(String endpoint, String cookieValue, Object body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.put(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> pimShowDeprecatedFields = new ArrayList<>();
		List<Object> showSIN = new ArrayList<>();
		List<Object> showSSN = new ArrayList<>();
		List<Object> showTaxExemptions = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		if (data != null) {
			pimShowDeprecatedFields.add(data.get("pimShowDeprecatedFields"));
			showSIN.add(data.get("showSIN"));
			showSSN.add(data.get("showSSN"));
			showTaxExemptions.add(data.get("showTaxExemptions"));
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, pimShowDeprecatedFields, showSIN, showSSN,
				showTaxExemptions);
	}

	/**
	 * @Test9 - This method sends a POST request to create a new custom field
	 *        configuration in the system.
	 * 
	 * @description This method constructs and executes a POST API call to the
	 *              specified endpoint using a session cookie ("orangehrm") for
	 *              authentication. It accepts a request body as a JSON-formatted
	 *              string containing details of the custom field to be created. The
	 *              response is parsed to extract the HTTP status and attributes of
	 *              the newly created custom field from the "data" object, including
	 *              "id", "fieldName", "fieldType", "extraData", and "screen". These
	 *              values are returned in a CustomResponse object for validation or
	 *              further use.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - The request body as a JSON-formatted String containing
	 *                    new custom field data.
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of created custom field
	 *         attributes extracted from the response "data" object.
	 */
	public CustomResponse PostCustomField(String endpoint, String cookieValue, String body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.post(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> id = new ArrayList<>();
		List<Object> fieldName = new ArrayList<>();
		List<Object> fieldType = new ArrayList<>();
		List<Object> extraData = new ArrayList<>();
		List<Object> screen = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		if (data != null) {
			id.add(data.get("id"));
			fieldName.add(data.get("fieldName"));
			fieldType.add(data.get("fieldType"));
			extraData.add(data.get("extraData"));
			screen.add(data.get("screen"));
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, id, fieldName, fieldType, extraData, screen);
	}

	/**
	 * @Test10 - This method sends a PUT request to update a custom field
	 *         configuration in the system.
	 * 
	 * @description This method constructs and executes a PUT API call to the
	 *              specified endpoint using a session cookie ("orangehrm") for
	 *              authentication. It accepts a request body as a JSON-formatted
	 *              string containing custom field details. The response is parsed
	 *              to extract the HTTP status and relevant custom field attributes
	 *              from the "data" object, including "id", "fieldName",
	 *              "fieldType", "extraData", and "screen". These values are
	 *              collected and returned in a CustomResponse object for validation
	 *              or further processing.
	 *
	 * @param endpoint    - The specific API endpoint to hit (relative to the base
	 *                    URL).
	 * @param cookieValue - The session cookie value to authenticate the request.
	 * @param body        - The request body as a JSON-formatted String containing
	 *                    custom field configuration.
	 *
	 * @return CustomResponse - A custom response object containing the full HTTP
	 *         response, status code, status line, and lists of extracted custom
	 *         field attributes from the response "data" object.
	 */
	public CustomResponse PutCustomField(String endpoint, String cookieValue, String body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.put(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> id = new ArrayList<>();
		List<Object> fieldName = new ArrayList<>();
		List<Object> fieldType = new ArrayList<>();
		List<Object> extraData = new ArrayList<>();
		List<Object> screen = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		if (data != null) {
			id.add(data.get("id"));
			fieldName.add(data.get("fieldName"));
			fieldType.add(data.get("fieldType"));
			extraData.add(data.get("extraData"));
			screen.add(data.get("screen"));
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, id, fieldName, fieldType, extraData, screen);
	}
}
