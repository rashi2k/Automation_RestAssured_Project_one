package tests.crm.relationshipTypes;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.RelationshipTypes;
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

public class DeleteRelationshipTypes extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-307 [R1] [CRM_API] Verify the DELETE - relationship-types/{id} with Valid Pay Load")
    public void testDeleteRelationshipTypesWithValidPayLoad() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();

        Response responseCT = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + responseCT.getStatusCode());
        responseCT.getBody().prettyPrint();
        Assert.assertEquals(responseCT.statusCode(), 201);

        String saveRelationshipTypeId = JsonPath.read(responseCT.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.DELETE_RELATIONSHIP_TYPE.replace("{id}", saveRelationshipTypeId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString  = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result").toString()), saveRelationshipTypeId, "Invalid id");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-308 [R1] [CRM_API] Verify the DELETE - relationship-types/{id} with empty Pay Load")
    public void testDeleteRelationshipTypesWithEmptyPayLoad() {
        String updatedURI = APIConstants.DELETE_RELATIONSHIP_TYPE.replace("{id}", " ");

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400, 405);

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-309 [R1] [CRM_API] Verify the DELETE - relationship-types/{id} using invalid id")
    public void testDeleteRelationshipTypesWithInvalidId() {
        String invalidId = "a";
        String updatedURI = APIConstants.DELETE_RELATIONSHIP_TYPE.replace("{id}", invalidId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .delete(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }
}
