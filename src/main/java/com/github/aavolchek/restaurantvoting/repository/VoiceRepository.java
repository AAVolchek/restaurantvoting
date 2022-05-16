package com.github.aavolchek.restaurantvoting.repository;

import com.github.aavolchek.restaurantvoting.model.Voice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoiceRepository extends BaseRepository<Voice>{
    @Query("SELECT v FROM Voice v WHERE v.registeredDate =?1 AND v.user.id =?2")
    Optional<Voice> get(LocalDate data, int userId);

    @Query("SELECT v FROM Voice v JOIN FETCH v.restaurant WHERE v.registeredDate =?1 AND v.user.id =?2")
    Optional<Voice> getWithRestaurant(LocalDate data, int userId);

    @Query("SELECT v FROM Voice v JOIN FETCH v.user WHERE v.registeredDate =?1 AND v.restaurant.id =?2")
    List<Voice> getVoicesOfRestaurantWithUser(LocalDate data, int restaurantId);
}
