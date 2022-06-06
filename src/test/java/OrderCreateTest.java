import client.OrderClient;
import client.PreconditionClient;
import client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.OrderCreate;
import model.UserCreate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Create Order")
public class OrderCreateTest {

    private OrderClient orderClient;
    private UserClient userClient;
    private PreconditionClient preconditionClient;
    private String invalidIngredient = "61c0cd1f82001bda23234aa6d";


    private List<Object> ingredients;
    private List<Object> emptyIngredients;
    private List<Object> listOfInvalidIngredients = new ArrayList<>();
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        preconditionClient = new PreconditionClient();
        UserCreate userCreate = new UserCreate(userClient.email, userClient.password, userClient.name);
        Response response = userClient.createUser(userCreate);
        accessToken = response.getBody().path("accessToken");
        Response getIngredients = preconditionClient.getIngredients();
        ingredients = getIngredients.getBody().path("data._id");
    }

    @After
    public void tearDown() {if (accessToken != null) {userClient.deleteUser(accessToken);}}

    @Test
    @DisplayName("Check that the order can be created with valid ingredients and with authorization")
    public void orderCanBeCreatedWithValidData() {
        OrderCreate orderCreate = new OrderCreate(ingredients);
        Response response = orderClient.createOrder(orderCreate, accessToken);
        assertEquals("Status code is not 200!", 200, response.getStatusCode());
        assertEquals(true, response.getBody().path("success"));
        assertNotNull(response.getBody().path("order.number"));
    }

    @Test
    @DisplayName("Check that the order cannot be created without authorization") // Согласно документации заказ может создавать только авторизованный юзер
    public void orderCanNotBeCreatedWithOutAuthorization() {
        OrderCreate orderCreate = new OrderCreate(ingredients);
        Response response = orderClient.createOrder(orderCreate, null);
        assertEquals("Status code is not 401!", 401, response.getStatusCode());
        assertEquals(false, response.getBody().path("success"));
        assertEquals("You should be authorised", response.getBody().path("message"));
    }

    @Test
    @DisplayName("Check that the order cannot be created without ingredients")
    public void orderCanBeCreatedWithOutIngredients() {
        OrderCreate orderCreate = new OrderCreate(emptyIngredients);
        Response response = orderClient.createOrder(orderCreate, accessToken);
        assertEquals("Status code is not 400!", 400, response.getStatusCode());
        assertEquals(false, response.getBody().path("success"));
        assertEquals("Ingredient ids must be provided", response.getBody().path("message"));
    }

    @Test
    @DisplayName("Check that the order cannot be created with invalid ingredients")
    public void orderCanBeCreatedWithInvalidIngredients() {
        listOfInvalidIngredients.add(invalidIngredient);
        OrderCreate orderCreate = new OrderCreate(listOfInvalidIngredients);
        Response response = orderClient.createOrder(orderCreate, accessToken);
        assertEquals("Status code is not 500!", 500, response.getStatusCode());
    }
}
