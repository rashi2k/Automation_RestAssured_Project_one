package tests.crm.jointParticipation;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.JointParticipation;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PutJointParticipation extends BaseTest
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
    @Description("IQ-798 [R2][CRM_API] Verify the PUT - /customer/joint/addition with valid data")
    public void testPutJointParticipationWithValidData()
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

        String saveId = JsonPath.read(resGet.asString(), "$.result.id").toString();
        String updatePutURI = APIConstants.PUT_JOINT_PARTICIPATION.replace("{partnershipId}", saveId);

      //  JointParticipation updateJointParticipation = requestPayload.updateJointParticipation(Integer.parseInt(saveId),jointPartnershipId,Integer.parseInt(customerId),percentage);

        Map<String, Object> mapCustomer = new HashMap<>();
        mapCustomer.put("jointPartnershipId", jointPartnershipId);
        mapCustomer.put("customerId", Integer.parseInt(customerId));
        mapCustomer.put("percentage", percentage);
        mapCustomer.put("isDefault", Boolean.valueOf(PropertiesUtils.isDefault));

        List<Map> ava = new ArrayList<>();
        ava.add(mapCustomer);

        List<Integer> delete = new ArrayList<Integer>();
     //   delete.add(0);

        Map<String, Object> participation = new HashMap<>();
        participation.put("available", ava);
        participation.put("deleted",delete);

        HashMap<String,Object> dataBody = new HashMap<String,Object>();
        dataBody.put("id", Integer.parseInt(saveId));
        dataBody.put("code", createJointParticipation.getCode());
        dataBody.put("partnershipName", "AUT-TestUser");
        dataBody.put("isActive",createJointParticipation.getIsActive());
        dataBody.put("participation", participation);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(dataBody)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-800 [R2][CRM_API] Verify the PUT - /customer/joint/addition with empty data")
    public void testPutJointParticipationWithNullId()
    {
        String updatePutURI = APIConstants.PUT_JOINT_PARTICIPATION.replace("{partnershipId}", "");

        JointParticipation updateJointParticipation = requestPayload.createJointParticipation(jointPartnershipId,Integer.parseInt(customerId),percentage);
        updateJointParticipation.setCode(null);
        updateJointParticipation.setPartnershipName(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateJointParticipation)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-799 [R2][CRM_API] Verify the PUT - /customer/joint/addition with invalid data")
    public void testPutJointParticipationWithInvalidId()
    {
        String invalidId = "999999";
        String updatePutURI = APIConstants.PUT_JOINT_PARTICIPATION.replace("{partnershipId}", invalidId);

        JointParticipation updateJointParticipation = requestPayload.createJointParticipation(jointPartnershipId,Integer.parseInt(customerId),percentage);
        updateJointParticipation.setId(Integer.parseInt(invalidId));
        updateJointParticipation.setCode("888888");
        updateJointParticipation.setPartnershipName("777777");

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateJointParticipation)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

}
