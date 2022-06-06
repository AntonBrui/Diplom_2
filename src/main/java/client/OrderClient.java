package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrderCreate;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseHttpClient{
    private final String PATH = BASE_URL + "/orders";

    @Step("Create Orders")
    public Response createOrder(OrderCreate orderCreate, String accessToken) {
        if(accessToken != null){
            return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", accessToken)
                    .and()
                    .body(orderCreate)
                    .when()
                    .post(PATH);
        } return given()
                .header("Content-type", "application/json")
                .and()
                .body(orderCreate)
                .when()
                .post(PATH);
    }

    @Step("Get Orders")
    public Response getOrder(String accessToken) {
        if(accessToken != null){
            return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", accessToken)
                    .when()
                    .get(PATH);
        } return given()
                .header("Content-type", "application/json")
                .when()
                .get(PATH);
    }
}
