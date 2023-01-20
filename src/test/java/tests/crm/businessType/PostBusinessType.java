package tests.crm.businessType;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.BusinessType;
import com.informatics.pojos.crm.JointParticipation;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseTest;

import static com.informatics.utils.PropertiesUtils.businessType;

public class PostBusinessType extends BaseTest
{

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-938 [R3] [CRM_API] [Post] business type with valid payload")
    public void testPostBusinessTypeWithValidPayload()
    {
        BusinessType createBusinessType = requestPayload.CreateBusinessType(businessType);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(createBusinessType)
                .post(APIConstants.CREATE_BUSINESS_TYPE);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-941 [R3] [CRM_API] [Post] business type with empty payload")
    public void testPostBusinessTypeWithEmptyPayload()
    {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body("{ }")
                .post(APIConstants.CREATE_BUSINESS_TYPE);
        System.out.println("\n" + "Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }


}
