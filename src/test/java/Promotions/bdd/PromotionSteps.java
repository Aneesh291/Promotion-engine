package Promotions.bdd;

import Promotions.dto.CartItem;
import Promotions.dto.CartRequest;
import Promotions.dto.CartResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class PromotionSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    private String storeId;
    private final List<CartItem> items = new ArrayList<>();
    private MvcResult result;

    @Given("a cart for store {string}")
    public void aCartForStore(String storeId) {
        this.storeId = storeId;
    }

    @Given("the cart has {int} of {string} priced at {int}")
    public void theCartHas(int quantity, String productId, int price) {
        items.add(new CartItem(productId, quantity, price));
    }

    @When("the cart is checked out")
    public void theCartIsCheckedOut() throws Exception {
        String body = jsonMapper.writeValueAsString(new CartRequest(storeId, items));
        result = mockMvc.perform(post("/api/promotions/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int status) {
        assertEquals(status, result.getResponse().getStatus());
    }

    @Then("the total discount is {int}")
    public void theTotalDiscountIs(int discount) throws Exception {
        assertEquals(discount, response().totalDiscount());
    }

    @Then("the final amount is {int}")
    public void theFinalAmountIs(int amount) throws Exception {
        assertEquals(amount, response().finalAmount());
    }

    @Then("the applied promotion is {string}")
    public void theAppliedPromotionIs(String name) throws Exception {
        assertEquals(name, response().appliedPromotionName());
    }

    private CartResponse response() throws Exception {
        return jsonMapper.readValue(result.getResponse().getContentAsString(), CartResponse.class);
    }
}