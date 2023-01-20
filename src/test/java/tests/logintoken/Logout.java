package tests.logintoken;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseTest;


public class Logout extends BaseTest {
    @Test()
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login and get token")
    public void logout() {
        Response response = RestAssured.given().spec(repoSpec).when().body(requestPayload.logout()).post("/security-center/auth/logout");
        assertActions.verifyStatusCode(response);

        removeToken();
        System.out.println("------------------  "+System.getProperty("token"));
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 200);
    }

    private void removeToken() {
        try {
            System.clearProperty("token");
            System.clearProperty("refresh_token");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
