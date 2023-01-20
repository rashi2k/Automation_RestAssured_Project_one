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

public class DeleteOccupation extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-652 [R2] [CRM_API] Verify the DELETE - occupation by id with valid data")
    public void testDeleteOccupationWithValidData() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.DELETE_OCCUPATION.replace("{id}", saveOccupationId);

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-658 [R2] [CRM_API] Verify the DELETE - occupation invalid data")
    public void testDeleteOccupationWithInvalidData() {
        String invalidId = "999999";
        String updatedURI = APIConstants.DELETE_OCCUPATION.replace("{id}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "OCCUPATION_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Occupation can not be found", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-660 [R2] [CRM_API] Verify the DELETE - occupation by id empty data")
    public void testDeleteOccupationWithEmptyData() {
        String updatedURI = APIConstants.DELETE_OCCUPATION.replace("{id}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 405);
    }
}
