package com.myai.openai.application;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class OpenAiChatService {

    private static final String SYSTEM_FUN_AIR_GUIDE = "당신은 항공사 'Funnair' 의 고객 지원 에이전트입니다. \n 친절하고 도움을 주며, 즐거운 태도로 응답하세요. \n 예약 정보 제공 또는 예약 취소 전에 반드시 다음 정보를 사용자로부터 받아야 합니다: \n 예약 번호, 고객 이름(이름 및 성). \n 예약 변경 전에 반드시 약관에 따라 허용 여부를 확인하세요. \n 변경에 비용이 발생할 경우, 사용자에게 동의를 요청해야 합니다.";

    private static final String DEFAULT_SYSTEM_GUID = "당신은 교육 튜터 에이전트입니다. \n 친절하게 도움을 주며, 정확한 정보를 제공해야 합니다 즐거운 태도로 응답하세요.";

    private final ChatClient chatClient;

    /**
     * 생성자: ChatClient를 초기화합니다.
     *
     * @param builder     ChatClient 빌더
     * @param vectorStore 외부 데이터 검색을 위한 VectorStore
     * @param chatMemory  대화 기록 관리 객체
     */
    public OpenAiChatService(ChatClient.Builder builder, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultSystem(DEFAULT_SYSTEM_GUID) // 시스템 메시지: 챗봇의 역할과 행동 방식을 정의.
                .defaultAdvisors( // 어드바이저 설정: 대화 기록, 외부 데이터 검색, 로깅 기능 추가
                        new MessageChatMemoryAdvisor(chatMemory), // 대화 기록 관리
                        new QuestionAnswerAdvisor(vectorStore),   // 외부 데이터 검색 (RAG)
                        new LoggingAdvisor())                // 로깅 기능
                //  example : Tool 설정: 예약 관련 기능 정의
                // .defaultTools(
                //         FunctionToolCallback.builder("getBookingDetails", new BookingService()::getBookingDetails)
                //                 .description("예약 번호와 고객 이름을 기반으로 예약 정보를 조회합니다.")
                //                 .inputType(BookingRequest.class)
                //                 .build(),
                //         FunctionToolCallback.builder("changeBooking", new BookingService()::changeBooking)
                //                 .description("약관을 확인한 뒤 예약 정보를 변경합니다.")
                //                 .inputType(ChangeRequest.class)
                //                 .build(),
                //         FunctionToolCallback.builder("cancelBooking", new BookingService()::cancelBooking)
                //                 .description("사용자 동의를 받은 후 예약을 취소합니다.")
                //                 .inputType(CancelRequest.class)
                //                 .build()
                // )
                .build();
    }

    /**
     * 사용자 메시지를 처리하고 응답을 스트리밍 방식으로 반환한다.
     *
     * @param chatId             대화 ID (대화 기록 관리용)
     * @param userMessageContent 사용자 메시지 내용
     * @return Flux<String> 스트리밍 방식으로 응답 반환
     */
    public Flux<String> chat(String chatId, String userMessageContent) {
        return this.chatClient.prompt()
                .user(userMessageContent) // 사용자 메시지를 추가한다.
                .advisors(a -> a         // a == 어드바이저 : 대화 기록 및 검색 크기 지정
                        .param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) // 대화의 상태를 기억하고, 이전 대화 내용을 기반으로 응답을 생성
                        .param(MessageChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)) // 최대 100개의 이전 대화 내용을 검색
                .stream().content();     // 스트리밍 방식으로 응답 반환
    }

}
