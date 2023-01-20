package tests.crm.settingsController;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.ValidationEvents;
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

import java.util.ArrayList;
import java.util.List;

public class PutValidationEvents extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1336 [R1][CRM_API][Setting] Verify the PUT - Setting filled with passing both valid eventCode and customerTypeId")
    public void testPutValidationEventWithValidEventCodeAndCustomerTypeId() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        Boolean isUsed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString()));
        Boolean isApprovalRequired = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString()));

        int fieldId = 1;
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, isUsed, isEditAllowed, isApprovalRequired);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].fieldId").toString()), String.valueOf(fieldId), "Invalid fieldId");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isUsed").toString()), String.valueOf(isUsed), "Invalid isUsed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isEditAllowed").toString()), String.valueOf(isEditAllowed), "Invalid isEditAllowed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isApprovalRequired").toString()), String.valueOf(isApprovalRequired), "Invalid isApprovalRequired");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1337 [R1][CRM_API][Setting] Verify the PUT - Setting filled with passing both invalid eventCode and customerTypeId")
    public void testPutValidationEventWithInvalidEventCodeAndCustomerTypeId() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        Boolean isUsed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString()));
        Boolean isApprovalRequired = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString()));

        int fieldId = 1;
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, isUsed, isEditAllowed, isApprovalRequired);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        String invalidEventCode = "QA_ASA_EC";
        String invalidCustomerTypeId = "10000";
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", invalidEventCode).replace("{customerTypeId}", invalidCustomerTypeId));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.errorCode").toString()), "CM_R_CUSTOMER_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.description").toString()), "There is no cm r customer type related data for the given customer type id - " + invalidCustomerTypeId, "Invalid description");
        softAssert.assertAll();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1349 [R1][CRM_API][Setting] Verify the PUT - Setting filled with passing valid eventCode and invalid customerTypeId")
    public void testPutValidationEventWithValidEventCodeAndInvalidCustomerTypeId() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        Boolean isUsed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString()));
        Boolean isApprovalRequired = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString()));

        int fieldId = 1;
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, isUsed, isEditAllowed, isApprovalRequired);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        String invalidCustomerTypeId = "10000";
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", invalidCustomerTypeId));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.errorCode").toString()), "CM_R_CUSTOMER_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.description").toString()), "There is no cm r customer type related data for the given customer type id - " + invalidCustomerTypeId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1350 [R1][CRM_API][Setting] Verify the PUT - Setting filled with passing invalid eventCode and valid customerTypeId")
    public void testPutValidationEventWithInvalidEventCodeAndValidCustomerTypeId() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        Boolean isUsed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString()));
        Boolean isApprovalRequired = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString()));

        int fieldId = 1;
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, isUsed, isEditAllowed, isApprovalRequired);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        String invalidEventCode = "QA_ASA_EC";
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", invalidEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.errorCode").toString()), "CM_R_VALIDATION_EVENT_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.description").toString()), "There is no cm r validation event for the given code - " + invalidEventCode, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1351 [R1][CRM_API][Setting] Verify the PUT - Setting filled with passing both null eventCode and customerTypeId")
    public void testPutValidationEventWithNullEventCodeAndCustomerTypeId() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        Boolean isUsed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString()));
        Boolean isApprovalRequired = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString()));

        int fieldId = 1;
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, isUsed, isEditAllowed, isApprovalRequired);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", " ").replace("{customerTypeId}", " "));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400, 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1356 [R1][CRM_API][Setting] Verify the PUT - Setting filled by updating multiple fields at the same time")
    public void testPutValidationEventWithMultipleFields() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        Boolean isUsed0 = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed0 = Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString());
        Boolean isApprovalRequired0 = Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString());
        Boolean isUsed1 = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[1].isUsed").toString()));
        Boolean isEditAllowed1 = Boolean.valueOf(JsonPath.read(res.asString(), "$.result[1].isEditAllowed").toString());
        Boolean isApprovalRequired1 = Boolean.valueOf(JsonPath.read(res.asString(), "$.result[1].isApprovalRequired").toString());

        int fieldId0 = 1;
        int fieldId1 = 3;
        ValidationEvents updateValidationEvent0 = requestPayload.updateValidationEvents(fieldId0, isUsed0, isEditAllowed0, isApprovalRequired0);
        ValidationEvents updateValidationEvent1 = requestPayload.updateValidationEvents(fieldId1, isUsed1, isEditAllowed1, isApprovalRequired1);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent0);
        requestBody.add(updateValidationEvent1);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].fieldId").toString()), String.valueOf(fieldId0), "Invalid fieldId");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isUsed").toString()), String.valueOf(isUsed0), "Invalid isUsed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isEditAllowed").toString()), String.valueOf(isEditAllowed0), "Invalid isEditAllowed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isApprovalRequired").toString()), String.valueOf(isApprovalRequired0), "Invalid isApprovalRequired");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].fieldId").toString()), String.valueOf(fieldId1), "Invalid fieldId");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].isUsed").toString()), String.valueOf(isUsed1), "Invalid isUsed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].isEditAllowed").toString()), String.valueOf(isEditAllowed1), "Invalid isEditAllowed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].isApprovalRequired").toString()), String.valueOf(isApprovalRequired1), "Invalid isApprovalRequired");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1357 [R1][CRM_API][Setting] Verify the PUT - Setting filled with valid data to the isUsed field")
    public void testPutValidationEventWithValidIsUsed() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        res.getBody().prettyPrint();
        Boolean isUsed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString()));
        Boolean isApprovalRequired = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString()));

        int fieldId = 1;
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, isUsed, isEditAllowed, isApprovalRequired);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].fieldId").toString()), String.valueOf(fieldId), "Invalid fieldId");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isUsed").toString()), String.valueOf(isUsed), "Invalid isUsed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isEditAllowed").toString()), String.valueOf(isEditAllowed), "Invalid isEditAllowed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isApprovalRequired").toString()), String.valueOf(isApprovalRequired), "Invalid isApprovalRequired");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1358 [R1][CRM_API][Setting] Verify the PUT - Setting filled with valid data to the isEditAllowed field")
    public void testPutValidationEventWithValidIsEditAllowed() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        res.getBody().prettyPrint();
        Boolean isUsed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString()));
        Boolean isApprovalRequired = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString()));

        int fieldId = 1;
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, isUsed, isEditAllowed, isApprovalRequired);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].fieldId").toString()), String.valueOf(fieldId), "Invalid fieldId");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isUsed").toString()), String.valueOf(isUsed), "Invalid isUsed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isEditAllowed").toString()), String.valueOf(isEditAllowed), "Invalid isEditAllowed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isApprovalRequired").toString()), String.valueOf(isApprovalRequired), "Invalid isApprovalRequired");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1359 [R1][CRM_API][Setting] Verify the PUT - Setting filled with valid data to the isApprovalRequired field")
    public void testPutValidationEventWithValidIsApprovalRequired() {
        String validEventCode = PropertiesUtils.validationEventCode1;
        String validCustomerTypeId = PropertiesUtils.GET_CUS_TYPES_id0;
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId);

        Response res = RestAssured.given().spec(repoSpec).when().get(updatedURI);
        res.getBody().prettyPrint();
        Boolean isUsed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isUsed").toString()));
        Boolean isEditAllowed = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isEditAllowed").toString()));
        Boolean isApprovalRequired = !(Boolean.valueOf(JsonPath.read(res.asString(), "$.result[0].isApprovalRequired").toString()));

        int fieldId = 1;
        ValidationEvents updateValidationEvent = requestPayload.updateValidationEvents(fieldId, isUsed, isEditAllowed, isApprovalRequired);
        List<ValidationEvents> requestBody = new ArrayList<ValidationEvents>();
        requestBody.add(updateValidationEvent);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .body(requestBody)
                .put(APIConstants.UPDATE_VALIDATION_EVENT.replace("{eventCode}", validEventCode).replace("{customerTypeId}", validCustomerTypeId));
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].fieldId").toString()), String.valueOf(fieldId), "Invalid fieldId");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isUsed").toString()), String.valueOf(isUsed), "Invalid isUsed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isEditAllowed").toString()), String.valueOf(isEditAllowed), "Invalid isEditAllowed");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].isApprovalRequired").toString()), String.valueOf(isApprovalRequired), "Invalid isApprovalRequired");
        softAssert.assertAll();
    }
}
