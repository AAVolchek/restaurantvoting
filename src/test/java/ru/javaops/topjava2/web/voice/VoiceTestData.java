package ru.javaops.topjava2.web.voice;

import ru.javaops.topjava2.model.Restaurant;
import ru.javaops.topjava2.model.Voice;
import ru.javaops.topjava2.web.MatcherFactory;
import ru.javaops.topjava2.web.user.UserTestData;

import java.time.LocalDate;

public class VoiceTestData {
    public static final int VOICE_ADMIN_ID = 1;
    public static final int REST1_ID = 1;

    public static final MatcherFactory.Matcher<Voice> VOICE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Voice.class, "restaurant", "user");

    public static Restaurant restaurant1 = new Restaurant(1,"Coqfighter Finsbury Park");
    public static Restaurant restaurant2 = new Restaurant(2,"Piazza Italiana");

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
