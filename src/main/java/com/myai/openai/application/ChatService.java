package com.myai.openai.application;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public String chatMessage(String message) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();

        if (!ObjectUtils.isEmpty(chatResponse)) {
            return chatResponse
                    .getResult()
                    .getOutput()
                    .getText();
        }

        return null;
    }
}
