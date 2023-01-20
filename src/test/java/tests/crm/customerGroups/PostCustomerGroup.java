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

public class PostCustomerGroup extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-313 [CRM_API] Verify the POST -customer-groups with Valid Pay Load")
    public void testPostCustomerGroupWithValidPayLoad() { //POST -customer-groups with Valid Pay Load
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createCustomerGroups.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createCustomerGroups.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), createCustomerGroups.getIsActive(), "Invalid status");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-321 [CRM_API] Verify the POST - customer-groups with valid name and  empty code")
    public void testPostCustomerGroupWithValidNameAndEmptyCode() { //POST - customer-groups with valid name and  empty code
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        createCustomerGroups.setCode("");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "GROUP_CODE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Customer group code can't be null", "Invalid description");
        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-317 [CRM_API] Verify the POST - customer-groups with valid code and empty name")
    public void testPostCustomerGroupWithValidCodeAndEmptyName() { //POST - customer-groups with valid code and empty name
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        createCustomerGroups.setName("");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-321 [CRM_API] Verify the POST - customer-groups with valid name and  empty code")
    public void testPostCustomerGroupWithValidNameAndeEmptyCodeString() { //POST - customer-groups with valid name and  empty code
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        createCustomerGroups.setCode(" ");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "GROUP_CODE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Customer group code can't be null", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-317 [CRM_API] Verify the POST - customer-groups with valid code and empty name")
    public void testPostCustomerGroupWithValidCodeAndEmptyNameString() { //POST - customer-groups with valid code and empty name
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        createCustomerGroups.setName(" ");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-314 [CRM_API] Verify the POST - customer-groups with empty Pay Load")
    public void testPostCustomerGroupWithEmptyPayLoad() { //POST - customer-groups with empty Pay Load
        CustomerGroups createCustomerGroups = requestPayload.createNewCustomerGroup();
        createCustomerGroups.setCode("");
        createCustomerGroups.setName("");
        createCustomerGroups.setIsActive("");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createCustomerGroups)
                .post(APIConstants.CREATE_CUSTOMER_GROUP);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "GROUP_CODE_NULL", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Customer group code can't be null", "Invalid description");
        softAssert.assertAll();
    }
}
