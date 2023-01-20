package tests.crm.additionalInfoFields;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerAdditionalInfo;
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

public class GetAdditionalInfoFields extends BaseTest {

    public String fieldId = PropertiesUtils.fieldId0;

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-902 [R2] [CRM_API] Verify the GET - additional_info_fields with valid ID")
    public void testGetAdditionalInfoFieldWithValidId() {
        String updateURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_FIELD.replace("{id}", fieldId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), fieldId, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), PropertiesUtils.fieldName0, "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isMandatory").toString()), PropertiesUtils.fieldIsMandatory0, "Invalid isMandatory");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.isAvailableInList").toString()), PropertiesUtils.fieldIsAvailableInList0, "Invalid isAvailableInList");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.possibleValues.refInfoId").toString()), PropertiesUtils.pvRefInfoId0, "Invalid refInfoId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.possibleValues.code").toString()), PropertiesUtils.pvCode0, "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.possibleValues.name").toString()), PropertiesUtils.pvName0, "Invalid name");
        try {
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result.possibleValues.lovs").toString()), PropertiesUtils.pvLovs0, "Invalid lovs");
        }catch (Exception e) {

        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-903 [R2] [CRM_API] Verify the GET - additional_info_fields with invalid ID")
    public void testGetAdditionalInfoFieldWithInvalidId() {
        String invalidId = "9999999";
        String updateURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_FIELD.replace("{id}", invalidId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "Cm_R_ADDITIONAL_INFO_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no cm r additional info match with given id - " + invalidId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-901 [R2] [CRM_API] Verify the GET - additional_info_fields with null ID")
    public void testGetAdditionalInfoFieldWithNullId() {
        String updateURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_FIELD.replace("{id}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-898 [R2] [CRM_API] Verify the GET - additional_info_fields with valid Customer Type")
    public void testGetAdditionalInfoFieldWithValidCustomerType() {
        String updateURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_FIELD_CUS_TYPE.replace("{type}", PropertiesUtils.GET_CUS_TYPES_ID_name);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), fieldId, "Invalid id0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].name").toString()), PropertiesUtils.fieldName0, "Invalid name0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isMandatory").toString()), PropertiesUtils.fieldIsMandatory0, "Invalid isMandatory0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isAvailableInList").toString()), PropertiesUtils.fieldIsAvailableInList0, "Invalid isAvailableInList0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].possibleValues.refInfoId").toString()), PropertiesUtils.pvRefInfoId0, "Invalid refInfoId0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].possibleValues.code").toString()), PropertiesUtils.pvCode0, "Invalid code0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].possibleValues.name").toString()), PropertiesUtils.pvName0, "Invalid name0");
        try {
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].possibleValues.lovs").toString()), PropertiesUtils.pvLovs0, "Invalid lovs0");
        }catch (Exception e) {
        }
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].id").toString()), PropertiesUtils.fieldId1, "Invalid id1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].name").toString()), PropertiesUtils.fieldName1, "Invalid name1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].isMandatory").toString()), PropertiesUtils.fieldIsMandatory1, "Invalid isMandatory1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].isAvailableInList").toString()), PropertiesUtils.fieldIsAvailableInList1, "Invalid isAvailableInList1");
        try {
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].possibleValues").toString()), PropertiesUtils.possibleValues1, "Invalid possibleValues1");
        }catch (Exception e) {
        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-900 [R2] [CRM_API] Verify the GET - additional_info_fields with invalid Customer Type")
    public void testGetAdditionalInfoFieldWithInvalidCustomerType() {
        String invalidType = "99999";
        String updateURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_FIELD_CUS_TYPE.replace("{type}", invalidType);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FUNCTION_CODE", "Invalid error");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no function code match with given value - " + invalidType, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-897 [R2] [CRM_API] Verify the GET - additional_info_fields with null Customer Type")
    public void testGetAdditionalInfoFieldWithNullCustomerType() {
        String updateURI = APIConstants.GET_CUSTOMER_ADDITIONAL_INFO_FIELD_CUS_TYPE.replace("{type}", "");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updateURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }
}
