import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserCreate;
import model.UserLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Login User")
public class UserLoginTest {
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp () {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {if(accessToken != null){userClient.deleteUser(accessToken);}}

    @Test
    @DisplayName("Check that the user can login with valid data")
    public void userCanLoginWithValidData(){
        UserCreate userCreate = new UserCreate(userClient.email, userClient.password, userClient.name);
        Response response = userClient.createUser(userCreate);
        UserLogin userLogin = new UserLogin(userCreate.email, userCreate.password);
        Response loginResponse = userClient.loginUser(userLogin);
        accessToken = loginResponse.getBody().path("accessToken");
        assertEquals("Status code is not 200!", 200, loginResponse.getStatusCode());
        assertEquals(true, loginResponse.getBody().path("success"));
        assertEquals(userClient.email, loginResponse.getBody().path("user.email"));
        assertEquals(userClient.name, loginResponse.getBody().path("user.name"));
        assertNotNull(loginResponse.getBody().path("accessToken"));
        assertNotNull(loginResponse.getBody().path("refreshToken"));
    }
}
