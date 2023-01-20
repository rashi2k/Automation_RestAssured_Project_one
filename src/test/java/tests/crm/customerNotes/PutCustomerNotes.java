package tests.crm.customerNotes;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.CustomerNotes;
import com.informatics.pojos.admin.Tax;
import com.informatics.pojos.crm.TaxesExcepted;
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

public class PutCustomerNotes extends BaseTest
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
    @Description("IQ-961 [R2] [CRM_API] Verify PUT - customer notes with Valid payload & " +
            "IQ-956 [R2] [CRM_API] Verify PUT - customer notes with Valid contactNoteId")
    public void testPutCustomerNotesWithValidPayLoad()
    {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String saveCustomerNoteId = JsonPath.read(response.asString(), "$.result.contactNoteId").toString();
        String updatePutURI = APIConstants.PUT_CUSTOMER_NOTES.replace("{noteId}", saveCustomerNoteId);

        CustomerNotes updateCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        updateCustomerNotes.setContactNoteId(Integer.parseInt(saveCustomerNoteId));

        updateCustomerNotes.setCustomerNote("Hello World");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerNotes)
                .put(updatePutURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerNote").toString()), updateCustomerNotes.getCustomerNote(), "Invalid customerNote");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateCustomerNotes.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), customerId, "Invalid customerId");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-962 [R2] [CRM_API] Verify PUT - customer notes with Null Payload")
    public void testPutCustomerNotesWithEmptyPayLoad() {
        String updateURI = APIConstants.PUT_CUSTOMER_NOTES.replace("{noteId}", "");

        CustomerNotes updateCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        updateCustomerNotes.setCustomerNote(null);
        updateCustomerNotes.setIsActive(null);
        updateCustomerNotes.setCustomerId(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerNotes)
                .put(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-957 [R2] [CRM_API] Verify PUT - customer notes with Invalid contactNoteId with all other valid data")
    public void testPutCustomerNotesWithInvalidContactNoteId()
    {
        String invalidId = "999999";
        String updateNoteURI = APIConstants.PUT_CUSTOMER_NOTES.replace("{noteId}", invalidId);

        CustomerNotes updateCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));

        updateCustomerNotes.setContactNoteId(Integer.parseInt(invalidId));

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerNotes)
                .put(updateNoteURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-959 [R2] [CRM_API] Verify PUT - customer notes with Null contactNoteId and all other valid data")
    public void testPutCustomerNotesWithNullContactNoteId()
    {
        String updateNoteURI = APIConstants.PUT_CUSTOMER_NOTES.replace("{noteId}", "");

        CustomerNotes updateCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        updateCustomerNotes.setContactNoteId(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerNotes)
                .put(updateNoteURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-963 [R2] [CRM_API] Verify PUT - customer notes with Null customerNote and all other valid data")
    public void testPutCustomerNotesWithNullCustomerNote()
    {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String saveCustomerNoteId = JsonPath.read(response.asString(), "$.result.contactNoteId").toString();
        String updateNoteURI = APIConstants.PUT_CUSTOMER_NOTES.replace("{noteId}", saveCustomerNoteId);

        CustomerNotes updateCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        updateCustomerNotes.setContactNoteId(Integer.parseInt(saveCustomerNoteId));

        updateCustomerNotes.setCustomerNote(null);
        updateCustomerNotes.setIsActive(createCustomerNotes.getIsActive());
        updateCustomerNotes.setCustomerId(createCustomerNotes.getCustomerId());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerNotes)
                .put(updateNoteURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-965 [R2] [CRM_API] Verify PUT - customer notes with Invalid customerId")
    public void testPutCustomerNotesWithInvalidCustomerId()
    {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String saveCustomerNoteId = JsonPath.read(response.asString(), "$.result.contactNoteId").toString();
        String updateNoteURI = APIConstants.PUT_CUSTOMER_NOTES.replace("{noteId}", saveCustomerNoteId);

        CustomerNotes updateCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        updateCustomerNotes.setContactNoteId(Integer.parseInt(saveCustomerNoteId));

        updateCustomerNotes.setCustomerNote(createCustomerNotes.getCustomerNote());
        updateCustomerNotes.setIsActive(createCustomerNotes.getIsActive());
        updateCustomerNotes.setCustomerId(99999);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerNotes)
                .put(updateNoteURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-966 [R2] [CRM_API] Verify PUT - customer notes with Null status")
    public void testPutCustomerNotesWithNullIsActive()
    {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String saveCustomerNoteId = JsonPath.read(response.asString(), "$.result.contactNoteId").toString();
        String updateNoteURI = APIConstants.PUT_CUSTOMER_NOTES.replace("{noteId}", saveCustomerNoteId);

        CustomerNotes updateCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        updateCustomerNotes.setContactNoteId(Integer.parseInt(saveCustomerNoteId));

        updateCustomerNotes.setCustomerNote(createCustomerNotes.getCustomerNote());
        updateCustomerNotes.setIsActive(null);
        updateCustomerNotes.setCustomerId(createCustomerNotes.getCustomerId());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerNotes)
                .put(updateNoteURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-967 [R2] [CRM_API] Verify PUT - customer notes with Null customerId")
    public void testPutCustomerNotesWithNullCustomerId()
    {
        String updateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String saveCustomerNoteId = JsonPath.read(response.asString(), "$.result.contactNoteId").toString();
        String updateNoteURI = APIConstants.PUT_CUSTOMER_NOTES.replace("{noteId}", saveCustomerNoteId);

        CustomerNotes updateCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        updateCustomerNotes.setContactNoteId(Integer.parseInt(saveCustomerNoteId));

        updateCustomerNotes.setCustomerNote(createCustomerNotes.getCustomerNote());
        updateCustomerNotes.setIsActive(createCustomerNotes.getIsActive());
        updateCustomerNotes.setCustomerId(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerNotes)
                .put(updateNoteURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);
    }

}
