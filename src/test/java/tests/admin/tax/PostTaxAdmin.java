package tests.admin.tax;

import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import tests.BaseTest;

public class PostTaxAdmin extends BaseTest{
	 @Test()
	    @Severity(SeverityLevel.NORMAL)
	    @Description("Verify login and get token")
	    public void postTax() {
	        Response response = RestAssured.given().spec(repoSpec).when().body(requestPayload.tax()).post("/control-center/tax");
	        assertActions.verifyStatusCode(response);
	    }
}
