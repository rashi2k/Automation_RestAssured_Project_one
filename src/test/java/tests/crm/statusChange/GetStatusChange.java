package tests.crm.statusChange;

import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.StatusChange;
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

public class GetStatusChange extends BaseTest {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-862 [R2] [CRM_API] [Get] change status with valid payload")
    public void testGetCustomerStatusWithValidPayLoad() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(APIConstants.GET_CUSTOMER_STATUS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.activeStatusId0, "Invalid id0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].code").toString()), PropertiesUtils.activeStatusCode0, "Invalid code0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].status").toString()), PropertiesUtils.activeStatus0, "Invalid status0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].id").toString()), PropertiesUtils.activeStatusId1, "Invalid id1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].code").toString()), PropertiesUtils.activeStatusCode1, "Invalid code1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].status").toString()), PropertiesUtils.activeStatus1, "Invalid status1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].id").toString()), PropertiesUtils.activeStatusId2, "Invalid id2");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].code").toString()), PropertiesUtils.activeStatusCode2, "Invalid code2");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].status").toString()), PropertiesUtils.activeStatus2, "Invalid status2");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[3].id").toString()), PropertiesUtils.activeStatusId3, "Invalid id3");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[3].code").toString()), PropertiesUtils.activeStatusCode3, "Invalid code3");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[3].status").toString()), PropertiesUtils.activeStatus3, "Invalid status3");
        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("IQ-863 [R2] [CRM_API] [Get] change status by restricted-trans by valid payload")
    public void testGetRestrictedTransactionsWithValidPayLoad() {
        Response response = RestAssured.given().spec(repoSpec)
                .when()
                .get(APIConstants.GET_RESTRICTED_TRANSACTIONS);
        System.out.println("Status Code: " + response.getStatusCode());
        response.getBody().prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);

        SoftAssert softAssert = new SoftAssert();
        String responseToString = response.asString();
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].id").toString()), PropertiesUtils.resTransId0, "Invalid id0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].code").toString()), PropertiesUtils.resTransCode0, "Invalid code0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[0].status").toString()), PropertiesUtils.resTransStatus0, "Invalid status0");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].id").toString()), PropertiesUtils.resTransId1, "Invalid id1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].code").toString()), PropertiesUtils.resTransCode1, "Invalid code1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[1].status").toString()), PropertiesUtils.resTransStatus1, "Invalid status1");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].id").toString()), PropertiesUtils.resTransId2, "Invalid id2");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].code").toString()), PropertiesUtils.resTransCode2, "Invalid code2");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[2].status").toString()), PropertiesUtils.resTransStatus2, "Invalid status2");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[3].id").toString()), PropertiesUtils.resTransId3, "Invalid id3");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[3].code").toString()), PropertiesUtils.resTransCode3, "Invalid code3");
        softAssert.assertEquals((JsonPath.read(responseToString, "$.result[3].status").toString()), PropertiesUtils.resTransStatus3, "Invalid status3");
        softAssert.assertAll();
    }
}
