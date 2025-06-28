package com.pijieh.personalsite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pijieh.personalsite.controllers.AdminController;
import com.pijieh.personalsite.database.DBService;

@WebMvcTest(AdminController.class)
class AdminControllerTests {

    @Autowired
    private AdminController adminController;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    DBService dataSource;

    @Test
    void contextLoads() {
        assert adminController != null : "Test controller is null!";
    }

    @Test
    void noSessionShouldReturnForbidden() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/admin")).andExpect(status().isForbidden());
    }

    @Test
    void havingSessionShouldReturnOk() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/admin").session(mockHttpSession)).andExpect(status().isOk());
    }

    @Test
    void logoutShouldInvalidateSession() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("username", "dummy_user");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/logout").session(mockHttpSession))
                .andExpect(status().is3xxRedirection());

        assert mockHttpSession.isInvalid() : "Mock session is still valid after logout!";
    }
}
