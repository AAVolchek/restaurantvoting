package ru.javaops.topjava2.web.voice;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.javaops.topjava2.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;
import static ru.javaops.topjava2.web.voice.VoiceTestData.REST1_ID;
import static ru.javaops.topjava2.web.voice.VoiceTestData.VOICE_ADMIN_ID;

@TestPropertySource(properties = {"time-limit.voice=00:00:00"})
class VoiceControllerAfterTheEndTest extends AbstractVoiceControllerTest{

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocation() throws Exception{
        perform(MockMvcRequestBuilders
                .post(REST_URL + REST1_ID)
                .param("restaurantId", Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(content().string(
                        containsString("It is impossible to vote for the restaurant. The voting ended at")));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + VOICE_ADMIN_ID)
                .param("restaurantId", Integer.toString(REST1_ID)))
                .andDo(print())
                .andExpect(content().string(
                        containsString("It is impossible to vote for the restaurant. The voting ended at")));
    }
}
