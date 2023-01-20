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

public class PutRelationshipTypes extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-304 [R1] [CRM_API] Verify the PUT - Relationship-Types by id with Valid Pay Load")
    public void testPutRelationshipTypeWithValidPayLoad() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();
        Response responseCT = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + responseCT.getStatusCode());
        responseCT.getBody().prettyPrint();
        Assert.assertEquals(responseCT.statusCode(), 201);

        String saveRelationshipTypeId = JsonPath.read(responseCT.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_RELATIONSHIP_TYPE.replace("{id}", saveRelationshipTypeId);

        RelationshipTypes updateRelationshipType = requestPayload.createRelationshipType();
        updateRelationshipType.setId(saveRelationshipTypeId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateRelationshipType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveRelationshipTypeId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), updateRelationshipType.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), updateRelationshipType.getIsActive(), "Invalid status");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-305 [R1] [CRM_API] Verify the PUT - Relationship-Types by id with empty Pay Load")
    public void testPutRelationshipTypeWithEmptyPayLoad() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();
        Response responseCT = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + responseCT.getStatusCode());
        responseCT.getBody().prettyPrint();
        Assert.assertEquals(responseCT.statusCode(), 201);

        String saveRelationshipTypeId = JsonPath.read(responseCT.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_RELATIONSHIP_TYPE.replace("{id}", saveRelationshipTypeId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 404);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Null Id Found", "Invalid message");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NULL", "Invalid error");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-306 [R1] [CRM_API] Verify the PUT - Relationship-Types by id using invalid id and valid name")
    public void testPutRelationshipTypeWithInvalidIdAndValidName() {
        String invalidId = "x";
        String updatedURI = APIConstants.UPDATE_RELATIONSHIP_TYPE.replace("{id}", invalidId);

        RelationshipTypes updateRelationshipType = requestPayload.createRelationshipType();
        updateRelationshipType.setId(invalidId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateRelationshipType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-789 [R1] [CRM_API] [Put] relationship types with invalid id and valid is Active")
    public void testPutRelationshipTypeWithInvalidIdAndValidIsActive() {
        String invalidId = "x";
        String updatedURI = APIConstants.UPDATE_RELATIONSHIP_TYPE.replace("{id}", invalidId);

        RelationshipTypes updateRelationshipType = requestPayload.createRelationshipType();
        updateRelationshipType.setId(invalidId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateRelationshipType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-790 [R1] [CRM_API] [Put] relationship types with valid and invalid is Active")
    public void testPutRelationshipTypeWithValidIdAndInvalidIsActive() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();
        Response responseCT = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + responseCT.getStatusCode());
        responseCT.getBody().prettyPrint();
        Assert.assertEquals(responseCT.statusCode(), 201);

        String saveRelationshipTypeId = JsonPath.read(responseCT.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_RELATIONSHIP_TYPE.replace("{id}", saveRelationshipTypeId);

        RelationshipTypes updateRelationshipType = requestPayload.createRelationshipType();
        updateRelationshipType.setId(saveRelationshipTypeId);
        updateRelationshipType.setIsActive("eeee");

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateRelationshipType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-790 [R1] [CRM_API] [Put] relationship types validate names for duplicate values")
    public void testPutRelationshipTypeWithDuplicateNames() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();
        Response responseCT = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + responseCT.getStatusCode());
        responseCT.getBody().prettyPrint();
        Assert.assertEquals(responseCT.statusCode(), 201);

        Response responseA = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_ALL_RELATIONSHIP_TYPES);
        String relationshipTypeName = JsonPath.read(responseA.asString(), "$.result[0].name").toString();

        String saveRelationshipTypeId = JsonPath.read(responseCT.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.UPDATE_RELATIONSHIP_TYPE.replace("{id}", saveRelationshipTypeId);

        RelationshipTypes updateRelationshipType = requestPayload.createRelationshipType();
        updateRelationshipType.setId(saveRelationshipTypeId);
        updateRelationshipType.setName(relationshipTypeName);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateRelationshipType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 406);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "ORA-00001: unique constraint (SICI.LINK_PARTY_TYPE_UNIQUE) violated\n", "Invalid message");
        softAssert.assertAll();
    }

}
