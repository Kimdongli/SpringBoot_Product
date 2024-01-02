package com.example.product.user;


import com.example.product.cart.Cart;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false,unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 255)
    private String access_token;

    @Column(length = 255)
    private String refresh_token;

    @Column(length = 50)
    @Convert(converter = StringArrayConverter.class)
    private List<String> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Cart> carts = new ArrayList<>();

    @Builder
    public User(Long id, String email, String password,String name, String access_token, String refresh_token, List<String> roles,String platform,List<Cart> carts) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.roles = roles;
        this.carts = carts;

    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}

