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

public class DeleteCustomerGroup extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-385 [CRM_API] Verify the DELETE -customer-groups/{id} with Valid Pay Load")
    public void testDeleteCustomerGroupUsingValidId() { //DELETE -customer-groups/{id} with Valid Pay Load
        CustomerGroups customerGroups =  requestPayload.createNewCustomerGroup();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(customerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updateURI = APIConstants.DELETE_CUSTOMER_GROUP.replace("{id}", customerGroupId);
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString  = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), customerGroupId, "Invalid id");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-392 [CRM_API] Verify the DELETE - customer-groups/{id} using invalid id")
    public void testDeleteCustomerGroupUsingInvalidId() {//DELETE - customer-groups/{id} using invalid id
        String updateURI = APIConstants.DELETE_CUSTOMER_GROUP.replace("{id}",  "999999");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        String statusCode = String.valueOf(response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString  = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid Status Code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "Cm_R_Customer_Group_Not_Found", "Invalid error code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is No customer group related data found -", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-386 [CRM_API] Verify the DELETE -customer-groups/{id} with empty Pay Load")
    public void testDeleteCustomerGroupUsingEmptyId() {//DELETE -customer-groups/{id} with empty Pay Load
        String URI = APIConstants.DELETE_CUSTOMER_GROUP.replace("{id}",  "");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(URI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 405);
    }
}
