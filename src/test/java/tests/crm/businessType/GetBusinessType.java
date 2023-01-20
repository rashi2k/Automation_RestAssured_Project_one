package tests.crm.businessType;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.BusinessType;
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

import static com.informatics.utils.PropertiesUtils.businessType;

public class GetBusinessType extends BaseTest
{

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-935 [R3] [CRM_API] [Get] business type by id with valid id")
    public void testGetBusinessTypeWithValidID()
    {
        BusinessType createBusinessType = requestPayload.CreateBusinessType(businessType);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBusinessType)
                .post(APIConstants.CREATE_BUSINESS_TYPE);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);

        String saveId = JsonPath.read(response.asString(), "$.result.id").toString();
        String updatedURI = APIConstants.GET_BUSINESS_TYPE_ID.replace("{id}", saveId);
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), saveId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.businessType").toString()), PropertiesUtils.businessType, "Invalid businessType");

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-936 [R3] [CRM_API] [Get] business type by id with invalid id")
    public void testGetBusinessTypeWithInvalidID()
    {
        String invalidId = "a";
        String updatedURI = APIConstants.GET_BUSINESS_TYPE_ID.replace("{id}", invalidId);
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-937 [R3] [CRM_API] [Get] business type by id with empty id")
    public void testGetBusinessTypeWithEmptyID()
    {
        String updatedURI = APIConstants.GET_BUSINESS_TYPE_ID.replace("{id}", "null");
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-933 [R3] [CRM_API] [Get] business type with valid payload")
    public void testGetBusinessTypeWithValidPayload()
    {
        String updatedURI = APIConstants.GET_BUSINESS_TYPE + "?page=0&size=100";

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-934 [R3] [CRM_API] [Get] Business type validate if only entered number of records come for a page")
    public void testGetBusinessTypeWithRecordForAPage() {
        int RecordSize = 10;
        String updatedURI = APIConstants.GET_BUSINESS_TYPE + "?page=0&size=" + RecordSize;

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String responseToString = response.asString();
        int length = JsonPath.read(responseToString, "$.result.length()");

        int recordCount = 0;
        for (int i = 0; i < RecordSize; i++) {
            if (length <= RecordSize) {
                recordCount = recordCount + 1;
            }
        }
        System.out.println("Record Count: " + recordCount);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(recordCount, RecordSize, "Invalid record count");
        softAssert.assertAll();
    }

}
