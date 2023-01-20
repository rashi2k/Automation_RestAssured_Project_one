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

public class PutCivilStatusAdmin extends BaseTest {

    public static ArrayList<Integer> saveId = new ArrayList<Integer>();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter all fields with a valid data")
    public void testPutCivilStatusWithValidData() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.UPDATE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        CivilStatus updateCivilStatus = requestPayload.newCivilStatus();
        updateCivilStatus.setCivilStatusId(Integer.parseInt(saveCivilStatusId));

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCivilStatus)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resUpdate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatusId").toString()), saveCivilStatusId, "Invalid civilStatusId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.civilStatus").toString()), updateCivilStatus.getCivilStatus(), "Invalid civilStatus");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(updateCivilStatus.getIsActive()), "Invalid isActive");
        softAssert.assertAll();

        Integer saveStatusId = JsonPath.read(resUpdate.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter all as fields blank")
    public void testPutCivilStatusWithAllFieldsBlank() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.UPDATE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        Integer saveStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter valid civilStatusId and others as blank")
    public void testPutCivilStatusWithValidCivilStatusIdAndOthersBlank() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.UPDATE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        CivilStatus updateCivilStatus = requestPayload.newCivilStatus();
        updateCivilStatus.setCivilStatusId(Integer.parseInt(saveCivilStatusId));
        updateCivilStatus.setCivilStatus(null);
        updateCivilStatus.setIsActive(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCivilStatus)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        Integer saveStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter valid isActive and others as blank")
    public void testPutCivilStatusWithValidIsActiveAndOthersBlank() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.UPDATE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        CivilStatus updateCivilStatus = requestPayload.newCivilStatus();
        updateCivilStatus.setCivilStatusId(null);
        updateCivilStatus.setCivilStatus(null);

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCivilStatus)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        Integer saveStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter valid civilStatusId and others as invalid")
    public void testPutCivilStatusWithValidCivilStatusIdAndOthersInvalid() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.UPDATE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        Map<String, Object> updateCivilStatus = new HashMap<>();
        updateCivilStatus.put("civilStatusId", saveCivilStatusId);
        updateCivilStatus.put("civilStatus", civilStatus.getCivilStatus());
        updateCivilStatus.put("isActive", "Invalid");

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCivilStatus)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        Integer saveStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter valid isActive and others as invalid")
    public void testPutCivilStatusWithValidIsActiveIdAndOthersInvalid() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.UPDATE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        Map<String, Object> updateCivilStatus = new HashMap<>();
        updateCivilStatus.put("civilStatusId", 9999999);
        updateCivilStatus.put("civilStatus", 9999999);
        updateCivilStatus.put("isActive", civilStatus.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCivilStatus)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        Integer saveStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter invalid civilStatusId and others as valid ")
    public void testPutCivilStatusWithInvalidCivilStatusIdAndOthersValid() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        Integer invalidStatusId = 9999999;
        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.UPDATE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", String.valueOf(invalidStatusId));

        Map<String, Object> updateCivilStatus = new HashMap<>();
        updateCivilStatus.put("civilStatusId", invalidStatusId);
        updateCivilStatus.put("civilStatus", 9999999);
        updateCivilStatus.put("isActive", civilStatus.getIsActive());

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCivilStatus)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 404);

        Integer saveStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("[ADMIN] Enter invalid isActive and others as valid ")
    public void testPutCivilStatusWithInvalidIsActiveAndOthersValid() {
        CivilStatus civilStatus = requestPayload.newCivilStatus();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(civilStatus)
                .post(APIConstants.CREATE_ADMIN_CIVIL_STATUS);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveCivilStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId").toString();
        String updateURI = APIConstants.UPDATE_ADMIN_CIVIL_STATUS_ID.replace("{civilStatusId}", saveCivilStatusId);

        Map<String, Object> updateCivilStatus = new HashMap<>();
        updateCivilStatus.put("civilStatusId", saveCivilStatusId);
        updateCivilStatus.put("civilStatus", civilStatus.getCivilStatus());
        updateCivilStatus.put("isActive", "Invalid");

        Response resUpdate = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateCivilStatus)
                .put(updateURI);
        System.out.println("Status Code: " + resUpdate.getStatusCode());
        resUpdate.getBody().prettyPrint();
        Assert.assertEquals(resUpdate.statusCode(), 400);

        Integer saveStatusId = JsonPath.read(resCreate.asString(), "$.result.civilStatusId");
        saveId.add(saveStatusId);
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
