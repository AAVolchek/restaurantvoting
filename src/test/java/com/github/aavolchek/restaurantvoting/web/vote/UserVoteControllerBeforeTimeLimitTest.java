package com.github.aavolchek.restaurantvoting.web.vote;

import com.github.aavolchek.restaurantvoting.model.Vote;
import com.github.aavolchek.restaurantvoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.aavolchek.restaurantvoting.web.vote.VoteTestData.REST1_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {"time-limit.vote=23:59:59"})
class UserVoteControllerBeforeTimeLimitTest extends UserVoteControllerTest {
    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders
                        .put(REST_URL + VoteTestData.VOTE_ADMIN_ID)
                        .param("restaurantId", Integer.toString(REST1_ID)))
                        .andDo(print())
                        .andExpect(status().isNoContent());
        VoteTestData.VOTE_MATCHER.assertMatch(voteRepository.getById(VoteTestData.VOTE_ADMIN_ID), updated);
    }
}
