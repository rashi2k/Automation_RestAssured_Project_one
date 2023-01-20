package tests.crm.customerBasicInfo;

import com.informatics.endpoints.APIConstants;
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

public class PutCustomerBasicInfo extends BaseTest {

    public Integer customerId;

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
        customerId = Integer.parseInt(JsonPath.read(response.asString(), "$.result.customerId").toString());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-357 [R1][CRM_API] Validate the process of Updating a customer with Valid payload")
    public void testPutCustomerBasicInfoWithValidPayLoad() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customer id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid lead code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-359 [R1][CRM_API] Validate the process of editing a customer with Empty payload")
    public void testPutCustomerBasicInfoWithEmptyPayLoad() {
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", "");

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-312 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null First Name")
    public void testPutCustomerBasicInfoWithNullFirstName() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), null, createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-322 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null Middle Name")
    public void testPutCustomerBasicInfoWithNullMiddleName() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), null, createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "middleName field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-326 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null Last Name")
    public void testPutCustomerBasicInfoWithNullLastName() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), null, createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-330 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null Calling Name")
    public void testPutCustomerBasicInfoWithNullCallingName() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getMiddleName(), createCustomer.getLastName(), null, createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "callingName field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-334 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null Surname with Initials")
    public void testPutCustomerBasicInfoWithNullSurnameWithInitials() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getMiddleName(), createCustomer.getLastName(), createCustomer.getCallingName(), null, createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-336 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null Salutation")
    public void testPutCustomerBasicInfoWithNullSalutation() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getMiddleName(), createCustomer.getLastName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), null, "Prof. ", createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "salutation field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-338 [R1][CRM_API] Verify the PUT - update_customer_basic_info with invalid Salutation")
    public void testPutCustomerBasicInfoWithInvalidSalutation() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getMiddleName(), createCustomer.getLastName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), "a", createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-340 [R1][CRM_API] Verify the PUT - update_customer_basic_info with invalid data type to Salutation")
    public void testPutCustomerBasicInfoWithInvalidSalutationDataType() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getMiddleName(), createCustomer.getLastName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), String.valueOf(9999), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-360 [R1][CRM_API] Validate the process of updating a customer by only providing mandatory values")
    public void testPutCustomerBasicInfoWithOnlyMandatoryValues() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), null, null, createCustomer.getSurnameWithInitials(), null, null, createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "callingName field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-388 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null input in salutationOther")
    public void testPutCustomerBasicInfoWitNullSalutationOther() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), null, createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customer id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid lead code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-395 [R1] [CRM_API] Validate the process of updating a customer with null civilStatusId")
    public void testPutCustomerBasicInfoWitNullCivilStatusId() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), null, createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-397 [R1][CRM_API] Validate the process of updating a customer with Invalid civilStatusId")
    public void testPutCustomerBasicInfoWitInvalidCivilStatusId() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), 99999, createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    /*
    //Can not insert String to civilStatusId: type - int
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-398 [R1][CRM_API] Validate the process of update a customer with Invalid data type as civilStatusId")
    public void testPutCustomerBasicInfoWitInvalidCivilStatusIdDataType() {
        try {
            CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

            Response responseA = RestAssured.given().spec(repoSpec)
                    .when()
                    .body(createCustomer)
                    .post(APIConstants.CREATE_CUS_BASIC_INFO);
            System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
            responseA.getBody().prettyPrint();
            Assert.assertEquals(responseA.statusCode(), 201);

            String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
            String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

            CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), Integer.parseInt("one"), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
            Response responseB = RestAssured.given().spec(repoSpec)
                    .when()
                    .body(updateCustomerBasicInfo)
                    .put(updatedURI);
            System.out.println("Status Code: " + responseB.getStatusCode());
            responseB.getBody().prettyPrint();
            Assert.assertEquals(responseB.statusCode(), 400);
        }
        catch (Exception e) {
            System.out.println("Can not insert String to civilStatusId: type - int");
            throw new SkipException("Can not insert String to civilStatusId: type - int");
        }
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-399 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null input in gender")
    public void testPutCustomerBasicInfoWitNullGender() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), null, createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(),createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-400 [R1][CRM_API] Validate the process of updating a customer with null ‘policyholderPreferredLanguage’")
    public void testPutCustomerBasicInfoWitNullPolicyholderPreferredLanguage() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), null, createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-401 [R1][CRM_API] Verify the PUT - update_customer_basic_info with invalid data in gender")
    public void testPutCustomerBasicInfoWithInvalidGender() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), "HIM", createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-402 [R1] [CRM_API] Verify the PUT - update_customer_basic_info with invalid format in gender")
    public void testPutCustomerBasicInfoWithInvalidGenderFormat() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), String.valueOf(1), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-403 [R1][CRM_API] Validate the process of updating a customer with Invalid ‘policyholderPreferredLanguage’")
    public void testPutCustomerBasicInfoWithInvalidPolicyholderPreferredLanguage() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), "english", createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-404 [R1][CRM_API] Validate the process of updating a customer with Null ID Type and Null ID number")
    public void testPutCustomerBasicInfoWithNullIDTypeAndNullIdNumber() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object> mapId = new HashMap<>();
        mapId.put("idType", null);
        mapId.put("idNumber", null);
        mapId.put("isPrimary", true);

        List<Map> idNumbers = new ArrayList<>();
        idNumbers.add(mapId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), idNumbers, createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-405 [R1][CRM_API] Validate the process of updating a customer with Null ID Type and Valid ID number")
    public void testPutCustomerBasicInfoWithNullIDTypeAndValidIdNumber() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object> mapId = new HashMap<>();
        mapId.put("idType", null);
        mapId.put("idNumber", idNumber);
        mapId.put("isPrimary", true);

        List<Map> idNumbers = new ArrayList<>();
        idNumbers.add(mapId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), idNumbers, createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-406 [R1][CRM_API] Validate the process of updating a customer with Valid ID Type and Null ID number")
    public void testPutCustomerBasicInfoWithValidIDTypeAndNullIdNumber() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object> mapId = new HashMap<>();
        mapId.put("idType", idType);
        mapId.put("idNumber", null);
        mapId.put("isPrimary", true);

        List<Map> idNumbers = new ArrayList<>();
        idNumbers.add(mapId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), idNumbers, createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-407 [R1][CRM_API] Validate the process of updating a customer with Invalid ID type with Valid ID number")
    public void testPutCustomerBasicInfoWithInvalidIDTypeAndValidIdNumber() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object> mapId = new HashMap<>();
        mapId.put("idType", 99999);
        mapId.put("idNumber", idNumber);
        mapId.put("isPrimary", true);

        List<Map> idNumbers = new ArrayList<>();
        idNumbers.add(mapId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), idNumbers, createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-407 [R1][CRM_API] Validate the process of updating a customer with Invalid ID type with Valid ID number")
    public void testPutCustomerBasicInfoWithValidIDTypeAndValidIdNumber() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object> mapId = new HashMap<>();
        mapId.put("idType", idType);
        mapId.put("idNumber", idNumber);
        mapId.put("isPrimary", true);

        List<Map> idNumbers = new ArrayList<>();
        idNumbers.add(mapId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), idNumbers, createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customer id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid lead code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-409 [R1][CRM_API] Validate the process of updating a customer with Null relationshipTypeId and Null customerId:")
    public void testPutCustomerBasicInfoWithNullRelationshipIdAndNullCustomerId() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object>  mapCus = new HashMap<>();
        mapCus.put("relationshipTypeId", null);
        mapCus.put("customerId", null);

        List<Map> relatedCustomers = new ArrayList<>();
        relatedCustomers.add(mapCus);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), relatedCustomers);
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_LINK_PARTY_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no link party type related data for link party type id -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-410 [R1][CRM_API] Validate the process of updating a customer with Null relationshipTypeId and valid customerId:")
    public void testPutCustomerBasicInfoWithNullRelationshipIdAndValidCustomerId() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object>  mapCus = new HashMap<>();
        mapCus.put("relationshipTypeId", null);
        mapCus.put("customerId", customerId);

        List<Map> relatedCustomers = new ArrayList<>();
        relatedCustomers.add(mapCus);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), relatedCustomers);
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_LINK_PARTY_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no link party type related data for link party type id -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-411 [R1][CRM_API] Validate the process of updating a customer with valid relationshipTypeId and Null customerId")
    public void testPutCustomerBasicInfoWithValidRelationshipIdAndNullCustomerId() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object>  mapCus = new HashMap<>();
        mapCus.put("relationshipTypeId", relationshipTypeId);
        mapCus.put("customerId", null);

        List<Map> relatedCustomers = new ArrayList<>();
        relatedCustomers.add(mapCus);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), relatedCustomers);
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-412 [R1][CRM_API] Validate the process of updating a customer with invalid relationshipTypeId and invalid customerId:")
    public void testPutCustomerBasicInfoWithInvalidRelationshipIdAndInvalidCustomerId() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object>  mapCus = new HashMap<>();
        mapCus.put("relationshipTypeId", 99999);
        mapCus.put("customerId", 99999);

        List<Map> relatedCustomers = new ArrayList<>();
        relatedCustomers.add(mapCus);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), relatedCustomers);
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_LINK_PARTY_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no link party type related data for link party type id -99999", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-413 [R1][CRM_API] Validate the process of updating a customer with invalid relationshipTypeId and valid customerId:")
    public void testPutCustomerBasicInfoWithInvalidRelationshipIdAndValidCustomerId() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object>  mapCus = new HashMap<>();
        mapCus.put("relationshipTypeId", 99999);
        mapCus.put("customerId", customerId);

        List<Map> relatedCustomers = new ArrayList<>();
        relatedCustomers.add(mapCus);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), relatedCustomers);
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_LINK_PARTY_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no link party type related data for link party type id -99999", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-414 [R1] [CRM_API] Validate the process of updating a customer with valid relationshipTypeId and invalid customerId:")
    public void testPutCustomerBasicInfoWithValidRelationshipIdAndInvalidCustomerId() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        Map<String, Object>  mapCus = new HashMap<>();
        mapCus.put("relationshipTypeId", relationshipTypeId);
        mapCus.put("customerId", 999999);

        List<Map> relatedCustomers = new ArrayList<>();
        relatedCustomers.add(mapCus);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), relatedCustomers);
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer related data for the given customer id - 999999", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-415 [R1][CRM_API] Validate the process of updating a customer with null ‘customerPortalAccess’")
    public void testPutCustomerBasicInfoWithNullCustomerPortalAccess() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), null, createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    /*
    //Can not insert String or Integer value into CustomerPortalAccess - type: boolean
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-417 [R1] [CRM_API] Validate the process of creating a customer with invalid ‘customerPortalAccess’")
    public void testPutCustomerBasicInfoWithInvalidCustomerPortalAccess() {
        System.out.println("Can not insert String or Integer value into CustomerPortalAccess - type: boolean");
        throw new SkipException("Can not insert String or Integer value into CustomerPortalAccess - type: boolean");
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-418 [R1] [CRM_API] Validate the process of creating a customer with valid ‘customerPortalAccess’")
    public void testPutCustomerBasicInfoWithValidCustomerPortalAccess() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), false, createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customer id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid lead code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-420 [R1] Verify the PUT - update_customer_basic_info with m,f,o in gender")
    public void testPutCustomerBasicInfoWithGenderAsF() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), "F", createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customer id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid lead code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-420 [R1] Verify the PUT - update_customer_basic_info with m,f,o in gender")
    public void testPutCustomerBasicInfoWithGenderAsO() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), "O", createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customer id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid lead code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-421 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null input in uwCategories")
    public void testPutCustomerBasicInfoWithNullUwCategories() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        List<Integer> uwCategories = new ArrayList<Integer>();
        uwCategories.add(null);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), uwCategories, createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NATURE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for underwriting category id -null", "Invalid description");
        softAssert.assertAll();
    }

    /*
    //Can not insert String value into uwCategories - type: int
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-424 [R1][CRM_API] Verify the PUT - update_customer_basic_info with invalid format in uwCategories")
    public void testPutCustomerBasicInfoWithInvalidUwCategoriesFormat() {
        try {
            CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(customerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

            Response responseA = RestAssured.given().spec(repoSpec)
                    .when()
                    .body(createCustomer)
                    .post(APIConstants.CREATE_CUS_BASIC_INFO);
            System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
            responseA.getBody().prettyPrint();
            Assert.assertEquals(responseA.statusCode(), 201);

            String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
            String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

            List<Integer> uwCategories = new ArrayList<Integer>();
            uwCategories.add(Integer.parseInt("A"));

            CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), uwCategories, createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
            Response responseB = RestAssured.given().spec(repoSpec)
                    .when()
                    .body(updateCustomerBasicInfo)
                    .put(updatedURI);
            System.out.println("Status Code: " + responseB.getStatusCode());
            responseB.getBody().prettyPrint();
            Assert.assertEquals(responseB.statusCode(), 400);
        }
        catch (Exception e) {
            System.out.println("Can not insert String value into uwCategories - type: int");
            throw new SkipException("Can not insert String value into uwCategories - type: int");
        }
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-425 [R1][CRM_API] Verify the PUT - update_customer_basic_info with 1,2,3 in uwCategories")
    public void testPutCustomerBasicInfoWithValidUwCategories() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        List<Integer> uwCategories = new ArrayList<Integer>();
        uwCategories.add(uwCategoryId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), uwCategories, createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customer id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid lead code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-426 [R1] [CRM_API] Verify the PUT - update_customer_basic_info with null input in customerGroups")
    public void testPutCustomerBasicInfoWithNullCustomerGroups() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        List<Integer> customerGroups = new ArrayList<Integer>();
        customerGroups.add(null);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), customerGroups, createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_GROUP_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer group related data for customer group id -null", "Invalid description");
        softAssert.assertAll();
    }

    /*
    //Can not insert String value into customerGroups - type: int
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-435 [R1][CRM_API] Verify the PUT - update_customer_basic_info with invalid format in customerGroups")
    public void testPutCustomerBasicInfoWithInvalidCustomerGroupsFormat() {
        try {
            CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(customerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

            Response responseA = RestAssured.given().spec(repoSpec)
                    .when()
                    .body(createCustomer)
                    .post(APIConstants.CREATE_CUS_BASIC_INFO);
            System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
            responseA.getBody().prettyPrint();
            Assert.assertEquals(responseA.statusCode(), 201);

            String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
            String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

            List<Integer> customerGroups = new ArrayList<Integer>();
            customerGroups.add(Integer.parseInt("A"));

            CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), customerGroups, createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
            Response responseB = RestAssured.given().spec(repoSpec)
                    .when()
                    .body(updateCustomerBasicInfo)
                    .put(updatedURI);
            System.out.println("Status Code: " + responseB.getStatusCode());
            responseB.getBody().prettyPrint();
            Assert.assertEquals(responseB.statusCode(), 400);
        }
        catch (Exception e) {
            System.out.println("Can not insert String value into customerGroups - type: int");
            throw new SkipException("Can not insert String value into customerGroups - type: int");
        }
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-437 [R1] [CRM_API] Verify the PUT - update_customer_basic_info with 4,3 inputs in customerGroups")
    public void testPutCustomerBasicInfoWithValidCustomerGroups() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        List<Integer> customerGroups = new ArrayList<Integer>();
        customerGroups.add(customerGroupId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), customerGroups, createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerId").toString()), saveCustomerId, "Invalid customer id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.leadCode").toString()), saveLeadCode, "Invalid lead code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-446 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null input in dob")
    public void testPutCustomerBasicInfoWithNullDob() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), null, createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "dob field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-460 [R1][CRM_API] Verify the PUT - update_customer_basic_info with invalid format in dob")
    public void testPutCustomerBasicInvalidDobFormat() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), "15-12-1995", createCustomer.getNationality(), createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-482 [R1][CRM_API] Verify the PUT - update_customer_basic_info with null input in nationality")
    public void testPutCustomerBasicInfoWithNullNationality() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), null, createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);

        String statusCode = String.valueOf(responseB.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "nationality field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-483 [R1][CRM_API] Verify the PUT - update_customer_basic_info with invalid format in nationality")
    public void testPutCustomerBasicInfoWithInvalidNationalityFormat() {
        CustomerBasicInfo createCustomer = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String updatedURI = APIConstants.UPDATE_INDIVIDUAL_CUS_BASIC_INFO.replace("{id}", saveCustomerId);

        CustomerBasicInfo updateCustomerBasicInfo = requestPayload.updateCustomerBasicInfo(Integer.parseInt(saveCustomerId), createCustomer.getCustomerTypeCode(), createCustomer.getFirstName(), createCustomer.getLastName(), createCustomer.getMiddleName(), createCustomer.getCallingName(), createCustomer.getSurnameWithInitials(), createCustomer.getSalutationCode(), createCustomer.getSalutationOther(), createCustomer.getGender(), createCustomer.getDob(), "Sri lankan", createCustomer.getCivilStatusId(), createCustomer.getPolicyholderPreferredLanguage(), createCustomer.getUwCategories(), createCustomer.getCustomerGroups(), createCustomer.getCustomerNatureId(), createCustomer.getCustomerPortalAccess(), createCustomer.getIdNumbers(), createCustomer.getRelatedCustomers());
        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerBasicInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 400);
    }



}
