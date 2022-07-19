package com.github.aavolchek.restaurantvoting.web.vote;

import com.github.aavolchek.restaurantvoting.model.Restaurant;
import com.github.aavolchek.restaurantvoting.model.Vote;
import com.github.aavolchek.restaurantvoting.web.MatcherFactory;
import com.github.aavolchek.restaurantvoting.web.user.UserTestData;

import java.time.LocalDate;

public class VoteTestData {
    public static final int VOTE_ADMIN_ID = 1;
    public static final int REST1_ID = 1;

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);

    public static Restaurant restaurant1 = new Restaurant(1,"Coqfighter Finsbury Park", "75 Beak Str." +
            "W1F 9SS");
    public static Restaurant restaurant2 = new Restaurant(2,"Piazza Italiana","str. Mozart 89");

    public static Vote getNew() {
        return new Vote(null, null, LocalDate.now(), null);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_ADMIN_ID, UserTestData.admin, LocalDate.now(), restaurant2);
    }

    public static Vote getVoteOfAdmin() {
        return new Vote(1, UserTestData.admin, LocalDate.now(), restaurant1);
    }
}
