package com.example.product.product;

import com.example.product.option.Option;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Setter
public class Product {

    // ** PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** 상품명, 입력값 필수
    @Column(length = 100, nullable = false)
    private String productName;

    // ** 상품 설명, 입력값 필수
    @Column(length = 500, nullable = false)
    private String description;

    // ** 가격
    @Column(nullable = false)
    private int price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<Option> options = new ArrayList<>();

    @Builder
    public Product(Long id, String productName, String description, int price, List<Option> options) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.options = options;
    }
}
