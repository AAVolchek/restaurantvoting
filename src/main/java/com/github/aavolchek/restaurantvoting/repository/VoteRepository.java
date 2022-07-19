package com.github.aavolchek.restaurantvoting.repository;

import com.github.aavolchek.restaurantvoting.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote>{
    @Query("SELECT v FROM Vote v WHERE v.registeredDate =?1 AND v.user.id =?2")
    Optional<Vote> get(LocalDate data, int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.registeredDate =?1 AND v.user.id =?2")
    Optional<Vote> getWithRestaurant(LocalDate data, int userId);
}
