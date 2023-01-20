package tests.logintoken;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseTest;

public class Login extends BaseTest {
    private static Logger Log = LogManager.getLogger(Login.class.getName());
    public static String token;
    public static String refreshToken;

    @Test()
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login and get token")
    public void testGetToken() {
        Response response = RestAssured.given().spec(repoSpec).when().body(requestPayload.login()).post("/security-center/auth/login");
        assertActions.verifyStatusCode(response);
        JsonPath responseJson = response.jsonPath();
        token = responseJson.get("result.access_token");
        refreshToken = responseJson.get("result.refresh_token");

        setToken(token, refreshToken);
        System.out.println("------------------Access Token----------------\n "+System.getProperty("token"));
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.statusCode(), 200);
    }

    private void setToken(String token, String refreshToken) {
        try {
            System.setProperty("token", token);
            System.setProperty("refresh_token", refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
