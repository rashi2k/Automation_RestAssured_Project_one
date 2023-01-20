package tests.crm.underwritingCategories;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.UnderwritingCategories;
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

public class PutUnderwritingCategories extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-371 [CRM_API] Verify the PUT - uw_categories by id with valid data")
    public void testUpdateUWCategoriesWithValidPayLoad() { //PUT - uw_categories by id with valid data
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_UW_CATEGORY.replace("{id}", uwCategoryId);

        UnderwritingCategories updateUnderwritingCategory = requestPayload.createUnderwritingCategory();
        updateUnderwritingCategory.setId(uwCategoryId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateUnderwritingCategory)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        Assert.assertEquals(responseCG.statusCode(), 200);
        responseCG.getBody().prettyPrint();

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), uwCategoryId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), updateUnderwritingCategory.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), updateUnderwritingCategory.getName(), "Invalid name");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-373 [CRM_API] Verify the PUT - uw_categories by id with invalid code and valid name")
    public void testUpdateUWCategoriesWithInvalidCodeAndValidName() {//PUT - uw_categories by id with invalid code and valid name
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "result.id").toString();
        String updatedURI = APIConstants.UPDATE_UW_CATEGORY.replace("{id}", uwCategoryId);

        String invalidCode = "VLP146";
        UnderwritingCategories updateUnderwritingCategory = requestPayload.createUnderwritingCategory();
        updateUnderwritingCategory.setId(uwCategoryId);
        updateUnderwritingCategory.setCode(invalidCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateUnderwritingCategory)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 400);
        response.getBody().prettyPrint();

        String statusCode = String.valueOf(responseCC.getStatusCode());
        String responseToString = response.asString();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-374 [CRM_API] Verify the PUT - uw_categories by id with empty code and valid name")
    public void testUpdateUWCategoriesWithEmptyCodeAndValidName() {//PUT - uw_categories by id with empty code and valid name
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "result.id").toString();
        String updatedURI = APIConstants.UPDATE_UW_CATEGORY.replace("{id}", uwCategoryId);

        UnderwritingCategories updateUnderwritingCategory = requestPayload.createUnderwritingCategory();
        updateUnderwritingCategory.setId(uwCategoryId);
        updateUnderwritingCategory.setCode("");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateUnderwritingCategory)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 400);
        response.getBody().prettyPrint();

        String statusCode = String.valueOf(responseCC.getStatusCode());
        String responseToString = response.asString();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-376 [CRM_API] Verify the [CRM_API] Verify the PUT - uw_categories by id with empty name and valid code")
    public void testUpdateUWCategoriesWithEmptyNameAndValidCode() {//PUT - uw_categories by id with empty name and valid code
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "result.id").toString();
        String updatedURI = APIConstants.UPDATE_UW_CATEGORY.replace("{id}", uwCategoryId);

        UnderwritingCategories updateUnderwritingCategory = requestPayload.createUnderwritingCategory();
        updateUnderwritingCategory.setId(uwCategoryId);
        updateUnderwritingCategory.setName("");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateUnderwritingCategory)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 400);
        response.getBody().prettyPrint();

        String statusCode = String.valueOf(responseCC.getStatusCode());
        String responseToString = response.asString();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-372 [CRM_API] Verify the PUT - uw_categories by id with empty data")
    public void testUpdateUWCategoriesWithEmptyData() {//PUT - uw_categories by id with empty data
        String updatedURI = APIConstants.UPDATE_UW_CATEGORY.replace("{id}", "");

        UnderwritingCategories updateUnderwritingCategory = requestPayload.createUnderwritingCategory();
        updateUnderwritingCategory.setId("");
        updateUnderwritingCategory.setCode("");
        updateUnderwritingCategory.setName("");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateUnderwritingCategory)
                .put(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 405);
        response.getBody().prettyPrint();
    }
}

