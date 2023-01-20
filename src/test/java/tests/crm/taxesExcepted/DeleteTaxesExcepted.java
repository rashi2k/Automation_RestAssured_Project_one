package tests.crm.taxesExcepted;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.CustomerBasicInfo;
import com.informatics.pojos.crm.TaxesExcepted;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;

import java.util.Map;

public class DeleteTaxesExcepted extends BaseTest
{
    public String customerType, salutationCode, genderCode, nationality, preferLang, customerId;
    public Integer uwCategoryId, customerGroupId, idType, relationshipTypeId, civilStatusId, taxTypeId;
    public String idNumber = "1322323222";

    //DELETE METHOD DELETED FROM SWAGGER
//    @BeforeMethod
//    public void initiate() {
//        //get taxTypeId - customerId
//        Response response = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_TAX);
//        taxTypeId = Integer.parseInt(JsonPath.read(response.asString(), "$.result[0].taxTypeId").toString());
//
//        //get cus title
//        Response responseA = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_TITLE);
//        salutationCode = JsonPath.read(responseA.asString(), "$.result[0].salutationCode").toString();
//
//        //get cus gender
//        Response responseB = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_GENDER);
//        genderCode = JsonPath.read(responseB.asString(), "$.result[1].genderCode").toString();
//
//        //get cus nationality
//        Response responseC = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_NATIONALITY);
//        nationality = JsonPath.read(responseC.asString(), "$.result[0].nationality").toString();
//
//        //get cus civil status
//        Response responseD = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_CIVIL_STATUS);
//        civilStatusId = Integer.parseInt(JsonPath.read(responseD.asString(), "$.result[0].civilStatusId").toString());
//
//        //get cus prefered language
//        Response responseE = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_PREF_LANG);
//        preferLang = JsonPath.read(responseE.asString(), "$.result[0].iso3Code").toString();
//
//        //get cus id type
//        Response responseF = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_ID_TYPE);
//        idType = Integer.parseInt(JsonPath.read(responseF.asString(), "$.result[0].identityTypeCode").toString());
//
//        //get cus relationship type
//        Response responseG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_RELATIONSHIP_TYPE);
//        relationshipTypeId = Integer.parseInt(JsonPath.read(responseG.asString(), "$.result[0].id").toString());
//
//        //get customer group ID
//        Response responseCG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_ALL_CUSTOMER_GROUPS);
//        customerGroupId = Integer.parseInt(JsonPath.read(responseCG.asString(), "$.result[0].id").toString());
//
//        //get uw category  ID
//        Response responseUG = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_UW_CATEGORIES);
//        uwCategoryId = Integer.parseInt(JsonPath.read(responseUG.asString(), "$.result[0].id").toString());
//
//        //get cus type
//        Response responseCT = RestAssured.given().spec(repoSpec).when().get(APIConstants.GET_CUS_TYPE);
//        customerType = JsonPath.read(responseCT.asString(), "$.result[0].typeCode").toString();
//
//        //create test customer to get cusID
//        CustomerBasicInfo createTestCustomer = requestPayload.createTestCustomer(customerType, uwCategoryId, customerGroupId, salutationCode, genderCode, nationality, civilStatusId, preferLang, idType, idNumber);
//        Response responseCI = RestAssured.given().spec(repoSpec).when()
//                .body(createTestCustomer)
//                .post(APIConstants.CREATE_CUS_BASIC_INFO);
//        customerId = JsonPath.read(responseCI.asString(), "$.result.customerId").toString();
//
//    }

//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-536 [R2][CRM_API] Verify the DELETE - taxes_excepted with valid input in id ")
//    public void testDeleteTaxesExceptedWithValidId() {
//        String updateURI = APIConstants.PUT_TAXES_EXCEPTED_CUSTOMER_ID.replace("{customerId}", customerId);
//
//        Map createTaxesExcepted = requestPayload.newTaxesExcepted(Integer.parseInt(customerId), taxTypeId);
//        Response response = RestAssured.given().spec(repoSpec)
//                .when()
//                .body(createTaxesExcepted)
//                .put(updateURI);
//        System.out.println("Status Code: " + response.getStatusCode());
//        response.getBody().prettyPrint();
//        Assert.assertEquals(response.statusCode(), 200);
//
//      //  String saveTaxesExceptedId = JsonPath.read(response.asString(), "$.result.id").toString();
//        String updateDeleteURI = APIConstants.DELETE_TAXES_EXCEPTED_EXCEPTED_ID.replace("{customerId}", customerId);
//
//        Response resDelete = RestAssured.given().spec(repoSpec)
//                .when()
//                .delete(updateDeleteURI);
//        System.out.println("Status Code: " + resDelete.getStatusCode());
//        resDelete.getBody().prettyPrint();
//        Assert.assertEquals(resDelete.statusCode(), 200);
//
 //   }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-535 [R2][CRM_API] Verify the DELETE - taxes_excepted with null input in id")
//    public void testDeleteTaxesExceptedWithNullId() {
//
//        String updatePutURI = APIConstants.DELETE_TAXES_EXCEPTED_EXCEPTED_ID.replace("{customerId}", "");
//        Response resUpdate = RestAssured.given().spec(repoSpec)
//                .when()
//                .delete(updatePutURI);
//        System.out.println("Status Code: " + resUpdate.getStatusCode());
//        resUpdate.getBody().prettyPrint();
//        Assert.assertEquals(resUpdate.statusCode(), 400);
//
//    }
//
//    @Test
//    @Severity(SeverityLevel.NORMAL)
//    @Description("IQ-537 [R2][CRM_API] Verify the DELETE - taxes_excepted with invalid input in id")
//    public void testDeleteTaxesExceptedWithInvalidId() {
//
//        String invalidId = "99999";
//        String updatePutURI = APIConstants.DELETE_TAXES_EXCEPTED_EXCEPTED_ID.replace("{customerId}", invalidId);
//
//        Response resUpdate = RestAssured.given().spec(repoSpec)
//                .when()
//                .delete(updatePutURI);
//        System.out.println("Status Code: " + resUpdate.getStatusCode());
//        resUpdate.getBody().prettyPrint();
//        Assert.assertEquals(resUpdate.statusCode(), 400);
//
//    }
}
