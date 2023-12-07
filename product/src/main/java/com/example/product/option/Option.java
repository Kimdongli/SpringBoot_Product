package com.example.product.option;

import com.example.product.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Option_tb",
        // 아이디를 자동으로 검색
        indexes = {
            @Index(name = "option_product_id_index",columnList = "product_id")
        })
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 옵션 상품 이름
    @Column(length = 100, nullable = false)
    private String optionName;

    // 옵션 상품 가격
    private Long price;

    // 옵션 상품 수량
    private Long quantity;

    public Option(String optionName, Long price, Long quantity, Product product) {
        this.optionName = optionName;
        this.price = price;
        this.quantity = quantity;
        this.product = product;
    }
}
