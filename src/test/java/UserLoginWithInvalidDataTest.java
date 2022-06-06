import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserCreate;
import model.UserLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)

@DisplayName("Login with invalid data")
public class UserLoginWithInvalidDataTest {

    private UserClient userClient;
    private String accessToken;

    private final String email;
    private final String password;

    public UserLoginWithInvalidDataTest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"test003@test.com", "pa$$w0rd1"}, //авторизация с валидным email и невалидным паролем
                {"test003@test.com", ""}, //авторизация с валидным email и пустым паролем
                {"test003@test.com", null}, //авторизация с валидным email и null вместо пароля
                {"test00112@test.com", "pa$$w0rd"}, //авторизация с валидным паролем и невалидным email
                {"", "pa$$w0rd"}, //авторизация с валидным паролем и пустым email
                {null, "pa$$w0rd"} //авторизация с валидным паролем и null вместо email

        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {if (accessToken != null) {userClient.deleteUser(accessToken);}}

    @Test
    @DisplayName("Check that the user can not login with invalid data")
    public void userCanNotLoginWithInvalidData() {
        UserCreate user = new UserCreate("test003@test.com", "pa$$w0rd", userClient.name);
        Response createResponse = userClient.createUser(user);
        accessToken = createResponse.getBody().path("accessToken");
        UserLogin userLogin = new UserLogin(email, password);
        Response loginResponse = userClient.loginUser(userLogin);
        assertEquals("Status Code is not 401", 401, loginResponse.getStatusCode());
        assertEquals(false, loginResponse.getBody().path("success"));
        assertEquals("email or password are incorrect", loginResponse.getBody().path("message"));
    }
}
