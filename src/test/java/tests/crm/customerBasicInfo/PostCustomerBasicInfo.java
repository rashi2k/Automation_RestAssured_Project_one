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

public class PostCustomerBasicInfo extends BaseTest {

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
    @Description("IQ-224 [CRM_API] Validate the process of creating a customer with Valid payload")
    public void testPostCustomerWithValidPayLoad() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-236 [R1] [CRM_API] Validate the process of creating a customer with Empty payload")
    public void testPostCustomerWithEmptyPayLoad() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-237 [CRM_API] Verify the POST - create_customer_basic_info with invalid data in salutation")
    public void testPostCustomerWithInvalidSalutationData() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setSalutationCode("99999");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-238 [CRM_API] Verify the POST - create_customer_basic_info with invalid format in salutation")
    public void testPostCustomerWithInvalidSalutationFormat() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setSalutationCode("TEST");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-225 [CRM_API] Verify the POST - create_customer_basic_info with null input in salutation")
    public void testPostCustomerWithNullSalutation() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setSalutationCode(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "salutation field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-239 [R1] [CRM_API] Validate the process of creating a customer by only providing mandatory values")
    public void testPostCustomerWithOnlyMandatoryValues() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setCallingName(null);
        createCustomerBasicInfo.setSalutationCode(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "callingName field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-240 [CRM_API] Verify the POST - create_customer_basic_info with 0,1,2 inputs in salutation")
    public void testPostCustomerWithSalutationAs1() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setSalutationCode("1");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-240 [CRM_API] Verify the POST - create_customer_basic_info with 0,1,2 inputs in salutation")
    public void testPostCustomerWithSalutationAs2() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setSalutationCode("2");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-241 [R1] [CRM_API] Validate the process of creating a customer with null civilStatusId")
    public void testPostCustomerWithNullCivilStatusId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setCivilStatusId(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-242 [R1] [CRM_API] Validate the process of creating a customer with Invalid civilStatusId")
    public void testPostCustomerWithInvalidCivilStatusId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setCivilStatusId(9999);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    /*
    //Can not insert String value into CivilStatusId - type: int
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-243 [R1] [CRM_API] Validate the process of creating a customer with Invalid data type as civilStatusId")
    public void testPostCustomerWithInvalidDataTypeForCivilStatusId() {
        try {
            CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
            createCustomerBasicInfo.setCivilStatusId(Integer.valueOf("TEST"));

            Response response = RestAssured.given().spec(repoSpec)
                    .when()
                    .body(createCustomerBasicInfo)
                    .post(APIConstants.CREATE_CUS_BASIC_INFO);
            System.out.println("\n" + "Status Code: " + response.getStatusCode());
            response.getBody().prettyPrint();
            Assert.assertEquals(response.statusCode(), 400);
        } catch (Exception e) {
            System.out.println("Can not insert String value into CivilStatusId - type: int");
            throw new SkipException("Can not insert String value into CivilStatusId - type: int");
        }
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-248 [R1] [CRM_API] Verify the POST - create_customer_basic_info with null Customer Type Code")
    public void testPostCustomerWithNullCustomerTypeCode() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setCustomerTypeCode(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer type related data for customer type code -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-249 [R1] [CRM_API] Verify the POST - create_customer_basic_info with invalid Customer Type Code")
    public void testPostCustomerWithInvalidCustomerTypeCode() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        String invalidCustomerTypeCode = "XX";
        createCustomerBasicInfo.setCustomerTypeCode(invalidCustomerTypeCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-250 [R1] [CRM_API] Verify the POST - create_customer_basic_info with invalid format Customer Type Code")
    public void testPostCustomerWithInvalidFormatCustomerTypeCode() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        String invalidCustomerTypeCode = "9999";
        createCustomerBasicInfo.setCustomerTypeCode(invalidCustomerTypeCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-251 [R1] [CRM_API] Validate the process of creating a customer with null ‘policyholderPreferredLanguage’")
    public void testPostCustomerWithNullPolicyholderPreferredLanguage() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setPolicyholderPreferredLanguage(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-253 [R1] [CRM_API] Verify the POST - create_customer_basic_info with null First Name")
    public void testPostCustomerWithNullFirstName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setFirstName(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-254 [R1] [CRM_API] Verify the POST - create_customer_basic_info with null Middle Name")
    public void testPostCustomerWithNullMiddleName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-256 [R1] [CRM_API] Validate the process of creating a customer with Invalid ‘policyholderPreferredLanguage’")
    public void testPostCustomerWithInvalidPolicyholderPreferredLanguage() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        String invalidPolicyholderPreferredLanguage = "EEE";
        createCustomerBasicInfo.setPolicyholderPreferredLanguage(invalidPolicyholderPreferredLanguage);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-258 [R1] [CRM_API] Verify the POST - create_customer_basic_info with null Last Name")
    public void testPostCustomerWithNullLastName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setLastName(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-259 [R1] [CRM_API] Verify the POST - create_customer_basic_info with null Calling Name")
    public void testPostCustomerWithNullCallingName() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setCallingName(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FIELD_CANNOT_BE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "callingName field cannot null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-262 [R1] [CRM_API] Verify the POST - create_customer_basic_info with null Surname with Initials")
    public void testPostCustomerWithNullSurnameWithInitials() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setSurnameWithInitials(null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-263 [R1] [CRM_API] Validate the process of creating a customer with Null ID Type and ID number")
    public void testPostCustomerWithNullIdTypeAndIdNumber() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, null, relationshipTypeId, null, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-264 [R1] [CRM_API] Validate the process of creating a customer with Null ID Type and Valid ID number")
    public void testPostCustomerWithNullIdTypeAndValidIdNumber() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, null, relationshipTypeId, idNumber, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-265 [R1] [CRM_API] Validate the process of creating a customer with ID Type and Null ID number")
    public void testPostCustomerWithValidIdTypeAndNullIdNumber() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, null, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400, 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-268 [R1] [CRM_API] Validate the process of creating a customer with Invalid ID type with Valid ID number")
    public void testPostCustomerWithInvalidIdTypeAndValidIdNumber() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, "9999", relationshipTypeId, idNumber, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-267 [CRM_API] Verify the POST - create_customer_basic_info with null input in salutationOther")
    public void testPostCustomerWithNullSalutationOther() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-271 [R1] [CRM_API] Validate the process of creating a customers with Valid ID types")
    public void testPostCustomerWithValidIdTypes() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, "2", relationshipTypeId, idNumber, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-273 [R1] [CRM_API] Verify the POST - create_customer_basic_info with null input in gender")
    public void testPostCustomerWithNullGender() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setGender(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-274 [CRM_API] Verify the POST - create_customer_basic_info with invalid data in gender")
    public void testPostCustomerWithInvalidGender() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setGender("EE");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-275 [R1] [CRM_API] Validate the process of creating a customer with Null relationshipTypeId and Null customerId")
    public void testPostCustomerWithNullRelationshipIdAndCustomerId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, null, idNumber, null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_LINK_PARTY_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no link party type related data for link party type id -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-276 [CRM_API] Verify the POST - create_customer_basic_info with invalid format in gender")
    public void testPostCustomerWithInvalidDataFormatInGender() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setGender("1");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-277 [R1] [CRM_API] Validate the process of creating a customer with Null relationshipTypeId and customerId:")
    public void testPostCustomerWithNullRelationshipIdAndValidCustomerId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, null, idNumber, customerId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_LINK_PARTY_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no link party type related data for link party type id -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-278 [R1] [CRM_API] Validate the process of creating a customer with relationshipTypeId and Null customerId")
    public void testPostCustomerWithValidRelationshipIdAndNullCustomerId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, null);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
//
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-279 [R1] [CRM_API] Validate the process of creating a customer with invalid relationshipTypeId and invalid customerId:")
    public void testPostCustomerWithInvalidRelationshipIdAndCustomerId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, 99999, idNumber, 999999);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_LINK_PARTY_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no link party type related data for link party type id -99999", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-280 [CRM_API] Verify the POST - create_customer_basic_info with m,f,o inputs in gender")
    public void testPostCustomerWithGenderAsF() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setGender("F");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-280 [CRM_API] Verify the POST - create_customer_basic_info with m,f,o inputs in gender")
    public void testPostCustomerWithGenderAsO() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setGender("O");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-281 [R1] [CRM_API] Validate the process of creating a customer with invalid relationshipTypeId and valid customerId:")
    public void testPostCustomerWithInvalidRelationshipIdAndValidCustomerId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, 99999, idNumber, customerId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_LINK_PARTY_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no link party type related data for link party type id -99999", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-282 [R1] [CRM_API] Validate the process of creating a customer with valid relationshipTypeId and invalid customerId:")
    public void testPostCustomerWithValidRelationshipIdAndInvalidCustomerId() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, 999999);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer related data for the given customer id - 999999", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-283 [CRM_API] Verify the POST - create_customer_basic_info with null input in uwCategories")
    public void testPostCustomerWithNullUwCategories() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, null, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NATURE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for underwriting category id -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-284 [R1] [CRM_API] Validate the process of creating a customer with null customerPortalAccess")
    public void testPostCustomerWithNullCustomerPortalAccess() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setCustomerPortalAccess(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400, 406);
    }

    /*
    //Can not insert String or Integer value into CustomerPortalAccess - type: boolean
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-285 [R1] [CRM_API] Validate the process of creating a customer with invalid ‘customerPortalAccess’")
    public void testPostCustomerWithInvalidCustomerPortalAccess() {
        System.out.println("Can not insert String or Integer value into CustomerPortalAccess - type: boolean");
        throw new SkipException("Can not insert String or Integer value into CustomerPortalAccess - type: boolean");
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-288 [R1] [CRM_API] Validate the process of creating a customer with ‘customerPortalAccess’")
    public void testPostCustomerWithValidCustomerPortalAccess() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setCustomerPortalAccess(false);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-287 [CRM_API] Verify the POST - create_customer_basic_info with invalid data in uwCategories")
    public void testPostCustomerWithInvalidUwCategories() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, 999999, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NATURE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for underwriting category id -999999", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-291 [CRM_API] Verify the POST - create_customer_basic_info with null input in customerGroups")
    public void testPostCustomerWithNullCustomerGroups() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, null, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_GROUP_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer group related data for customer group id -null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-292 [CRM_API] Verify the POST - create_customer_basic_info with invalid data in customerGroups")
    public void testPostCustomerWithInvalidCustomerGroups() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, 999999, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_CUSTOMER_GROUP_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer group related data for customer group id -999999", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-486 [CRM_API] Verify the POST - create_customer_basic_info with null input in nationality")
    public void testPostCustomerWithNullNationality() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setNationality(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-487 [CRM_API] Verify the POST - create_customer_basic_info with invalid data in nationality")
    public void testPostCustomerWithInvalidNationality() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setNationality("XX");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-488 [CRM_API] Verify the POST - create_customer_basic_info with invalid format in nationality")
    public void testPostCustomerWithInvalidNationalityFormat() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setNationality("Sri Lankan");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-489 [CRM_API] Verify the POST - create_customer_basic_info with valid input in nationality")
    public void testPostCustomerWithValidNationality() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-297 [CRM_API] Verify the POST - create_customer_basic_info with null input in dob")
    public void testPostCustomerWithNullDob() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setDob(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-298 [CRM_API] Verify the POST - create_customer_basic_info with invalid data in dob")
    public void testPostCustomerWithInvalidDob() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setDob("April 3rd 1994");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-300 [CRM_API] Verify the POST - create_customer_basic_info with invalid format in dob")
    public void testPostCustomerWithInvalidDobFormat() {
        CustomerBasicInfo createCustomerBasicInfo = requestPayload.createCustomerBasicInfo(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, relationshipTypeId, idNumber, customerId);
        createCustomerBasicInfo.setDob("10-15-1994");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerBasicInfo)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
    }
}
