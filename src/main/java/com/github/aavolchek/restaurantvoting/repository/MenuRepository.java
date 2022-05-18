package com.github.aavolchek.restaurantvoting.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.aavolchek.restaurantvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@CacheConfig(cacheNames = "menu")
public interface MenuRepository extends BaseRepository<Menu>{
    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT m From Menu m JOIN FETCH m.dishList WHERE m.registeredDate =?1")
    @Cacheable
    List<Menu> findAllByDateWithRestaurantAndDishList(LocalDate date);

    @Query("SELECT m From Menu m JOIN FETCH m.restaurant JOIN FETCH m.dishList WHERE m.id = ?1 AND m.restaurant.id =?2")
    Optional<Menu> getWithRestaurantAndDishList(int id, int restaurantId);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = ?1")
    @Cacheable
    List<Menu> getAll(int restaurantId);
}
