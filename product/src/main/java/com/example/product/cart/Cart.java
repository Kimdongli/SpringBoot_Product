package com.example.product.cart;

import com.example.product.option.Option;
import com.example.product.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name ="cart_tb",
        indexes = {
                @Index(name = "cart_user_id_idx", columnList = "user_id"),
                @Index(name = "cart_option_id_idx", columnList = "option_id"),
        },
        // ** 고유값 설정
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_cart_option_user", columnNames = {"user_id","option_id"})
        })
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** user별로 카트에 묶임
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    @OneToOne(fetch = FetchType.LAZY)
    private Option option;

    // Cart 안에 들어가 있는 총 수량
    @Column(length = 100, nullable = false)
    private Long quantity;

    // Cart 안에 들어가 있는 총 가격
    @Column(nullable = false)
    private Long price;


    @Builder
    public Cart(Long id, User user, Option option, Long quantity, Long price) {
        this.id = id;
        this.user = user;
        this.option = option;
        this.quantity = quantity;
        this.price = price;
    }

    public void update(Long quantity, Long price){
        this.quantity = quantity;
        this.price = price;
    }
}