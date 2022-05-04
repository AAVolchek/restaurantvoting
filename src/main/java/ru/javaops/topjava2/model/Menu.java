package ru.javaops.topjava2.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "registered_data"},
        name = "menus_unique_restaurant_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Menu extends NamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "registered_data", nullable = false, columnDefinition = "date default now()")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate registeredDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "menu_dishes",
            joinColumns = {@JoinColumn(name = "menu_id")},
            inverseJoinColumns = @JoinColumn(name = "dish_id"))
    private List<Dish> dishList;

    public Menu(Restaurant restaurant, LocalDate registeredDate, List<Dish> dishList) {
        this.restaurant = restaurant;
        this.registeredDate = registeredDate;
        this.dishList = dishList;
    }

    public Menu(Integer id, String name, Restaurant restaurant, LocalDate registeredDate, List<Dish> dishList) {
        super(id, name);
        this.restaurant = restaurant;
        this.registeredDate = registeredDate;
        this.dishList = dishList;
    }
}
