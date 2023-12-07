package com.example.product.product;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import com.example.product.option.Option;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


public class ProductResponse {

    @Getter
    @Setter
    public static class FindAllDTO {
        // ** PK
        private Long id;

        // ** 상품명
        private String productName;

        // ** 상품 설명
        private String description;

        // ** 이미지 정보
        private String image;

        // ** 가격
        private int price;


        public FindAllDTO(Product product) {
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
        }
    }

    @Setter
    @Getter
    public static class FindByIdDTO {
        // ** PK
        private Long id;

        // ** 상품명
        private String productName;

        // ** 상품 설명
        private String description;

        // ** 이미지 정보
        private String image;

        // ** 가격
        private int price;

        private List<OptionDTO> optionList;

        public FindByIdDTO(Product product, List<Option> optionList) {
            this.id = product.getId();
            this.productName = product.getProductName();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
            this.optionList = optionList.stream().map(OptionDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Setter
    @Getter
    public static class OptionDTO{
        private Long id;
        private String optionName;
        private Long price;
        private Long quantity;

        public OptionDTO(Option option) {
            this.id = option.getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            quantity= option.getQuantity();
        }
    }

    @Setter
    @Getter
    public static class CreateDTO{
        // ** 상품명
        private String productName;

        // ** 상품 설명
        private String description;

        // ** 이미지 정보
        private String image;

        // ** 가격
        private int price;
    }

}