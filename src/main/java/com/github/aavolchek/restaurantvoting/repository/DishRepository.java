package com.github.aavolchek.restaurantvoting.repository;

import com.github.aavolchek.restaurantvoting.model.Dish;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@CacheConfig(cacheNames = "dish")
public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = ?1")
    @Cacheable
    List<Dish> getAll(int restaurantId);
}
