package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.UserCreate;
import model.UserLogin;
import model.UserUpdate;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseHttpClient {
    private final String PATH = BASE_URL + "/auth";

    Random random = new Random();
    public final String email = "something" + random.nextInt(100000) + "@yandex.ru";
    public final String password = RandomStringUtils.randomAlphabetic(10);
    public final String name = RandomStringUtils.randomAlphabetic(10);

    @Step("Create User")
    public Response createUser(UserCreate user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(PATH + "/register");
    }

    @Step("Login User")
    public Response loginUser(UserLogin userLogin){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userLogin)
                .when()
                .post(PATH + "/login");
    }

    @Step("Update User")
    public Response updateUser(UserUpdate userUpdate, String accessToken){
        if(accessToken != null){
            return given()
                    .header("Content-type", "application/json")
                    .header("Authorization", accessToken)
                    .and()
                    .body(userUpdate)
                    .when()
                    .patch(PATH + "/user");
        } return given()
                .header("Content-type", "application/json")
                .and()
                .body(userUpdate)
                .when()
                .patch(PATH + "/user");
    }

    @Step("Delete User")
    public Response deleteUser(String token){
        return given()
                .header("Authorization", token)
                .when()
                .delete(PATH + "/user");
    }
}
