package com.github.aavolchek.restaurantvoting.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.aavolchek.restaurantvoting.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@CacheConfig(cacheNames = "dish")
public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d From Dish d JOIN FETCH d.restaurant WHERE d.id = ?1 AND d.restaurant.id = ?2")
    Optional<Dish> getWithRestaurant(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = ?1")
    @Cacheable
    List<Dish> getAll(int restaurantId);
}
