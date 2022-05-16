package com.github.aavolchek.restaurantvoting.web.voice;

import com.github.aavolchek.restaurantvoting.model.Voice;
import com.github.aavolchek.restaurantvoting.util.JsonUtil;
import com.github.aavolchek.restaurantvoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {"time-limit.voice=23:59:59"})
class VoiceControllerBeforeTheEndTest extends AbstractVoiceControllerTest {

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createWithLocation() throws Exception{
        Voice newVoice = VoiceTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + VoiceTestData.REST1_ID)
                .param("restaurantId", Integer.toString(VoiceTestData.REST1_ID)))
                .andDo(print());

        Voice created = VoiceTestData.VOICE_MATCHER.readFromJson(action);
        int newId = created.getId();
        newVoice.setId(newId);
        VoiceTestData.VOICE_MATCHER.assertMatch(created, newVoice);
        VoiceTestData.VOICE_MATCHER.assertMatch(voiceRepository.getById(newId), newVoice);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Voice updated = VoiceTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + VoiceTestData.VOICE_ADMIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        VoiceTestData.VOICE_MATCHER.assertMatch(voiceRepository.getById(VoiceTestData.VOICE_ADMIN_ID), updated);
    }
}