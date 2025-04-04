package com.myai.openai.api;

import com.myai.openai.api.request.ImageRequestDTO;
import com.myai.openai.application.ImageGenerateService;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ImageGenerationController {

    private final ImageGenerateService imageGenerateService;

    public ImageGenerationController(ImageGenerateService imageGenerateService) {
        this.imageGenerateService = imageGenerateService;
    }

    @PostMapping(value = "/v1/images/generate", consumes = "application/json; charset=UTF-8")
    public List<String> image(@RequestBody ImageRequestDTO request) {
        ImageResponse imageResponse = imageGenerateService.generateOpenAiImage(request);

        return imageResponse.getResults().stream()
                .map(result -> result.getOutput().getUrl())
                .toList();
    }

}
