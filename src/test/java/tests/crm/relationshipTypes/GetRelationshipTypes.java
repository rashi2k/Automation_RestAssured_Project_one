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

public class GetRelationshipTypes extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-232 [R1] [CRM_API] Verify the GET - Relationship-Types with Valid Pay Load")
    public void testGetAllRelationshipTypes() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();

        Response responseCT = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + responseCT.getStatusCode());
        responseCT.getBody().prettyPrint();
        Assert.assertEquals(responseCT.statusCode(), 201);

        String saveRelationshipTypeId = JsonPath.read(responseCT.asString(), "$.result.id").toString();

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(APIConstants.GET_ALL_RELATIONSHIP_TYPES);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        Boolean isRelationshipTypeFound = false;
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            String relationshipTypeId = JsonPath.read(responseToString, "$.result[" + i + "].id").toString();
            if (relationshipTypeId.equals(saveRelationshipTypeId)) {
                isRelationshipTypeFound = true;
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].id").toString()), saveRelationshipTypeId, "Invalid id");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()), createRelationshipType.getName(), "Invalid name");
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].isActive").toString()), createRelationshipType.getIsActive(), "Invalid status");
                break;
            }
        }
        softAssert.assertTrue(isRelationshipTypeFound, "Relationship Type not found");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-299 [R1] [CRM_API] Verify the GET - Relationship-Types by id with Valid Pay Load")
    public void testGetRelationshipTypeWithValidId() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();

        Response responseCT = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + responseCT.getStatusCode());
        responseCT.getBody().prettyPrint();
        Assert.assertEquals(responseCT.statusCode(), 201);

        String saveRelationshipTypeId = JsonPath.read(responseCT.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_RELATIONSHIP_TYPE_ID.replace("{id}", saveRelationshipTypeId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveRelationshipTypeId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createRelationshipType.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), createRelationshipType.getIsActive(), "Invalid status");
        softAssert.assertAll();
    }

    /*
    //    ISSUE WITH REQUEST URL WHEN SENDING EMPTY LOAD
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-301 [R1] [CRM_API] Verify the GET - Relationship-Types by id with empty Pay Load")
    public void testGetRelationshipTypeWithEmptyId() {
        String updatedURI = APIConstants.GET_RELATIONSHIP_TYPE_ID.replace("{id}", " ");

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }
    */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-303 [R1] [CRM_API] Verify the GET - Relationship-Types by id using invalid id")
    public void testGetRelationshipTypesWithInvalidId() {
        String invalidId = "x";
        String updatedURI = APIConstants.GET_RELATIONSHIP_TYPE_ID.replace("{id}", invalidId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-931 [R1] [CRM_API] [Get] relationship_types pages with valid payload")
    public void testGetRelationshipTypesPagesWithValidPayLoad() {
        int pageSize = 2;
        String updatedURI = APIConstants.GET_RELATIONSHIP_TYPE_PAGE + "?page=0&size=" + pageSize;

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String responseToString = response.asString();
        int length = JsonPath.read(responseToString, "$.result.length()");

        int recordCount = 0;
        for (int i = 0; i < pageSize; i++) {
            if (length <= pageSize) {
                recordCount = recordCount + 1;
            }
        }
        System.out.println("Record Count: " + recordCount);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(recordCount, pageSize, "Invalid record count");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-932 [R1] [CRM_API] [Get] relationship types pages with invalid number of records for pages")
    public void testGetRelationshipTypesPagesWithInvalidNoOfPages() {
        int pageSize = 1000;
        String updatedURI = APIConstants.GET_RELATIONSHIP_TYPE_PAGE + "?page=0&size=" + pageSize;

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String responseToString = response.asString();
        int length = JsonPath.read(responseToString, "$.result.length()");

        int recordCount = 0;
        for (int i = 0; i < pageSize; i++) {
            if (pageSize <= length) {
                recordCount = recordCount + 1;
            }
        }
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(recordCount < pageSize, "Invalid record count");
        softAssert.assertAll();
    }
}
