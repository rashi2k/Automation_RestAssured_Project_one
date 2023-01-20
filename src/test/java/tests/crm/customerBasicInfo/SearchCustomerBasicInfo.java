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

public class SearchCustomerBasicInfo extends BaseTest {

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
    @Description("IQ-440 [R1] [CRM_API] Verify GET - Search_customer with Valid firstName")
    public void testSearchCustomerBasicInfoWithValidFirstName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", createCustomerBasicInfo.getFirstName());

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        String responseToString = responseB.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String cusId = JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString();
            if (cusId.equals(saveCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                try {
                    softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result[" + i + "].customerCode")), null, "Invalid customerCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationCode").toString()), String.valueOf(createCustomerBasicInfo.getSalutationCode()), "Invalid salutation");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationOther").toString()), salutation, "Invalid salutationOther");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerNatureId").toString()), String.valueOf(createCustomerBasicInfo.getCustomerNatureId()), "Invalid customerNatureId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idType").toString()), String.valueOf(idType), "Invalid idType");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-490 [R1] [CRM_API] Verify GET - Search_customer with {firstName} with similar data")
    public void testSearchCustomerBasicInfoWithSimilarFirstName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", createCustomerBasicInfo.getFirstName().substring(0,6)) + "?page=0&size=99999";

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        String responseToString = responseB.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String cusId = JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString();
            if (cusId.equals(saveCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                try {
                    softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result[" + i + "].customerCode")), null, "Invalid customerCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationCode").toString()), String.valueOf(createCustomerBasicInfo.getSalutationCode()), "Invalid salutation");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationOther").toString()), salutation, "Invalid salutationOther");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerNatureId").toString()), String.valueOf(createCustomerBasicInfo.getCustomerNatureId()), "Invalid customerNatureId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idType").toString()), String.valueOf(idType), "Invalid idType");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-441 [R1] [CRM_API] Verify GET - Search_customer with Invalid firstName")
    public void testSearchCustomerBasicInfoWithInvalidFirstName() {
        String invalidFirstName = "XXAE-ZZ1EB";
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", invalidFirstName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-444 [R1] [CRM_API] Verify GET - Search_customer with Valid lastName")
    public void testSearchCustomerBasicInfoWithValidLastName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", createCustomerBasicInfo.getLastName());

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        String responseToString = responseB.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String cusId = JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString();
            if (cusId.equals(saveCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                try {
                    softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result[" + i + "].customerCode")), null, "Invalid customerCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationCode").toString()), String.valueOf(createCustomerBasicInfo.getSalutationCode()), "Invalid salutation");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationOther").toString()), salutation, "Invalid salutationOther");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerNatureId").toString()), String.valueOf(createCustomerBasicInfo.getCustomerNatureId()), "Invalid customerNatureId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idType").toString()), String.valueOf(idType), "Invalid idType");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-492 [R1] [CRM_API] Verify GET - Search_customer with {lastName} with similar data")
    public void testSearchCustomerBasicInfoWithSimilarLastName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", createCustomerBasicInfo.getLastName().substring(0,6)) + "?page=0&size=99999";

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        String responseToString = responseB.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String cusId = JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString();
            if (cusId.equals(saveCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                try {
                    softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result[" + i + "].customerCode")), null, "Invalid customerCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationCode").toString()), String.valueOf(createCustomerBasicInfo.getSalutationCode()), "Invalid salutation");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationOther").toString()), salutation, "Invalid salutationOther");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerNatureId").toString()), String.valueOf(createCustomerBasicInfo.getCustomerNatureId()), "Invalid customerNatureId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idType").toString()), String.valueOf(idType), "Invalid idType");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-445 [R1] [CRM_API] Verify GET - Search_customer with Invalid lastName")
    public void testSearchCustomerBasicInfoWithInvalidLastName() {
        String invalidLastName = "XXAE-ZZ1EB";
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", invalidLastName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-442 [R1] [CRM_API] Verify GET - Search_customer with Valid middleName")
    public void testSearchCustomerBasicInfoWithValidMiddleName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", createCustomerBasicInfo.getMiddleName());

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        String responseToString = responseB.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String cusId = JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString();
            if (cusId.equals(saveCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                try {
                    softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result[" + i + "].customerCode")), null, "Invalid customerCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationCode").toString()), String.valueOf(createCustomerBasicInfo.getSalutationCode()), "Invalid salutation");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationOther").toString()), salutation, "Invalid salutationOther");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerNatureId").toString()), String.valueOf(createCustomerBasicInfo.getCustomerNatureId()), "Invalid customerNatureId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idType").toString()), String.valueOf(idType), "Invalid idType");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-491 [R1] [CRM_API] Verify GET - Search_customer with {middleName} with similar data")
    public void testSearchCustomerBasicInfoWithSimilarMiddleName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", createCustomerBasicInfo.getMiddleName().substring(0,6)) + "?page=0&size=99999";

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        String responseToString = responseB.asString();
        Boolean isCustomerFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String cusId = JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString();
            if (cusId.equals(saveCustomerId)) {
                isCustomerFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), saveCustomerId, "Invalid customerId");
                try {
                    softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result[" + i + "].customerCode")), null, "Invalid customerCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].temporaryCode").toString()), null, "Invalid temporaryCode");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationCode").toString()), String.valueOf(createCustomerBasicInfo.getSalutationCode()), "Invalid salutation");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].salutationOther").toString()), salutation, "Invalid salutationOther");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerNatureId").toString()), String.valueOf(createCustomerBasicInfo.getCustomerNatureId()), "Invalid customerNatureId");
                try {
                    softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
                } catch (Exception e) {
                }
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].policyholderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idType").toString()), String.valueOf(idType), "Invalid idType");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
                softAssert.assertAll();
            }
        }
        softAssert.assertTrue(isCustomerFound, "Customer not found");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-443 [R1] [CRM_API] Verify GET - Search_customer with Invalid middleName")
    public void testSearchCustomerBasicInfoWithInvalidMiddleName() {
        String invalidMiddleName = "XXAE-ZZ1EB";
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", invalidMiddleName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-494 [R1] [CRM_API] Verify GET - Search_customer with Null values")
    public void testSearchCustomerBasicInfoWithNullValues() {
        String updatedURI = APIConstants.SEARCH_INDIVIDUAL_CUS_BASIC_INFO.replace("{searchBy}", " ");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-344 [R1] [CRM_API] Verify GET - Search_customer with Valid customerId")
    public void testSearchCustomerBasicInfoWithValidCustomerId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String body = "customerId.equals=" + saveCustomerId + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";

        String updatedURI = APIConstants.SEARCH_CUS_BASIC_INFO + "?" + body + pagination;

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .post(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), saveCustomerId, "Invalid customerId");
        try{
            softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result[0].permanentCode")), null , "Invalid customerCode");
        }
        catch (Exception e) {
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].temporaryCode").toString()), null , "Invalid temporaryCode");
        }
        catch (Exception e){
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].salutationCode").toString()), salutationId, "Invalid salutation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].salutationOther").toString()), salutation, "Invalid salutationOther");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
        try {
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
        }
        catch (Exception e){
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].policyHolderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].idType").toString()), idTypeId, "Invalid idType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-423 [R1] [CRM_API] Verify GET - Search_customer with Invalid customerId")
    public void testSearchCustomerBasicInfoWithInvalidCustomerId() {
        String invalidCustomerId = "999999";
        String body = "customerId.equals=" + invalidCustomerId + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";

        String updatedURI = APIConstants.SEARCH_CUS_BASIC_INFO + "?" + body + pagination;

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .post(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-429 [R1] [CRM_API] Verify GET - Search_customer with Valid leadCode")
    public void testSearchCustomerBasicInfoWithValidLeadCode() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response responseA = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + responseA.getStatusCode());
        responseA.getBody().prettyPrint();
        Assert.assertEquals(responseA.statusCode(), 201);

        String saveCustomerId = JsonPath.read(responseA.asString(), "$.result.customerId").toString();
        String saveCustomerLeadCode = JsonPath.read(responseA.asString(), "$.result.leadCode").toString();
        String body = "leadCode.contains=" + saveCustomerLeadCode + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";

        String updatedURI = APIConstants.SEARCH_CUS_BASIC_INFO + "?" + body + pagination;

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .post(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), saveCustomerId, "Invalid customerId");
        try{
            softAssert.assertEquals(String.valueOf(JsonPath.read(responseToString, "$.result[0].permanentCode")), null , "Invalid customerCode");
        }
        catch (Exception e) {
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].leadCode").toString()), saveCustomerLeadCode, "Invalid leadCode");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].temporaryCode").toString()), null , "Invalid temporaryCode");
        }
        catch (Exception e){
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerTypeCode").toString()), createCustomerBasicInfo.getCustomerTypeCode(), "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].uwCategories").toString()), String.valueOf(createCustomerBasicInfo.getUwCategories()), "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerPortalAccess").toString()), String.valueOf(createCustomerBasicInfo.getCustomerPortalAccess()), "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].firstName").toString()), createCustomerBasicInfo.getFirstName(), "Invalid firstName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].middleName").toString()), createCustomerBasicInfo.getMiddleName(), "Invalid middleName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].lastName").toString()), createCustomerBasicInfo.getLastName(), "Invalid lastName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].callingName").toString()), createCustomerBasicInfo.getCallingName(), "Invalid callingName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].surnameWithInitials").toString()), createCustomerBasicInfo.getSurnameWithInitials(), "Invalid surnameWithInitials");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].salutationCode").toString()), salutationId, "Invalid salutation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].salutationOther").toString()), salutation, "Invalid salutationOther");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].gender").toString()), createCustomerBasicInfo.getGender(), "Invalid gender");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerGroups").toString()), String.valueOf(createCustomerBasicInfo.getCustomerGroups()), "Invalid customerGroups");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].dob").toString()), createCustomerBasicInfo.getDob(), "Invalid dob");
        try {
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].nationality").toString()), createCustomerBasicInfo.getNationality(), "Invalid nationality");
        }
        catch (Exception e){
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].civilStatusId").toString()), String.valueOf(createCustomerBasicInfo.getCivilStatusId()), "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].policyHolderPreferredLanguage").toString()), createCustomerBasicInfo.getPolicyholderPreferredLanguage(), "Invalid policyHolderPreferredLanguage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].idType").toString()), idTypeId, "Invalid idType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].idNumber").toString()), idNumber, "Invalid idNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].relatedCustomers[0].relationshipTypeId").toString()), String.valueOf(relationshipTypeId), "Invalid relationshipTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].relatedCustomers[0].customerId").toString()), String.valueOf(customerId), "Invalid customerId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-430 [R1] [CRM_API] Verify GET - Search_customer with Invalid leadCode")
    public void testSearchCustomerBasicInfoWithInvalidLeadCode() {
        String invalidLeadCode = "QAX00001";
        String body = "leadCode.contains=" + invalidLeadCode + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";

        String updatedURI = APIConstants.SEARCH_CUS_BASIC_INFO + "?" + body + pagination;

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .post(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-432 [R1] [CRM_API] Verify GET - Search_customer with Valid temporaryCode")
    public void testSearchCustomerBasicInfoWithValidTemporaryCode() {
        String validTemporaryCode = "T00000000000001";
        String body = "temporaryCode.contains=" + validTemporaryCode + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";

        String updatedURI = APIConstants.SEARCH_CUS_BASIC_INFO + "?" + body + pagination;

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .post(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.searchId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].permanentCode").toString()), PropertiesUtils.searchPermCode , "Invalid customerCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].leadCode").toString()), PropertiesUtils.searchLeadCode, "Invalid leadCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].temporaryCode").toString()), PropertiesUtils.searchTempCode , "Invalid temporaryCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerTypeCode").toString()), PropertiesUtils.searchType, "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].uwCategories[0]").toString()), PropertiesUtils.searchUwCategories, "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerPortalAccess").toString()), PropertiesUtils.searchPortalAccess, "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].firstName").toString()), PropertiesUtils.searchName, "Invalid firstName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].middleName").toString()), PropertiesUtils.searchName, "Invalid middleName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].lastName").toString()), PropertiesUtils.searchName, "Invalid lastName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].callingName").toString()), PropertiesUtils.searchName, "Invalid callingName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].surnameWithInitials").toString()), PropertiesUtils.getSurnameWithInitials, "Invalid surnameWithInitials");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].salutationCode").toString()), PropertiesUtils.searchSalutation, "Invalid salutation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].salutationOther").toString()), PropertiesUtils.searchSalutationOther, "Invalid salutationOther");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].gender").toString()), PropertiesUtils.searchGender, "Invalid gender");
        //softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerGroups[0]").toString()), PropertiesUtils.searchCustomerGroups, "Invalid customerGroups");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].dob").toString()), PropertiesUtils.searchDob, "Invalid dob");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].nationality").toString()), PropertiesUtils.searchNationality, "Invalid nationality");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].civilStatusId").toString()), PropertiesUtils.searchCivilStatus, "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].policyHolderPreferredLanguage").toString()), PropertiesUtils.searchLang, "Invalid policyHolderPreferredLanguage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].identityId").toString()), PropertiesUtils.searchIdentityId, "Invalid identityId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].idType").toString()), PropertiesUtils.searchIdType, "Invalid idType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].idNumber").toString()), PropertiesUtils.searchIdNumber, "Invalid idNumber");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-433 [R1] [CRM_API] Verify GET - Search_customer with Invalid temporaryCode")
    public void testSearchCustomerBasicInfoWithInvalidTemporaryCode() {
        String invalidTemporaryCode = "QA_AUT_T_001";
        String body = "temporaryCode.contains=" + invalidTemporaryCode + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";

        String updatedURI = APIConstants.SEARCH_CUS_BASIC_INFO + "?" + body + pagination;

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .post(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-427 [R1] [CRM_API] Verify GET - Search_customer with Valid customer code")
    public void testSearchCustomerBasicInfoWithValidCustomerCode() {
        String validPermanentCode = "P00000000000001";
        String body = "customerCode.contains=" + validPermanentCode + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";

        String updatedURI = APIConstants.SEARCH_CUS_BASIC_INFO + "?" + body + pagination;

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .post(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.searchId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].permanentCode").toString()), PropertiesUtils.searchPermCode , "Invalid customerCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].leadCode").toString()), PropertiesUtils.searchLeadCode, "Invalid leadCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].temporaryCode").toString()), PropertiesUtils.searchTempCode , "Invalid temporaryCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerTypeCode").toString()), PropertiesUtils.searchType, "Invalid customerTypeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].uwCategories[0]").toString()), PropertiesUtils.searchUwCategories, "Invalid uwCategories");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerPortalAccess").toString()), PropertiesUtils.searchPortalAccess, "Invalid customerPortalAccess");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].firstName").toString()), PropertiesUtils.searchName, "Invalid firstName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].middleName").toString()), PropertiesUtils.searchName, "Invalid middleName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].lastName").toString()), PropertiesUtils.searchName, "Invalid lastName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].callingName").toString()), PropertiesUtils.searchName, "Invalid callingName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].surnameWithInitials").toString()), PropertiesUtils.getSurnameWithInitials, "Invalid surnameWithInitials");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].salutationCode").toString()), PropertiesUtils.searchSalutation, "Invalid salutation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].salutationOther").toString()), PropertiesUtils.searchSalutationOther, "Invalid salutationOther");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].gender").toString()), PropertiesUtils.searchGender, "Invalid gender");
        //softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerGroups[0]").toString()), PropertiesUtils.searchCustomerGroups, "Invalid customerGroups");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].dob").toString()), PropertiesUtils.searchDob, "Invalid dob");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].nationality").toString()), PropertiesUtils.searchNationality, "Invalid nationality");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].civilStatusId").toString()), PropertiesUtils.searchCivilStatus, "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].policyHolderPreferredLanguage").toString()), PropertiesUtils.searchLang, "Invalid policyHolderPreferredLanguage");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].identityId").toString()), PropertiesUtils.searchIdentityId, "Invalid identityId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].idType").toString()), PropertiesUtils.searchIdType, "Invalid idType");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].idNumbers[0].idNumber").toString()), PropertiesUtils.searchIdNumber, "Invalid idNumber");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-428 [R1] [CRM_API] Verify GET - Search_customer with Invalid customerCode")
    public void testSearchCustomerBasicInfoWithInvalidCustomerCode() {
        String invalidPermanentCode = "QA_AUT_P_001";
        String body = "customerCode.contains=" + invalidPermanentCode + "&distinct=true";
        String pagination = "&pageNumber=0&pageSize=10&paged=true";

        String updatedURI = APIConstants.SEARCH_CUS_BASIC_INFO + "?" + body + pagination;

        Response responseB = RestAssured.given().spec(repoSpec)
                .when()
                .post(updatedURI);
        System.out.println("\n" + "Status Code: " + responseB.getStatusCode());
        responseB.getBody().prettyPrint();
        Assert.assertEquals(responseB.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseB.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }
}




