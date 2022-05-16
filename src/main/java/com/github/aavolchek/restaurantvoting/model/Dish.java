package com.github.aavolchek.restaurantvoting.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"name", "restaurant_id", "price"}, name = "dish_unique_name_restaurant_price_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    @NotNull
    private BigDecimal price;

    public Dish(Restaurant restaurant, BigDecimal price) {
        this.restaurant = restaurant;
        this.price = price;
    }

    public Dish(Integer id, String name, Restaurant restaurant, BigDecimal price) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
    }
}
