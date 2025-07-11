package testcases;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import rest.ApiUtil;
import rest.CustomResponse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import coreUtilities.utils.FileOperations;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class RestAssured_TestCases {

	private static String baseUrl;
	private static String username;
	private static String password;
	private static String cookieValue = null;
	private ApiUtil apiUtil;
	private String apiUtilPath = System.getProperty("user.dir") + "\\src\\main\\java\\rest\\ApiUtil.java";
	private String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\TestData.xlsx";

	/**
	 * @BeforeClass method to perform login via Selenium and retrieve session cookie
	 *              for authenticated API calls.
	 * 
	 *              Steps: 1. Setup ChromeDriver using WebDriverManager. 2. Launch
	 *              browser and open the OrangeHRM login page. 3. Perform login with
	 *              provided username and password. 4. Wait for login to complete
	 *              and extract the 'orangehrm' session cookie. 5. Store the cookie
	 *              value to be used in API requests. 6. Quit the browser session.
	 * 
	 *              Throws: - InterruptedException if thread sleep is interrupted. -
	 *              RuntimeException if the required session cookie is not found.
	 */

	@Test(priority = 0, groups = { "PL1" }, description = "Login to the application using Selenium and retrieve session cookie")
	public void loginWithSeleniumAndGetCookie() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		apiUtil = new ApiUtil();
		baseUrl = apiUtil.getBaseUrl();
		username = apiUtil.getUsername();
		password = apiUtil.getPassword();

		driver.get(baseUrl + "/web/index.php/auth/login");
		Thread.sleep(3000); // Wait for page load

		// Login to the app
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(6000); // Wait for login

		// Extract cookie named "orangehrm"
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("orangehrm")) {
				cookieValue = cookie.getValue();
				break;
			}
		}

		driver.quit();

		if (cookieValue == null) {
			throw new RuntimeException("orangehrm cookie not found after login");
		}
	}

	@Test(priority = 1, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/employment-statuses?limit=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Print and verify the response status code and response body\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetEmpStatus() throws IOException {

		// Defining Endpoint
		String endpoint = "/web/index.php/api/v2/admin/employment-statuses?limit=0";

		// Defining upcoming responce from Rest Method
		CustomResponse customResponse = apiUtil.GetEmpStatus(endpoint, cookieValue, null);

		// Checking for implementation which we are using is correct or not
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetEmpStatus",
				List.of("given", "cookie", "get", "response"));

		// Print response status code and body to console for debugging/visibility
		System.out.println("Status Code: " + customResponse.getStatusCode());

		System.out.println("-----------------------------------------------------------------------------------------");
		// Assert that the implementation and Status we need is Satisfied
		Assert.assertTrue(isImplementationCorrect,
				"GetEmpStatus must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.getIds());
		Assert.assertNotNull(customResponse.getNames());
	}

	@Test(priority = 2, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/job-titles?limit=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Print and verify the response status code and response body\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetJobTitle() throws IOException {

		// Defining Endpoint
		String endpoint = "/web/index.php/api/v2/admin/job-titles?limit=0";

		// Defining upcoming responce from Rest Method
		CustomResponse customResponse = apiUtil.GetJobTitle(endpoint, cookieValue, null);

		// Checking for implementation which we are using is correct or not
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetJobTitle",
				List.of("given", "cookie", "get", "response"));

		// Print response status code and body to console for debugging/visibility

		// Assert that the implementation and Status we need is Satisfied
		Assert.assertTrue(isImplementationCorrect,
				"GetJobTitle must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.getIds());
		Assert.assertNotNull(customResponse.getNames());
	}

	@Test(priority = 3, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/subunits' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Print and verify the response status code and response body\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetAdminSubunit() throws IOException {

		// Defining Endpoint
		String endpoint = "/web/index.php/api/v2/admin/subunits";

		// Defining upcoming responce from Rest Method
		CustomResponse customResponse = apiUtil.GetAdminSubunit(endpoint, cookieValue, null);

		// Checking for implementation which we are using is correct or not
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetAdminSubunit",
				List.of("given", "cookie", "get", "response"));

		// Assert that the implementation and Status we need is Satisfied
		Assert.assertTrue(isImplementationCorrect,
				"GetAdminSubunit must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.getIds());
		Assert.assertNotNull(customResponse.getNames());
		Assert.assertNotNull(customResponse.unitIds);
		Assert.assertNotNull(customResponse.descriptions);
		Assert.assertNotNull(customResponse.level);
		Assert.assertNotNull(customResponse.left);
		Assert.assertNotNull(customResponse.right);

	}

	@Test(priority = 4, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/pim/employees' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Print and verify the response status code and response body\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetPimEmp() throws IOException {

		// Defining Endpoint
		String endpoint = "/web/index.php/api/v2/pim/employees";

		// Defining upcoming responce from Rest Method
		CustomResponse customResponse = apiUtil.GetPimEmp(endpoint, cookieValue, null);

		// Checking for implementation which we are using is correct or not
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetPimEmp",
				List.of("given", "cookie", "get", "response"));

		// Assert that the implementation and Status we need is Satisfied
		Assert.assertTrue(isImplementationCorrect,
				"GetPimEmp must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.employeeNumbers);
		Assert.assertNotNull(customResponse.lastNames);
		Assert.assertNotNull(customResponse.firstNames);
		Assert.assertNotNull(customResponse.employeeIds);

	}

	@Test(priority = 5, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/pim/reports/defined?limit=50&offset=0&sortField=report.name&sortOrder=ASC' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Print and verify the response status code and response body\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetReportASC() throws IOException {

		// Defining Endpoint
		String endpoint = "/web/index.php/api/v2/pim/reports/defined?limit=50&offset=0&sortField=report.name&sortOrder=ASC";

		// Defining upcoming responce from Rest Method
		CustomResponse customResponse = apiUtil.GetReportASC(endpoint, cookieValue, null);

		// Checking for implementation which we are using is correct or not
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetReportASC",
				List.of("given", "cookie", "get", "response"));

		// Assert that the implementation and Status we need is Satisfied
		Assert.assertTrue(isImplementationCorrect,
				"GetReportASC must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.getIds());
		Assert.assertNotNull(customResponse.getNames());
	}

	@Test(priority = 6, groups = {
			"PL1" }, description = "1. Fetch user list using GET request to '/web/index.php/api/v2/leave/leave-types/eligible?includeAllocated=true' endpoint\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Print and verify the response status code and response body\n"
					+ "4. Assert that the response status code is 200 (OK)")
	public void GetLeaveEligibility() throws IOException {

		// Defining Endpoint
		String endpoint = "/web/index.php/api/v2/leave/leave-types/eligible?includeAllocated=true";

		// Defining upcoming responce from Rest Method
		CustomResponse customResponse = apiUtil.GetLeaveEligibility(endpoint, cookieValue, null);

		// Checking for implementation which we are using is correct or not
		boolean isImplementationCorrectt = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"GetLeaveEligibility", List.of("given", "cookie", "get", "response"));
		Assert.assertTrue(isImplementationCorrectt,
				"GetLeaveEligibility must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.employeeNumbers);
		Assert.assertNotNull(customResponse.lastNames);
		Assert.assertNotNull(customResponse.firstNames);
		Assert.assertNotNull(customResponse.employeeIds);
	}

	@Test(priority = 7, groups = {
			"PL1" }, description = "1. Send a PUT request to the '/web/index.php/api/v2/admin/ldap-config' endpoint with a valid cookie\n"
					+ "2. Print and verify the response status code and response body\n"
					+ "3. Assert that the response status code is 200 and validate implementation correctness")
	public void PutAdminConfig() throws Exception {

		String endpoint = "/web/index.php/api/v2/admin/ldap-config";

		Map<String, String> testData = FileOperations.readExcelPOI(excelPath, "PutAdminConfig");

		String requestBody = "{\n" + "  \"enable\": " + getJsonValue(testData.get("enable")) + ",\n"
				+ "  \"hostname\": " + getJsonValue(testData.get("hostname")) + ",\n" + "  \"port\": "
				+ getJsonValue(testData.get("port")) + ",\n" + "  \"encryption\": "
				+ getJsonValue(testData.get("encryption")) + ",\n" + "  \"ldapImplementation\": "
				+ getJsonValue(testData.get("ldapImplementation")) + ",\n" + "  \"bindAnonymously\": "
				+ getJsonValue(testData.get("bindAnonymously")) + ",\n" + "  \"bindUserDN\": "
				+ getJsonValue(testData.get("bindUserDN")) + ",\n" + "  \"bindUserPassword\": "
				+ getJsonValue(testData.get("bindUserPassword")) + ",\n" + "  \"userLookupSettings\": [\n" + "    {\n"
				+ "      \"baseDN\": " + getJsonValue(testData.get("baseDN")) + ",\n" + "      \"searchScope\": "
				+ getJsonValue(testData.get("searchScope")) + ",\n" + "      \"userNameAttribute\": "
				+ getJsonValue(testData.get("userNameAttribute")) + ",\n" + "      \"userSearchFilter\": "
				+ getJsonValue(testData.get("userSearchFilter")) + ",\n" + "      \"userUniqueIdAttribute\": "
				+ getJsonValue(testData.get("userUniqueIdAttribute")) + ",\n"
				+ "      \"employeeSelectorMapping\": []\n" + "    }\n" + "  ],\n" + "  \"dataMapping\": {\n"
				+ "    \"firstName\": " + getJsonValue(testData.get("firstName")) + ",\n" + "    \"middleName\": "
				+ getJsonValue(testData.get("middleName")) + ",\n" + "    \"lastName\": "
				+ getJsonValue(testData.get("lastName")) + ",\n" + "    \"workEmail\": "
				+ getJsonValue(testData.get("workEmail")) + ",\n" + "    \"employeeId\": "
				+ getJsonValue(testData.get("employeeId")) + ",\n" + "    \"userStatus\": "
				+ getJsonValue(testData.get("userStatus")) + "\n" + "  },\n"
				+ "  \"mergeLDAPUsersWithExistingSystemUsers\": "
				+ getJsonValue(testData.get("mergeLDAPUsersWithExistingSystemUsers")) + ",\n" + "  \"syncInterval\": "
				+ getJsonValue(testData.get("syncInterval")) + "\n" + "}";

		System.out.println(requestBody);

		CustomResponse customResponse = apiUtil.PutAdminConfig(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PutAdminConfig",
				List.of("given", "cookie", "put", "response"));

		Assert.assertTrue(isImplementationCorrect,
				"PutAdminConfig must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.enable);
		Assert.assertNotNull(customResponse.hostname);
		Assert.assertNotNull(customResponse.port);
		Assert.assertNotNull(customResponse.encryption);
		Assert.assertNotNull(customResponse.ldapImplementation);
		Assert.assertNotNull(customResponse.bindAnonymously);
		Assert.assertNotNull(customResponse.bindUserDN);
		Assert.assertNotNull(customResponse.hasBindUserPassword);
		Assert.assertNotNull(customResponse.userLookupSettings);
		Assert.assertNotNull(customResponse.dataMapping);
		Assert.assertNotNull(customResponse.mergeLDAPUsersWithExistingSystemUsers);
		Assert.assertNotNull(customResponse.syncInterval);
	}

	@Test(priority = 8, groups = {
			"PL1" }, description = "1. Send a PUT request to the '/web/index.php/api/v2/pim/optional-field' endpoint with a valid cookie\n"
					+ "2. Print and verify the response status code and response body\n"
					+ "3. Assert that the response status code is 200 and validate implementation correctness")

	public void PutOptionalField() throws Exception {
		String endpoint = "/web/index.php/api/v2/pim/optional-field";

		Map<String, String> testData = FileOperations.readExcelPOI(excelPath, "PutOptionalField");

		String requestBody = "{\n" + "    \"pimShowDeprecatedFields\": " + testData.get("pimShowDeprecatedFields")
				+ ",\n" + "    \"showSIN\": " + testData.get("showSIN") + ",\n" + "    \"showSSN\": "
				+ testData.get("showSSN") + ",\n" + "    \"showTaxExemptions\": " + testData.get("showTaxExemptions")
				+ "\n" + "}";

		CustomResponse customResponse = apiUtil.PutOptionalField(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PutOptionalField",
				List.of("given", "cookie", "put", "response"));

		Assert.assertTrue(isImplementationCorrect,
				"PutOptionalField must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.employeeNumbers);
		Assert.assertNotNull(customResponse.lastNames);
		Assert.assertNotNull(customResponse.firstNames);
		Assert.assertNotNull(customResponse.employeeIds);
	}

	@Test(priority = 9, groups = {
			"PL1" }, description = "1. Send a POST request to the '/web/index.php/api/v2/pim/custom-fields' endpoint with a valid cookie\n"
					+ "2. Extract the Details of the newly created Custom Field from the response\n"
					+ "3. Print and verify the request body, status code, and response body\n"
					+ "4. Assert that the status code is 200 and validate implementation correctness")

	public void PostCustomField() throws Exception {
		String endpoint = "/web/index.php/api/v2/pim/custom-fields";

		Map<String, String> testData = FileOperations.readExcelPOI(excelPath, "PostCustomField");

		String requestBody = "{" + "\"fieldName\": \"" + testData.get("fieldName").trim() + "\"," + "\"screen\": \""
				+ testData.get("screen").trim() + "\"," + "\"fieldType\": " + formatFieldType(testData.get("fieldType"))
				+ "," + "\"extraData\": " + formatExtraData(testData.get("extraData")) + "}";

		CustomResponse customResponse = apiUtil.PostCustomField(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PostCustomField",
				List.of("given", "cookie", "post", "response"));
		Assert.assertTrue(isImplementationCorrect,
				"PostCustomField must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.getId());
		Assert.assertNotNull(customResponse.fieldName);
		Assert.assertNotNull(customResponse.fieldType);
		Assert.assertNotNull(customResponse.extraData);
		Assert.assertNotNull(customResponse.screen);
	}

	@Test(priority = 10, groups = {
			"PL1" }, description = "1. Generate a PUT Request to the '/web/index.php/api/v2/pim/custom-fields/3' endpoint iwth valid Cookies"
					+ "2. Print and verify the request body, status code, and response body\n"
					+ "3. Assert that the status code is 200 and validate implementation correctness")

	public void PutCustomField() throws Exception {
		String endpoint = "/web/index.php/api/v2/pim/custom-fields/1";

		Map<String, String> testData = FileOperations.readExcelPOI(excelPath, "PutCustomField");

		String requestBody = "{" + "\"fieldName\": \"" + testData.get("fieldName") + "\"," + "\"screen\": \""
				+ testData.get("screen") + "\"," + "\"fieldType\": " + formatFieldType(testData.get("fieldType")) + ","
				+ "\"extraData\": " + formatExtraData(testData.get("extraData")) + "}";

		CustomResponse customResponse = apiUtil.PutCustomField(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PutCustomField",
				List.of("given", "cookie", "put", "response"));

		Assert.assertTrue(isImplementationCorrect,
				"PutCustomField must be implementated using the Rest assured  methods only!");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");
		Assert.assertNotNull(customResponse.getId());
		Assert.assertNotNull(customResponse.fieldName);
		Assert.assertNotNull(customResponse.fieldType);
		Assert.assertNotNull(customResponse.extraData);
		Assert.assertNotNull(customResponse.screen);
	}
	/*----------------------Helper method----------------------------*/

	private String formatFieldType(String value) {
		// Handles float string like "0.0" -> "0"
		try {
			return String.valueOf((int) Double.parseDouble(value));
		} catch (Exception e) {
			return "0"; // Default fallback
		}
	}

	private String formatExtraData(String value) {
		if (value == null || value.trim().equalsIgnoreCase("null") || value.trim().isEmpty()) {
			return "null"; // Keep it as null (not a string)
		} else {
			return "\"" + value.trim() + "\""; // Wrap in quotes
		}
	}

	private String quoteOrNull(String val) {
		return (val == null || val.trim().equalsIgnoreCase("null") || val.trim().isEmpty()) ? "null"
				: "\"" + val + "\"";
	}

	private String getJsonValue(String value) {
		if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("null")) {
			return "null";
		}

		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			return value.toLowerCase();
		}

		try {
			if (value.contains(".")) {
				double d = Double.parseDouble(value);
				return String.valueOf((int) d); // handle 3890.0 → 3890
			}
			Integer.parseInt(value);
			return value;
		} catch (NumberFormatException ignored) {
		}

		return "\"" + value.trim() + "\"";
	}
}
