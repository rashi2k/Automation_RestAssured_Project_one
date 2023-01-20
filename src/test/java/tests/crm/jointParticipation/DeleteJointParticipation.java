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

public class DeleteJointParticipation extends BaseTest {

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
    @Description("IQ-801 [R2][CRM_API] verify DELETE - /customer/joint/{partnershipId}/removal with valid data")
    public void testDeleteJointParticipationWithValidId()
    {
        JointParticipation createJointParticipation = requestPayload.createJointParticipation(jointPartnershipId, Integer.parseInt(customerId), percentage);

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

        String saveId = JsonPath.read(resGet.asString(), "$.result.id").toString();
        String updatePutURI = APIConstants.DELETE_JOINT_PARTICIPATION.replace("{partnershipId}", saveId);

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatePutURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-802 [R2][CRM_API] verify DELETE - /customer/joint/{partnershipId}/removal with invalid data")
    public void testDeleteJointParticipationWithInvalidId()
    {
        String invalidId = "@@@@@@@";
        String updatePutURI = APIConstants.DELETE_JOINT_PARTICIPATION.replace("{partnershipId}", invalidId);

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatePutURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-803 [R2][CRM_API] verify DELETE - /customer/joint/{partnershipId}/removal with empty data")
    public void testDeleteJointParticipationWithNullId()
    {
        String updatePutURI = APIConstants.DELETE_JOINT_PARTICIPATION.replace("{partnershipId}", "");

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatePutURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 400);
    }

}
