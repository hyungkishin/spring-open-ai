package com.myai.openai.client;


import com.myai.openai.application.OpenAiChatService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final OpenAiChatService agent;


    public AssistantService(OpenAiChatService agent) {
        this.agent = agent;
    }


    public String chat(String userMessage) {
        return agent.chat(userMessage);
    }

}
