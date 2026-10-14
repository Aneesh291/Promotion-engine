package Promotions.service;

import Promotions.dto.CartItem;
import Promotions.dto.CartRequest;
import Promotions.model.Promotion;
import Promotions.model.PromotionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountCalculatorTest {

    private final DiscountCalculator calculator = new DiscountCalculator();

    private static final LocalDate START = LocalDate.of(2026, 1, 1);
    private static final LocalDate END = LocalDate.of(2026, 12, 31);

    // ---------- sample offers ----------

    private Promotion buy2Get1Maggi() {
        return new Promotion(101L, "Buy 2 Get 1 Maggi", PromotionType.BUY_X_GET_Y,
                "S1", "MAGGI-70G", 2, 0, 1, START, END);
    }

    private Promotion cokeFlat20() {
        return new Promotion(205L, "Rs 20 off Coke", PromotionType.FLAT_OFF,
                "S1", "COKE-1L", 1, 0, 20, START, END);
    }

    private Promotion tenPercentAbove1000() {
        return new Promotion(310L, "10% off above Rs 1000", PromotionType.PERCENT_OFF_CART,
                "S1", null, 0, 1000, 10, START, END);
    }

    private CartRequest cart(CartItem... items) {
        return new CartRequest("S1", List.of(items));
    }

    // ---------- Buy 2 Get 1 ----------

    @Test
    void buy2Get1_threeItems_oneFree() {
        int discount = calculator.calculate(buy2Get1Maggi(),
                cart(new CartItem("MAGGI-70G", 3, 14)));
        assertEquals(14, discount);
    }

    @Test
    void buy2Get1_twoItems_noneFree() {
        int discount = calculator.calculate(buy2Get1Maggi(),
                cart(new CartItem("MAGGI-70G", 2, 14)));
        assertEquals(0, discount);
    }

    @Test
    void buy2Get1_sixItems_twoFree() {
        int discount = calculator.calculate(buy2Get1Maggi(),
                cart(new CartItem("MAGGI-70G", 6, 14)));
        assertEquals(28, discount);
    }

    // ---------- Flat off ----------

    @Test
    void flatOff_productInCart_givesDiscount() {
        int discount = calculator.calculate(cokeFlat20(),
                cart(new CartItem("COKE-1L", 1, 60)));
        assertEquals(20, discount);
    }

    @Test
    void flatOff_productNotInCart_noDiscount() {
        int discount = calculator.calculate(cokeFlat20(),
                cart(new CartItem("MAGGI-70G", 3, 14)));
        assertEquals(0, discount);
    }

    @Test
    void flatOff_neverMoreThanItemCost() {
        int discount = calculator.calculate(cokeFlat20(),
                cart(new CartItem("COKE-1L", 1, 10)));
        assertEquals(10, discount);
    }

    // ---------- Percent off cart ----------

    @Test
    void percentOff_belowMinimum_noDiscount() {
        int discount = calculator.calculate(tenPercentAbove1000(),
                cart(new CartItem("RICE-25KG", 1, 999)));
        assertEquals(0, discount);
    }

    @Test
    void percentOff_exactlyAtMinimum_applies() {
        int discount = calculator.calculate(tenPercentAbove1000(),
                cart(new CartItem("RICE-25KG", 1, 1000)));
        assertEquals(100, discount);
    }
}