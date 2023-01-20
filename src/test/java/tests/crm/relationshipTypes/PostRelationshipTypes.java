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

public class PostRelationshipTypes extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-216 [R1] [CRM_API] Verify the POST - Relationship-Types with Valid Pay Load")
    public void testPostRelationshipTypeWithValidPayLoad() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), createRelationshipType.getName(), "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isActive").toString()), createRelationshipType.getIsActive(), "Invalid status");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-226 [R1] [CRM_API] Verify the POST - Relationship-Types with empty Pay Load")
    public void testPostRelationshipTypeWithEmptyPayLoad() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-784 [R1] [CRM_API] [Post] -relationship_types with valid name and empty is Active")
    public void testPostRelationshipTypeWithValidNameAndEmptyIsActive() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();
        createRelationshipType.setIsActive(null);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-786 [R1] [CRM_API] [Post] tax_registration with valid name and invalid is Active")
    public void testPostRelationshipTypeWithValidNameAndInvalidIsActive() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();
        createRelationshipType.setIsActive("a");
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    /*
    //IGNORED - CAN NOT ADD INTEGER TO STRING
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-787 [R1] [CRM_API] [Post] relationship types with invalid name and valid is Active")
    public void testPostRelationshipTypeWithInvalidNameAndValidIsActive() {
        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();
        createRelationshipType.setName(1234);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-929 [R1] [CRM_API] [Post] relationship_types validating name for duplicate values.")
    public void testPostRelationshipTypeWithDuplicateNames() {
        //get existing name
        Response responseA = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_ALL_RELATIONSHIP_TYPES);
        String relationshipTypeName = JsonPath.read(responseA.asString(), "$.result[0].name").toString();

        RelationshipTypes createRelationshipType = requestPayload.createRelationshipType();
        createRelationshipType.setName(relationshipTypeName);
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createRelationshipType)
                .post(APIConstants.CREATE_RELATIONSHIP_TYPE);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 406);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "ORA-00001: unique constraint (SICI.LINK_PARTY_TYPE_UNIQUE) violated\n", "Invalid message");
        softAssert.assertAll();
    }
}
