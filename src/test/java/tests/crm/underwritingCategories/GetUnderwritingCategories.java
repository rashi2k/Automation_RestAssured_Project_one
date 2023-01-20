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

public class GetUnderwritingCategories extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-311 [CRM_API] Verify the GET - uw_categories with valid Code and Name")
    public void testGetAllUWCategories() { //GET - uw_categories with valid Code and Name
        UnderwritingCategories createUnderwritingCategory = requestPayload.createUnderwritingCategory();

        //create UW category
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategory)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_UW_CATEGORIES +  "?page=0&size=99999";

        //get all uw categories
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        Boolean isUWCategoryFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        System.out.println(uwCategoryId);
        for (int i = 0; i < length; i++) {
            String uwCategoryCode = JsonPath.read(responseToString, "$.result[" + i + "].code").toString();
            if (uwCategoryCode.toString().equals(createUnderwritingCategory.getCode())) {
                isUWCategoryFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), uwCategoryId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createUnderwritingCategory.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createUnderwritingCategory.getName(), "Invalid name");
                break;
            }
        }
        softAssert.assertTrue(isUWCategoryFound, "UW Category not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-342 [CRM_API] Verify the GET - uw_categories by id with valid id")
    public void testGetUWCategoriesWithValidId() { //GET - uw_categories by id with valid id
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();

        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_UW_CATEGORIES_ID.replace("{id}", uwCategoryId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), uwCategoryId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createUnderwritingCategories.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createUnderwritingCategories.getName(), "Invalid name");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-345 [CRM_API] Verify the GET - uw_categories by id with invalid id")
    public void testGetUWCategoriesWithInvalidId() { //GET - uw_categories by id with invalid id
        String invalidId = "99999";
        String updatedURI = APIConstants.GET_UW_CATEGORIES_ID.replace("{id}", invalidId);

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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for the given category id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    /*
    //ISSUE WITH REQUEST URL WHEN SENDING EMPTY LOAD
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-343 [CRM_API] Verify the GET - uw_categories by id with empty id")
    public void testGetUWCategoriesWithEmptyId() { //GET - uw_categories by id with empty id
        System.out.println("Can not insert emptyId to GET uw categories");
        throw new SkipException("Can not insert emptyId to GET uw categories");
    }
    */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-346 [CRM_API] Verify the GET - uw_categories by code with valid code")
    public void testGetUWCategoriesWithValidCode() { //GET - customer-groups /{code} with Valid Pay Load
        UnderwritingCategories createUnderwritingCategory = requestPayload.createUnderwritingCategory();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategory)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_UW_CATEGORIES_CODE.replace("{code}", createUnderwritingCategory.getCode());

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), uwCategoryId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.code").toString()), createUnderwritingCategory.getCode(), "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createUnderwritingCategory.getName(), "Invalid name");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-350 [CRM_API] Verify the GET - uw_categories by code with invalid code")
    public void testGetUWCategoriesWithInvalidCode() { //GET - uw_categories by code with invalid code
        String invalidCode = "E1234";
        String updatedURI = APIConstants.GET_UW_CATEGORIES_CODE.replace("{code}", invalidCode);

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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for the given category code -" + invalidCode, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-348 [CRM_API] Verify the GET - uw_categories by code with empty data")
    public void testGetUWCategoriesWithEmptyCode() { //GET - customer-groups /{code} with empty Pay Load
        String emptyCode = "";
        String updatedURI = APIConstants.GET_UW_CATEGORIES_CODE.replace("{code}", emptyCode);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-387 [CRM_API] Verify the GET - uw_categories by word with valid data")
    public void testGetUWCategoriesWithValidSearchWord() { //GET - uw_categories by word with valid data
        UnderwritingCategories createUnderwritingCategory = requestPayload.createUnderwritingCategory();

        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategory)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_UW_CATEGORIES_WORD.replace("{word}", createUnderwritingCategory.getName());

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        Boolean isUWCategoryFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        System.out.println(uwCategoryId);
        for (int i = 0; i < length; i++) {
            String uwCategoryCode = JsonPath.read(responseToString, "$.result[" + i + "].code").toString();
            if (uwCategoryCode.toString().equals(createUnderwritingCategory.getCode())) {
                isUWCategoryFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), uwCategoryId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createUnderwritingCategory.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createUnderwritingCategory.getName(), "Invalid name");
                break;
            }
        }
        softAssert.assertTrue(isUWCategoryFound, "UW Category not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-391 [CRM_API] Verify the GET - uw_categories by word with similar data")
    public void testGetUWCategoriesWithSimilarSearchWord() { //GET - uw_categories by word with similar data
        UnderwritingCategories createUnderwritingCategory = requestPayload.createUnderwritingCategory();
        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategory)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String similarName = createUnderwritingCategory.getName();
        String customerGroupId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_UW_CATEGORIES_WORD.replace("{word}", similarName.substring(0, 9));

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        Boolean isUWCategoryFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String uwCategoryCode = JsonPath.read(responseToString, "$.result[" + i + "].code").toString();
            if (uwCategoryCode.toString().equals(createUnderwritingCategory.getCode())) {
                isUWCategoryFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), customerGroupId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()), createUnderwritingCategory.getCode(), "Invalid code");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createUnderwritingCategory.getName(), "Invalid name");
                break;
            }
        }
        softAssert.assertTrue(isUWCategoryFound, "UW Category Group not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-389 [CRM_API] Verify the GET - uw_categories by word with invalid data")
    public void testGetUWCategoriesWithInvalidSearchWord() { //GET - uw_categories by word with invalid data
        String invalidWord = "InvalidName";
        String updatedURI = APIConstants.GET_UW_CATEGORIES_WORD.replace("{word}", invalidWord);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), "[]", "Invalid result");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-390 [CRM_API] Verify the GET - uw_categories by word with empty data")
    public void testGetUWCategoriesWithEmptySearchWord() { //GET - uw_categories by word with empty data
        String emptyWord = "";
        String updatedURI = APIConstants.GET_UW_CATEGORIES_WORD.replace("{word}", emptyWord);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
}
