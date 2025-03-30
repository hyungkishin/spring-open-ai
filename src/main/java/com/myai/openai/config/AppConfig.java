package com.myai.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.Resource;

@Description("OpenAI Chat Client 및 Model 설정을 관리하는 구성 클래스")
@Configuration
public class AppConfig {

    @Value("classpath:/prompt.txt")
    private Resource resource;

    // ChatClient <----------------OpenApi Key-------------------> LLM ( openAi )
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        // System Message ( LLM 에 역할을 부여 )
        // return chatClientBuilder.defaultSystem(resource).build();
        // return chatClientBuilder.build();
        return chatClientBuilder.defaultAdvisors(
                new MessageChatMemoryAdvisor(new InMemoryChatMemory())
        ).build();
    }

}
