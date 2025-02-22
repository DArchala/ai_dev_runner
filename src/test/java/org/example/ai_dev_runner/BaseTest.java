package org.example.ai_dev_runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

@Slf4j
@SuppressWarnings("all")
@SpringBootTest
public abstract class BaseTest {

    @Autowired
    protected RestClient restClient;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @SneakyThrows
    public String asJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

}
