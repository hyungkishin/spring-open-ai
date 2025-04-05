package com.myai.openai.api.request;

import java.util.List;

public record ImageAnalysisDto(
        String imageUrl,
        String analysisText,
        List<String> youtubeUrls
) {
}
