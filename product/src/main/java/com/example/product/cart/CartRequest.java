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

        // 'toEntity' 메소드는 'SaveDTO' 객체를 'Cart' 엔티티로 변환합니다.
        // 이 메소드는 장바구니에 상품을 추가할 때 사용됩니다.
        public Cart toEntity(Option option, User user){

            return Cart.builder()
                    .option(option)
                    .quantity(quantity)
                    .user(user)
                    .price(option.getPrice() * quantity)
                    .build();
        }
    }

    // 'UpdateDTO'는 장바구니의 상품 수량을 업데이트할 때 사용되는 Data Transfer Object입니다.
    @Getter
    @Setter
    public static class UpdateDTO{
        private Long quantity;
        private Long cartId;
    }
}
