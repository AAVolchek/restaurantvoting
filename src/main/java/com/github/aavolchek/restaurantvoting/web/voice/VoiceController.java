package com.github.aavolchek.restaurantvoting.web.voice;

import com.github.aavolchek.restaurantvoting.model.Restaurant;
import com.github.aavolchek.restaurantvoting.model.Voice;
import com.github.aavolchek.restaurantvoting.repository.MenuRepository;
import com.github.aavolchek.restaurantvoting.repository.RestaurantRepository;
import com.github.aavolchek.restaurantvoting.repository.VoiceRepository;
import com.github.aavolchek.restaurantvoting.web.AuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil.checkNew;
import static com.github.aavolchek.restaurantvoting.util.validation.ValidationUtil.checkTimeLimit;

@RestController
@RequestMapping(value = VoiceController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "VoiceController")
public class VoiceController {
    public static final String REST_URL ="/api/user/voice";

    @Value("${time-limit.voice}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime timeLimitForVoting;

    private final VoiceRepository voiceRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public VoiceController(VoiceRepository voiceRepository, MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.voiceRepository = voiceRepository;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping(value = "{restaurantId}")
    @ResponseBody
    @Transactional
    public Voice createWithLocation(@PathVariable("restaurantId") int restaurantId, @ApiIgnore @AuthenticationPrincipal AuthUser user) {
        log.info("create voice for restaurant {}", restaurantId);
        checkTimeLimit(timeLimitForVoting);
        Voice voice = new Voice(user.getUser(), LocalDate.now(), getRestaurantById(restaurantId));
        checkNew(voice);
        return voiceRepository.save(voice);
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
            voice.setRestaurant(getRestaurantById(restaurantId));
            voiceRepository.save(voice);
        }
    }

    @GetMapping("/voice-of-user-for-today")
    public Voice getVoiceOfUserForToday(@AuthenticationPrincipal AuthUser user){
        return voiceRepository.getWithRestaurant(LocalDate.now(), user.id()).orElse(null);
    }

    private Restaurant getRestaurantById(int id){
        return restaurantRepository.findById(id).orElse(null);
    }

}
