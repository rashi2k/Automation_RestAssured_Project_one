package tests.crm.customerContactInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.ContactInfo;
import com.informatics.pojos.crm.CustomerBasicInfo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCustomerContactInfo extends BaseTest {

    public String customerId, fName;

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

        //get customer fName
        Response responseCN = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", customerId));
        fName = JsonPath.read(responseCN.asString(), "$.result.firstName").toString();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-920 [R2][CRM_API] GET verify contact info with valid ID")
    public void testGetContactInfoWithValidId() {
        String updatedURI = APIConstants.UPDATE_CUS_CONTACT_INFO.replace("{id}", customerId);
        ContactInfo contactInfo = requestPayload.contactInfo(emailTypeId, fName);

        List<ContactInfo> availableInfo = new ArrayList<>();
        availableInfo.add(contactInfo);
        List<Integer> deletedInfo = new ArrayList<Integer>();

        Map<String, Object> emailInfo = new HashMap<>();
        emailInfo.put("available", availableInfo);
        emailInfo.put("deleted", deletedInfo);

        List<Map> availableOtherInfo = new ArrayList<>();
        Map<String, Object>  otherInfo = new HashMap<>();
        otherInfo.put("available", availableOtherInfo);
        otherInfo.put("deleted", deletedInfo);

        Map<String, Object>  cusInfo = new HashMap<>();
        cusInfo.put("email", emailInfo);
        cusInfo.put("address", otherInfo);
        cusInfo.put("contact", 	otherInfo);
        cusInfo.put("contactPerson", otherInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(cusInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCusEmailId = Integer.parseInt(JsonPath.read(response.asString(), "$.result.email.available[0].id").toString());

        String updatedGetURI = APIConstants.GET_CUS_CONTACT_INFO.replace("{id}", customerId);
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.emails[0].id").toString()), String.valueOf(saveCusEmailId) , "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.emails[0].emailTypeId").toString()), String.valueOf(contactInfo.getEmailTypeId()) , "Invalid emailTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.emails[0].email").toString()), contactInfo.getEmail(), "Invalid email");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.emails[0].isReceiveNotification").toString()), String.valueOf(contactInfo.getIsReceiveNotification()), "Invalid isReceiveNotification");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.emails[0].isPrimary").toString()), String.valueOf(contactInfo.getIsPrimary()), "Invalid isPrimary");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-921 [R2][CRM_API] GET verify contact info with invalid ID")
    public void testGetContactInfoWithInvalidId() {
        String invalidId = "999999A";
        String updatedGetURI = APIConstants.GET_CUS_CONTACT_INFO.replace("{id}", invalidId);
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);
    }
}
