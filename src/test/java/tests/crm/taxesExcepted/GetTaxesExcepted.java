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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTaxesExcepted extends BaseTest {

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
    @Description("IQ-1946 [R4][CRM_API] Verify the GET - taxes_excepted with valid input in CustomerId")
    public void testGetTaxesExceptedWithValidCustomerId() {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String taxesExceptedId = JsonPath.read(response.asString(), "$.result[0].id").toString();

        String updateGetURI = APIConstants.GET_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isTaxesExcepted= false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String taxesExceptedIdGet = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (taxesExceptedIdGet.equals(taxesExceptedId)) {
                isTaxesExcepted = true;
               // softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), taxesExcepted.getId(), "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerId").toString()), customerId, "Invalid taxTypeCode");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].taxTypeId").toString()), String.valueOf(taxTypeId), "Invalid taxType");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].referenceNumber").toString()), taxesExcepted.getReferenceNumber(), "Invalid referenceNumber");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].from").toString().substring(0,10)), taxesExcepted.getFrom(), "Invalid From");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].to").toString().substring(0,10)), taxesExcepted.getTo(), "Invalid To");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isApprovalRequired").toString()), String.valueOf(taxesExcepted.getIsApprovalRequired()), "Invalid IsApprovalRequired");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(taxesExcepted.getIsActive()), "Invalid IsActive");
                break;
            }
        }
        softAssert.assertTrue(isTaxesExcepted, "Taxes Excepted not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1945 [R4][CRM_API] Verify the GET - taxes_excepted with invalid inputs in CustomerId")
    public void testGetTaxesExceptedWithInvalidCustomerId()
    {
        String invalidId = "Asa9999";
        String updateGetURI = APIConstants.GET_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", invalidId);
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1947 [R4][CRM_API] Verify the GET - taxes_excepted with null input in CustomerId")
    public void testGetTaxesExceptedWithNullCustomerId()
    {
        String updateGetURI = APIConstants.GET_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", "");
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1950 [R4][CRM_API] Verify the GET - taxes_excepted {exemptionId} with valid input in exemption id")
    public void testGetTaxesExceptedWithValidExemptionId()
    {
        String updatedURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
        TaxesExcepted taxesExcepted = requestPayload.taxesExcepted(Integer.valueOf(customerId),taxTypeId);

        List<TaxesExcepted> added = new ArrayList<>();
        added.add(taxesExcepted);
        List<TaxesExcepted> deletedIds = new ArrayList<>();

        Map<String, Object> taxesExceptedMapId = new HashMap<>();
        taxesExceptedMapId.put("exTaxAdd", added);
        taxesExceptedMapId.put("exTaxDelete", deletedIds);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(taxesExceptedMapId)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String taxesExceptedId = JsonPath.read(response.asString(), "$.result[0].id").toString();
        String updateGetURI = APIConstants.GET_TAXES_EXCEPTED_EXCEPTED_ID.replace("{exemptionId}",taxesExceptedId);


        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.customerId").toString()), customerId, "Invalid customerId");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.taxTypeId").toString()), String.valueOf(taxesExcepted.getTaxTypeId()), "Invalid taxTypeId");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.referenceNumber").toString()),taxesExcepted.getReferenceNumber(), "Invalid referenceNumber");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.from").toString().substring(0,10)), String.valueOf(taxesExcepted.getFrom()), "Invalid From");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.to").toString().substring(0,10)), String.valueOf(taxesExcepted.getTo()), "Invalid To");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.isApprovalRequired").toString()), String.valueOf(taxesExcepted.getIsApprovalRequired()), "Invalid IsApprovalRequired");
        softAssert.assertEquals((JsonPath.read(resGet.asString(), "$.result.isActive").toString()), String.valueOf(taxesExcepted.getIsActive()), "Invalid IsActive");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1949 [R4][CRM_API] Verify the GET - taxes_excepted {exemptionId} with invalid input in exemption id")
    public void testGetTaxesExceptedWithInvalidExemptionId() {
        String invalidId = "999999";
        String updateGetURI = APIConstants.GET_TAXES_EXCEPTED_EXCEPTED_ID.replace("{exemptionId}",invalidId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CUSTOMER_TAX_EXCEMPT_NOT_FOUND", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer tax exemption related data for the given id - 999999", "Invalid description");

        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1948 [R4][CRM_API] Verify the GET - taxes_excepted {exemptionId}with null input in exemption id")
    public void testGetTaxesExceptedWithNullExemptionId()
    {
        String updateGetURI = APIConstants.GET_TAXES_EXCEPTED_EXCEPTED_ID.replace("{exemptionId}","");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 404);
    }

}
