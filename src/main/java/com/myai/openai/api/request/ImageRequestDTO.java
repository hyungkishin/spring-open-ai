package com.myai.openai.api.request;

public record ImageRequestDTO(
        String message,
        String model,
        int count
){}
