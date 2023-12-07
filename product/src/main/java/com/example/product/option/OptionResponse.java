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

        public FindByProductIdDTD(Option option){
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity= option.getQuantity();
        }
    }

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

        public FindAllDTO(Option option){
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity= option.getQuantity();
        }
    }

    @Setter
    @Getter
    public static class CreateDTO{
        private Long id;

        // 옵션 상품 이름
        private String optionName;

        // 옵션 상품 가격
        private Long price;

        // 옵션 상품 수량
        private Long quantity;

        private Long product;
        public CreateDTO(Option option){
            this.id = option.getId();
            this.product = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.quantity= option.getQuantity();
        }
        public Option toEntity(ProductRepository productRepository) {
            Product product = productRepository.findById(this.product)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + this.product));
            return new Option(this.optionName, this.price, this.quantity, product);
        }
    }
}
