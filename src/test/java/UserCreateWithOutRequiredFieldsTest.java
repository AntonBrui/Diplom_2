import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserCreate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)

@DisplayName("Create User without required fields")
public class UserCreateWithOutRequiredFieldsTest {

    private UserClient userClient;

    private final String email;
    private final String password;
    private final String name;

    public UserCreateWithOutRequiredFieldsTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"test001@test.com", "pa$$w0rd", ""},
                {"test001@test.com", "pa$$w0rd", null},
                {"test001@test.com", "", "UserName"},
                {"test001@test.com", null, "UserName"},
                {"", "pa$$w0rd", "UserName"},
                {null, "pa$$w0rd", "UserName"}

        };
    }

    @Before
    public void setUp() {userClient = new UserClient();}

    @Test
    @DisplayName("Check that the user can not be created without required fields")
    public void userCanNotBeCreatedWithOutRequiredFields() {
        UserCreate user = new UserCreate(email, password, name);
        Response response = userClient.createUser(user);
        assertEquals("Status Code is not 403", 403, response.getStatusCode());
        assertEquals(false, response.getBody().path("success"));
        assertEquals("Email, password and name are required fields", response.getBody().path("message"));
    }
}