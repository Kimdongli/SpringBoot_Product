package com.example.product.product;

import com.example.product.option.Option;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<Option> options = new ArrayList<>();

    // ** 이미지 정보
    @Column(length = 100)
    private String image;

    // ** 가격
    private int price;

}
