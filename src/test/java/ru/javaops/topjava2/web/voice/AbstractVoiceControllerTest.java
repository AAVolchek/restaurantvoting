package ru.javaops.topjava2.web.voice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.topjava2.repository.VoiceRepository;
import ru.javaops.topjava2.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.topjava2.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.topjava2.web.voice.VoiceTestData.VOICE_MATCHER;

public abstract class AbstractVoiceControllerTest extends AbstractControllerTest {

    protected static final String REST_URL = VoiceController.REST_URL + '/';

    @Autowired
    protected VoiceRepository voiceRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getVoiceOfUserForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/voice-of-user-for-today"))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOICE_MATCHER.contentJson(VoiceTestData.getVoiceOfAdmin()));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
}
