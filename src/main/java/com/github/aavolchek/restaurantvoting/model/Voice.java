package com.github.aavolchek.restaurantvoting.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "voice", uniqueConstraints = {@UniqueConstraint(columnNames = {"registered_date", "user_id"},
       name = "voice_unique_date_user_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"user, restaurant"})
public class Voice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @Column(name = "registered_date",nullable = false, columnDefinition = "date default now()")
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate registeredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    public Voice(User user, LocalDate registeredDate, Restaurant restaurant) {
        this.user = user;
        this.registeredDate = registeredDate;
        this.restaurant = restaurant;
    }

    public Voice(Integer id, User user, LocalDate registeredDate, Restaurant restaurant) {
        super(id);
        this.user = user;
        this.registeredDate = registeredDate;
        this.restaurant = restaurant;
    }
}
