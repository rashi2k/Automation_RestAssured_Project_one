package tests.admin.tax;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import tests.BaseTest;

public class GetTaxAdmin extends BaseTest{
	  private static Logger Log = LogManager.getLogger(GetTaxAdmin.class.getName());

	    @Test()
	    @Severity(SeverityLevel.NORMAL)
	    @Description("Verify getting all the Taxes ")
	    public void testGetAllTaxes() {
	        Response response = RestAssured.given().spec(repoSpec).when().get("/control-center/tax");
	        response.getBody().prettyPrint();
	        Assert.assertEquals(response.statusCode(), 200);
	        JsonPath responseJson = response.jsonPath();
	        List<String> result = responseJson.get("result");
	        Assert.assertEquals(result.size()==0, false);
	    }
}
