package tests.admin.personCivilStatus;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.admin.CivilStatus;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseTest;

public class DeleteCivilStatusAdmin extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter one invalid data")
    public void testDeleteCivilStatusWithValidId() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.DELETE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter all as fields blank")
    public void testDeleteCivilStatusWithEmptyId() {
        String updateURI = APIConstants.DELETE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", " ");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter one invalid data")
    public void testDeleteCivilStatusWithInvalidId() {
        String invalidCivilStatusId = "9999999";
        String updateURI = APIConstants.DELETE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", invalidCivilStatusId);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);
    }
}

