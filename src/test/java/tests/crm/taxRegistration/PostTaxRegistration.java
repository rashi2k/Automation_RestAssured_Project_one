package tests.crm.taxRegistration;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.TaxRegistration;
import com.informatics.utils.PropertiesUtils;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

public class PostTaxRegistration extends BaseTest {

    public String salutationCode, genderCode, nationality, preferLang, customerId;
    public Integer uwCategoryId, customerGroupId, idType, relationshipTypeId, civilStatusId, taxTypeId;
    public String idNumber = "1322323222";
    public String customerType = PropertiesUtils.INDIVIDUAL_CUS_TYPE;

    //POST DELETED FROM SWAGGER
//    @BeforeMethod
//    public void initiate() {
//        //get taxTypeId - customerId
//        Response response = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_TAX);
//        taxTypeId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].taxTypeId").toString());
//
//        //get cus title
//        Response responseA = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_TITLE);
//        salutationCode = JsonPath.read(responseA.asString(), "$.result[0].salutationCode").toString();
//
//        //get cus gender
//        Response responseB = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_GENDER);
//        genderCode = JsonPath.read(responseB.asString(), "$.result[1].genderCode").toString();
//
//        //get cus nationality
//        Response responseC = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_NATIONALITY);
//        nationality = JsonPath.read(responseC.asString(), "$.result[0].nationality").toString();
//
//        //get cus civil status
//        Response responseD = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_CIVIL_STATUS);
//        civilStatusId = Integer.parseInt(JsonPath.read(responseD.asString(), "$.result[0].civilStatusId").toString());
//
//        //get cus prefered language
//        Response responseE = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_PREF_LANG);
//        preferLang = JsonPath.read(responseE.asString(), "$.result[0].iso3Code").toString();
//
//        //get cus id type
//        Response responseF = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_ID_TYPE);
//        idType = Integer.parseInt(JsonPath.read(responseF.asString(), "$.result[0].identityTypeCode").toString());
//
//        //get cus relationship type
//        Response responseG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_RELATIONSHIP_TYPE);
//        relationshipTypeId = Integer.parseInt(JsonPath.read(responseG.asString(), "$.result[0].id").toString());
//
//        //get customer group ID
//        Response responseCG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_ALL_CUSTOMER_GROUPS);
//        customerGroupId = Integer.parseInt(JsonPath.read(responseCG.asString(), "$.result[0].id").toString());
//
//        //get uw category  ID
//        Response responseUG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_UW_CATEGORIES);
//        uwCategoryId = Integer.parseInt(JsonPath.read(responseUG.asString(), "$.result[0].id").toString());
//
//        //get cus type
//        Response responseCT = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_TYPE);
//        customerType = JsonPath.read(responseCT.asString(), "$.result[0].typeCode").toString();
//
//        //create test customer to get cusID
//        CustomerBasicInfo createTestCustomer = requestPayload.createTestCustomer(customerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, idNumber);
//        Response responseCI = RestAssured.given().spec(repoSpec).when()
//                .body(createTestCustomer)
//                .post(APIConstants.CREATE_CUS_BASIC_INFO);
//        customerId = JsonPath.read(responseCI.asString(), "$.result.customerId").toString();
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-588 [R2] [CRM_API] [Post] tax registration with valid payload")
//    public void testPostTaxRegistrationWithValidPayLoad() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 201);
//
//        SoftAssert softAssert = new SoftAssert();
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.referenceNumber").toString()), createTaxRegistration.getReferenceNumber(), "Invalid referenceNumber");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.taxTypeId").toString()), String.valueOf(createTaxRegistration.getTaxTypeId()), "Invalid taxTypeId");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.isActive").toString()), String.valueOf(createTaxRegistration.getIsActive()), "Invalid isActive");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.customerId").toString()), customerId, "Invalid customerId");
//        softAssert.assertAll();
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-590 [R2] [CRM_API] [POST] tax registration with is active as false , true and null and valid taxTypeid , valid referenceNumber")
//    public void testPostTaxRegistrationWithIsActiveFalseTrueAndNull() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        //isActive as True
//        System.out.println("\n" + "--------------------isActive as true------------------- ");
//        TaxRegistration createTaxRegistrationTrue = requestPayload.newTaxRegistration(taxTypeId);
//        Response responseTrue = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistrationTrue)
//                .post(updateURI);
//        System.out.println("Status Code: " + responseTrue.getStatusCode());
//        responseTrue.getBody().prettyPrint();
//        Assert.assertEquals(responseTrue.statusCode(), 201);
//
//        SoftAssert softAssert = new SoftAssert();
//        softAssert.assertEquals((JsonPath.read(responseTrue.asString(), "$.result.referenceNumber").toString()), createTaxRegistrationTrue.getReferenceNumber(), "Invalid referenceNumber");
//        softAssert.assertEquals((JsonPath.read(responseTrue.asString(), "$.result.taxTypeId").toString()), String.valueOf(createTaxRegistrationTrue.getTaxTypeId()), "Invalid taxTypeId");
//        softAssert.assertEquals((JsonPath.read(responseTrue.asString(), "$.result.isActive").toString()), String.valueOf(createTaxRegistrationTrue.getIsActive()), "Invalid isActive");
//        softAssert.assertEquals((JsonPath.read(responseTrue.asString(), "$.result.customerId").toString()), customerId, "Invalid customerId");
//        softAssert.assertAll();
//
//        //isActive as False
//        System.out.println("\n" + "--------------------isActive as false------------------- ");
//        TaxRegistration createTaxRegistrationFalse = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistrationFalse.setIsActive(false);
//        Response responseFalse = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistrationFalse)
//                .post(updateURI);
//        System.out.println("Status Code: " + responseFalse.getStatusCode());
//        responseFalse.getBody().prettyPrint();
//        Assert.assertEquals(responseFalse.statusCode(), 201);
//
//        softAssert.assertEquals((JsonPath.read(responseFalse.asString(), "$.result.referenceNumber").toString()), createTaxRegistrationFalse.getReferenceNumber(), "Invalid referenceNumber");
//        softAssert.assertEquals((JsonPath.read(responseFalse.asString(), "$.result.taxTypeId").toString()), String.valueOf(createTaxRegistrationFalse.getTaxTypeId()), "Invalid taxTypeId");
//        softAssert.assertEquals((JsonPath.read(responseFalse.asString(), "$.result.isActive").toString()), String.valueOf(createTaxRegistrationFalse.getIsActive()), "Invalid isActive");
//        softAssert.assertEquals((JsonPath.read(responseFalse.asString(), "$.result.customerId").toString()), customerId, "Invalid customerId");
//        softAssert.assertAll();
//
//        //isActive as null
//        System.out.println("\n" + "--------------------isActive as null------------------- ");
//        TaxRegistration createTaxRegistrationNull = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistrationFalse.setIsActive(null);
//        Response responseNull = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistrationFalse)
//                .post(updateURI);
//        System.out.println("Status Code: " + responseNull.getStatusCode());
//        responseNull.getBody().prettyPrint();
//        Assert.assertEquals(responseNull.statusCode(), 400);
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-598 [R2] [CRM_API] [Post] tax_registration by empty payload")
//    public void testPostTaxRegistrationWithEmptyPayLoad() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistration.setTaxTypeId(null);
//        createTaxRegistration.setReferenceNumber(null);
//        createTaxRegistration.setIsActive(null);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 500);
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-601 [R2] [CRM_API] [Post] tax_registration with invalid taxTypeId and valid is active , valid reference number")
//    public void testPostTaxRegistrationWithInvalidTaxTypeId() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistration.setTaxTypeId(99999);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//    }
//
//    /*
//    //IGNORED - can not insert integer to referenceNumber - type: String
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-612 [R2] [CRM_API] [Post] tax_registration with valid taxTypeId , valid is Active and not existing referenceNumber")
//    public void testPostTaxRegistrationWithNonExistingReferenceNumber() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistration.setReferenceNumber(00099771);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//    }
//     */
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-629 [R2] [CRM_API] [Post] tax_registration with empty taxTypeId and valid referenceNumber, valid is Active")
//    public void testPostTaxRegistrationWithEmptyTaxTypeId() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistration.setTaxTypeId(null);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 500);
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-636 [R2] [CRM_API] [Post] tax_registration with valid taxTypeId , valid is Active and empty referenceNumber")
//    public void testPostTaxRegistrationWithEmptyReferenceNumber() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistration.setReferenceNumber(null);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 406);
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-593 [R2] [CRM_API] [Post] tax registration with valid taxTypeId , valid referenceNumber and is active null")
//    public void testPostTaxRegistrationWithNullIsActiveNull() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistration.setIsActive(null);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 406);
//    }
//
//    /*
//    //can not insert string to taxTypeId - type: int
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-688 [R2] [CRM_API] [Post] tax_registration with taxTypeId invalid format and valid is Active , valid referenceNumber")
//    public void testPostTaxRegistrationWithInvalidTaxTypeIdFormat() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistration.setTaxTypeId(AA001);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//    }
//
//    //can not insert integer to referenceNumber - type: String
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-698 [R2] [CRM_API] [Post] tax_registration with invalid referenceNumber Format and valid is Active, valid taxTypeId")
//    public void testPostTaxRegistrationWithInvalidReferenceNumberFormat() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistration.setReferenceNumber(123);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistration)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//    }
//     */
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-1361 [R2] [CRM_API] [POST] tax registration Verifying referenceNumber for character length 30[")
//    public void testPostTaxRegistrationVerifyingReferenceNumberCharacterLength() {
//        String updateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);
//
//        //exactly 30 characters
//        System.out.println("\n" + "--------------------Exactly 30 characters for referenceNumber------------------- ");
//        TaxRegistration createTaxRegistrationTrue = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistrationTrue.setReferenceNumber("VAT000431234567891234567891234");
//
//        Response responseTrue = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistrationTrue)
//                .post(updateURI);
//        System.out.println("Status Code: " + responseTrue.getStatusCode());
//        responseTrue.getBody().prettyPrint();
//        Assert.assertEquals(responseTrue.statusCode(), 201);
//
//        SoftAssert softAssert = new SoftAssert();
//        softAssert.assertEquals((JsonPath.read(responseTrue.asString(), "$.result.referenceNumber").toString()), createTaxRegistrationTrue.getReferenceNumber(), "Invalid referenceNumber");
//        softAssert.assertEquals((JsonPath.read(responseTrue.asString(), "$.result.taxTypeId").toString()), String.valueOf(createTaxRegistrationTrue.getTaxTypeId()), "Invalid taxTypeId");
//        softAssert.assertEquals((JsonPath.read(responseTrue.asString(), "$.result.isActive").toString()), String.valueOf(createTaxRegistrationTrue.getIsActive()), "Invalid isActive");
//        softAssert.assertEquals((JsonPath.read(responseTrue.asString(), "$.result.customerId").toString()), customerId, "Invalid customerId");
//        softAssert.assertAll();
//
//        //more than 30 characters
//        System.out.println("\n" + "--------------------More than 30 characters for referenceNumber------------------- ");
//        TaxRegistration createTaxRegistrationFalse = requestPayload.newTaxRegistration(taxTypeId);
//        createTaxRegistrationFalse.setReferenceNumber("VAT00043123456789123456789123467");
//
//        Response responseFalse = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxRegistrationFalse)
//                .post(updateURI);
//        System.out.println("Status Code: " + responseFalse.getStatusCode());
//        responseFalse.getBody().prettyPrint();
//        Assert.assertEquals(responseFalse.statusCode(), 400);
//    }
}
