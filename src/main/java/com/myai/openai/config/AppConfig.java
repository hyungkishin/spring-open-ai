package com.myai.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Description("OpenAI Chat Client 설정을 관리하는 구성 클래스")
@Configuration
public class AppConfig {

    // ChatClient <----------------OpenApi Key-------------------> LLM ( openAi )
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem("당신은 교육 튜터입니다. 개념을 명확하고 간단하게 설명하세요") // System Message ( LLM 에 역할을 부여 )
                .build();
    }

}
