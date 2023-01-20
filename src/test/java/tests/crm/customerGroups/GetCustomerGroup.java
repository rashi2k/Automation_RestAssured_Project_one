package tests.crm.customerGroups;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerGroups;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;
import com.jayway.jsonpath.JsonPath;

public class GetCustomerGroup extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-332 [CRM_API] Verify the GET - customer-groups with Valid Pay Load")
    public void testGetAllCustomerGroups() { //GET - customer-groups with Valid Pay Load
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();

        //create customer group
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_ALL_CUSTOMER_GROUPS + "?page=0&size=99999";

        //get all customer groups
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        Boolean isCustomerGroupFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String customerGroupCode = JsonPath.read(responseToString, "$.result[" + i + "].code").toString();
            if (customerGroupCode.equals(createCustomerGroups.getCode())) {
                isCustomerGroupFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), customerGroupId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createCustomerGroups.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createCustomerGroups.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), createCustomerGroups.getIsActive(), "Invalid status");
                break;
            }
        }
        softAssert.assertTrue(isCustomerGroupFound, "Customer Group not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-351 [CRM_API] Verify the GET - customer-groups /{id} with Valid Pay Load")
    public void testGetCustomerGroupWithValidId() { //GET - customer-groups /{id} with Valid Pay Load
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_ID.replace("{id}", customerGroupId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), customerGroupId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createCustomerGroups.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createCustomerGroups.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), createCustomerGroups.getIsActive(), "Invalid status");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-341 [CRM_API] Verify the GET - customer-groups /{id} using invalid ID")
    public void testGetCustomerGroupWithInvalidId() { //GET - customer-groups /{id} using invalid ID
        String invalidId = "99999";
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_ID.replace("{id}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "Cm_R_Customer_Group_Not_Found", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is No customer group related data found -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    /*
    //ISSUE WITH REQUEST URL WHEN SENDING EMPTY LOAD
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-339 [CRM_API] Verify the GET - customer-groups /{id} with empty Pay Load")
    public void testGetCustomerGroupWithEmptyId() { //GET - customer-groups /{id} with empty Pay Load
        System.out.println("Can not insert emptyId to GET customer groups");
        throw new SkipException("Can not insert emptyId to GET customer groups");
    }
    */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-355 [CRM_API] Verify the GET - customer-groups /{code} with Valid Pay Load")
    public void testGetCustomerGroupWithValidCode() { //GET - customer-groups /{code} with Valid Pay Load
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_CODE.replace("{code}", createCustomerGroups.getCode());

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), customerGroupId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createCustomerGroups.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createCustomerGroups.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), createCustomerGroups.getIsActive(), "Invalid status");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-358 [CRM_API] Verify the GET - customer-groups /{code} using invalid code")
    public void testGetCustomerGroupWithInvalidCode() { //GET - customer-groups /{code} using invalid code
        String invalidCode = "E1234";
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_CODE.replace("{code}", invalidCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "Cm_R_Customer_Group_Not_Found", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is No customer group related data found -" + invalidCode, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-356 [CRM_API] Verify the GET - customer-groups /{code} with empty Pay Load")
    public void testGetCustomerGroupWithEmptyCode() { //GET - customer-groups /{code} with empty Pay Load
        String emptyCode = "";
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_CODE.replace("{code}", emptyCode);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-478 [CRM_API] Verify the GET -customer-groups/search/{name} with valid data")
    public void testGetCustomerGroupWithValidName() { //GET -customer-groups/search/{name} with valid data
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_NAME.replace("{name}", createCustomerGroups.getName());

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        Boolean isCustomerGroupFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String customerGroupCode = JsonPath.read(responseToString, "$.result[" + i + "].code").toString();
            if (customerGroupCode.equals(createCustomerGroups.getCode())) {
                isCustomerGroupFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), customerGroupId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createCustomerGroups.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createCustomerGroups.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), createCustomerGroups.getIsActive(), "Invalid status");
                break;
            }
        }
        softAssert.assertTrue(isCustomerGroupFound, "Customer Group not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-481 [CRM_API] Verify the GET -customer-groups/search/{name} with similar data")
    public void testGetCustomerGroupWithSimilarName() { //GET -customer-groups/search/{name} with similar data
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String similarName = createCustomerGroups.getName();
        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_NAME.replace("{name}", similarName.substring(0, 5));

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        Boolean isCustomerGroupFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String customerGroupCode = JsonPath.read(responseToString, "$.result[" + i + "].code").toString();
            if (customerGroupCode.toString().equals(createCustomerGroups.getCode())) {
                isCustomerGroupFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), customerGroupId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createCustomerGroups.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createCustomerGroups.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), createCustomerGroups.getIsActive(), "Invalid status");
                break;
            }
        }
        softAssert.assertTrue(isCustomerGroupFound, "Customer Group not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-479 [CRM_API] Verify the GET -customer-groups/search/{name} with invalid data")
    public void testGetCustomerGroupWithInvalidName() { //GET -customer-groups/search/{name} with invalid data
        String invalidName = "99999 Company";
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_NAME.replace("{name}", invalidName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-480 [CRM_API] Verify the GET -customer-groups/search/{name} with empty data")
    public void testGetCustomerGroupWithEmptyName() { //GET -customer-groups/search/{name} with empty data
        String emptyName = "";
        String updatedURI = APIConstants.GET_SINGLE_CUSTOMER_GROUP_NAME.replace("{name}", emptyName);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
}

