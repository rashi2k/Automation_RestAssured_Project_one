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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

public class PutCustomerBankInfo extends BaseTest {

    public String salutationCode, genderCode, nationality, preferLang, customerId, fName, lName, idType;
    public Integer uwCategoryId, customerGroupId, relationshipTypeId, civilStatusId, taxTypeId, bankId, branchId;
    public String idNumber = "1322323222";
    public String customerType = PropertiesUtils.INDIVIDUAL_CUS_TYPE;

    @BeforeMethod
    public void initiate() {
        //get bankId
        Response responseBankId = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_BANK);
        bankId = Integer.parseInt(JsonPath.read(responseBankId.asString(), "$.result[0].id").toString());

        //get branchId
        Response responseBranchId = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_BANK_BRANCH);
        branchId = Integer.parseInt(JsonPath.read(responseBranchId.asString(), "$.result[0].id").toString());

        //get taxTypeId
        Response response = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_TAX);
        taxTypeId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].taxTypeId").toString());

        //get cus title
        Response responseA = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_TITLE);
        salutationCode = JsonPath.read(responseA.asString(), "$.result[0].salutationCode").toString();

        //get cus gender
        Response responseB = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_GENDER);
        genderCode = JsonPath.read(responseB.asString(), "$.result[1].genderCode").toString();

        //get cus nationality
        Response responseC = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_NATIONALITY);
        nationality = JsonPath.read(responseC.asString(), "$.result[0].nationality").toString();

        //get cus civil status
        Response responseD = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_CIVIL_STATUS);
        civilStatusId = Integer.parseInt(JsonPath.read(responseD.asString(), "$.result[0].civilStatusId").toString());

        //get cus preferred language
        Response responseE = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_PREF_LANG);
        preferLang = JsonPath.read(responseE.asString(), "$.result[0].iso3Code").toString();

        //get cus id type
        Response responseF = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_ID_TYPE);
        idType = JsonPath.read(responseF.asString(), "$.result[0].identityTypeCode").toString();

        //get cus relationship type
        Response responseG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_RELATIONSHIP_TYPE);
        relationshipTypeId = Integer.parseInt(JsonPath.read(responseG.asString(), "$.result[0].id").toString());

        //get customer group ID
        Response responseCG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_ALL_CUSTOMER_GROUPS);
        customerGroupId = Integer.parseInt(JsonPath.read(responseCG.asString(), "$.result[0].id").toString());

        //get uw category  ID
        Response responseUG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_UW_CATEGORIES);
        uwCategoryId = Integer.parseInt(JsonPath.read(responseUG.asString(), "$.result[0].id").toString());

        //create test customer to get cusID
        CustomerBasicInfo createTestCustomer = requestPayload.createTestCustomer(customerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, idNumber);
        Response responseCI = RestAssured.given().spec(repoSpec).when()
                .body(createTestCustomer)
                .post(APIConstants.CREATE_CUS_BASIC_INFO);
        customerId = JsonPath.read(responseCI.asString(), "$.result.customerId").toString();

        //get customer fName and lName
        Response responseCN = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_BASIC_INFO_ID.replace("{id}", customerId));
        fName = JsonPath.read(responseCN.asString(), "$.result.firstName").toString();
        lName = JsonPath.read(responseCN.asString(), "$.result.lastName").toString();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1473 [R2] [CRM_API] Verify the PUT - employment_bank_info with valid payload (Bank info)")
    public void testPutBankInfoWithValidPayLoad() {
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

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

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
    @Description("IQ-1474 [R2] [CRM_API] Verify the PUT - employment_bank_info with empty payload (Bank info)")
    public void testPutBankInfoWithEmptyPayLoad() {
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
        updateBankInfo.setBankAccountName(null);
        updateBankInfo.setBankAccountNo(null);
        updateBankInfo.setBankId(null);
        updateBankInfo.setBranchId(null);
        updateBankInfo.setIsPrimary(null);
        updateBankInfo.setIsActive(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-682 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Bank Account Name")
    public void testPutBankInfoWithNullBankAccountName() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(null);
        updateBankInfo.setBankAccountNo(createBankInfo.getBankAccountNo());
        updateBankInfo.setBankId(createBankInfo.getBankId());
        updateBankInfo.setBranchId(createBankInfo.getBranchId());
        updateBankInfo.setIsPrimary(createBankInfo.getIsPrimary());
        updateBankInfo.setIsActive(createBankInfo.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-685 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Bank ID")
    public void testPutBankInfoWithNullBankId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(createBankInfo.getBankAccountName());
        updateBankInfo.setBankAccountNo(createBankInfo.getBankAccountNo());
        updateBankInfo.setBankId(null);
        updateBankInfo.setBranchId(createBankInfo.getBranchId());
        updateBankInfo.setIsPrimary(createBankInfo.getIsPrimary());
        updateBankInfo.setIsActive(createBankInfo.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-690 [R2] [CRM_API] Verify the PUT - employment_bank_info with invalid Bank ID")
    public void testPutBankInfoWithInvalidBankId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        Integer invalidId = 999999;
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(createBankInfo.getBankAccountName());
        updateBankInfo.setBankAccountNo(createBankInfo.getBankAccountNo());
        updateBankInfo.setBankId(invalidId);
        updateBankInfo.setBranchId(createBankInfo.getBranchId());
        updateBankInfo.setIsPrimary(createBankInfo.getIsPrimary());
        updateBankInfo.setIsActive(createBankInfo.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "AD_BANK_REFERENCE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no bank reference data found - ", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-693 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Branch ID")
    public void testPutBankInfoWithNullBranchId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(createBankInfo.getBankAccountName());
        updateBankInfo.setBankAccountNo(createBankInfo.getBankAccountNo());
        updateBankInfo.setBankId(createBankInfo.getBankId());
        updateBankInfo.setBranchId(null);
        updateBankInfo.setIsPrimary(createBankInfo.getIsPrimary());
        updateBankInfo.setIsActive(createBankInfo.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-695 [R2] [CRM_API] Verify the PUT - employment_bank_info with invalid Branch ID")
    public void testPutBankInfoWithInvalidBranchId() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        Integer invalidId = 999999;
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(createBankInfo.getBankAccountName());
        updateBankInfo.setBankAccountNo(createBankInfo.getBankAccountNo());
        updateBankInfo.setBankId(createBankInfo.getBankId());
        updateBankInfo.setBranchId(invalidId);
        updateBankInfo.setIsPrimary(createBankInfo.getIsPrimary());
        updateBankInfo.setIsActive(createBankInfo.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "AD_BRANCH_REFERENCE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no branch reference data found - ", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-696 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Bank Account Number")
    public void testPutBankInfoWithNullBankAccountNumber() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(createBankInfo.getBankAccountName());
        updateBankInfo.setBankAccountNo(null);
        updateBankInfo.setBankId(createBankInfo.getBankId());
        updateBankInfo.setBranchId(createBankInfo.getBranchId());
        updateBankInfo.setIsPrimary(createBankInfo.getIsPrimary());
        updateBankInfo.setIsActive(createBankInfo.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    /*
    //TC not valid
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-701 [R2] [CRM_API] Verify the PUT - employment_bank_info with invalid Bank Account Number")
    public void testPutBankInfoWithInvalidBankAccountNumber() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(createBankInfo.getBankAccountName());
        updateBankInfo.setBankAccountNo("QA_DHA_BANo@@");
        updateBankInfo.setBankId(createBankInfo.getBankId());
        updateBankInfo.setBranchId(createBankInfo.getBranchId());
        updateBankInfo.setIsPrimary(createBankInfo.getIsPrimary());
        updateBankInfo.setIsActive(createBankInfo.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-702 [R2] [CRM_API] Verify the PUT - employment_bank_info with null Is Primary")
    public void testPutBankInfoWithNullIsPrimary() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(createBankInfo.getBankAccountName());
        updateBankInfo.setBankAccountNo(createBankInfo.getBankAccountNo());
        updateBankInfo.setBankId(createBankInfo.getBankId());
        updateBankInfo.setBranchId(createBankInfo.getBranchId());
        updateBankInfo.setIsPrimary(null);
        updateBankInfo.setIsActive(createBankInfo.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    /*
    //Can not insert String or Integer value into IsPrimary - type: boolean
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-705 [R2] [CRM_API] Verify the PUT - employment_bank_info with invalid Is Primary")
    public void testPutBankInfoWithInvalidIsPrimary() {
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "AD_BANK_REFERENCE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no bank reference data found", "Invalid description");
        softAssert.assertAll();
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-892 [R2] [CRM][API] Verify the PUT - employment_bank_info with null Is Active")
    public void testPutBankInfoWithNullIsActive() {
        String updateCreateURI = APIConstants.CREATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo createBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBankInfo)
                .post(updateCreateURI);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Response resGet = RestAssured.given().spec(repoSpec)//get customerBankId
                .when()
                .get(APIConstants.GET_CUSTOMER_BANK_INFO.replace("{customerId}", customerId));
        Integer customerBankId = Integer.parseInt(JsonPath.read(resGet.asString(), "$.result[0].customerBankId").toString());

        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);
        updateBankInfo.setCustomerBankId(customerBankId);
        updateBankInfo.setBankAccountName(createBankInfo.getBankAccountName());
        updateBankInfo.setBankAccountNo(createBankInfo.getBankAccountNo());
        updateBankInfo.setBankId(createBankInfo.getBankId());
        updateBankInfo.setBranchId(createBankInfo.getBranchId());
        updateBankInfo.setIsPrimary(createBankInfo.getIsActive());
        updateBankInfo.setIsActive(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    /*
    //Can not insert String or Integer value into IsActive - type: boolean
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-894 [R2] [CRM_API] Verify the PUT - employment_bank_info with invalid Is Active")
    public void testPutBankInfoWithInvalidIsPrimary() {
        String updatePutURI = APIConstants.UPDATE_CUSTOMER_BANK_INFO.replace("{customerId}", customerId);
        BankInfo updateBankInfo = requestPayload.newBankInfo(bankId, branchId, fName, lName);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBankInfo)
                .put(updatePutURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "AD_BANK_REFERENCE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no bank reference data found", "Invalid description");
        softAssert.assertAll();
    }
     */

}
