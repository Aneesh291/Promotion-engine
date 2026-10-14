package Promotions.controller;

import Promotions.dto.CartRequest;
import Promotions.dto.CartResponse;
import Promotions.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping("/apply")
    public CartResponse apply(@Valid @RequestBody CartRequest cart) {
        return promotionService.apply(cart);
    }
}