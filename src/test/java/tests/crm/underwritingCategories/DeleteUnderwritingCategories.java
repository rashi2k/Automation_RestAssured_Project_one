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

public class DeleteUnderwritingCategories extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-380 [CRM_API] Verify the DELETE - uw_categories valid data")
    public void testDeleteUWCategoryUsingValidId() { //DELETE - uw_categories valid data
        UnderwritingCategories createUnderwritingCategories = requestPayload.createUnderwritingCategory();

        Response responseCC = RestAssured.given().spec(repoSpec)
                .when()
                .body(createUnderwritingCategories)
                .post(APIConstants.CREATE_UW_CATEGORY);
        System.out.println("Status Code: " + responseCC.getStatusCode());
        responseCC.getBody().prettyPrint();
        Assert.assertEquals(responseCC.statusCode(), 201);

        String uwCategoryId = JsonPath.read(responseCC.asString(), "$.result.id").toString();
        String updateURI = APIConstants.DELETE_UW_CATEGORY.replace("{id}", uwCategoryId);
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-382 [CRM_API] Verify the DELETE - uw_categories invalid data")
    public void testDeleteUWCategoryUsingInvalidId() {//DELETE - uw_categories invalid data
        String invalidId = "99999";
        String updateURI = APIConstants.DELETE_UW_CATEGORY.replace("{id}",  invalidId);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        String statusCode = String.valueOf(response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString  = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid Status Code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "CM_R_UW_CATEGORY_NOT_FOUND", "Invalid error code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no underwriting category related data for the given category id -" + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-384 [CRM_API] Verify the DELETE - uw_categories by id empty data")
    public void testDeleteUWCategoryUsingEmptyId() {//DELETE - uw_categories by id empty data
        String URI = APIConstants.DELETE_UW_CATEGORY.replace("{id}",  "");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .delete(URI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 405);
    }
}
