package com.github.aavolchek.restaurantvoting.web.voice;

import com.github.aavolchek.restaurantvoting.model.Voice;
import com.github.aavolchek.restaurantvoting.repository.RestaurantRepository;
import com.github.aavolchek.restaurantvoting.repository.VoiceRepository;
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

import static com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil.checkTimeLimit;

@RestController
@RequestMapping(value = UserVoiceController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "UserVoiceController")
public class UserVoiceController {
    public static final String REST_URL ="/api/user/voice";

    @Value("${time-limit.voice}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime timeLimitForVoting;

    private final VoiceRepository voiceRepository;
    private final RestaurantRepository restaurantRepository;

    public UserVoiceController(VoiceRepository voiceRepository, RestaurantRepository restaurantRepository) {
        this.voiceRepository = voiceRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping(value = "{restaurantId}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Voice> createWithLocation(@PathVariable("restaurantId") int restaurantId, @ApiIgnore @AuthenticationPrincipal AuthUser user) {
        log.info("create voice for restaurant {}", restaurantId);
        if(voiceRepository.get(LocalDate.now(), user.id()).orElse(null) != null) {
            checkTimeLimit(timeLimitForVoting);
        }
        Voice voice = new Voice(user.getUser(), LocalDate.now(), restaurantRepository.getById(restaurantId));
        Voice created = voiceRepository.save(voice);
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
        log.info("update voice - restaurant {}", restaurantId);
        checkTimeLimit(timeLimitForVoting);
        Voice voice = voiceRepository.get(LocalDate.now(), user.id()).orElse(null);
        if (voice != null) {
            voice.setRestaurant(restaurantRepository.getById(restaurantId));
            voiceRepository.save(voice);
        }
    }

    @GetMapping("/voice-of-user-for-today")
    public ResponseEntity<Voice> getVoiceOfUserForToday(@ApiIgnore @AuthenticationPrincipal AuthUser user){
        return ResponseEntity.of(voiceRepository.getWithRestaurant(LocalDate.now(), user.id()));
    }
}
