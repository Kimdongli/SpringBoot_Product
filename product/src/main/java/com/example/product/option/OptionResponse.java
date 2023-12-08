package com.example.product.option;

import com.example.product.product.Product;
import com.example.product.product.ProductRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

public class OptionResponse {


    private Long id;

    private Long productId;
    private Product product;

    // 옵션 상품 이름
    private String optionName;

    // 옵션 상품 가격
    private Long price;

    // 옵션 상품 수량
    private Long quantity;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class FindByProductIdDTD{
        private Long id;

        private Long productId;

        // 옵션 상품 이름
        private String optionName;

        // 옵션 상품 가격
        private Long price;

        // 옵션 상품 수량
        private Long quantity;

        // 이 생성자는 Option 객체를 FindByProductIdDTD 객체로 변환합니다.
        public FindByProductIdDTD(Option option){
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity= option.getQuantity();
        }
    }

    // 'FindAllDTO'는 모든 옵션을 찾을 때 사용되는 Data Transfer Object입니다.
    @Setter
    @Getter
    @NoArgsConstructor
    public static class FindAllDTO{
        private Long id;

        private Long productId;

        // 옵션 상품 이름
        private String optionName;

        // 옵션 상품 가격
        private Long price;

        // 옵션 상품 수량
        private Long quantity;

        // 이 생성자는 Option 객체를 FindAllDTO 객체로 변환합니다.
        public FindAllDTO(Option option){
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity= option.getQuantity();
        }
    }

    // 'CreateDTO'는 새로운 옵션을 생성할 때 사용되는 Data Transfer Object입니다.
    @Setter
    @Getter
    public static class CreateDTO{


        // 옵션 상품 이름
        private String optionName;

        // 옵션 상품 가격
        private Long price;

        // 옵션 상품 수량
        private Long quantity;

        private Long product;

        public CreateDTO(){

        }
        // 이 생성자는 Option 객체를 CreateDTO 객체로 변환합니다.
        public CreateDTO(Option option){
            this.product = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity= option.getQuantity();
        }
        // 'toEntity' 메소드는 'CreateDTO' 객체를 'Option' 엔티티로 변환합니다.
        // 이 메소드는 옵션을 생성할 때 사용됩니다.
        public Option toEntity(ProductRepository productRepository) {
            Product product = productRepository.findById(this.product)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + this.product));
            return new Option(this.optionName, this.price, this.quantity, product);
        }
    }
}
