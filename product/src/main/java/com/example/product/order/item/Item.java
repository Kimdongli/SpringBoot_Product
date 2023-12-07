package com.example.product.order.item;


import com.example.product.option.Option;
import com.example.product.order.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long price;


    @Builder
    public Item(Long id, Option option, Order order, Long quantity, Long price) {
        this.id = id;
        this.option = option;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }
}
