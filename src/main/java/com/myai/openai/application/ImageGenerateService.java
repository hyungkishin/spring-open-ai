package com.myai.openai.application;

import com.myai.openai.api.request.ImageRequestDTO;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

@Service
public class ImageGenerateService {

    private static final String IMAGE_QUALITY_HD = "hd";

    private final OpenAiImageModel openAiImageModel;

    public ImageGenerateService(OpenAiImageModel openAiImageModel) {
        this.openAiImageModel = openAiImageModel;
    }

    public ImageResponse generateOpenAiImage(final ImageRequestDTO request) {
        return openAiImageModel
                .call(new ImagePrompt(request.message(),
                        OpenAiImageOptions.builder()
                                .withModel(request.model())
                                .withQuality(IMAGE_QUALITY_HD)
                                .withN(request.count())
                                .withHeight(1024)
                                .withWidth(1024).build())
                );
    }

}
