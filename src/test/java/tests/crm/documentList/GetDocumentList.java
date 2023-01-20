package tests.crm.documentList;

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

public class GetDocumentList extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-865 [R2][CRM_API] Verify the GET - Document list with Valid Pay Load Verifying with inputs in Mandatory fields->Individual ")
    public void testGetDocumentListWithValidTypeIndividual() {
        String updatedURI = APIConstants.GET_DOCUMENT_LIST.replace("{type}", "individual");

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.id, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].moduleId").toString()), PropertiesUtils.moduleId, "Invalid moduleId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].moduleCode").toString()), PropertiesUtils.moduleCode, "Invalid moduleCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].subModuleId").toString()), PropertiesUtils.subModuleId, "Invalid subModuleId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].subModuleCode").toString()), PropertiesUtils.subModuleCode, "Invalid subModuleCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].functionId").toString()), PropertiesUtils.functionId, "Invalid functionId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].functionCode").toString()), PropertiesUtils.functionCode, "Invalid functionCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentId").toString()), PropertiesUtils.documentId, "Invalid documentId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentCode").toString()), PropertiesUtils.documentCode, "Invalid documentCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentType").toString()), PropertiesUtils.documentType, "Invalid documentType");

        softAssert.assertAll();

        }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-869 [R2][CRM_API] Verify the GET - Document list with Valid Pay Load Verifying with inputs in Mandatory fields ->Corporate")
    public void testGetDocumentListWithValidTypeCorporate() {
        String updatedURI = APIConstants.GET_DOCUMENT_LIST.replace("{type}", "corporate");

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.idCorporate, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].moduleId").toString()), PropertiesUtils.moduleIdCorporate, "Invalid moduleId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].moduleCode").toString()), PropertiesUtils.moduleCodeCorporate, "Invalid moduleCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].subModuleId").toString()), PropertiesUtils.subModuleIdCorporate, "Invalid subModuleId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].subModuleCode").toString()), PropertiesUtils.subModuleCodeCorporate, "Invalid subModuleCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].functionId").toString()), PropertiesUtils.functionIdCorporate, "Invalid functionId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].functionCode").toString()), PropertiesUtils.functionCodeCorporate, "Invalid functionCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentId").toString()), PropertiesUtils.documentIdCorporate, "Invalid documentId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentCode").toString()), PropertiesUtils.documentCodeCorporate, "Invalid documentCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentType").toString()), PropertiesUtils.documentTypeCorporate, "Invalid documentType");

        softAssert.assertAll();

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-872 [R2][CRM_API] Verify the GET - Document list with Valid Pay Load Verifying with inputs in Mandatory fields->Special")
    public void testGetDocumentListWithValidTypeSpecial() {
        String updatedURI = APIConstants.GET_DOCUMENT_LIST.replace("{type}", "special");

        Response responseCG = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);

        System.out.println("Status Code: " + responseCG.getStatusCode());
        responseCG.getBody().prettyPrint();
        Assert.assertEquals(responseCG.statusCode(), 200);

        String responseToString = responseCG.asString();
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.idSpecial, "Invalid id");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].moduleId").toString()), PropertiesUtils.moduleIdSpecial, "Invalid moduleId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].moduleCode").toString()), PropertiesUtils.moduleCodeSpecial, "Invalid moduleCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].subModuleId").toString()), PropertiesUtils.subModuleIdSpecial, "Invalid subModuleId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].subModuleCode").toString()), PropertiesUtils.subModuleCodeSpecial, "Invalid subModuleCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].functionId").toString()), PropertiesUtils.functionIdSpecial, "Invalid functionId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].functionCode").toString()), PropertiesUtils.functionCodeSpecial, "Invalid functionCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentId").toString()), PropertiesUtils.documentIdSpecial, "Invalid documentId");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentCode").toString()), PropertiesUtils.documentCodeSpecial, "Invalid documentCode");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].documentType").toString()), PropertiesUtils.documentTypeSpecial, "Invalid documentType");

        softAssert.assertAll();

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-876 [R2][CRM_API] Verify the GET -Document list by type with empty Pay Load")
    public void testGetDocumentListWithEmptyPayLoad() {
        String updatedURI = APIConstants.GET_DOCUMENT_LIST.replace("{type}", "null");

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
        softAssert.assertEquals((JsonPath.read(responseToString, "$.description").toString()), "There is no function code match with given value - null", "Invalid description");

        softAssert.assertAll();

    }

}

