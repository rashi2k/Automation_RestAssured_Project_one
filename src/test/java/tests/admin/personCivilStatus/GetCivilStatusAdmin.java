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
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

public class GetCivilStatusAdmin extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter all fields with a valid data")
    public void testGetCivilStatusWithValidData() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(APIConstants.GET_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isCivilStatusFound= false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String civilStatusId = JsonPath.read(responseToString, "$.result[" + i + "].civilStatusId").toString();
            if (civilStatusId.equals(saveCivilStatusId)) {
                isCivilStatusFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].civilStatus").toString()), civilStatus.getCivilStatus(), "Invalid civilStatus");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(civilStatus.getIsActive()), "Invalid isActive");
                break;
            }
        }
        softAssert.assertTrue(isCivilStatusFound, "Civil Status not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter all fields with a valid data")
    public void testGetCivilStatusWithValidId() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.GET_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resGet.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatusId").toString()), saveCivilStatusId, "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatus").toString()), civilStatus.getCivilStatus(), "Invalid civilStatus");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(civilStatus.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter all as fields blank")
    public void testGetCivilStatusWithEmptyData() {
        String updateURI = APIConstants.GET_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", " ");

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter one invalid data")
    public void testGetCivilStatusWithInvalidData() {
        String invalidCivilStatusId = "9999999";
        String updateURI = APIConstants.GET_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", invalidCivilStatusId);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);
    }

}
