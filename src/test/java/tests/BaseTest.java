package tests;

import com.informatics.utils.DataUtils;
import com.informatics.utils.PropertiesUtils;
import io.restassured.RestAssured;
//import io.restassured.path.json.JsonPath;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import com.informatics.endpoints.APIConstants;
import com.informatics.payload.RequestPayloads;
import actions.APIActions;
import actions.AssertActions;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseTest {

	protected RequestSpecification repoSpec;
	protected AssertActions assertActions = new AssertActions();
	protected APIActions apiActions = new APIActions();
	protected RequestPayloads requestPayload = new RequestPayloads();

	public String individualCustomerType, corporateCustomerType, idType, idTypeId, idNumber, country, bankName,
			countryName, salutationCode, genderCode, nationality, preferLang, salutation, salutationId;
	public Integer uwCategoryId, customerGroupId, relationshipTypeId, civilStatusId, customerId, bankId, branchId, businessTypeId,
			emailTypeId, jointPartnershipId, percentage, countryId, taxTypeId, docTypeId, occupationId, transId0, transId1;

	@BeforeTest
	protected void setUpConfiguration() {
		repoSpec = new RequestSpecBuilder().setBaseUri(APIConstants.baseUrl).setAccept("application/json")
				.setContentType("application/json").build().log().all();
		repoSpec.given().relaxedHTTPSValidation();

		testGetToken();

		if (System.getProperty("token") != null) {
			repoSpec.given().header("Authorization", "Bearer " + System.getProperty("token"));
		}
	}

	//login
	public void testGetToken() {
		Response response = RestAssured.given().spec(repoSpec).when().body(requestPayload.login()).post("/security-center/auth/login");
		assertActions.verifyStatusCode(response);
		//JsonPath responseJson = response.jsonPath();
		String token = JsonPath.read(response.asString(),"$.result.access_token");
		String refreshToken = JsonPath.read(response.asString(),"$.result.refresh_token");

		setToken(token, refreshToken);
		System.out.println("------------------Access Token----------------\n "+System.getProperty("token"));
		System.out.println(response.getStatusCode());
		Assert.assertEquals(response.statusCode(), 200);
	}

	private void setToken(String token, String refreshToken) {
		try {
			System.setProperty("token", token);
			System.setProperty("refresh_token", refreshToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//logout
	@AfterTest
	public void logout() {
		Response response = RestAssured.given().spec(repoSpec).when().body(requestPayload.logout()).post("/security-center/auth/logout");
		assertActions.verifyStatusCode(response);

		removeToken();
		System.out.println("------------------  "+System.getProperty("token"));
		System.out.println(response.getStatusCode());
		Assert.assertEquals(response.statusCode(), 200);
	}

	private void removeToken() {
		try {
			System.clearProperty("token");
			System.clearProperty("refresh_token");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	//-------------------------------------------Get base information for http methods --------------------------------------------------

	//Response header
	public String baseResponse(String endpoint, String jsonProperty) {
		Response response = RestAssured.given().spec(repoSpec).when().get(endpoint);
		String placeHolderVar = JsonPath.read(response.asString(), jsonProperty).toString();
		return placeHolderVar;
	}

	//get base bank info -- country code
	public void getBaseCountryInfo() {
		country = baseResponse(APIConstants.GET_COUNTRY, "$.result[0].iso2code");
		countryId = Integer.parseInt(baseResponse(APIConstants.GET_COUNTRY, "$.result[0].countryId"));
		countryName = baseResponse(APIConstants.GET_COUNTRY, "$.result[0].countryName");
	}

	//get base tax info -- tax type id
	public void getBaseTaxInfo() {
		taxTypeId = Integer.parseInt(baseResponse(APIConstants.GET_TAX, "$.result[0].taxTypeId"));
	}

	//get base document info -- doc type id
	public void getBaseDocumentInfo() {
		docTypeId = Integer.parseInt(baseResponse(APIConstants.GET_CUS_DOCUMENTS_BULK, "$.result[0].documentTypeId"));
	}

	//get base occupation info -- occupation id
	public void getBaseOccupationInfo() {
		occupationId = Integer.parseInt(baseResponse(APIConstants.GET_OCCUPATION, "$.result[0].id"));
	}

	//get base restricted transaction info -- transaction id
	public void getBaseResTransactionInfo() {
		transId0 = Integer.parseInt(baseResponse(APIConstants.GET_RESTRICTED_TRANSACTIONS, "$.result[0].id"));
		transId1  = Integer.parseInt(baseResponse(APIConstants.GET_RESTRICTED_TRANSACTIONS, "$.result[1].id"));
	}

	//get base joint participation info
	public void getBaseJointParticipationInfo() {
		jointPartnershipId  = Integer.parseInt(PropertiesUtils.jointPartnershipId);
		percentage  = Integer.parseInt(PropertiesUtils.percentage);
	}

	//get base bank branch info
	public void getBaseBankBranchInfo() {
		bankId = Integer.parseInt(baseResponse(APIConstants.GET_BANK, "$.result[0].id"));
		bankName = baseResponse(APIConstants.GET_BANK, "$.result[0].name");
		branchId = Integer.parseInt(baseResponse(APIConstants.GET_BANK_BRANCH, "$.result[0].id"));
	}

	//get base customer info
	public void getBaseCustomerInfo() {
		salutationCode = baseResponse(APIConstants.GET_CUS_TITLE, "$.result[0].salutationCode");
		salutation = baseResponse(APIConstants.GET_CUS_TITLE, "$.result[0].salutation");
		salutationId = baseResponse(APIConstants.GET_CUS_TITLE, "$.result[0].salutationId");
		genderCode = baseResponse(APIConstants.GET_CUS_GENDER, "$.result[0].genderCode");
		nationality = baseResponse(APIConstants.GET_CUS_NATIONALITY, "$.result[0].nationality");
		civilStatusId = Integer.parseInt(baseResponse(APIConstants.GET_CUS_CIVIL_STATUS, "$.result[0].civilStatusId"));
		preferLang = baseResponse(APIConstants.GET_CUS_PREF_LANG, "$.result[0].iso3Code");
		idType = baseResponse(APIConstants.GET_CUS_ID_TYPE, "$.result[0].identityTypeCode");
		idTypeId = baseResponse(APIConstants.GET_CUS_ID_TYPE, "$.result[0].identityTypeId");
		relationshipTypeId = Integer.parseInt(baseResponse(APIConstants.GET_CUS_RELATIONSHIP_TYPE, "$.result[0].id"));
		customerGroupId = Integer.parseInt(baseResponse(APIConstants.GET_ALL_CUSTOMER_GROUPS, "$.result[0].id"));
		uwCategoryId = Integer.parseInt(baseResponse(APIConstants.GET_UW_CATEGORIES, "$.result[0].id"));
		businessTypeId = Integer.parseInt(baseResponse(APIConstants.GET_BUSINESS_TYPE, "$.result[0].id"));
		emailTypeId = Integer.parseInt(baseResponse(APIConstants.GET_EMAIL_TYPE_BULK, "$.result[0].emailTypeId"));
		idNumber = String.valueOf((int)(Math.random() * 999999999));
		individualCustomerType  = PropertiesUtils.INDIVIDUAL_CUS_TYPE;
		corporateCustomerType  = PropertiesUtils.CORPORATE_CUS_TYPE;
	}
}
