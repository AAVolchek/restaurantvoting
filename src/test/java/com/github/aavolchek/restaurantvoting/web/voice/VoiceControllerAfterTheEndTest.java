package com.github.aavolchek.restaurantvoting.web.voice;

import com.github.aavolchek.restaurantvoting.model.Voice;
import com.github.aavolchek.restaurantvoting.util.JsonUtil;
import com.github.aavolchek.restaurantvoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@TestPropertySource(properties = {"time-limit.voice=00:00:00"})
class VoiceControllerAfterTheEndTest extends AbstractVoiceControllerTest{

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createWithLocation() throws Exception{
        perform(MockMvcRequestBuilders
                .post(REST_URL + VoiceTestData.REST1_ID)
                .param("restaurantId", Integer.toString(VoiceTestData.REST1_ID)))
                .andDo(print())
                .andExpect(content().string(
                        containsString("It is impossible to vote for the restaurant. The voting ended at")));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Voice updated = VoiceTestData.getUpdated();
        perform(MockMvcRequestBuilders
                .put(REST_URL + VoiceTestData.VOICE_ADMIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(content().string(
                        containsString("It is impossible to vote for the restaurant. The voting ended at")));
    }
}
