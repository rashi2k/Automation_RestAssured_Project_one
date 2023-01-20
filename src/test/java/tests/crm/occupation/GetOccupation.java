package tests.crm.occupation;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.admin.BankBranch;
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

public class GetOccupation extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-563 [R2] [CRM_API] Verify the GET - occupation with valid data")
    public void testGetOccupationWithValidData() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_OCCUPATION+ "?page=0&size=999999";

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isOccupationFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String occupationId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (occupationId.equals(saveOccupationId)) {
                isOccupationFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveOccupationId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].occupation").toString()), createOccupation.getOccupation(), "Invalid occupation");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createOccupation.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(createOccupation.getIsActive()), "Invalid isActive");
                break;
            }
        }
        softAssert.assertTrue(isOccupationFound, "Occupation not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-572 [R2] [CRM_API] Verify the GET - occupation by id with valid id")
    public void testGetOccupationWithValidId() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_OCCUPATION_ID.replace("{id}", saveOccupationId);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveOccupationId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupation").toString()), createOccupation.getOccupation(), "Invalid occupation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createOccupation.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createOccupation.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    /*
    //IGNORED - ISSUE WITH REQUEST URL WHEN SENDING EMPTY LOAD
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-575 [R2] [CRM_API] Verify the GET - occupation by id with empty id")
    public void testGetOccupationWithEmptyId() {
        String updatedURI = APIConstants.GET_OCCUPATION_ID.replace("{id}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-575 [R2] [CRM_API] Verify the GET - occupation by id with invalid id")
    public void testGetOccupationWithInvalidId() {
        String invalidId = "999999";
        String updatedURI = APIConstants.GET_OCCUPATION_ID.replace("{id}", invalidId);

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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "OCCUPATION_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Occupation can not be found", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-579 [R2] [CRM_API] Verify the GET - occupation by code with valid occupation")
    public void testGetOccupationWithValidOccupation() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupation = JsonPath.read(resCreate.asString(), "$.result.occupation").toString();
        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_OCCUPATION_CODE.replace("{code}", saveOccupation);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = resCreate.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveOccupationId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.occupation").toString()), createOccupation.getOccupation(), "Invalid occupation");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createOccupation.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), String.valueOf(createOccupation.getIsActive()), "Invalid isActive");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-582 [R2] [CRM_API] Verify the GET - occupation by code with empty occupation")
    public void testGetOccupationWithEmptyOccupation() {
        String updatedURI = APIConstants.GET_OCCUPATION_CODE.replace("{code}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-581 [R2] [CRM_API] Verify the GET - occupation by code with invalid occupation")
    public void testGetOccupationWithInvalidOccupation() {
        String invalidCode = "@x@12@99";
        String updatedURI = APIConstants.GET_OCCUPATION_CODE.replace("{code}", invalidCode);

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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "OCCUPATION_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Occupation can not be found", "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-564 [R2] [CRM_API] Verify the GET - occupation by name with valid data")
    public void testGetOccupationWithValidName() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String saveOccupationName = JsonPath.read(resCreate.asString(), "$.result.name").toString();
        String updatedURI = APIConstants.GET_OCCUPATION_SEARCH.replace("{name}", saveOccupationName);

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isOccupationFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String occupationId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (occupationId.equals(saveOccupationId)) {
                isOccupationFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveOccupationId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].occupation").toString()), createOccupation.getOccupation(), "Invalid occupation");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createOccupation.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(createOccupation.getIsActive()), "Invalid isActive");
                break;
            }
        }
        softAssert.assertTrue(isOccupationFound, "Occupation not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-566 [R2] [CRM_API] Verify the GET - occupation by name with empty data")
    public void testGetOccupationWithEmptyName() {
        String updatedURI = APIConstants.GET_OCCUPATION_SEARCH.replace("{name}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-565 [R2] [CRM_API] Verify the GET - occupation by name with invalid data")
    public void testGetOccupationWithInvalidName() {
        String invalidName = "X_@_99X";
        String updatedURI = APIConstants.GET_OCCUPATION_SEARCH.replace("{name}", invalidName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-567 [R2] [CRM_API] Verify the GET - occupation by name with similar data")
    public void testGetOccupationWithSimilarName() {
        Occupation createOccupation = requestPayload.newOccupation();
        Response resCreate = RestAssured.given().spec(repoSpec)
                .when()
                .body(createOccupation)
                .post(APIConstants.CREATE_OCCUPATION);
        System.out.println("Status Code: " + resCreate.getStatusCode());
        resCreate.getBody().prettyPrint();
        Assert.assertEquals(resCreate.statusCode(), 201);

        String saveOccupationId = JsonPath.read(resCreate.asString(), "$.result.id").toString();
        String saveOccupationName = JsonPath.read(resCreate.asString(), "$.result.name").toString();
        String updatedURI = APIConstants.GET_OCCUPATION_SEARCH.replace("{name}", saveOccupationName.substring(0,11));

        Response resGet = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + resGet.getStatusCode());
        resGet.getBody().prettyPrint();
        Assert.assertEquals(resGet.statusCode(), 200);

        String responseToString = resGet.asString();
        Boolean isOccupationFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String occupationId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (occupationId.equals(saveOccupationId)) {
                isOccupationFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveOccupationId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].occupation").toString()), createOccupation.getOccupation(), "Invalid occupation");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createOccupation.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), String.valueOf(createOccupation.getIsActive()), "Invalid isActive");
                break;
            }
        }
        softAssert.assertTrue(isOccupationFound, "Occupation not found");
        softAssert.assertAll();
    }
}
