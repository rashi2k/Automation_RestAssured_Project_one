package tests.crm.customerGroups;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerGroups;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

public class PutCustomerGroup extends BaseTest {

    Response responseCC;
    Response responseCG;

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-379 [CRM_API] Verify the PUT - customer-groups/{id} with Valid Pay Load")
    public void testUpdateCustomerGroupWithValidPayLoad() { //PUT - customer-groups/{id} with Valid Pay Load
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updateURI = APIConstants.UPDATE_CUSTOMER_GROUP.replace("{id}", customerGroupId);

        CustomerGroups updateCustomerGroups = requestPayload.createNewCustomerGroup();
        updateCustomerGroups.setId(customerGroupId);
        responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerGroups)
                .put(updateURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        Assert.assertEquals(responseCG.statusCode(), 200);
        responseCG.getBody().prettyPrint();

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), customerGroupId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), updateCustomerGroups.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), updateCustomerGroups.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), updateCustomerGroups.getIsActive(), "Invalid status");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-325 [CRM_API] Verify the PUT - customer-groups/{id} with valid id and empty code")
    public void testUpdateCustomerGroupWithValidIdAndEmptyCode() {//PUT - customer-groups/{id} with valid id and empty code
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updateURI = APIConstants.UPDATE_CUSTOMER_GROUP.replace("{id}", customerGroupId);

        CustomerGroups updateCustomerGroups = requestPayload.createNewCustomerGroup();
        updateCustomerGroups.setId(customerGroupId);
        updateCustomerGroups.setCode("");

        responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerGroups)
                .put(updateURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        String statusCode = String.valueOf(responseCG.getStatusCode());
        Assert.assertEquals(responseCG.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-383 [CRM_API] Verify the PUT - customer-groups/{id} with valid code and invalid id")
    public void testUpdateCustomerGroupWithValidCodeAndInvalidId() {//PUT - customer-groups/{id} with valid code and invalid id
        String invalidId = "99999";
        String updateURI = APIConstants.UPDATE_CUSTOMER_GROUP.replace("{id}", invalidId);

        CustomerGroups updateCustomerGroups = requestPayload.createNewCustomerGroup();
        updateCustomerGroups.setId(invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerGroups)
                .put(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 404);
        response.getBody().prettyPrint();

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Entity not found", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NOT_FOUND", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-319 [CRM_API] Verify the PUT - customer-groups/{id} with valid code and empty id")
    public void testUpdateCustomerGroupWithValidCodeAndEmptyId() {//PUT - customer-groups/{id} with valid code and empty id
        String emptyId = "";
        String updateURI = APIConstants.UPDATE_CUSTOMER_GROUP.replace("{id}", emptyId);

        CustomerGroups updateCustomerGroups = requestPayload.createNewCustomerGroup();
        updateCustomerGroups.setId(emptyId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerGroups)
                .put(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 405);
        response.getBody().prettyPrint();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-381 [CRM_API] Verify the PUT - customer-groups/{id} with empty Pay Load")
    public void testUpdateCustomerGroupWithEmptyPayLoad() {//PUT - customer-groups/{id} with empty Pay Load
        CustomerGroups updateCustomerGroups = requestPayload.createNewCustomerGroup();
        updateCustomerGroups.setId("");
        updateCustomerGroups.setCode("");
        updateCustomerGroups.setName("");
        updateCustomerGroups.setIsActive("");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCustomerGroups)
                .put(APIConstants.UPDATE_CUSTOMER_GROUP.replace("{id}", ""));
        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 405);
        response.getBody().prettyPrint();
    }

}



