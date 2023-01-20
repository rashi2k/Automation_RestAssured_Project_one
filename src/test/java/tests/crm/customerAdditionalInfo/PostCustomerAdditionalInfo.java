package tests.crm.customerAdditionalInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerAdditionalInfo;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.EmploymentInfo;
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

public class PostCustomerAdditionalInfo extends BaseTest {

    // ParentCompanyId = CustomerId
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
    @Description("IQ-727 [R2] [CRM_API] Verify the POST - Additional_info_data_crud by additionalI with Valid Pay Load")
    public void testPostAdditionalInfoWithValidPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", customerId);
        CustomerAdditionalInfo customerAdditionalInfo = requestPayload.customerAdditionalInfo();

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerAdditionalInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.additionalInfoValue").toString()), customerAdditionalInfo.getAdditionalInfoValue(), "Invalid additionalInfoValue");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(customerAdditionalInfo.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.cmRAdditionalInfoId").toString()), String.valueOf(customerAdditionalInfo.getCmRAdditionalInfoId()), "Invalid cmRAdditionalInfoId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-728 [R2][CRM_API] Verify the POST - Additional_info_data_crud with empty Pay Load")
    public void testPostAdditionalInfoWithEmptyPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", customerId);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);

        String statusCode = String.valueOf(resCreate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "Cm_R_Additional_Info_Id_Null", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Additional Info Reference Id cannot be null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-772 [R2][CRM_API] [Put] Verify the POST - Additional_info_data_crud with NULL ID")
    public void testPostAdditionalInfoWithNullID() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_ADDITIONAL_INFO.replace("{customerId}", "");
        CustomerAdditionalInfo customerAdditionalInfo = requestPayload.customerAdditionalInfo();

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerAdditionalInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 400);
    }

}
