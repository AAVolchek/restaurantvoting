package com.github.aavolchek.restaurantvoting.web.menu;

import com.github.aavolchek.restaurantvoting.repository.DishRepository;
import com.github.aavolchek.restaurantvoting.repository.MenuRepository;
import com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import com.github.aavolchek.restaurantvoting.model.Dish;
import com.github.aavolchek.restaurantvoting.model.Menu;
import com.github.aavolchek.restaurantvoting.repository.RestaurantRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "AdminRestaurantController")
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
    public Menu get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        return menuRepository.findById(id).filter(menu -> menu.getRestaurant().getId() == restaurantId)
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        if(get(id, restaurantId) != null) {
            menuRepository.deleteExisted(id);
        }
    }

    @GetMapping("/menus")
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return menuRepository.getAll(restaurantId);
    }

    @PostMapping
    @ResponseBody
    @Transactional
    public Menu createWithLocation(@PathVariable int restaurantId,
                                                   @RequestParam String name,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registeredDate,
                                                   @RequestParam List<Integer> dishList) {
        log.info("create menu for restaurant {}", restaurantId);
        List<Dish> dishes = getDishList(dishList, restaurantId);
        Menu menu = new Menu(null,
                name, restaurantRepository.getById(restaurantId),
                registeredDate == null ? LocalDate.now() : registeredDate, dishes);
        ValidationUtil.checkNew(menu);
        return  menuRepository.save(menu);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {} menu for restaurant {}", menu, restaurantId);
        ValidationUtil.assureIdConsistent(menu, id);
        ValidationUtil.assureIdConsistent(menu.getRestaurant(), restaurantId);
        menu.getDishList().stream().forEach(x -> ValidationUtil.assureIdConsistent(x.getRestaurant(), restaurantId));
        if (!menu.isNew() && get(id, restaurantId) != null) {
            menuRepository.save(menu);
        }
    }

    @GetMapping("/with-restaurant-and-dish-list/{id}")
    public Menu getWithRestaurantAndDishList(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        return menuRepository.getWithRestaurantAndDishList(id, restaurantId).orElse(null);
    }

    private List<Dish> getDishList(List<Integer> dishListInteger, int restaurantId) {
         return dishListInteger.stream()
                .map(s -> dishRepository.findById(s)
                        .filter(dish -> dish.getRestaurant().getId() == restaurantId)
                        .orElseThrow(() -> new NotFoundException("Dish id= "+s+" for restaurant id= "+restaurantId+" Not Found!")))
                .collect(Collectors.toList());
    }
}
