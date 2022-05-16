package com.github.aavolchek.restaurantvoting.repository;

import org.springframework.transaction.annotation.Transactional;
import com.github.aavolchek.restaurantvoting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant>{
}
