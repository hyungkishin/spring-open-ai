package com.myai.openai.config;

import com.myai.openai.application.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartRunner implements CommandLineRunner {

    private final ChatService chatService;

    public CommandLineAppStartRunner(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void run(String... args) throws Exception {
        chatService.startChat();
    }

}
