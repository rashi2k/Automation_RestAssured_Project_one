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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTaxRegistration extends BaseTest {

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
    @Description("IQ-585 [R2] [CRM_API] [Get] tax registration with valid payload")
    public void testGetTaxRegistrationWithValidPayLoad() {
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
        String updateGetURI = APIConstants.GET_TAX_REGISTRATION_CUS_ID.replace("{id}", customerId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isTaxRegistrationFound= false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String id = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (id.equals(taxRegistrationId)) {
                isTaxRegistrationFound = true;
                softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result[" + i + "].id").toString()), taxRegistrationId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result[" + i + "].referenceNumber").toString()), createTaxRegistration.getReferenceNumber(), "Invalid referenceNumber");
                softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result[" + i + "].taxTypeId").toString()), String.valueOf(createTaxRegistration.getTaxTypeId()), "Invalid taxTypeId");
                softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result[" + i + "].isActive").toString()), String.valueOf(createTaxRegistration.getIsActive()), "Invalid isActive");
                softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result[" + i + "].customerId").toString()), customerId, "Invalid customerId");
                break;
            }
        }
        softAssert.assertTrue(isTaxRegistrationFound, "Tax Registration not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-594 [R2] [CRM_API] [Get] tax_registration by Id with valid Id")
    public void testGetTaxRegistrationWithValidId() {
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
        String updateGetURI = APIConstants.GET_TAX_REGISTRATION_ID.replace("{id}", taxRegistrationId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.id").toString()), taxRegistrationId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.referenceNumber").toString()), createTaxRegistration.getReferenceNumber(), "Invalid referenceNumber");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.taxTypeId").toString()), String.valueOf(createTaxRegistration.getTaxTypeId()), "Invalid taxTypeId");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.isActive").toString()), String.valueOf(createTaxRegistration.getIsActive()), "Invalid isActive");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.customerId").toString()), customerId, "Invalid customerId");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-595 [R2] [CRM_API] [Get] tax registration by id with invalid Id")
    public void testGetTaxRegistrationWithInvalidId() {
        String invalidId = "99999";
        String updateGetURI = APIConstants.GET_TAX_REGISTRATION_ID.replace("{id}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.errorCode").toString()), "CM_M_CUSTOMER_TAX_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.description").toString()), "There is no customer tax related data for the given customer tax id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-596 [R2] [CRM_API] [Get] tax registration by Id with empty Id")
    public void testGetTaxRegistrationWithEmptyId() {
        String updateGetURI = APIConstants.GET_TAX_REGISTRATION_ID.replace("{id}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }
}
