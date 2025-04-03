package com.myai.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Description("OpenAI Chat Client 및 Model 설정을 관리하는 구성 클래스")
@Configuration
public class ChatClientConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Bean
    public ChatClient chatClient() {
        // OpenAI API 객체 생성
        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(apiKey) // API 키 설정
                .build();

        // OpenAI ChatOptions 생성 (모델 및 옵션 설정)
        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .model("gpt-4o") // 사용할 모델 이름 (예: gpt-4, gpt-3.5-turbo 등)
                .temperature(0.7) // 응답 다양성 설정 (0~2 범위)
                .maxTokens(500)   // 최대 토큰 수 제한
                .build();

        // OpenAI ChatModel 생성
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)       // OpenAI API 연결
                .defaultOptions(chatOptions) // 기본 옵션 적용
                .build();

        // ChatClient 생성 및 반환
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory()) // 대화 기록 관리 어드바이저 추가
                )
                .build();
    }

}