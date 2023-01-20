package actions;

import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;

public class AssertActions {
	SoftAssert softAssert = new SoftAssert();
	public void verifyStatusCode(Response response) {
		softAssert.assertEquals(String.valueOf(response.getStatusCode()).startsWith("20"), true,
				"value of status code is" + response.getStatusCode());
	}

	public void verifyResponseBody(String actual, String expected, String description) {
		softAssert.assertEquals(actual, expected, description);

	}

	public void verifyResponseBody(float actual, float expected, String description) {
		softAssert.assertEquals(actual, expected, description);

	}

	public void verifyResponseBody(int actual, int expected, String description) {
		softAssert.assertEquals(actual, expected, description);

	}

	public void verifyResponseBody(double actual, double expected, String description) {
		softAssert.assertEquals(actual, expected, description);

	}

	public void verifyResponseBody(boolean actual, boolean expected, String description) {
		softAssert.assertEquals(actual, expected, description);

	}

}
