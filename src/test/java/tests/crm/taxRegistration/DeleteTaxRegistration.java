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

public class DeleteTaxRegistration extends BaseTest {

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
    @Description("IQ-651 [R2] [CRM_API] [Delete] tax_registration with valid payload")
    public void testDeleteTaxRegistrationWithValidPayLoad() {
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

        String updateDeleteURI = APIConstants.DELETE_TAX_REGISTRATION.replace("{id}", taxRegistrationId);
        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateDeleteURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-653 [R2] [CRM_API] [Delete] tax_registration with empty payload")
    public void testDeleteTaxRegistrationWithEmptyPayLoad() {
        String updateURI = APIConstants.DELETE_TAX_REGISTRATION.replace("{id}", "");
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-654 [R2] [CRM_API] [Delete] tax_registration with invalid id")
    public void testDeleteTaxRegistrationWithInvalidId() {
        String invalidId = "999999";
        String updateURI = APIConstants.DELETE_TAX_REGISTRATION.replace("{id}", invalidId);
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.errorCode").toString()), "CM_M_CUSTOMER_TAX_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(resUpdate.asString(), "$.description").toString()), "There is no customer tax related data for the given customer tax id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }
}
