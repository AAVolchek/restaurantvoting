package com.github.aavolchek.restaurantvoting.web.restaurant;

import com.github.aavolchek.restaurantvoting.model.Voice;
import com.github.aavolchek.restaurantvoting.repository.VoiceRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.aavolchek.restaurantvoting.model.Restaurant;
import com.github.aavolchek.restaurantvoting.repository.RestaurantRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "AdminRestaurantController")
// TODO: cache only most requested data!
@CacheConfig(cacheNames = "restaurants")
public class AdminRestaurantController {
    private final RestaurantRepository repository;
    private final VoiceRepository voiceRepository;

    public static final String REST_URL = "/api/admin/restaurants";

    public AdminRestaurantController(RestaurantRepository repository, VoiceRepository voiceRepository) {
        this.repository = repository;
        this.voiceRepository = voiceRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @GetMapping
    @Cacheable
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public Restaurant createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return repository.save(restaurant);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {}", id);
        assureIdConsistent(restaurant, id);
        repository.save(restaurant);
    }

    @GetMapping("/voices/{restaurantId}")
    public List<Voice> getRestaurantVoices(@PathVariable("restaurantId") int restaurantId){
        log.info("getAll restaurant voices {}", restaurantId);
        return voiceRepository.getVoicesOfRestaurantWithUser(LocalDate.now(), restaurantId);
    }
}
