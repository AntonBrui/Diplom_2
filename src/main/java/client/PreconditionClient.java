package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PreconditionClient extends BaseHttpClient {

    @Step("Get ingredients")
    public Response getIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(BASE_URL + "/ingredients");
    }
}
