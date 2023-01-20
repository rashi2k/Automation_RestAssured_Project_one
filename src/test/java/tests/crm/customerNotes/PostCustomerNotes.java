package tests.crm.customerNotes;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.CustomerNotes;
import com.informatics.utils.PropertiesUtils;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

public class PostCustomerNotes extends BaseTest
{
    public String customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
    }

    @BeforeMethod
    public void createParentCustomer() {
        //create test customer to get customerID
        CustomerBasicInfo createTestCustomer = requestPayload.createTestCustomer(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, idNumber);
        Response response = RestAssured.given().spec(repoSpec).when()
                .body(createTestCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        customerId = JsonPath.read(response.asString(), "$.result.customerId").toString();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-908 [R2] [CRM_API] Verify POST - customer notes with Valid payload")
    public void testPostCustomerNotesWithValidPayload() {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.customerNote").toString()), String.valueOf(createCustomerNotes.getCustomerNote()), "Invalid customerNote");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.isActive").toString()), String.valueOf(createCustomerNotes.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.customerId").toString()), customerId, "Invalid customerId");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-910 [R2] [CRM_API] Verify POST - customer notes with Null payload")
    public void testPostCustomerNotesWithEmptyPayLoad() {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));

        createCustomerNotes.setCustomerNote(null);
        createCustomerNotes.setIsActive(null);
        createCustomerNotes.setCustomerId(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-907 [R2] [CRM_API] Verify POST - customer notes with Null customerId")
    public void testPostCustomerNotesWithNullCustomerId() {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", "");

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        createCustomerNotes.setCustomerId(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-905 [R2] [CRM_API] Verify POST - customer notes with Invalid customerId")
    public void testPostCustomerNotesWithInvalidCustomerId() {
        String invalidId = "999999";
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", invalidId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        createCustomerNotes.setCustomerId(Integer.parseInt(invalidId));
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_NOT_FOUND", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no related data for the given customerId - 999999", "Invalid description");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-922 [R2] [CRM_API] Verify POST - customer notes with Null customerNote and all other valid data")
    public void testPostCustomerNotesWithNullCustomerNote() {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        createCustomerNotes.setCustomerNote(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-923 [R2] [CRM_API] Verify POST - customer notes with Null status")
    public void testPostCustomerNotesWithNullIsActive() {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        createCustomerNotes.setIsActive(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);
    }


}
