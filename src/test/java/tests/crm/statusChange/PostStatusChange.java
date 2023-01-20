package tests.crm.statusChange;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.admin.Bank;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.StatusChange;
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

public class PostStatusChange extends BaseTest {

    public String customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseResTransactionInfo();
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
    @Description("IQ-864 [R2] [CRM_API] [Post] change status with valid payload")
    public void testPostStatusChangeWithValidPayLoad() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(transId0, transId1);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.statusId").toString()), String.valueOf(createStatusChange.getStatusId()), "Invalid statusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.remark").toString()), createStatusChange.getRemark(), "Invalid remark");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.restrictedTransIds[0]").toString()), String.valueOf(transId0), "Invalid restrictedTransIds0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.restrictedTransIds[1]").toString()), String.valueOf(transId1), "Invalid restrictedTransIds0");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-878 [R2] [CRM_API] [Post] change status with empty payload")
    public void testPostStatusChangeWithEmptyPayLoad() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(transId0, transId1);
        createStatusChange.setStatusId(null);
        createStatusChange.setRemark(null);
        createStatusChange.setRestrictedTransIds(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Null Id Found", "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "StatusId should not be null", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-867 [R2] [CRM_API] [Post] change status with invalid statusId and valid remark , valid restrictedTransId")
    public void testPostStatusChangeWithInvalidStatusId() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(transId0, transId1);
        createStatusChange.setStatusId(99999);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_STATUS_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Status id not found in CM_R_CUSTOMER_STATUS", "Invalid description");
        softAssert.assertAll();
    }


    /*
    //changed to invalid remark data format
    //IGNORED - CAN NOT ADD INTEGER TO STRING
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-870 [R2] [CRM_API] [Post] change status with invalid remark and valid statusId, valid restrictedTransId")
    public void testPostStatusChangeWithInvalidRemark() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(transId0, transId1);
        createStatusChange.setRemark("123@#$diiii1234");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-873 [R2] [CRM_API] [Post] change status with invalid restrictedTransId and valid statusId , valid remark")
    public void testPostStatusChangeWithInvalidRestrictedId() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(9999, 8888);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_STATUS_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "RestrictedTransIds not found in CmRInactRestrictTrxn.", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-882 [R2] [CRM_API] [Post] change status with valid statusId , valid remark and empty restrictedTransId")
    public void testPostStatusChangeWithEmptyRestrictedId() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(transId0, transId1);
        createStatusChange.setRestrictedTransIds(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "SYSTEM_COMMON_ERROR", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-885 [R2] [CRM_API] [Post] change status with valid restrictedTransId, valid statusId, and empty remark")
    public void testPostStatusChangeWithEmptyRemark() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(transId0, transId1);
        createStatusChange.setRemark(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);
    }

    /*
    //IGNORED - CAN NOT ADD STRING TO INTEGER
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-886 [R2] [CRM_API] [Post] change status with different format for statusId and valid remark , valid restrictedTransId")
    public void testPostStatusChangeWithDifferentStatusIdFormat() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(transId0, transId1);
        createStatusChange.setStatusId(Integer.parseInt("AAA01"));

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    //IGNORED - CAN NOT ADD STRING TO INTEGER
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-888 [R2] [CRM_API] [Post] change status with different format restrictedTransId and valid remark, valid statusId")
    public void testPostStatusChangeWithDifferentRestrictedIdFormat() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(Integer.parseInt("1csd"), Integer.parseInt("2ada"));

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
     */

    //max character = 200
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1360 [R2] [CRM_API] [POST] change status Verifying remark for character length 45")
    public void testPostStatusChangeWithCharacterLengthForRemark() {
        String updateURI = APIConstants.CREATE_STATUS_CHANGE.replace("{customerId}", customerId);
        StatusChange createStatusChange = requestPayload.statusChange(transId0, transId1);
        createStatusChange.setRemark("ddadasd adj13123 1njsdsndf asdakdnskdf nsdfnskdf neqeqw dargadaadadaSDASCA sdq " +
                "aqdadadasd ada wdqadasdasfd ae fafasdfafaedae ddadasd adj13123 1njsdsndf asdakdnskdf nsdfnskdf neqeqw " +
                "dargadaadadaSDASCA sdq aqdadadasd "); //215 characters

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createStatusChange)
                .post(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
}
