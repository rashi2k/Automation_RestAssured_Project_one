package tests.crm.jointParticipation;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.JointParticipation;
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


public class GetJointParticipation extends BaseTest
{
    public String customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseJointParticipationInfo();
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
    @Description("IQ-724 [R2][CRM_API] Verify the GET -/customer/joint/code/{code} with valid data")
    public void testGetJointParticipationWithValidData()
    {
        JointParticipation createJointParticipation = requestPayload.createJointParticipation(jointPartnershipId,Integer.parseInt(customerId),percentage);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createJointParticipation)
                .post(APIConstants.CREATE_JOINT_PARTICIPATION);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String saveCode = createJointParticipation.getCode();
        String updateGetURI = APIConstants.GET_JOINT_PARTICIPATION_CODE.replace("{code}", saveCode);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()),saveCode, "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()),String.valueOf(createJointParticipation.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.partnershipName").toString()),String.valueOf(createJointParticipation.getPartnershipName()), "Invalid partnershipName");

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.participation[0].jointPartnershipId").toString()), String.valueOf(PropertiesUtils.jointPartnershipId), "Invalid jointPartnershipId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.participation[0].customerId").toString()),customerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.participation[0].percentage").toString()),String.valueOf(PropertiesUtils.percentage), "Invalid percentage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.participation[0].isDefault").toString()),String.valueOf(PropertiesUtils.isDefault), "Invalid isDefault");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-726 [R2][CRM_API] Verify the GET -/customer/joint/code/{code} with Empty data")
    public void testGetJointParticipationWithNullCode() {
        String updateGetURI = APIConstants.GET_JOINT_PARTICIPATION_CODE.replace("{code}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-713 [R2][CRM_API] Verify the GET -/customer/joint/{partnershipId} with Valid data")
    public void testGetJointParticipationWithValidId()
    {
        JointParticipation createJointParticipation = requestPayload.createJointParticipation(jointPartnershipId,Integer.parseInt(customerId),percentage);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createJointParticipation)
                .post(APIConstants.CREATE_JOINT_PARTICIPATION);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String saveCode = createJointParticipation.getCode();
        String updateGetURI = APIConstants.GET_JOINT_PARTICIPATION_CODE.replace("{code}", saveCode);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);

        String saveId = JsonPath.read(resGet.asString(), "$.result.id").toString();
        String updateGetIdURI = APIConstants.GET_JOINT_PARTICIPATION_ID.replace("{partnershipId}", saveId);

        Response resGetId = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetIdURI);
        System.out.println("Status Code: " + resGetId.getStatusCode());
        resGetId.getBody().prettyPrint();
        Assert.assertEquals(resGetId.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGetId.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()),saveCode, "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()),String.valueOf(createJointParticipation.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.partnershipName").toString()),String.valueOf(createJointParticipation.getPartnershipName()), "Invalid partnershipName");

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.participation[0].jointPartnershipId").toString()), String.valueOf(PropertiesUtils.jointPartnershipId), "Invalid jointPartnershipId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.participation[0].customerId").toString()),customerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.participation[0].percentage").toString()),String.valueOf(PropertiesUtils.percentage), "Invalid percentage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.participation[0].isDefault").toString()),String.valueOf(PropertiesUtils.isDefault), "Invalid isDefault");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-718 [R2][CRM_API] Verify the GET -/customer/joint/{partnershipId} with empty data")
    public void testGetJointParticipationWithNullId()
    {
        String updateGetURI = APIConstants.GET_JOINT_PARTICIPATION_ID.replace("{partnershipId}", "");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-353 [R1][CRM_API] Verify the GET - get_customer_basic_info with invalid ID")
    public void testGetCustomerBasicInfoWithInvalidId()
    {
        String invalidId = "99999";
        String updateGetURI = APIConstants.GET_JOINT_PARTICIPATION_ID.replace("{partnershipId}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CmR_JointPartnership_NOT_FOUND", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customerJointPartnership related data for the given partnership id -99999", "Invalid description");

        softAssert.assertAll();
    }

}


