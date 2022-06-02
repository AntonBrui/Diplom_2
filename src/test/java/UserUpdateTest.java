import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserCreate;
import model.UserLogin;
import model.UserUpdate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@DisplayName("Update User")
public class UserUpdateTest {
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp () {
        userClient = new UserClient();
        UserCreate userCreate = new UserCreate(userClient.email, userClient.password, userClient.name);
        UserLogin userLogin = new UserLogin(userCreate.email, userCreate.password);
        Response createResponse = userClient.createUser(userCreate);
        Response loginResponse = userClient.loginUser(userLogin);
        accessToken = loginResponse.getBody().path("accessToken");
    }

    @After
    public void tearDown() {if(accessToken != null){userClient.deleteUser(accessToken);}}

    @Test
    @DisplayName("Check that the user can update profile with Authorization")
    public void userCanUpdateProfile(){
        UserUpdate userUpdate = new UserUpdate("1" + userClient.email, "1" + userClient.name);
        Response updateResponse = userClient.updateUser(userUpdate, accessToken);
        assertEquals("Status code is not 200!", 200, updateResponse.getStatusCode());
        assertEquals(true, updateResponse.getBody().path("success"));
        assertEquals("1" + userClient.email, updateResponse.getBody().path("user.email"));
        assertEquals("1" + userClient.name, updateResponse.getBody().path("user.name"));

    }

    @Test
    @DisplayName("Check that the user cannot update profile without Authorization")
    public void userCanNotUpdateProfile(){
        UserUpdate userUpdate = new UserUpdate("1" + userClient.email, "1" + userClient.name);
        Response updateResponse = userClient.updateUser(userUpdate, null);
        assertEquals("Status code is not 401!", 401, updateResponse.getStatusCode());
        assertEquals(false, updateResponse.getBody().path("success"));
        assertEquals("You should be authorised", updateResponse.getBody().path("message"));
    }
}
