package com.myai.openai.ui;

import com.myai.openai.application.ChatService;
import com.myai.openai.entity.Answer;
import com.myai.openai.ui.request.ChatRequest;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // LLM ( gpt-4o ) 와 통신할수 있는 객체 : ChatClient
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatService.chat(message);
    }

    @GetMapping("/chat-message")
    public String chatMessage(@RequestParam("message") String message) {
        return chatService.chatMessage(message);
    }

    @GetMapping("/chat-place")
    public String chatPlaceMessage(@ModelAttribute ChatRequest request) {
        return chatService.chatPlaceMessage(request);
    }

    @GetMapping("/chat-json")
    public ChatResponse chatJsonMessage(@RequestParam("message") String message) {
        return chatService.chatJsonMessage(message);
    }

    @GetMapping("/chat-object")
    public Answer chatObject(@RequestParam("message") String message) {
        return chatService.chatObject(message);
    }

    @GetMapping("/recipe")
    public Answer recipe(String foodName, String question) {
        return chatService.recipe(foodName, question);
    }

}
