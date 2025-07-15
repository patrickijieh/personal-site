package com.pijieh.personalsite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pijieh.personalsite.controllers.LoginController;
import com.pijieh.personalsite.database.DatabaseService;

@WebMvcTest(LoginController.class)
class LoginControllerTests {

    @Autowired
    private LoginController loginController;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    DatabaseService dataSource;

    // Sanity check
    @Test
    void contextLoads() {
        assert loginController != null : "Test controller is null!";
    }

    @Test
    void havingSessionShouldRedirectToAdmin() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("username", "dummy_user");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login").session(mockHttpSession))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin"));
    }
}
