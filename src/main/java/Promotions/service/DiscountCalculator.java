package Promotions.service;

import Promotions.dto.CartItem;
import Promotions.dto.CartRequest;
import Promotions.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class DiscountCalculator {

    // Decides which rule to use based on the offer's type
    public int calculate(Promotion promo, CartRequest cart) {
        return switch (promo.type()) {
            case BUY_X_GET_Y -> buyXGetY(promo, cart);
            case FLAT_OFF -> flatOff(promo, cart);
            case PERCENT_OFF_CART -> percentOffCart(promo, cart);
        };
    }

    // Buy 2 get 1: every group of 3 items gives 1 free
    private int buyXGetY(Promotion promo, CartRequest cart) {
        CartItem item = findItem(cart, promo.productId());
        if (item == null) {
            return 0;
        }
        int groupSize = promo.minQuantity() + promo.rewardValue();   // 2 + 1 = 3
        int groups = item.quantity() / groupSize;                    // how many full groups
        int freeItems = groups * promo.rewardValue();                // 1 free per group
        return freeItems * item.price();
    }

    // Rs 20 off if the product is in the cart (at least minQuantity)
    private int flatOff(Promotion promo, CartRequest cart) {
        CartItem item = findItem(cart, promo.productId());
        if (item == null || item.quantity() < promo.minQuantity()) {
            return 0;
        }
        int itemTotal = item.quantity() * item.price();
        return Math.min(promo.rewardValue(), itemTotal);   // never discount more than the item costs
    }

    // 10% off the whole bill if it's at least minCartAmount
    private int percentOffCart(Promotion promo, CartRequest cart) {
        int total = cartTotal(cart);
        if (total < promo.minCartAmount()) {
            return 0;
        }
        return total * promo.rewardValue() / 100;
    }

    public int cartTotal(CartRequest cart) {
        int total = 0;
        for (CartItem item : cart.items()) {
            total += item.quantity() * item.price();
        }
        return total;
    }

    private CartItem findItem(CartRequest cart, String productId) {
        for (CartItem item : cart.items()) {
            if (item.productId().equals(productId)) {
                return item;
            }
        }
        return null;
    }
}