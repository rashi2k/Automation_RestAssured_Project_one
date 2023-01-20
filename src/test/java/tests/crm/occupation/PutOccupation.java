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

public class PutOccupation extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-606 [R2] [CRM_API] Verify the PUT - occupation by id with valid data")
    public void testPutOccupationWithValidData() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_OCCUPATION.replace("{id}", saveOccupationId);

        Occupation updateOccupation = requestPayload.newOccupation();
        updateOccupation.setId(Integer.parseInt(saveOccupationId));
        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateOccupation)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveOccupationId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupation").toString()), updateOccupation.getOccupation(), "Invalid occupation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), updateOccupation.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateOccupation.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-616 [R2] [CRM_API] Verify the PUT - occupation by id with empty data")
    public void testPutOccupationWithEmptyData() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_OCCUPATION.replace("{id}", saveOccupationId);

        Occupation updateOccupation = requestPayload.newOccupation();
        updateOccupation.setId(null);
        updateOccupation.setOccupation(null);
        updateOccupation.setName(null);
        updateOccupation.setIsActive(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateOccupation)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Invalid id", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NULL", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-641 [R2] [CRM_API] Verify the PUT - occupation by id with empty Occupation and valid name and isActive")
    public void testPutOccupationWithEmptyOccupation() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_OCCUPATION.replace("{id}", saveOccupationId);

        Occupation updateOccupation = requestPayload.newOccupation();
        updateOccupation.setId(Integer.parseInt(saveOccupationId));
        updateOccupation.setOccupation(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateOccupation)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-647 [R2] [CRM_API] Verify the PUT - occupation by id with empty isActive and valid name and occupation")
    public void testPutOccupationWithEmptyIsActive() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_OCCUPATION.replace("{id}", saveOccupationId);

        Occupation updateOccupation = requestPayload.newOccupation();
        updateOccupation.setId(Integer.parseInt(saveOccupationId));
        updateOccupation.setIsActive(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateOccupation)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1441 [R2] [CRM_API] Verify the PUT - occupation by id with empty name and valid isActive and occupation")
    public void testPutOccupationWithEmptyName() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_OCCUPATION.replace("{id}", saveOccupationId);

        Occupation updateOccupation = requestPayload.newOccupation();
        updateOccupation.setId(Integer.parseInt(saveOccupationId));
        updateOccupation.setName(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateOccupation)
                .put(updatedURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);
    }
}
