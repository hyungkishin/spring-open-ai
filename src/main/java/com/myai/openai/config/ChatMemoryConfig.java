package com.myai.openai.config;

import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

    @Bean
    public InMemoryChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}