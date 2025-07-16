package com.pijieh.personalsite;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.pijieh.personalsite.database.DatabaseService;

@SpringBootTest
class PersonalsiteApplicationTests {

    @MockitoBean
    DatabaseService dataSource;

    @Test
    void contextLoads() {
    }
}
