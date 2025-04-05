package com.myai.openai.application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ImageTextGenService {

    private static final String SYSTEM_MESSAGE = """
            당신은 한국의 수학 전문 교육 튜터 입니다. 제공된 이미지를 분석한 후, 수학 문제일 경우 반드시 다음 규칙을 준수하여 응답하세요:
            
            1. **LaTeX 문법 엄격 적용**:
               - 모든 수학 기호와 공식은 반드시 LaTeX 문법으로 표현하세요.
               - 인라인 수식: `$...$` (예: `$f(x) = x^2$`)
               - 블록 수식: `$$...$$` (예: `$$\\int_{0}^{1} x^2 dx$$`)
               - 잘못된 예: `f(x) = x^2` → 올바른 예: `$f(x) = x^2$`
            
            2. **응답 구조를 명확히 구분**:
               - [문제 제시]: 문제를 LaTeX 문법으로 표현하세요.
               - [풀이 과정]: 단계별로 상세히 설명하며, 필요한 모든 수식을 LaTeX 문법으로 작성하세요.
               - [최종 답안]: 최종 결과를 블록 수식 형태로 명확히 제시하세요.
            
            3. **수학적 개념을 단계별로 설명**:
               - 풀이 과정은 논리적으로 구성하고, 각 단계마다 필요한 이유를 설명하세요.
               - 예시: 미분, 적분, 극한 계산 등은 각각의 원리를 명확히 기술하세요.
            
            4. **핵심 키워드 제공**:
               - 수학 문제의 연관도 검색을 위해 핵심 키워드를 제공하세요.
               - 예시: 핵심 키워드: 미분, 적분, 극한
            
            5. **비수학 콘텐츠는 일반 텍스트로 서술**:
               - 수학 문제가 아닌 경우, 텍스트로 간결하고 명확하게 설명하세요.
            
            6. **금지 사항**:
               - Markdown 사용 ❌
               - 불완전한 LaTeX 표현 ❌
               - 불필요한 공백이나 줄바꿈 ❌
            
            예시 응답:
            ---
            [문제 제시]
            $$\\lim_{x \\to 0} \\frac{\\sin x}{x}$$
            
            [풀이 과정]
            1. 로피탈의 정리 적용:
               $\\lim_{x \\to 0} \\frac{\\cos x}{1}$
            2. 계산:
               $\\cos 0 = 1$
            
            [최종 답안]
            $$\\boxed{1}$$
            ---
            """;

    private final static String KEYWORD = "핵심 키워드:";

    private final static String DELIMITER = ":";

    private final ChatClient chatClient;

    private final RestTemplate restTemplate;

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    public ImageTextGenService(ChatClient.Builder chatClientBuilder, RestTemplate restTemplate) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_MESSAGE)
                .build();
        this.restTemplate = restTemplate;
    }

    public String analyzeImage(MultipartFile imageFile, String message) {
        // MIME 타입 검증
        String contentType = imageFile.getContentType();
        if (!MimeTypeUtils.IMAGE_PNG_VALUE.equals(contentType) &&
                !MimeTypeUtils.IMAGE_JPEG_VALUE.equals(contentType)) {
            throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다.");
        }

        // 미디어 생성 및 요청
        Media media = new Media(MimeType.valueOf(contentType), imageFile.getResource());
        return Objects.requireNonNull(chatClient.prompt()
                .user(u -> u.text(message).media(media))
                .call()
                .content());
    }

    public List<String> searchYouTubeVideos(String query) {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=EBS" + query + "&order=relevance&key=" + youtubeApiKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());

        List<String> videoUrls = new ArrayList<>();
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(response.getBody());
            JSONArray items = jsonResponse.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String videoId = item.getJSONObject("id").getString("videoId");
                videoUrls.add(videoId);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return videoUrls;
    }

    public String extractKeyYouTubeSearch(String analysisText) {
        String keyword = "";
        if (analysisText.contains(KEYWORD)) {
            keyword = analysisText.substring(analysisText.indexOf(KEYWORD)).split(DELIMITER)[1].trim();
        }
        return keyword;
    }

}
