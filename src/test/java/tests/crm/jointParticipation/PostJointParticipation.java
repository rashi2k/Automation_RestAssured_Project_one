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
import tests.BaseTest;

import static com.informatics.utils.PropertiesUtils.*;

public class PostJointParticipation extends BaseTest
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
    @Description("IQ-795 [R2][CRM_API] Verify the POST - /customer/joint/addition with valid data")
    public void testPostJointParticipationWithValidData()
    {
        JointParticipation createJointParticipation = requestPayload.createJointParticipation(jointPartnershipId,Integer.parseInt(customerId) , percentage);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createJointParticipation)
                .post(APIConstants.CREATE_JOINT_PARTICIPATION);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-797 [R2][CRM_API] Verify the POST - /customer/joint/addition with empty data")
    public void testPostJointParticipationWithEmptyPayLoad() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    //Can only send invalid data to code and partnership name
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-796 [R2][CRM_API] Verify the POST - /customer/joint/addition with invalid data")
    public void testPostJointParticipationWithInvalidData() {
        JointParticipation createJointParticipation = requestPayload.createJointParticipation(jointPartnershipId,Integer.parseInt(customerId),percentage);

        createJointParticipation.setCode("@@@@@@");
        createJointParticipation.setPartnershipName("#@");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createJointParticipation)
                .post(APIConstants.CREATE_JOINT_PARTICIPATION);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

}
