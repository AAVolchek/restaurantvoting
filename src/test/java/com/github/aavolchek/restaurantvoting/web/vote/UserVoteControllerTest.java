package com.github.aavolchek.restaurantvoting.web.vote;

import com.github.aavolchek.restaurantvoting.model.Vote;
import com.github.aavolchek.restaurantvoting.repository.VoteRepository;
import com.github.aavolchek.restaurantvoting.web.AbstractControllerTest;
import com.github.aavolchek.restaurantvoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.aavolchek.restaurantvoting.web.vote.VoteTestData.REST1_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {
    protected static final String REST_URL = UserVoteController.REST_URL + '/';

    @Autowired
    protected VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getVoteOfUserForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/vote-of-user-for-today"))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.getVoteOfAdmin()));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createWithLocation() throws Exception{
        Vote newVoice = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + REST1_ID)
                .param("restaurantId", Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = VoteTestData.VOTE_MATCHER.readFromJson(action);
        int newId = created.getId();
        newVoice.setId(newId);
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVoice);
        VoteTestData.VOTE_MATCHER.assertMatch(voteRepository.getById(newId), newVoice);
    }
}
