package com.myai.openai.ui;

import com.myai.openai.application.OpenAiChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class OpenAiController {

    private final OpenAiChatService openAiChatService;

    public OpenAiController(OpenAiChatService openAiChatService) {
        this.openAiChatService = openAiChatService;
    }

    @GetMapping("/ask-ai")
    public Flux<String> getResponseOptions(String message) {
        return openAiChatService.chat("1S", message);
    }

}
