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
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostCivilStatusAdmin extends BaseTest {

    public static ArrayList<Integer> saveId = new ArrayList<Integer>();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter all fields with a valid data")
    public void testPostCivilStatusWithValidData() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatus").toString()), civilStatus.getCivilStatus(), "Invalid civilStatus");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(civilStatus.getIsActive()), "Invalid isActive");
        softAssert.assertAll();

        Integer saveStatusId = JsonPath.read(response.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter all as fields blank")
    public void testPostCivilStatusWithAllFieldsBlank() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter valid civilStatus and others as blank")
    public void testPostCivilStatusWithValidCivilStatusAndOthersBlank() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        civilStatus.setIsActive(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter valid isActive and others as blank")
    public void testPostCivilStatusWithValidIsActiveAndOthersBlank() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        civilStatus.setCivilStatus(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter valid civilStatus and others as invalid")
    public void testPostCivilStatusWithValidCivilStatusAndOthersInvalid() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();

        Map<String, Object> newCivilStatus = new HashMap<>();
        newCivilStatus.put("civilStatusId", null);
        newCivilStatus.put("civilStatus", civilStatus.getCivilStatus());
        newCivilStatus.put("isActive", "Invalid");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(newCivilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter valid isActive and others as invalid")
    public void testPostCivilStatusWithValidIsActivesAndOthersInvalid() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();

        Map<String, Object> newCivilStatus = new HashMap<>();
        newCivilStatus.put("civilStatusId", null);
        newCivilStatus.put("civilStatus", null);
        newCivilStatus.put("isActive", civilStatus.getIsActive());

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(newCivilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter invalid civilStatus and others as valid")
    public void testPostCivilStatusWithInvalidCivilStatusAndOthersValid() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();

        Map<String, Object> newCivilStatus = new HashMap<>();
        newCivilStatus.put("civilStatusId", null);
        newCivilStatus.put("civilStatus", null);
        newCivilStatus.put("isActive", civilStatus.getIsActive());

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(newCivilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter invalid isActive and others as valid")
    public void testPostCivilStatusWithInvalidIsActiveAndOthersValid() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();

        Map<String, Object> newCivilStatus = new HashMap<>();
        newCivilStatus.put("civilStatusId", null);
        newCivilStatus.put("civilStatus", civilStatus.getCivilStatus());
        newCivilStatus.put("isActive", "Invalid");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(newCivilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    //DELETE created civil status test data
    @AfterClass
    public void deleteCreatedData() {
        System.out.println(saveId);
        for (int i = 0; i < saveId.size(); i++) {
        String updateURI = APIConstants.DELETE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", String.valueOf(saveId.get(i)));
        Response resDelete = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + resDelete.getStatusCode());
        resDelete.getBody().prettyPrint();
        Assert.assertEquals(resDelete.statusCode(), 200);
        }
    }



}
