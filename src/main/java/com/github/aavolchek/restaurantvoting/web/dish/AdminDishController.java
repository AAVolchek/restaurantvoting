package com.github.aavolchek.restaurantvoting.web.dish;

import com.github.aavolchek.restaurantvoting.model.Dish;
import com.github.aavolchek.restaurantvoting.repository.DishRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "AdminDishController")
public class AdminDishController {
    public static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dish";

    private final DishRepository dishRepository;

    public AdminDishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish {} for restaurant {}", id, restaurantId);
        return dishRepository.findById(id).filter(dish -> dish.getRestaurant().getId() == restaurantId)
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete dish {} for restaurant {}", id, restaurantId);
        if(get(id, restaurantId) != null) {
            dishRepository.deleteExisted(id);
        }
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return dishRepository.getAll(restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dish createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create {} for restaurant {}", dish, restaurantId);
        checkNew(dish);
        assureIdConsistent(dish.getRestaurant(), restaurantId);
        return dishRepository.save(dish);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {} dish for restaurant {}", dish, restaurantId);
        assureIdConsistent(dish, id);
        assureIdConsistent(dish.getRestaurant(), restaurantId);
        if (!dish.isNew() && get(id, restaurantId) != null) {
            dishRepository.save(dish);
        }
    }

    @GetMapping("/{id}/with-restaurant/")
    public Dish getWithRestaurant(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish {} for restaurant {}", id, restaurantId);
        return dishRepository.getWithRestaurant(id, restaurantId).orElse(null);
    }
}
