package com.example.product.cart;

import com.example.product.option.Option;
import com.example.product.product.Product;



import lombok.Getter;
import lombok.Setter;



import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {


    // ** UpdateDTO: Cart의 업데이트 요청을 처리하기 위해 사용되며 생성자에서는 장바구니 리스트를 받아 각 장바구니를 CartDTO로 변환한 후 dtoList에 저장.
    @Setter
    @Getter
    public static class UpdateDTO{
        private List<CartDTO> dtoList;

        private Long totalPrice;

        public UpdateDTO(List<Cart> dtoList) {
            this.dtoList = dtoList.stream().map(CartDTO::new).collect(Collectors.toList());
            this.totalPrice = totalPrice;
        }

        @Setter
        @Getter
        public class CartDTO{
            private Long cartId;

            private Long optionId;

            public String optionName;

            private Long quantity;

            private Long price;

            public CartDTO(Cart cart) {
                this.cartId = cart.getId();
                this.optionId = cart.getOption().getId();
                this.optionName = cart.getOption().getOptionName();
                this.quantity = cart.getQuantity();
                this.price = cart.getPrice();
            }
        }
    }


    // ** Cart 모든 항목을 조회하여 요청하기 위해 사용됨.생성자에서는 장바구니 리스트를 받아 각 장바구니를 CartDTO로 변환한 후 dtoList에 저장합니다.
    @Setter
    @Getter
    public static class FindAllDTO {
        List<ProductDTO> products;

        private Long totalPrice;

        // 전체 원소중에 하나 원소가 이렇게 작동할것이다.(람다식)
        // ** 1. 최초로 Cart(Entity)로 받는다.
        // ** 2. Cart(Entity) Option 이 있고
        // ** 3. Option안에는 Product가있다.
        // ** new ProductDTO(product) 가 밑에 있는 ProductDTO 와 맞아 떨어져야한다.
        public FindAllDTO(List<Cart> cartList) {
            this.products = cartList.stream()
                    .map(cart -> cart.getOption().getProduct()).distinct()
                    .map(product -> new ProductDTO(cartList, product)).collect(Collectors.toList());


            this.totalPrice = cartList.stream()
                    .mapToLong(cart -> cart.getOption().getPrice() * cart.getQuantity())
                    .sum();
        }

        // 상품과 그 상품에 관련된 장바구니 항목들을 나타내는 DTO입니다. 상품 ID, 상품 이름, 장바구니 항목들을 나타내는
        // CartDTO의 리스트를 필드로 가집니다. 생성자에서는 장바구니 리스트와 상품 엔티티를 받아 필드값을 설정합니다.
        @Setter
        @Getter
        public class ProductDTO {
            private Long id;

            private String productName;


            List<CartDTO> cartDTOS;

            public ProductDTO(List<Cart> cartList,Product product) {
                this.id = product.getId();
                this.productName = product.getProductName();
                this.cartDTOS = cartList.stream()
                        .filter(cart -> cart.getOption().getProduct().getId() == product.getId())
                        .map(CartDTO::new).collect(Collectors.toList());
            }
            // 장바구니의 각 항목을 나타내는 DTO입니다. 장바구니 ID, 옵션 DTO, 수량, 가격을 필드로 가집니다. 생성자에서는 장바구니 엔티티를 받아 필드값을 설정합니다.
            @Setter
            @Getter
            public class CartDTO{
                private Long id;

                private OptionDTO optionDTO;

                private Long quantity;
                private Long price;

                public CartDTO(Cart cart) {
                    this.id = cart.getId();
                    this.quantity= cart.getQuantity();
                    this.optionDTO = new OptionDTO(cart.getOption());
                    this.price = cart.getPrice();
                }

                // 옵션을 나타내는 DTO입니다. 옵션 ID, 옵션 이름, 가격을 필드로 가집니다. 생성자에서는 옵션 엔티티를 받아 필드값을 설정합니다.
                @Setter
                @Getter
                public class OptionDTO{

                    private Long id;
                    private String optionName;
                    private Long price;

                    public OptionDTO(Option option) {
                        this.id = option.getId();
                        this.optionName = option.getOptionName();
                        this.price = option.getPrice();
                    }
                }
            }
        }
    }
}
