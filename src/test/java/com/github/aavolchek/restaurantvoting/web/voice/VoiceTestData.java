package com.github.aavolchek.restaurantvoting.web.voice;

import com.github.aavolchek.restaurantvoting.model.Voice;
import com.github.aavolchek.restaurantvoting.web.MatcherFactory;
import com.github.aavolchek.restaurantvoting.web.user.UserTestData;
import com.github.aavolchek.restaurantvoting.model.Restaurant;

import java.time.LocalDate;

public class VoiceTestData {
    public static final int VOICE_ADMIN_ID = 1;
    public static final int REST1_ID = 1;

    public static final MatcherFactory.Matcher<Voice> VOICE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Voice.class, "restaurant", "user");

    public static Restaurant restaurant1 = new Restaurant(1,"Coqfighter Finsbury Park", "75 Beak Str." +
            "W1F 9SS");
    public static Restaurant restaurant2 = new Restaurant(2,"Piazza Italiana","str. Mozart 89");

    public static Voice getNew() {
        return new Voice(null, null, LocalDate.now(), null);
    }

    public static Voice getUpdated() {
        return new Voice(VOICE_ADMIN_ID, UserTestData.admin, LocalDate.now(), restaurant2);
    }

    public static Voice getVoiceOfAdmin() {
        return new Voice(1, UserTestData.admin, LocalDate.now(), restaurant1);
    }
}
