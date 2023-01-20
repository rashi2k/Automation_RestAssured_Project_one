package tests.crm.customerContactInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.admin.BankBranch;
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

public class PutCustomerContactInfo extends BaseTest {

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
    @Description("IQ-925 [R2][CRM_API] PUT verify contact info update with existing ids")
    public void testPutContactInfoWithExistingId() {
        String updatedURI = APIConstants.UPDATE_CUS_CONTACT_INFO.replace("{id}", customerId);
        ContactInfo contactInfo = requestPayload.contactInfo(emailTypeId, fName);

        List<ContactInfo> availableInfo = new ArrayList<>();
        availableInfo.add(contactInfo);
        List<Integer> deletedInfo = new ArrayList<Integer>();

        Map<String, Object>  emailInfo = new HashMap<>();
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

        ContactInfo updateContactInfo = requestPayload.contactInfo(emailTypeId, fName);
        updateContactInfo.setId(saveCusEmailId);
        updateContactInfo.setEmail(fName+ "@yahoomail.com");

        List<ContactInfo> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateContactInfo);


        Map<String, Object>  emailInfoUpdate = new HashMap<>();
        emailInfoUpdate.put("available", availableInfoUpdate);
        emailInfoUpdate.put("deleted", deletedInfo);

        Map<String, Object>  cusInfoUpdate = new HashMap<>();
        cusInfoUpdate.put("email", emailInfoUpdate);
        cusInfoUpdate.put("address", otherInfo);
        cusInfoUpdate.put("contact", 	otherInfo);
        cusInfoUpdate.put("contactPerson", otherInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(cusInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].id").toString()),String.valueOf(updateContactInfo.getId()) , "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].emailTypeId").toString()),String.valueOf(updateContactInfo.getEmailTypeId()) , "Invalid emailTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].email").toString()), updateContactInfo.getEmail(), "Invalid email");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].isReceiveNotification").toString()), String.valueOf(updateContactInfo.getIsReceiveNotification()), "Invalid isReceiveNotification");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].isPrimary").toString()), String.valueOf(updateContactInfo.getIsPrimary()), "Invalid isPrimary");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-926 [R2][CRM_API] PUT verify contact info update with newly added items(no ids)")
    public void testPutContactInfoWithNewlyAddedItem() {
        String updatedURI = APIConstants.UPDATE_CUS_CONTACT_INFO.replace("{id}", customerId);
        ContactInfo contactInfo = requestPayload.contactInfo(emailTypeId, fName);

        List<ContactInfo> availableInfo = new ArrayList<>();
        availableInfo.add(contactInfo);
        List<Integer> deletedInfo = new ArrayList<Integer>();

        Map<String, Object>  emailInfo = new HashMap<>();
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

        ContactInfo updateContactInfo = requestPayload.contactInfo(emailTypeId, fName);
        updateContactInfo.setId(saveCusEmailId);
        updateContactInfo.setEmail(fName+ "@yahoomail.com");

        List<ContactInfo> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateContactInfo);


        Map<String, Object>  emailInfoUpdate = new HashMap<>();
        emailInfoUpdate.put("available", availableInfoUpdate);
        emailInfoUpdate.put("deleted", deletedInfo);

        Map<String, Object>  cusInfoUpdate = new HashMap<>();
        cusInfoUpdate.put("email", emailInfoUpdate);
        cusInfoUpdate.put("address", otherInfo);
        cusInfoUpdate.put("contact", 	otherInfo);
        cusInfoUpdate.put("contactPerson", otherInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(cusInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].id").toString()),String.valueOf(updateContactInfo.getId()) , "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].emailTypeId").toString()),String.valueOf(updateContactInfo.getEmailTypeId()) , "Invalid emailTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].email").toString()), updateContactInfo.getEmail(), "Invalid email");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].isReceiveNotification").toString()), String.valueOf(updateContactInfo.getIsReceiveNotification()), "Invalid isReceiveNotification");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.email.available[0].isPrimary").toString()), String.valueOf(updateContactInfo.getIsPrimary()), "Invalid isPrimary");
        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-927 [R2][CRM_API] PUT verify contact info update with existing invalid id")
    public void testPutContactInfoWithInvalidId() {
        String updatedURI = APIConstants.UPDATE_CUS_CONTACT_INFO.replace("{id}", customerId);
        ContactInfo contactInfo = requestPayload.contactInfo(emailTypeId, fName);

        List<ContactInfo> availableInfo = new ArrayList<>();
        availableInfo.add(contactInfo);
        List<Integer> deletedInfo = new ArrayList<Integer>();

        Map<String, Object>  emailInfo = new HashMap<>();
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

        ContactInfo updateContactInfo = requestPayload.contactInfo(emailTypeId, fName);
        updateContactInfo.setId(saveCusEmailId);
        updateContactInfo.setEmail(fName+ "@yahoomail.com");

        List<ContactInfo> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateContactInfo);


        Map<String, Object>  emailInfoUpdate = new HashMap<>();
        emailInfoUpdate.put("available", availableInfoUpdate);
        emailInfoUpdate.put("deleted", deletedInfo);

        Map<String, Object>  cusInfoUpdate = new HashMap<>();
        cusInfoUpdate.put("email", emailInfoUpdate);
        cusInfoUpdate.put("address", otherInfo);
        cusInfoUpdate.put("contact", 	otherInfo);
        cusInfoUpdate.put("contactPerson", otherInfo);

        String invalidId = "999999A";
        String updatePutURI = APIConstants.UPDATE_CUS_CONTACT_INFO.replace("{id}", invalidId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(cusInfoUpdate)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-928 [R2][CRM_API] PUT verify contact info update with newly added items(no ids) with invalid data")
    public void testPutContactInfoWithInvalidData() {
        String updatedURI = APIConstants.UPDATE_CUS_CONTACT_INFO.replace("{id}", customerId);
        ContactInfo contactInfo = requestPayload.contactInfo(emailTypeId, fName);

        List<ContactInfo> availableInfo = new ArrayList<>();
        availableInfo.add(contactInfo);
        List<Integer> deletedInfo = new ArrayList<Integer>();

        Map<String, Object>  emailInfo = new HashMap<>();
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

        Integer invalidId = 9999999;
        ContactInfo updateContactInfo = requestPayload.contactInfo(emailTypeId, fName);
        updateContactInfo.setId(invalidId);
        updateContactInfo.setEmail(fName+ "@yahoomail.com");

        List<ContactInfo> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateContactInfo);


        Map<String, Object>  emailInfoUpdate = new HashMap<>();
        emailInfoUpdate.put("available", availableInfoUpdate);
        emailInfoUpdate.put("deleted", deletedInfo);

        Map<String, Object>  cusInfoUpdate = new HashMap<>();
        cusInfoUpdate.put("email", emailInfoUpdate);
        cusInfoUpdate.put("address", otherInfo);
        cusInfoUpdate.put("contact", 	otherInfo);
        cusInfoUpdate.put("contactPerson", otherInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(cusInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_EMPLOYEE_EMAIL_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no cm m employee email related data for the given id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

}
