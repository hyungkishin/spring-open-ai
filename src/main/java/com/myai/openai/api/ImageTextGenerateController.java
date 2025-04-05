package com.myai.openai.api;

import com.myai.openai.api.request.ImageAnalysisDto;
import com.myai.openai.application.ImageTextGenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/image-text")
public class ImageTextGenerateController {

    private final ImageTextGenService imageTextGenerateService;

    @Value("${upload.path}")
    private String uploadPath;

    public ImageTextGenerateController(ImageTextGenService imageTextGenerateService) {
        this.imageTextGenerateService = imageTextGenerateService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ImageAnalysisDto> getMultimodalResponse(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam(defaultValue = "이 이미지에 무엇이 있나요?") String message) throws IOException {

        // Ensure the upload directory exists
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // Save the uploaded file to the specified upload path
        String filename = imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, filename);
        Files.write(filePath, imageFile.getBytes());

        String analysisText = imageTextGenerateService.analyzeImage(imageFile, message);
        String searchKeyword = imageTextGenerateService.extractKeyYouTubeSearch(analysisText);
        List<String> youtubeUrls = imageTextGenerateService.searchYouTubeVideos(searchKeyword);

        String imageUrl = "/uploads/" + filename;

        var response = new ImageAnalysisDto(imageUrl, analysisText, youtubeUrls);
        return ResponseEntity.ok(response);
    }

}
