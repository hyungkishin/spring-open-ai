package com.myai.openai.application;

import com.myai.openai.entity.Answer;
import com.myai.openai.entity.Movie;
import com.myai.openai.ui.request.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public String chatMessage(String message) {
        return Objects.requireNonNull(chatClient.prompt()
                        .user(message)
                        .call()
                        .chatResponse())
                .getResult()
                .getOutput()
                .getText();
    }

    public String chatPlaceMessage(final ChatRequest request) {
        return Objects.requireNonNull(
                        chatClient.prompt()
                                .user(request.message())
                                .system(sp -> sp.param("subject", request.subject()).param("tone", request.tone()))
                                .call()
                                .chatResponse())
                .getResult()
                .getOutput()
                .getText();
    }

    public ChatResponse chatJsonMessage(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

    public Answer chatObject(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(Answer.class);
    }

    public Answer recipe(String foodName, String question) {
        String recipeTemplate = """
                Answer for {foodName} for {question} ?
                """;

        return chatClient.prompt()
                .user(userSpec -> userSpec.text(recipeTemplate)
                        .param("foodName", foodName)
                        .param("question", question)
                )
                .call()
                .entity(Answer.class);
    }

    /**
     * 응답의 형태를 List 형식으로 받기
     */
    public List<String> chatList(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
    }

    /**
     * 응답의 형태를 Map 형식으로 받기
     */
    public Map<String, String> chatMap(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
    }

    public List<Movie> chatMovie(String directorName) {
        String template = """
                    Generate a list of movies directed by {directorName}. if the director is unknown, return null.
                    한국 영화는 한글로 표기해줘.
                    Each movie should include a title and release year. {format}
                """;

        List<Movie> movies = chatClient.prompt()
                .user(userSpec ->
                        userSpec.text(template)
                                .param("directorName", directorName)
                                .param("format", "json")
                )
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
        return movies;
    }

    public String getResponse(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public void startChat() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your message");

        while (true) {
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                System.out.println("Exiting chat...");
                break;
            }
            String response = getResponse(message);
            System.out.println("Bot: " + response);
        }
        scanner.close();
    }


}
