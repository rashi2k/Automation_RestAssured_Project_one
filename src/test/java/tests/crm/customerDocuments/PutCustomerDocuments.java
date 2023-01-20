package tests.crm.customerDocuments;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.ContactInfo;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.CustomerDocuments;
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

public class PutCustomerDocuments extends BaseTest {

    public String customerId;

    @BeforeClass
    public void initiate() {
        getBaseCustomerInfo();
        getBaseDocumentInfo();
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
    @Description("IQ-906 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using valid data")
    public void testPutCustomerDocumentsWithValidData() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCustomerDocId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(saveCustomerDocId);

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentPath").toString()), updateCustomerDocuments.getDocumentPath() , "Invalid documentPath");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].remark").toString()), updateCustomerDocuments.getRemark() , "Invalid remark");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].receivedDate").toString()), updateCustomerDocuments.getReceivedDate() , "Invalid receivedDate");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].functionDocumentId").toString()), String.valueOf(updateCustomerDocuments.getFunctionDocumentId()) , "Invalid functionDocumentId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentTypeId").toString()), String.valueOf(updateCustomerDocuments.getDocumentTypeId()) , "Invalid documentTypeId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isReceived").toString()),String.valueOf(updateCustomerDocuments.getIsReceived()) , "Invalid isReceived");
        softAssert.assertAll();
    }

    /*
    //CAN NOT EXECUTE AS (id:,) in request body
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-909 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using empty id")
    public void testPutCustomerDocumentsWithEmptyId() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);

        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);
        customerDocuments.setId();

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-911 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using invalid id")
    public void testPutCustomerDocumentsWithInvalidId() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer invalidId = 9999999;
        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(invalidId);

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CUSTOMER_DOCUMENT_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no customer document related data for the given id - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-912 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using empty functionDocumentId")
    public void testPutCustomerDocumentsWithEmptyFunctionDocumentId() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCustomerDocId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());

        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(saveCustomerDocId);
        updateCustomerDocuments.setFunctionDocumentId(null);

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    /*
    //IGNORED - CAN NOT ADD STRING TO INTEGER
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-913 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using invalid functionDocumentId")
    public void testPutCustomerDocumentsWithInvalidFunctionDocumentId() {
        Integer invalidId = Integer.parseInt("FD1");
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);

        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);
        customerDocuments.setFunctionDocumentId(invalidId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-914 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using empty documentTypeId")
    public void testPutCustomerDocumentsWithEmptyDocumentTypeId() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCustomerDocId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());

        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(saveCustomerDocId);
        updateCustomerDocuments.setDocumentTypeId(null);

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-915 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using invalid documentTypeId")
    public void testPutCustomerDocumentsWithInvalidDocumentTypeId() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCustomerDocId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());

        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(saveCustomerDocId);
        updateCustomerDocuments.setDocumentTypeId(999999);

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "DOCUMENT_TYPE_OR_FUNCTION_DOCUMENT_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is an error in fetching document type and function document type related data for the given ids - " + customerId, "Invalid description");
        softAssert.assertAll();

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-916 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using empty isReceived")
    public void testPutCustomerDocumentsWithEmptyIsReceived() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCustomerDocId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(saveCustomerDocId);
        updateCustomerDocuments.setIsReceived(null);

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-917 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using empty receivedDate")
    public void testPutCustomerDocumentsWithEmptyReceivedDate() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCustomerDocId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(saveCustomerDocId);
        updateCustomerDocuments.setReceivedDate(null);

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200); // received date allowed to be null (BUG IQ-1430)
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-918 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using invalid receivedDate")
    public void testPutCustomerDocumentsWithInvalidReceivedDate() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCustomerDocId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(saveCustomerDocId);
        updateCustomerDocuments.setReceivedDate("2022/15/04");

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-919 [R2] [CRM_API] Verify the PUT - customer/{id}/documents/upload using empty documentPath")
    public void testPutCustomerDocumentsWithEmptyDocumentPath() {
        String updatedURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object>  customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        Integer saveCustomerDocId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].id").toString());
        CustomerDocuments updateCustomerDocuments = requestPayload.customerDocuments(docTypeId);
        updateCustomerDocuments.setId(saveCustomerDocId);
        updateCustomerDocuments.setDocumentPath("");

        List<CustomerDocuments> availableInfoUpdate = new ArrayList<>();
        availableInfoUpdate.add(updateCustomerDocuments);

        Map<String, Object>  customerDocumentInfoUpdate = new HashMap<>();
        customerDocumentInfoUpdate.put("available", availableInfoUpdate);
        customerDocumentInfoUpdate.put("deleted", deletedInfo);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfoUpdate)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        String statusCode = String.valueOf(resUpdate.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "DOCUMENT_PATH_CANNOT_BE_NULL_OR_EMPTY", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "The document path cannot be null or empty", "Invalid description");
        softAssert.assertAll();
    }
}
