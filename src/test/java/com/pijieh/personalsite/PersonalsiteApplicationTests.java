package com.pijieh.personalsite;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.pijieh.personalsite.database.DBService;

@SpringBootTest
class PersonalsiteApplicationTests {

    @MockitoBean
    DBService dataSource;

    @Test
    void contextLoads() {
    }
}
