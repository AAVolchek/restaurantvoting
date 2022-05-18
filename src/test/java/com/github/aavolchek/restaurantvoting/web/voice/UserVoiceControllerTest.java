package com.github.aavolchek.restaurantvoting.web.voice;

import com.github.aavolchek.restaurantvoting.repository.VoiceRepository;
import com.github.aavolchek.restaurantvoting.web.AbstractControllerTest;
import com.github.aavolchek.restaurantvoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoiceControllerTest extends AbstractControllerTest {
    protected static final String REST_URL = UserVoiceController.REST_URL + '/';

    @Autowired
    protected VoiceRepository voiceRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getVoiceOfUserForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/voice-of-user-for-today"))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoiceTestData.VOICE_MATCHER.contentJson(VoiceTestData.getVoiceOfAdmin()));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
}
