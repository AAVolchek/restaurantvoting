package ru.javaops.topjava2.web.voice;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.model.Voice;
import ru.javaops.topjava2.util.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.topjava2.web.user.UserTestData.USER_MAIL;
import static ru.javaops.topjava2.web.voice.VoiceTestData.*;

@TestPropertySource(properties = {"time-limit.voice=23:59:59"})
class VoiceControllerBeforeTheEndTest extends AbstractVoiceControllerTest {

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocation() throws Exception{
        Voice newVoice = VoiceTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders
                .post(REST_URL + REST1_ID)
                .param("restaurantId", Integer.toString(REST1_ID)))
                .andDo(print());

        Voice created = VOICE_MATCHER.readFromJson(action);
        int newId = created.getId();
        newVoice.setId(newId);
        VOICE_MATCHER.assertMatch(created, newVoice);
        VOICE_MATCHER.assertMatch(voiceRepository.getById(newId), newVoice);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Voice updated = VoiceTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + VOICE_ADMIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        VOICE_MATCHER.assertMatch(voiceRepository.getById(VOICE_ADMIN_ID), updated);
    }
}
