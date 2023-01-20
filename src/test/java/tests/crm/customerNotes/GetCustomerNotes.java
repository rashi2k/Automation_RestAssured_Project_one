package tests.crm.customerNotes;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.CustomerNotes;
import com.informatics.pojos.crm.TaxRegistration;
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

public class GetCustomerNotes extends BaseTest {

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
    @Description("IQ-896 [R2] [CRM_API] Verify GET - customer notes with Valid customerId")
    public void testGetCustomerNotesWithValidCustomerId()
    {
        CustomerNotes createCustomerNotes = requestPayload.newCustomerNotes(Integer.parseInt(customerId));
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_NOTES.replace("{customerId}", customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerNotes)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String updateGetURI = APIConstants.GET_CUSTOMER_NOTES.replace("{customerId}", customerId);
        String saveContactNoteId = JsonPath.read(resCreate.asString(), "$.result.contactNoteId").toString();

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isContactNoteFound= false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String noteId = JsonPath.read(responseToString, "$.result[" + i + "].contactNoteId").toString();
            if (noteId.equals(saveContactNoteId)) {
                isContactNoteFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerNote").toString()), createCustomerNotes.getCustomerNote(), "Invalid customerNote");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(createCustomerNotes.getIsActive()), "Invalid IsActive");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), customerId, "Invalid customerId");
                break;
            }
        }
        softAssert.assertTrue(isContactNoteFound, "Contact Note not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-899 [R2] [CRM_API] Verify GET - customer notes with Invalid customerId")
    public void testGetCustomerNotesWithInvalidCustomerId() {
        String customerId = "999999";
        String updateGetURI = APIConstants.GET_CUSTOMER_NOTES.replace("{customerId}", customerId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }


}
