package tests.crm.customerDocuments;

import com.informatics.endpoints.APIConstants;
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

public class GetCustomerDocuments extends BaseTest {

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
    @Description("IQ-680 [R2] [CRM_API] Verify the GET - customer/{id}/documents with Valid Pay Load")
    public void testGetCustomerDocumentsWithValidPayLoad() {
        String updatedPutURI = APIConstants.UPDATE_CUS_DOCUMENTS.replace("{customerId}", customerId);
        CustomerDocuments customerDocuments = requestPayload.customerDocuments(docTypeId);

        List<CustomerDocuments> availableInfo = new ArrayList<>();
        availableInfo.add(customerDocuments);
        List<Integer> deletedInfo = new ArrayList<Integer>();
        Map<String, Object> customerDocumentInfo = new HashMap<>();
        customerDocumentInfo.put("available", availableInfo);
        customerDocumentInfo.put("deleted", deletedInfo);

        Response resPut = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerDocumentInfo)
                .put(updatedPutURI);
        System.out.println("Status Code: " + resPut.getStatusCode());
        resPut.getBody().prettyPrint();
        Assert.assertEquals(resPut.statusCode(), 200);

        String saveDocId = JsonPath.read(resPut.asString(), "$.result[0].id").toString();

        String updatedGetURI = APIConstants.GET_CUS_DOCUMENTS.replace("{customerId}", customerId);
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isCustomerDocumentFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String docId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (docId.equals(saveDocId)) {
                isCustomerDocumentFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].documentPath").toString()), customerDocuments.getDocumentPath(), "Invalid documentPath");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].remark").toString()), customerDocuments.getRemark(), "Invalid remark");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].receivedDate").toString()), customerDocuments.getReceivedDate(), "Invalid receivedDate");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].functionDocumentId").toString()), String.valueOf(customerDocuments.getFunctionDocumentId()), "Invalid functionDocumentId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].documentTypeId").toString()), String.valueOf(customerDocuments.getDocumentTypeId()), "Invalid documentTypeId");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isReceived").toString()), String.valueOf(customerDocuments.getIsReceived()), "Invalid isReceived");
                break;
            }
        }
        softAssert.assertTrue(isCustomerDocumentFound, "Customer Document not found");
        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-684 [R2] [CRM_API] Verify the GET - customer/{id}/documents with empty Pay Load")
    public void testGetCustomerDocumentsWithEmptyPayLoad() {
        String updatedGetURI = APIConstants.GET_CUS_DOCUMENTS.replace("{customerId}", "");
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-686 [R2] [CRM_API] Verify the GET - customer/{id}/documents using invalid ID")
    public void testGetCustomerDocumentsWithInvalidId() {
        String invalidId = "999999A";
        String updatedGetURI = APIConstants.GET_CUS_DOCUMENTS.replace("{customerId}", invalidId);
        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedGetURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 400);
    }
}
