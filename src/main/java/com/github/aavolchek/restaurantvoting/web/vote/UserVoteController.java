package com.github.aavolchek.restaurantvoting.web.vote;

import com.github.aavolchek.restaurantvoting.error.IllegalRequestDataException;
import com.github.aavolchek.restaurantvoting.model.Menu;
import com.github.aavolchek.restaurantvoting.model.Vote;
import com.github.aavolchek.restaurantvoting.repository.MenuRepository;
import com.github.aavolchek.restaurantvoting.repository.RestaurantRepository;
import com.github.aavolchek.restaurantvoting.repository.VoteRepository;
import com.github.aavolchek.restaurantvoting.web.AuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil.checkTimeLimit;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "UserVoteController")
public class UserVoteController {
    public static final String REST_URL ="/api/user/vote";

    @Value("${time-limit.vote}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime timeLimitForVoting;

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public UserVoteController(VoteRepository voteRepository, RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    @PostMapping(value = "{restaurantId}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Vote> createWithLocation(@PathVariable("restaurantId") int restaurantId, @ApiIgnore @AuthenticationPrincipal AuthUser user) {
        log.info("create vote for restaurant {}", restaurantId);
        Vote voice = new Vote(null, user.getUser(), LocalDate.now(), restaurantRepository.getById(restaurantId));
        Vote created = voteRepository.save(voice);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @Transactional
    public void update(@PathVariable("restaurantId") int restaurantId, @ApiIgnore @AuthenticationPrincipal AuthUser user){
        log.info("update vote - restaurant {}", restaurantId);
        checkTimeLimit(timeLimitForVoting);
        Optional<Vote> optionalVoice = voteRepository.get(LocalDate.now(), user.id());
        if (optionalVoice.isPresent()) {
            Vote voice = optionalVoice.get();
            voice.setRestaurant(restaurantRepository.getById(restaurantId));
            voteRepository.save(voice);
        } else
            throw new IllegalRequestDataException("Need create new vote");
    }

    @GetMapping("/vote-of-user-for-today")
    public ResponseEntity<Vote> getVoiceOfUserForToday(@ApiIgnore @AuthenticationPrincipal AuthUser user){
        return ResponseEntity.of(voteRepository.getWithRestaurant(LocalDate.now(), user.id()));
    }

    @GetMapping("/menus-today")
    public List<Menu> getAllMenu() {
        log.info("getAll today’s menu");
        LocalDate date = LocalDate.now();
        return menuRepository.findAllByDateWithRestaurantAndDishList(date);
    }
}
