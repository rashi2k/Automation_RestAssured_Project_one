package tests.crm.customerBankInfo;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.BankInfo;
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

public class DeleteCustomerBankInfo extends BaseTest {

    public String customerId, fName, lName;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseBankBranchInfo();
    }

    @BeforeMethod
    public void createParentCustomer() {
        //create test customer to get customerID
        CustomerBasicInfo createTestCustomer = requestPayload.createTestCustomer(individualCustomerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, idNumber);
        Response response = RestAssured.given().spec(repoSpec).when()
                .body(createTestCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        customerId = JsonPath.read(response.asString(), "$.result.customerId").toString();

        //get customer fName and lName
        Response responseCN = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", customerId));
        fName = JsonPath.read(responseCN.asString(), "$.result.firstName").toString();
        lName = JsonPath.read(responseCN.asString(), "$.result.lastName").toString();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1486 [R2] [CRM_API] Verify the DELETE - employment_bank_info with valid customerBankId (bank-info)")
    public void testDeleteBankInfoWithValidPayLoad() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setIsPrimary(false);
        updateBankInfo.setIsActive(false);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        String updateDeleteURI = APIConstants.DELETE_CUSTOMER_BANK_INFO.replace("{bankId}", String.valueOf(customerBankId));

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateDeleteURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.customerBankId").toString()), String.valueOf(customerBankId), "Invalid customerBankId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankAccountName").toString()), updateBankInfo.getBankAccountName(), "Invalid bankAccountName");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankAccountNo").toString()), updateBankInfo.getBankAccountNo(), "Invalid bankAccountNo");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.bankId").toString()), String.valueOf(updateBankInfo.getBankId()), "Invalid bankId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.branchId").toString()), String.valueOf(updateBankInfo.getBranchId()), "Invalid branchId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isPrimary").toString()), String.valueOf(updateBankInfo.getIsPrimary()), "Invalid isPrimary");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateBankInfo.getIsActive()), "Invalid isActive");
        softAssert.assertAll();


    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-707 [[R2] [CRM_API] Verify the DELETE - employment_bank_info with null ID (bank-info)")
    public void testDeleteBankInfoWithNullId() {
        String updateURI = APIConstants.DELETE_CUSTOMER_BANK_INFO.replace("{bankId}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-710 [R2] [CRM_API] Verify the DELETE - employment_bank_info with invalid ID (bank-info)")
    public void testDeleteBankInfoWithInvalidId() {
        String invalidId = "999999";
        String updateURI = APIConstants.DELETE_CUSTOMER_BANK_INFO.replace("{bankId}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_M_CUSTOMER_BANK_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no related data for the given bankId - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }
}
