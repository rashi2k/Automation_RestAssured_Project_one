package tests.crm.settingsController;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.ValidationEvents;
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
import java.util.List;

public class PutValidationEventsWithCustomerFlow extends BaseTest {

    public Integer customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseBankBranchInfo();
    }

    @BeforeMethod
    public void createParentCustomer() {
        //create test customer to get customerID
        CustomerBasicInfo createTestCustomer = requestPayload.createTestCustomer(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, idNumber);
        Response response = RestAssured.given().spec(repoSpec).when()
                .body(createTestCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        customerId = Integer.parseInt(JsonPath.read(response.asString(), "$.result.customerId").toString());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1422 [R1][CRM_API][Setting] Verify the PUT - Settings with customer flow with passing ''true'' for the ''isUsed'' field")
    public void testPutSettingWithCustomerFlowWithIsUsedAsTrue() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;

        int fieldId = 1; //field firstname
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, true, false, false);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        Response resUpdateSetting = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + resUpdateSetting.getStatusCode());
        resUpdateSetting.getBody().prettyPrint();
        Assert.assertEquals(resUpdateSetting.statusCode(), 200);

        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setFirstName(null);

        Response resCreateCustomer = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + resCreateCustomer.getStatusCode());
        resCreateCustomer .getBody().prettyPrint();
        Assert.assertEquals(resCreateCustomer.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreateCustomer.asString();
        String statusCode = String.valueOf(resCreateCustomer.getStatusCode());
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "firstName field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1424 [R1][CRM_API][Setting] Verify the PUT - Settings with customer flow with passing ''true'' for the ''isEditAllowed'' field and passing false to the isApprovalRequired field")
    public void testPutSettingWithCustomerFlowWithIsEditAllowedAsTrue() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;

        int fieldId = 1; //field firstname
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, true, true, false);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        Response resUpdateSetting = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + resUpdateSetting.getStatusCode());
        resUpdateSetting.getBody().prettyPrint();
        Assert.assertEquals(resUpdateSetting.statusCode(), 200);

        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        Response resCreateCustomer = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + resCreateCustomer.getStatusCode());
        resCreateCustomer.getBody().prettyPrint();
        Assert.assertEquals(resCreateCustomer.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreateCustomer.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), "AUT_QA_TEST_NAME", createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response resUpdateCustomer = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdateCustomer.getStatusCode());
        resUpdateCustomer.getBody().prettyPrint();
        Assert.assertEquals(resUpdateCustomer.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1425 [R1][CRM_API][Setting] Verify the PUT - Settings with customer flow with passing ''true'' for the ''isEditAllowed'' field and passing true to the isApprovalRequired field")
    public void testPutSettingWithCustomerFlowWithIsEditAllowedAndIsApprovalRequiredAsTrue() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;

        int fieldId = 1; //field firstname
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, true, true, true);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        Response resUpdateSetting = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + resUpdateSetting.getStatusCode());
        resUpdateSetting.getBody().prettyPrint();
        Assert.assertEquals(resUpdateSetting.statusCode(), 200);

        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        Response resCreateCustomer = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + resCreateCustomer.getStatusCode());
        resCreateCustomer.getBody().prettyPrint();
        Assert.assertEquals(resCreateCustomer.statusCode(), 201);

        String saveCustomerId = JsonPath.read(resCreateCustomer.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), "AUT_QA_TEST_NAME", createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response resUpdateCustomer = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdateCustomer.getStatusCode());
        resUpdateCustomer.getBody().prettyPrint();
        Assert.assertEquals(resUpdateCustomer.statusCode(), 200);
    }
}
