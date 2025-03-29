package com.myai.openai.ui.request;

public record ChatRequest(
        String subject,
        String tone,
        String message
) {
}
