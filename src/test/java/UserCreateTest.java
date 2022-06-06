import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserCreate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Create User")
public class UserCreateTest {

    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {if (accessToken != null) {userClient.deleteUser(accessToken);}}

    @Test
    @DisplayName("Check that the user can be created")
    public void userCanBeCreatedWithValidData() {
        UserCreate user = new UserCreate(userClient.email, userClient.password, userClient.name);
        Response response = userClient.createUser(user);
        assertEquals("Status code is not 200!", 200, response.getStatusCode());
        assertEquals(true, response.getBody().path("success"));
        assertEquals(userClient.email, response.getBody().path("user.email"));
        assertEquals(userClient.name, response.getBody().path("user.name"));
        assertNotNull(response.getBody().path("accessToken"));
        assertNotNull(response.getBody().path("refreshToken"));
        accessToken = response.getBody().path("accessToken");
        System.out.print("Response body is: " + response.asPrettyString());
    }

    @Test
    @DisplayName("Check that the user cannot be created with existing data")
    public void userCanNotBeCreatedWithExistingData() {
        UserCreate user = new UserCreate(userClient.email, userClient.password, userClient.name);
        Response firstUser = userClient.createUser(user);
        Response secondUser = userClient.createUser(user);
        accessToken = firstUser.getBody().path("accessToken");
        assertEquals("Status code is not 403!", 403, secondUser.getStatusCode());
        assertEquals(false, secondUser.getBody().path("success"));
        assertEquals("User already exists", secondUser.getBody().path("message"));
    }
}