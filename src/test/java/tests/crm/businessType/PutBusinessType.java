package tests.crm.businessType;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.BusinessType;
import com.informatics.pojos.crm.RelationshipTypes;
import com.informatics.utils.PropertiesUtils;
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


public class PutBusinessType extends BaseTest
{
    String businessType = PropertiesUtils.businessType;

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-944 [R3] [CRM_API] [Put] business type by id with valid payload")
    public void testPutBusinessTypeWithValidPayload()
    {
        BusinessType createBusinessType = requestPayload.CreateBusinessType(businessType);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBusinessType)
                .post(APIConstants.CREATE_BUSINESS_TYPE);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        String saveBusinessTypeId = JsonPath.read(response.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.PUT_BUSINESS_TYPE.replace("{id}", saveBusinessTypeId);

        BusinessType updateBusinessType = requestPayload.CreateBusinessType(businessType);
        updateBusinessType.setId(saveBusinessTypeId);
        updateBusinessType.setBusinessType(PropertiesUtils.updateBusinessType);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBusinessType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveBusinessTypeId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.businessType").toString()),PropertiesUtils.updateBusinessType, "Invalid businessType");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-945 [R3] [CRM_API] [Put] business type by id with empty payload")
    public void testPutBusinessTypeWithEmptyPayload()
    {
        String updatedURI = APIConstants.PUT_BUSINESS_TYPE.replace("{id}", "");

        BusinessType updateBusinessType = requestPayload.CreateBusinessType(businessType);
        updateBusinessType.setBusinessType(null);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBusinessType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 405);

//        SoftAssert softAssert = new SoftAssert();
//        String responseToString = responseCG.asString();
//        softAssert.assertEquals((JsonPath.read(responseToString, "$.message").toString()), "Null Id Found", "Invalid message");
//        softAssert.assertEquals((JsonPath.read(responseToString, "$.error").toString()), "ID_NULL", "Invalid error");
//        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-948 [R3] [CRM_API] [Put] business type by id with invalid id and valid businessType")
    public void testPutBusinessTypeWithInvalidIdAndValidBusinessType()
    {
        String invalidId = "a";
        String updatedURI = APIConstants.PUT_BUSINESS_TYPE.replace("{id}", invalidId);

        BusinessType updateBusinessType = requestPayload.CreateBusinessType(businessType);
        updateBusinessType.setId(invalidId);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBusinessType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-950 [R3] [CRM_API] [Put] busines types by id with empty id and valid businessType")
    public void testPutBusinessTypeWithEmptyIdAndValidBusinessType()
    {
        String updatedURI = APIConstants.PUT_BUSINESS_TYPE.replace("{id}", "");

        BusinessType updateBusinessType = requestPayload.CreateBusinessType(businessType);
        updateBusinessType.setId(null);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBusinessType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 405);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-953 [R3] [CRM_API] [Put] business type by id with empty businessType and valid Id")
    public void testPutBusinessTypeWithValidIdAndEmptyBusinessType()
    {
        BusinessType createBusinessType = requestPayload.CreateBusinessType(businessType);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBusinessType)
                .post(APIConstants.CREATE_BUSINESS_TYPE);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        String saveBusinessTypeId = JsonPath.read(response.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.PUT_BUSINESS_TYPE.replace("{id}", saveBusinessTypeId);

        BusinessType updateBusinessType = requestPayload.CreateBusinessType(businessType);
        updateBusinessType.setBusinessType(null);

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .body(updateBusinessType)
                .put(updatedURI);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 404);
    }
}
