package tests.crm.customerTypes;

import com.informatics.endpoints.APIConstants;
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

public class GetCustomerType extends BaseTest {
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-229 [R1][CRM_API] GET verify customer types with valid Type code & name")
    public void testGetCustomerTypesWithValidCode()
    { //GET - customer types with valid Type code & name
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(APIConstants.GET_CUS_TYPES);
        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.GET_CUS_TYPES_id0, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].typeCode").toString()), PropertiesUtils.GET_CUS_TYPES_typeCode0, "Invalid typeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].name").toString()), PropertiesUtils.GET_CUS_TYPES_name0, "Invalid name");

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].id").toString()), PropertiesUtils.GET_CUS_TYPES_id1, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].typeCode").toString()), PropertiesUtils.GET_CUS_TYPES_typeCode1, "Invalid typeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].name").toString()), PropertiesUtils.GET_CUS_TYPES_name1, "Invalid name");

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].id").toString()), PropertiesUtils.GET_CUS_TYPES_id2, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].typeCode").toString()), PropertiesUtils.GET_CUS_TYPES_typeCode2, "Invalid typeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].name").toString()), PropertiesUtils.GET_CUS_TYPES_name2, "Invalid name");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-235 [R1][CRM_API] GET- verify customer types {id} with valid iD")
    public void testGetCustomerTypesWithValidID() { //GET - customer types with valid ID
        String updatedURI = APIConstants.GET_CUS_TYPES_ID.replace("{id}", "1");
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), PropertiesUtils.GET_CUS_TYPES_ID_id, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.typeCode").toString()), PropertiesUtils.GET_CUS_TYPES_ID_typeCode, "Invalid typeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), PropertiesUtils.GET_CUS_TYPES_ID_name, "Invalid name");

        softAssert.assertAll();
    }

    /*
    //ISSUE WITH REQUEST URL WHEN SENDING EMPTY LOAD
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-244 [R1][CRM_API] GET verify customer types {id} with empty ID")
    public void testGetCustomerTypesWithEmptyID() { //GET - customer types with Empty ID
        String updatedURI = APIConstants.GET_CUS_TYPES_ID.replace("{id}", " ");
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.id").toString()), "1", "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.typeCode").toString()), "I", "Invalid typeCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result.name").toString()), "Individual", "Invalid name");

        softAssert.assertAll();
    }
     */

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-245 [R1][CRM_API] GET verify customer types {id} with invalid ID")
    public void testGetCustomerTypesWithInvalidId() { //GET - customer types with Invalid ID
        String updatedURI = APIConstants.GET_CUS_TYPES_ID.replace("{id}", "12345");
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "Cm_R_Customer_Type_Not_Found", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "Customer type not found for id - 12345", "Invalid description");

        softAssert.assertAll();
    }

   @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-804 [R1][CRM_API] GET verify customer types with valid data Type ")
    public void testGetCustomerTypesWithValidType() { //GET - customer types with Valid Type
        String updatedURI = APIConstants.GET_CUS_TYPES_TYPE.replace("{type}", "Individual");
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_id0, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].name").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_name0, "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isMandatory").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_isMandatory0, "Invalid isMandatory");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].isAvailableInList").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_isAvailableInList0, "Invalid isAvailableInList");

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].possibleValues.refInfoId").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_possibleValues_refInfoId, "Invalid refInfoId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].possibleValues.code").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_possibleValues_code, "Invalid code");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].possibleValues.name").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_possibleValues_name, "Invalid name");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].possibleValues.lovs").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_possibleValues_lovs, "Invalid lovs");
        } catch (Exception e){

        }

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].id").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_id1, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].name").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_name1, "Invalid name");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].isMandatory").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_isMandatory1, "Invalid isMandatory");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].isAvailableInList").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_isAvailableInList1, "Invalid isAvailableInList");
        try{
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].possibleValues").toString()), PropertiesUtils.GET_CUS_TYPES_TYPE_possibleValues1 , "Invalid possibleValues");
        }catch (Exception e){

        }


        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-808 [R1][CRM_API] GET verify customer types with empty data")
    public void testGetCustomerTypesWithEmptyType() { //GET - customer types with Empty Type
        String updatedURI = APIConstants.GET_CUS_TYPES_TYPE.replace("{type}", " ");
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-806 [R1][CRM_API] GET verify customer types with invalid data Type ")
    public void testGetCustomerTypesWithInvalidType() { //GET - customer types with Invalid Type
        String updatedURI = APIConstants.GET_CUS_TYPES_TYPE.replace("{type}", "12345");
        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 400);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = responseCG.asString();

        softAssert.assertEquals((JsonPath.read(responseToString, "$.httpCode").toString()), "400", "Invalid httpCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.errorCode").toString()), "FUNCTION_CODE", "Invalid errorCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no function code match with given value - 12345", "Invalid description");


        softAssert.assertAll();
    }
}
