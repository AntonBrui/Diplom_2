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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;


@DisplayName("Get Order")
public class OrderGetTest {

    private OrderClient orderClient;
    private UserClient userClient;
    private PreconditionClient preconditionClient;
    private List<Object> ingredients;
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
        OrderCreate orderCreate = new OrderCreate(ingredients);
        Response orderCreateResponse = orderClient.createOrder(orderCreate, accessToken);
    }

    @After
    public void tearDown() {if (accessToken != null) {userClient.deleteUser(accessToken);}}

    @Test
    @DisplayName("Check receiving orders wit authorization")
    public void orderCanBereceivedWithAuthorization() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getOrder(accessToken);
        List<Object> orders = response.getBody().path("orders");
        assertEquals("Status code is not 200!", 200, response.getStatusCode());
        assertEquals(true, response.getBody().path("success"));
        assertThat(orders, hasSize(greaterThan(0)));
    }

    @Test
    @DisplayName("Check receiving orders without authorization")
    public void ordersCannotBeReceivedWithoutAuthorization() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getOrder(null);
        assertEquals("Status code is not 401!", 401, response.getStatusCode());
        assertEquals(false, response.getBody().path("success"));
        assertEquals("You should be authorised", response.getBody().path("message"));
    }
}