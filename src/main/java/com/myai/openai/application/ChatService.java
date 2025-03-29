package com.myai.openai.application;

import com.myai.openai.ui.request.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
        return Objects.requireNonNull(chatClient.prompt()
                        .user(message)
                        .call()
                        .chatResponse())
                .getResult()
                .getOutput()
                .getText();
    }

    public String chatPlaceMessage(final ChatRequest request) {
        return Objects.requireNonNull(
                        chatClient.prompt()
                                .user(request.message())
                                .system(sp -> sp.param("subject", request.subject()).param("tone", request.tone()))
                                .call()
                                .chatResponse())
                .getResult()
                .getOutput()
                .getText();
    }

}
