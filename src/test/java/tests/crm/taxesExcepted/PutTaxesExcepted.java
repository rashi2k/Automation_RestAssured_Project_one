package tests.crm.taxesExcepted;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.CustomerDocuments;
import com.informatics.pojos.admin.Tax;
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

public class PutTaxesExcepted extends BaseTest
{
    public String customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseTaxInfo();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1968 [R4][CRM_API] Verify the PUT - taxes_excepted with valid payload  ")
    public void testPutTaxesExceptedWithValidPayload() {

        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), String.valueOf(updateTaxesExcepted.getId()), "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].customerId").toString()),String.valueOf( updateTaxesExcepted.getCustomerId()), "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].taxTypeId").toString()), String.valueOf(updateTaxesExcepted.getTaxTypeId()), "Invalid taxTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].referenceNumber").toString()),updateTaxesExcepted.getReferenceNumber(), "Invalid referenceNumber");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].from").toString().substring(0,10)), String.valueOf(updateTaxesExcepted.getFrom()), "Invalid From");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].to").toString().substring(0,10)), String.valueOf(updateTaxesExcepted.getTo()), "Invalid To");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isApprovalRequired").toString()), String.valueOf(updateTaxesExcepted.getIsApprovalRequired()), "Invalid IsApprovalRequired");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isActive").toString()), String.valueOf(updateTaxesExcepted.getIsActive()), "Invalid IsActive");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1969 [R4][CRM_API] Verify the PUT - taxes_excepted with empty payload")
    public void testPutTaxesExceptedWithEmptyPayload() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1970 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid input in id")
    public void testPutTaxesExceptedWithInvalidId() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

       TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(99999999);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }

    // id can be null and it creat taxes excepted
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1971 [R4][CRM_API] Verify the PUT - taxes_excepted with null input in id")
    public void testPutTaxesExceptedWithNullId() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(null);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 200);
    }

    // invalid customerId will covert to url customerId
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1972 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid input in customerId")
    public void testPutTaxesExceptedWithInvalidCustomerId() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

      Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
      TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setCustomerId(99999999);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1973 [R4][CRM_API] Verify the PUT - taxes_excepted with null input in customerId")
    public void testPutTaxesExceptedWithNullCustomerId() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setCustomerId(null);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1974 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid input in customerId")
    public void testPutTaxesExceptedWithInvalidCustomerIds()
    {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String invalidId = "999999";
        String updatedPutURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", invalidId);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedPutURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1975 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid input in taxTypeId")
    public void testPutTaxesExceptedWithInvalidTaxTypeId() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setTaxTypeId(99999999);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1976 [R4][CRM_API] Verify the PUT - taxes_excepted with null input in taxTypeId")
    public void testPutTaxesExceptedWithNullTaxTypeId() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setTaxTypeId(null);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }
                // Can't do automation
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-1977 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid input in referenceNumber")
//    public void testPutTaxesExceptedWithInvalidReferenceNumber()
//    {
//        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
//        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
//
//        List<TaxesExcepted> added = new ArrayList<>();
//        added.add(taxesExcepted);
//        List<TaxesExcepted> deletedIds = new ArrayList<>();
//
//        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
//        taxesExceptedMapId.put("exTaxAdd", added);
//        taxesExceptedMapId.put("exTaxDelete", deletedIds);
//
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(taxesExceptedMapId)
//                .put(updatedURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 200);
//
//        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
//        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
//        updateTaxesExcepted.setId(taxesExceptedId);
//        updateTaxesExcepted.setReferenceNumber("99999999");
//
//        List<TaxesExcepted> addedUpdate = new ArrayList<>();
//        addedUpdate.add(updateTaxesExcepted);
//
//        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
//        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
//        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);
//
//        Response responseUpdate = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(taxesExceptedMapIdUpdate)
//                .put(updatedURI);
//        System.out.println("Status Code: " + responseUpdate.getStatusCode());
//        responseUpdate.getBody().prettyPrint();
//        Assert.assertEquals(responseUpdate.statusCode(), 400);
//    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1978 [R4][CRM_API] Verify the PUT - taxes_excepted with null input in referenceNumber")
    public void testPutTaxesExceptedWithNullReferenceNumber() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setReferenceNumber(null);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1979 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid input in from")
    public void testPutTaxesExceptedWithInvalidFrom() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setFrom("99999999");

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1980 [R4][CRM_API] Verify the PUT - taxes_excepted with null input in from")
    public void testPutTaxesExceptedWithNullFrom() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setFrom(null);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1981 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid input in to")
    public void testPutTaxesExceptedWithInvalidTo() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setTo("99999999");

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1982 [R4][CRM_API] Verify the PUT - taxes_excepted with null input in to")
    public void testPutTaxesExceptedWithNullTo() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setTo(null);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1984 [R4][CRM_API] Verify the PUT - taxes_excepted with null input in isApprovalRequired")
    public void testPutTaxesExceptedWithNullIsApprovalRequired() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setIsApprovalRequired(null);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1986 [R4][CRM_API] Verify the PUT - taxes_excepted with null input in isActive")
    public void testPutTaxesExceptedWithNullIsActive() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setIsActive(null);

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1990 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid format in from")
    public void testPutTaxesExceptedWithInvalidFormatFrom() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());

        //invalid format in from as "YYYY/DD/MM"
        System.out.println("\n" + "--------------------invalid format in from as \"YYYY/DD/MM\"------------------- ");
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setFrom("2021/28/06");

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);

        //invalid format in from as "MM/DD/YYYY"
        System.out.println("\n" + "--------------------invalid format in from as \"MM/DD/YYYY\"------------------- ");
        TaxesExcepted updateTaxesExceptedMonth = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExceptedMonth.setId(taxesExceptedId);
        updateTaxesExceptedMonth.setFrom("06/28/2022");

        List<TaxesExcepted> addedUpdateMonth = new ArrayList<>();
        addedUpdateMonth.add(updateTaxesExceptedMonth);

        Map<String, Object>  taxesExceptedMapIdUpdateMonth = new HashMap<>();
        taxesExceptedMapIdUpdateMonth.put("exTaxAdd", addedUpdateMonth);
        taxesExceptedMapIdUpdateMonth.put("exTaxDelete", deletedIds);

        Response responseUpdateMonth = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdateMonth)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdateMonth.getStatusCode());
        responseUpdateMonth.getBody().prettyPrint();
        Assert.assertEquals(responseUpdateMonth.statusCode(), 400);

        //invalid format in from as "DD/MM/YYYY"
        System.out.println("\n" + "--------------------invalid format in from as \"DD/MM/YYYY\"------------------- ");
        TaxesExcepted updateTaxesExceptedDate = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExceptedDate.setId(taxesExceptedId);
        updateTaxesExceptedDate.setFrom("28/06/2020");

        List<TaxesExcepted> addedUpdateDate = new ArrayList<>();
        addedUpdateDate.add(updateTaxesExceptedDate);

        Map<String, Object>  taxesExceptedMapIdUpdateDate = new HashMap<>();
        taxesExceptedMapIdUpdateDate.put("exTaxAdd", addedUpdateDate);
        taxesExceptedMapIdUpdateDate.put("exTaxDelete", deletedIds);

        Response responseUpdateDate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdateDate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdateDate.getStatusCode());
        responseUpdateDate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdateDate.statusCode(), 400);

    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-753 [R4][CRM_API] Verify the PUT - taxes_excepted with invalid format in to")
    public void testPutTaxesExceptedWithInvalidFormatTo() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object>  taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer taxesExceptedId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());

        //invalid format in from as "YYYY/DD/MM"
        System.out.println("\n" + "--------------------invalid format in from as \"YYYY/DD/MM\"------------------- ");
        TaxesExcepted updateTaxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExcepted.setId(taxesExceptedId);
        updateTaxesExcepted.setTo("2023/28/06");

        List<TaxesExcepted> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxesExcepted);

        Map<String, Object>  taxesExceptedMapIdUpdate = new HashMap<>();
        taxesExceptedMapIdUpdate.put("exTaxAdd", addedUpdate);
        taxesExceptedMapIdUpdate.put("exTaxDelete", deletedIds);

        Response responseUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdate.getStatusCode());
        responseUpdate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdate.statusCode(), 400);

        //invalid format in from as "MM/DD/YYYY"
        System.out.println("\n" + "--------------------invalid format in from as \"MM/DD/YYYY\"------------------- ");
        TaxesExcepted updateTaxesExceptedMonth = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExceptedMonth.setId(taxesExceptedId);
        updateTaxesExceptedMonth.setTo("06/28/2024");

        List<TaxesExcepted> addedUpdateMonth = new ArrayList<>();
        addedUpdateMonth.add(updateTaxesExceptedMonth);

        Map<String, Object>  taxesExceptedMapIdUpdateMonth = new HashMap<>();
        taxesExceptedMapIdUpdateMonth.put("exTaxAdd", addedUpdateMonth);
        taxesExceptedMapIdUpdateMonth.put("exTaxDelete", deletedIds);

        Response responseUpdateMonth = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdateMonth)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdateMonth.getStatusCode());
        responseUpdateMonth.getBody().prettyPrint();
        Assert.assertEquals(responseUpdateMonth.statusCode(), 400);

        //invalid format in from as "DD/MM/YYYY"
        System.out.println("\n" + "--------------------invalid format in from as \"DD/MM/YYYY\"------------------- ");
        TaxesExcepted updateTaxesExceptedDate = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);
        updateTaxesExceptedDate.setId(taxesExceptedId);
        updateTaxesExceptedDate.setTo("28/06/2025");

        List<TaxesExcepted> addedUpdateDate = new ArrayList<>();
        addedUpdateDate.add(updateTaxesExceptedDate);

        Map<String, Object>  taxesExceptedMapIdUpdateDate = new HashMap<>();
        taxesExceptedMapIdUpdateDate.put("exTaxAdd", addedUpdateDate);
        taxesExceptedMapIdUpdateDate.put("exTaxDelete", deletedIds);

        Response responseUpdateDate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapIdUpdateDate)
                .put(updatedURI);
        System.out.println("Status Code: " + responseUpdateDate.getStatusCode());
        responseUpdateDate.getBody().prettyPrint();
        Assert.assertEquals(responseUpdateDate.statusCode(), 400);

    }

    }

