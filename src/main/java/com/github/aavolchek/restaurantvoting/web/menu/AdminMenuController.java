package com.github.aavolchek.restaurantvoting.web.menu;

import com.github.aavolchek.restaurantvoting.model.Dish;
import com.github.aavolchek.restaurantvoting.model.Menu;
import com.github.aavolchek.restaurantvoting.repository.DishRepository;
import com.github.aavolchek.restaurantvoting.repository.MenuRepository;
import com.github.aavolchek.restaurantvoting.repository.RestaurantRepository;
import com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "AdminMenuController")
public class AdminMenuController {

    public static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menu";

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    public AdminMenuController(MenuRepository menuRepository, RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        return ResponseEntity.of(menuRepository.findById(id).filter(menu -> menu.getRestaurant().getId() == restaurantId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        if(get(id, restaurantId).getBody() != null) {
            menuRepository.deleteExisted(id);
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return  menuRepository.getAll(restaurantId);
    }

    @PostMapping
    @ResponseBody
    @Transactional
    public ResponseEntity<Menu> createWithLocation(@PathVariable int restaurantId,
                                   @RequestParam String name,
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registeredDate,
                                   @RequestParam List<Dish> dishList) {
        log.info("create menu for restaurant {}", restaurantId);
        Menu menu = new Menu(null,
                name, restaurantRepository.getById(restaurantId),
                registeredDate == null ? LocalDate.now() : registeredDate, dishList);
        ValidationUtil.checkNew(menu);
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id,
                       @PathVariable int restaurantId,
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registeredDate,
                       @RequestParam List<Dish> dishList) {
        log.info("update {} with id={}", restaurantId, id);
        Menu menu = get(id, restaurantId).getBody();
        if (menu != null) {
            menu.setDishList(dishList);
            menu.setRegisteredDate(registeredDate);
            menuRepository.save(menu);
        }
    }

    @GetMapping("/{id}/with-restaurant-and-dish-list")
    public Menu getWithRestaurantAndDishList(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        return menuRepository.getWithRestaurantAndDishList(id, restaurantId).orElse(null);
    }
}
