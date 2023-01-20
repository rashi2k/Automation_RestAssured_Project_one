package tests.crm.taxRegistration;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

import java.security.UnresolvedPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PutTaxRegistration extends BaseTest {

    public String customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseTaxInfo();
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
    @Description("IQ-709 [R2] [CRM_API] [Put] tax_registration with valid payload")
    public void testPutTaxRegistrationWithValidPayLoad() {
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{customerId}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        List<TaxRegistration> added = new ArrayList<>();
        added.add(createTaxRegistration);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxRegistrationMapId = new HashMap<>();
        taxRegistrationMapId.put("taxAdd", added);
        taxRegistrationMapId.put("taxDelete", deletedIds);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapId)
                .put(updateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result[0].id").toString();

        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        updateTaxRegistration.setId(Integer.parseInt(taxRegistrationId));

        List<TaxRegistration> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxRegistration);

        Map<String, Object> taxRegistrationMapIdUpdate = new HashMap<>();
        taxRegistrationMapIdUpdate.put("taxAdd", addedUpdate);
        taxRegistrationMapIdUpdate.put("taxDelete", deletedIds);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapIdUpdate)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].id").toString()), taxRegistrationId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].referenceNumber").toString()), updateTaxRegistration.getReferenceNumber(), "Invalid referenceNumber");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].taxTypeId").toString()), String.valueOf(updateTaxRegistration.getTaxTypeId()), "Invalid taxTypeId");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].isActive").toString()), String.valueOf(updateTaxRegistration.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].customerId").toString()), customerId, "Invalid customerId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-640 [R2] [CRM_API] [Put] tax_registration with empty taxTypeId and valid is Active, valid referenceNumber")
    public void testPutTaxRegistrationWithEmptyTaxTypeId() {
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{customerId}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        List<TaxRegistration> added = new ArrayList<>();
        added.add(createTaxRegistration);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxRegistrationMapId = new HashMap<>();
        taxRegistrationMapId.put("taxAdd", added);
        taxRegistrationMapId.put("taxDelete", deletedIds);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapId)
                .put(updateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result[0].id").toString();

        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        updateTaxRegistration.setId(Integer.parseInt(taxRegistrationId));
        updateTaxRegistration.setTaxTypeId(null);

        List<TaxRegistration> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxRegistration);

        Map<String, Object> taxRegistrationMapIdUpdate = new HashMap<>();
        taxRegistrationMapIdUpdate.put("taxAdd", addedUpdate);
        taxRegistrationMapIdUpdate.put("taxDelete", deletedIds);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapIdUpdate)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 500);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-650 [R2] [CRM_API] [Put] tax_registration with valid taxTypeId , valid is Active and null reference number")
    public void testPutTaxRegistrationWithNullReferenceNumber() {
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{customerId}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        List<TaxRegistration> added = new ArrayList<>();
        added.add(createTaxRegistration);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxRegistrationMapId = new HashMap<>();
        taxRegistrationMapId.put("taxAdd", added);
        taxRegistrationMapId.put("taxDelete", deletedIds);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapId)
                .put(updateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result[0].id").toString();

        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        updateTaxRegistration.setId(Integer.parseInt(taxRegistrationId));
        updateTaxRegistration.setReferenceNumber(null);

        List<TaxRegistration> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxRegistration);

        Map<String, Object> taxRegistrationMapIdUpdate = new HashMap<>();
        taxRegistrationMapIdUpdate.put("taxAdd", addedUpdate);
        taxRegistrationMapIdUpdate.put("taxDelete", deletedIds);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapIdUpdate)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    /*
    //IGNORED - can not insert integer to referenceNumber - type: String
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-700 [R2] [CRM_API] [Put] tax_registration with invalid referenceNumber format and valid is Active , valid taxTypeId")
    public void testPutTaxRegistrationWithInvalidReferenceNumber() {
        String updateCreateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxRegistration)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{id}", taxRegistrationId);

        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
        updateTaxRegistration.setReferenceNumber(H000AALK);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateTaxRegistration)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-711 [R2] [CRM_API] [Put] tax_registration with valid referenceNumber, valid is Active and Invalid taxTypeId")
    public void testPutTaxRegistrationWithInvalidTaxTypeId() {
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{customerId}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        List<TaxRegistration> added = new ArrayList<>();
        added.add(createTaxRegistration);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxRegistrationMapId = new HashMap<>();
        taxRegistrationMapId.put("taxAdd", added);
        taxRegistrationMapId.put("taxDelete", deletedIds);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapId)
                .put(updateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result[0].id").toString();

        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        updateTaxRegistration.setId(Integer.parseInt(taxRegistrationId));
        updateTaxRegistration.setTaxTypeId(999999);

        List<TaxRegistration> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxRegistration);

        Map<String, Object> taxRegistrationMapIdUpdate = new HashMap<>();
        taxRegistrationMapIdUpdate.put("taxAdd", addedUpdate);
        taxRegistrationMapIdUpdate.put("taxDelete", deletedIds);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapIdUpdate)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-715 [R2] [CRM_API] [Put] tax_registration with valid referenceNumber, valid isActive and null taxTypeId")
    public void testPutTaxRegistrationWithNullTaxTypeId() {
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{customerId}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        List<TaxRegistration> added = new ArrayList<>();
        added.add(createTaxRegistration);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxRegistrationMapId = new HashMap<>();
        taxRegistrationMapId.put("taxAdd", added);
        taxRegistrationMapId.put("taxDelete", deletedIds);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapId)
                .put(updateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result[0].id").toString();

        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        updateTaxRegistration.setId(Integer.parseInt(taxRegistrationId));
        updateTaxRegistration.setTaxTypeId(null);

        List<TaxRegistration> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxRegistration);

        Map<String, Object> taxRegistrationMapIdUpdate = new HashMap<>();
        taxRegistrationMapIdUpdate.put("taxAdd", addedUpdate);
        taxRegistrationMapIdUpdate.put("taxDelete", deletedIds);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapIdUpdate)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 500);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-859 [R2] [CRM_API] [Put] tax registration with is active as null and valid taxTypeId , valid referenceNumber")
    public void testPutTaxRegistrationWithNullIsActive() {
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{customerId}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        List<TaxRegistration> added = new ArrayList<>();
        added.add(createTaxRegistration);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxRegistrationMapId = new HashMap<>();
        taxRegistrationMapId.put("taxAdd", added);
        taxRegistrationMapId.put("taxDelete", deletedIds);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapId)
                .put(updateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result[0].id").toString();

        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        updateTaxRegistration.setId(Integer.parseInt(taxRegistrationId));
        updateTaxRegistration.setIsActive(null);

        List<TaxRegistration> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxRegistration);

        Map<String, Object> taxRegistrationMapIdUpdate = new HashMap<>();
        taxRegistrationMapIdUpdate.put("taxAdd", addedUpdate);
        taxRegistrationMapIdUpdate.put("taxDelete", deletedIds);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapIdUpdate)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    /*
    //IGNORED - can not insert integer to referenceNumber - type: String
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-860 [R2] [CRM_API] [Put] tax registration with not existing referenceNumber and valid is active, valid taxTypeId")
    public void testPutTaxRegistrationWithNonExistingReferenceNumber() {
        String updateCreateURI = APIConstants.CREATE_TAX_REGISTRATION_ADDITION.replace("{id}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createTaxRegistration)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{id}", taxRegistrationId);

        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId);
        updateTaxRegistration.setReferenceNumber(0300);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateTaxRegistration)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1362 [R2] [CRM_API] [PUT] tax registration Verifying referenceNumber for character length 30")
    public void testPutTaxRegistrationVerifyingReferenceNumberCharacterLength() {
        String updateURI = APIConstants.UPDATE_TAX_REGISTRATION.replace("{customerId}", customerId);

        TaxRegistration createTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        List<TaxRegistration> added = new ArrayList<>();
        added.add(createTaxRegistration);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxRegistrationMapId = new HashMap<>();
        taxRegistrationMapId.put("taxAdd", added);
        taxRegistrationMapId.put("taxDelete", deletedIds);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapId)
                .put(updateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 200);

        String taxRegistrationId = JsonPath.read(resCreate.asString(), "$.result[0].id").toString();

        //exactly 30 characters
        System.out.println("\n" + "--------------------Exactly 30 characters for referenceNumber------------------- ");
        TaxRegistration updateTaxRegistration = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        updateTaxRegistration.setId(Integer.parseInt(taxRegistrationId));
        updateTaxRegistration.setReferenceNumber("VAT000431234567891234567891234");

        List<TaxRegistration> addedUpdate = new ArrayList<>();
        addedUpdate.add(updateTaxRegistration);

        Map<String, Object> taxRegistrationMapIdUpdate = new HashMap<>();
        taxRegistrationMapIdUpdate.put("taxAdd", addedUpdate);
        taxRegistrationMapIdUpdate.put("taxDelete", deletedIds);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapIdUpdate)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].id").toString()), taxRegistrationId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].referenceNumber").toString()), updateTaxRegistration.getReferenceNumber(), "Invalid referenceNumber");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].taxTypeId").toString()), String.valueOf(updateTaxRegistration.getTaxTypeId()), "Invalid taxTypeId");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].isActive").toString()), String.valueOf(updateTaxRegistration.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.result[0].customerId").toString()), customerId, "Invalid customerId");
        softAssert.assertAll();

        //more than 30 characters
        System.out.println("\n" + "--------------------More than 30 characters for referenceNumber------------------- ");
        TaxRegistration updateTaxRegistrationM = requestPayload.newTaxRegistration(taxTypeId, Integer.parseInt(customerId));
        updateTaxRegistrationM.setId(Integer.parseInt(taxRegistrationId));
        updateTaxRegistrationM.setReferenceNumber("VAT00043123456789123456789123467");

        List<TaxRegistration> addedUpdateM = new ArrayList<>();
        addedUpdateM.add(updateTaxRegistrationM);

        Map<String, Object> taxRegistrationMapIdUpdateM = new HashMap<>();
        taxRegistrationMapIdUpdateM.put("taxAdd", addedUpdateM);
        taxRegistrationMapIdUpdateM.put("taxDelete", deletedIds);

        Response resUpdateM = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxRegistrationMapIdUpdateM)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdateM.getStatusCode());
        resUpdateM.getBody().prettyPrint();
        Assert.assertEquals(resUpdateM.statusCode(), 400);
    }
}
