package com.example.product.cart;

import com.example.product.option.Option;
import com.example.product.user.User;
import lombok.Getter;
import lombok.Setter;

public class CartRequest {

    @Getter
    @Setter
    public static class SaveDTO{
        private Long optionId;
        private Long quantity;

        public Cart toEntity(Option option, User user){

            return Cart.builder()
                    .option(option)
                    .quantity(quantity)
                    .user(user)
                    .price(option.getPrice() * quantity)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UpdateDTO{
        private Long quantity;
        private Long cartId;
    }
}
