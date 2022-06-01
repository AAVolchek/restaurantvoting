package com.github.aavolchek.restaurantvoting.web.voice;

import com.github.aavolchek.restaurantvoting.model.Voice;
import com.github.aavolchek.restaurantvoting.repository.VoiceRepository;
import com.github.aavolchek.restaurantvoting.web.AbstractControllerTest;
import com.github.aavolchek.restaurantvoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.aavolchek.restaurantvoting.web.voice.VoiceTestData.REST1_ID;
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

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createWithLocation() throws Exception{
        Voice newVoice = VoiceTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + REST1_ID)
                .param("restaurantId", Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(status().isCreated());

        Voice created = VoiceTestData.VOICE_MATCHER.readFromJson(action);
        int newId = created.getId();
        newVoice.setId(newId);
        VoiceTestData.VOICE_MATCHER.assertMatch(created, newVoice);
        VoiceTestData.VOICE_MATCHER.assertMatch(voiceRepository.getById(newId), newVoice);
    }
}
