package tests.crm.taxesExcepted;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.TaxRegistration;
import com.informatics.pojos.crm.TaxesExcepted;
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

import java.util.Date;
import java.util.Map;

public class PostTaxesExcepted extends BaseTest {
    public String salutationCode, genderCode, nationality, preferLang, customerId;
    public Integer uwCategoryId, customerGroupId, idType, relationshipTypeId, civilStatusId, taxTypeId;
    public String idNumber = "1322323222";
    public String customerType = PropertiesUtils.INDIVIDUAL_CUS_TYPE;

////POST DELETED FROM SWAGGER
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

//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-1423 [R2][CRM_API] Verify the POST - taxes_excepted with valid payload ")
//    public void testPostTaxesExceptedWithValidPayload() {
//        String updateURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
//
//        Map createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .put(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 200);
//    }



//        SoftAssert softAssert = new SoftAssert();
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.customerId").toString()), customerId, "Invalid customerId");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.taxTypeId").toString()), String.valueOf(createTaxesExcepted.getTaxTypeId()), "Invalid taxTypeId");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.referenceNumber").toString()), createTaxesExcepted.getReferenceNumber(), "Invalid referenceNumber");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.from").toString().substring(0, 10)), String.valueOf(createTaxesExcepted.getFrom()), "Invalid From");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.to").toString().substring(0, 10)), String.valueOf(createTaxesExcepted.getTo()), "Invalid To");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.isApprovalRequired").toString()), String.valueOf(createTaxesExcepted.getIsApprovalRequired()), "Invalid IsApprovalRequired");
//        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result.isActive").toString()), String.valueOf(createTaxesExcepted.getIsActive()), "Invalid IsActive");
//
//        softAssert.assertAll();
 //   }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-1421 [R2][CRM_API] Verify the POST - taxes_excepted with empty payload")
//    public void testPostTaxesExceptedWithEmptyPayLoad() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setCustomerId(null);
//        createTaxesExcepted.setTaxTypeId(null);
//        createTaxesExcepted.setReferenceNumber(null);
//        createTaxesExcepted.setFrom(null);
//        createTaxesExcepted.setTo(null);
//        createTaxesExcepted.setIsApprovalRequired(null);
//        createTaxesExcepted.setIsActive(null);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-731 [R2][CRM_API] Verify the POST - taxes_excepted with null input in taxTypeId")
//    public void testPostTaxesExceptedWithNullTaxTypeId() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTaxTypeId(null);
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
////        SoftAssert softAssert = new SoftAssert();
////        String responseToString = response.asString();
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "_TAX_TYPE_NOT_FOUND", "Invalid errorCode");
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no tax type data found", "Invalid description");
////
////        softAssert.assertAll();
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-734 [R2][CRM_API] Verify the POST - taxes_excepted with null input in referenceNumber")
//    public void testPostTaxesExceptedWithNullReferenceNumber() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setReferenceNumber(null);
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
////        SoftAssert softAssert = new SoftAssert();
////        String responseToString = response.asString();
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "_TAX_REFERENCE_NOT_FOUND", "Invalid errorCode");
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no tax reference data found", "Invalid description");
////
////        softAssert.assertAll();
//    }
//
//    // Bug Id = 1436
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-736 [R2][CRM_API] Verify the POST - taxes_excepted with null input in from")
//    public void testPostTaxesExceptedWithNullFrom() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setFrom(null);
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
////        SoftAssert softAssert = new SoftAssert();
////        String responseToString = response.asString();
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "_TAX_FROM_NOT_FOUND", "Invalid errorCode");
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no tax from data found", "Invalid description");
////
////        softAssert.assertAll();
//    }
//
//    //Bug Id = 1436
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-740 [R2][CRM_API] Verify the POST - taxes_excepted with null input in to")
//    public void testPostTaxesExceptedWithNullTo() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTo(null);
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 200);
//
////        SoftAssert softAssert = new SoftAssert();
////        String responseToString = response.asString();
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "_TAX_FROM_NOT_FOUND", "Invalid errorCode");
////        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no tax from data found", "Invalid description");
////
////        softAssert.assertAll();
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-732 [R2][CRM_API] Verify the POST - taxes_excepted with invalid input in taxTypeId")
//    public void testPostTaxesExceptedWithInvalidTaxTypeId() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTaxTypeId(99999);
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 406);
//
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-737 [R2][CRM_API] Verify the POST - taxes_excepted with invalid input in from")
//    public void testPostTaxesExceptedWithInvalidFrom() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setFrom("April 3rd 1994");
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-741 [R2][CRM_API] Verify the POST - taxes_excepted with invalid input in to")
//    public void testPostTaxesExceptedWithInvalidTo() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTo("April 3rd 1994");
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-738 [R2][CRM_API] Verify the POST - taxes_excepted with invalid format in from")
//    public void testPostTaxesExceptedWithInvalidFormatFrom() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        //invalid format in from as "YYYY/DD/MM"
//        System.out.println("\n" + "--------------------invalid format in from as \"YYYY/DD/MM\"------------------- ");
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTo("2022/27/05");
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
//
//        //invalid format in from as "MM/DD/YYYY"
//        System.out.println("\n" + "--------------------invalid format in from as \"MM/DD/YYYY\"------------------- ");
//        TaxesExcepted createTaxesExceptedMonth = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTo("05/28/2022");
//        Response responseMonth = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
//
//        //invalid format in from as "DD/MM/YYYY"
//        System.out.println("\n" + "--------------------invalid format in from as \"DD/MM/YYYY\"------------------- ");
//        TaxesExcepted createTaxesExceptedDate = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTo("28/05/2022");
//        Response responseDate = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-742 [R2][CRM_API] Verify the POST - taxes_excepted with invalid format in to")
//    public void testPostTaxesExceptedWithInvalidFormatTo() {
//        String updateURI = APIConstants.CREATE_TAXES_EXCEPTED_ADDITION.replace("{customerId}", customerId);
//
//        //invalid format in from as "YYYY/DD/MM"
//        System.out.println("\n" + "--------------------invalid format in from as \"YYYY/DD/MM\"------------------- ");
//        TaxesExcepted createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTo("2022/27/05");
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
//
//        //invalid format in from as "MM/DD/YYYY"
//        System.out.println("\n" + "--------------------invalid format in from as \"MM/DD/YYYY\"------------------- ");
//        TaxesExcepted createTaxesExceptedMonth = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTo("05/28/2022");
//        Response responseMonth = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
//
//        //invalid format in from as "DD/MM/YYYY"
//        System.out.println("\n" + "--------------------invalid format in from as \"DD/MM/YYYY\"------------------- ");
//        TaxesExcepted createTaxesExceptedDate = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        createTaxesExcepted.setTo("05/28/2022");
//        Response responseDate = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .post(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 400);
//
//    }
}



