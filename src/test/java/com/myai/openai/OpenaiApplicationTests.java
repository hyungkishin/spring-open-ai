package com.myai.openai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.ai.openai.api-key=${OPENAI_API_KEY}")
class OpenaiApplicationTests {

    @Test
    void contextLoads() {
    }

}
