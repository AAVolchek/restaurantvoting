package com.github.aavolchek.restaurantvoting.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Restaurant r) {
        this(r.getId(), r.getName());
    }
}
