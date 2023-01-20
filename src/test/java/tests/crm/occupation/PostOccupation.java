package tests.crm.occupation;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.admin.Occupation;
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

public class PostOccupation extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-583 [R2] [CRM_API] Verify the POST - occupation with valid data")
    public void testPostOccupationWithValidData() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupation").toString()), createOccupation.getOccupation(), "Invalid occupation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createOccupation.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createOccupation.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-584 [R2] [CRM_API] Verify the POST - occupation with empty data")
    public void testPostOccupationWithEmptyData() {
        Occupation createOccupation = requestPayload.newOccupation();
        createOccupation.setOccupation("");
        createOccupation.setName("");
        createOccupation.setIsActive(null);

        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-597 [R2] [CRM_API] Verify the POST - occupation with empty Occupation and valid name and isActive")
    public void testPostOccupationWithEmptyOccupation() {
        Occupation createOccupation = requestPayload.newOccupation();
        createOccupation.setOccupation(null);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-602 [R2] [CRM_API] Verify the POST - occupation with empty isActive and valid name and occupation")
    public void testPostOccupationWithEmptyIsActive() {
        Occupation createOccupation = requestPayload.newOccupation();
        createOccupation.setIsActive(null);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1442 [R2] [CRM_API] Verify the POST - occupation by id with empty name and valid isActive and occupation")
    public void testPostOccupationWithEmptyName() {
        Occupation createOccupation = requestPayload.newOccupation();
        createOccupation.setName(null);
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 406);
    }
}
