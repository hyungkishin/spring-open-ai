package com.myai.openai.application;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class OpenAiChatService {

    private final ChatModel chatModel;

    public OpenAiChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String message) {
        ChatResponse response = chatModel.call(
                new Prompt(message, OpenAiChatOptions.builder()
                        .model("gpt-4o")
                        .temperature(0.8)
                        .build())
        );

        return response.getResult()
                .getOutput()
                .getText();
    }

}
