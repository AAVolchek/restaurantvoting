package com.github.aavolchek.restaurantvoting.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "location", nullable = false)
    @NotNull
    private String location;

    public Restaurant(Integer id, String name, String location) {
        super(id, name);
        this.location = location;
    }

    public Restaurant(Restaurant r) {
        this(r.getId(), r.getName(), r.location);
    }
}
