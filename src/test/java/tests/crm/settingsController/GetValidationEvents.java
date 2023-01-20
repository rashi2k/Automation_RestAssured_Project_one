package tests.crm.settingsController;

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

public class GetValidationEvents extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1335 [R1][CRM_API][Setting] [Get] settings_default Verifying with valid payload")
    public void testGetAllValidationEvents() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(APIConstants.GET_ALL_VALIDATION_EVENTS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].id").toString()), PropertiesUtils.validationEventId1, "Invalid id1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].code").toString()), PropertiesUtils.validationEventCode1, "Invalid code1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].name").toString()), PropertiesUtils.validationEventName1, "Invalid name1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].id").toString()), PropertiesUtils.validationEventId2, "Invalid id2");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].code").toString()), PropertiesUtils.validationEventCode2, "Invalid code2");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].name").toString()), PropertiesUtils.validationEventName2, "Invalid name2");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[2].id").toString()), PropertiesUtils.validationEventId3, "Invalid id3");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[2].code").toString()), PropertiesUtils.validationEventCode3, "Invalid code3");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[2].name").toString()), PropertiesUtils.validationEventName3, "Invalid name3");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1352 [R1][CRM_API][Setting] Verify the GET - /references/validation-events with valid data")
    public void testGetValidationEvents() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS + "?page=0&size=2";

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].id").toString()), PropertiesUtils.validationEventId1, "Invalid id1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].code").toString()), PropertiesUtils.validationEventCode1, "Invalid code1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].name").toString()), PropertiesUtils.validationEventName1, "Invalid name1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].id").toString()), PropertiesUtils.validationEventId2, "Invalid id2");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].code").toString()), PropertiesUtils.validationEventCode2, "Invalid code2");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[1].name").toString()), PropertiesUtils.validationEventName2, "Invalid name2");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1366 [R1][CRM_API][Setting] Verify the GET - Settings Default with passing both valid eventCode and customerTypeId")
    public void testGetValidationEventsWithValidEventCodeAndCustomerTypeId() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", PropertiesUtils.validationEventCode1).replace("{customerTypeId}", PropertiesUtils.GET_CUS_TYPES_id0);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String responseToString = response.asString();
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
                softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].eventCode").toString()), PropertiesUtils.validationEventCode1, "Invalid Event code [" + i + "]");
                break;
            }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1367 [R1][CRM_API][Setting] Verify the GET - Setting Default with passing both invalid eventCode and customerTypeId")
    public void testGetValidationEventsWithInvalidEventCodeAndCustomerTypeId() {
        String invalidEventCode = "QA_ASA_EC";
        String invalidCustomerTypeId = "10000";
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", invalidEventCode).replace("{customerTypeId}", invalidCustomerTypeId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.errorCode").toString()), "VALIDATION_EVENT_FIELD", "Invalid error");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.description").toString()), "Customer type can not be found for customer type id - " + invalidCustomerTypeId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1368 [R1][CRM_API][Setting] Verify the GET - Setting Default with passing valid eventCode and invalid customerTypeId")
    public void testGetValidationEventsWithValidEventCodeAndInvalidCustomerTypeId() {
        String invalidCustomerTypeId = "10000";
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", PropertiesUtils.validationEventCode1).replace("{customerTypeId}", invalidCustomerTypeId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.errorCode").toString()), "VALIDATION_EVENT_FIELD", "Invalid error");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.description").toString()), "Customer type can not be found for customer type id - " + invalidCustomerTypeId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1369 [[R1][CRM_API][Setting] Verify the GET - Setting Default with passing invalid eventCode and valid customerTypeId")
    public void testGetValidationEventsWithInvalidEventCodeAndValidCustomerTypeId() {
        String invalidEventCode = "QA_ASA_EC";
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", invalidEventCode).replace("{customerTypeId}", PropertiesUtils.GET_CUS_TYPES_id0);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.errorCode").toString()), "VALIDATION_EVENT_FIELD", "Invalid error");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.description").toString()), "Validation event can not find for event code - " + invalidEventCode, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1370 [R1][CRM_API][Setting] Verify the GET - Setting Default with passing both null eventCode and customerTypeId")
    public void testGetValidationEventsWithNullEventCodeAndCustomerTypeId() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", " ").replace("{customerTypeId}", " ");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1371 [R1][CRM_API][Setting] Verify the GET - Setting Default with passing null eventCode and valid customerTypeId")
    public void testGetValidationEventsWithNullEventCodeAndValidCustomerTypeId() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", " ").replace("{customerTypeId}", PropertiesUtils.GET_CUS_TYPES_id0);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1372 [R1][CRM_API][Setting] Verify the GET - Setting Default with passing valid eventCode and null customerTypeId")
    public void testGetValidationEventsWithValidEventCodeAndNullCustomerTypeId() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_EVENT_CODE.replace("{eventCode}", PropertiesUtils.validationEventCode1).replace("{customerTypeId}", " ");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1343 [R1][CRM_API][Setting] Verify the GET - references/validation-events/search with Valid event_code")
    public void testSearchValidationEventWithValidEventCode() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_SEARCH_BY.replace("{searchBy}", PropertiesUtils.validationEventCode1);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].id").toString()), PropertiesUtils.validationEventId1, "Invalid id1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].code").toString()), PropertiesUtils.validationEventCode1, "Invalid code1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].name").toString()), PropertiesUtils.validationEventName1, "Invalid name1");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1344 [R1][CRM_API][Setting] Verify the GET - references/validation-events/search with Valid event_name")
    public void testSearchValidationEventWithValidEventName() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_SEARCH_BY.replace("{searchBy}", PropertiesUtils.validationEventName1);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].id").toString()), PropertiesUtils.validationEventId1, "Invalid id1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].code").toString()), PropertiesUtils.validationEventCode1, "Invalid code1");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result[0].name").toString()), PropertiesUtils.validationEventName1, "Invalid name1");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1346 [R1][CRM_API][Setting] Verify the GET - references/validation-events/search with Similar data for event_code")
    public void testSearchValidationEventWithSimilarEventCode() {
        String similarEventCode = PropertiesUtils.validationEventCode1.substring(0, 4);
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_SEARCH_BY.replace("{searchBy}", similarEventCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String responseToString = response.asString();
        int length = JsonPath.read(responseToString, "$.result.length()");
        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            softAssert.assertTrue((JsonPath.read(responseToString, "$.result[" + i + "].code").toString()).contains(similarEventCode), "Invalid Event code [" + i + "]");
            break;
        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1347 [R1][CRM_API][Setting] Verify the GET - references/validation-events/search with Similar data for event_name")
    public void testSearchValidationEventWithSimilarEventName() {
        String similarEventName = PropertiesUtils.validationEventName1.substring(0, 4);
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_SEARCH_BY.replace("{searchBy}", similarEventName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String responseToString = response.asString();
        int length = JsonPath.read(responseToString, "$.result.length()");
        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            softAssert.assertTrue((JsonPath.read(responseToString, "$.result[" + i + "].name").toString()).contains(similarEventName), "Invalid Event name [" + i + "]");
            break;
        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1348 [R1][CRM_API][Setting] Verify the GET - references/validation-events/search with an Invalid value for event_code")
    public void testSearchValidationEventWithInvalidEventCode() {
        String invalidEventCode = "aut_test_1";
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_SEARCH_BY.replace("{searchBy}", invalidEventCode);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1342 [R1][CRM_API][Setting] Verify the GET - references/validation-events/search with Invalid event_name")
    public void testSearchValidationEventWithInvalidEventName() {
        String invalidEventName = "AUT TEST 1";
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_SEARCH_BY.replace("{searchBy}", invalidEventName);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.result").toString()), "[]", "Invalid status code");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1345 [R1][CRM_API][Setting] Verify the GET - references/validation-events/search with Null value")
    public void testSearchValidationEventWithNullValue() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_SEARCH_BY.replace("{searchBy}", " ");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1338 [R1][CRM_API][Setting] Get Validate the process of Field Configurations (Edit Allowed/Edit Rule) with valid CustomerTypeId")
    public void testGetFieldConfigurationsWithValidCustomerTypeId() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_CUSTOMER_TYPE_ID.replace("{customerTypeId}",PropertiesUtils.GET_CUS_TYPES_id0);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        String responseToString = response.asString();
        int length = JsonPath.read(responseToString, "$.result.length()");

        SoftAssert softAssert = new SoftAssert();
        for (int i = 0; i < length; i++) {
            softAssert.assertEquals((JsonPath.read(responseToString, "$.result[" + i + "].customerTypeId").toString()), PropertiesUtils.GET_CUS_TYPES_id0, "Invalid cus type id [" + i + "]");
            break;
        }
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1339 [R1][CRM_API][Setting] Verify the GET - Field Configurations (Edit Allowed/Edit Rule) with Not existing in customerTypeId")
    public void testGetFieldConfigurationsWithInvalidCustomerTypeId() {
        String invalidCustomerTypeId = "10000";
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_CUSTOMER_TYPE_ID.replace("{customerTypeId}",invalidCustomerTypeId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);

        String statusCode = String.valueOf(response.getStatusCode());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.httpCode").toString()), statusCode, "Invalid status code");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.errorCode").toString()), "CUSTOMER_TYPE_NOT_FOUND", "Invalid error");
        softAssert.assertEquals((JsonPath.read(response.asString(), "$.description").toString()), "Customer type can not be found for customer type id - " + invalidCustomerTypeId, "Invalid description");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1340 [R1][CRM_API] [Setting] Verify the GET process of getting Field Configurations (Edit Allowed/Edit Rule) with Empty customerTypeId")
    public void testGetFieldConfigurationsWithEmptyCustomerTypeId() {
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_CUSTOMER_TYPE_ID.replace("{customerTypeId}"," ");

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-1341 [R1][CRM_API][Setting] Verify the GET - Field Configurations (Edit Allowed/Edit Rule) Invalid data type with customer TypeId")
    public void testGetFieldConfigurationsWithInvalidCustomerTypeIdDataType() {
        String invalidCustomerTypeId = "abcd";
        String updatedURI = APIConstants.GET_VALIDATION_EVENTS_CUSTOMER_TYPE_ID.replace("{customerTypeId}",invalidCustomerTypeId);

        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(updatedURI);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 400);
    }



}
