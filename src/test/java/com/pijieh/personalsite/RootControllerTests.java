package com.pijieh.personalsite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.pijieh.personalsite.controllers.RootController;
import com.pijieh.personalsite.database.DatabaseService;
import com.pijieh.personalsite.helpers.ResourceFinder;

@WebMvcTest(RootController.class)
class RootControllerTests {

    @Autowired
    private RootController rootController;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    DatabaseService dataSource;

    @MockitoBean
    ResourceFinder rsFinder;

    // Sanity check
    @Test
    void contextLoads() {
        assert rootController != null : "Test controller is null!";
    }

    @Test
    void resume() throws Exception {
        when(rsFinder.getResourceBytes("Ijieh_Patrick.pdf")).thenCallRealMethod();
        final byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/Ijieh_Patrick.pdf"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/resume")).andExpect(content().bytes(bytes))
                .andExpect(status().isOk());
    }

    @Test
    void resourceThrows() throws Exception {
        when(rsFinder.getResourceBytes(anyString())).thenThrow(new IOException());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/resume")).andExpect(status().isInternalServerError());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/icon")).andExpect(status().isInternalServerError());

    }
}
