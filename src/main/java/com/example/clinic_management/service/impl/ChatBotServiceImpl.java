package com.example.clinic_management.service.impl;

import com.example.clinic_management.service.ChatBotService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final OpenAiService openAiService;

    @Override
    public String generateResponse(String prompt) {
        try {
            ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .messages(List.of(userMessage))
                    .model("gpt-4o-mini")
                    .maxTokens(300)
                    .build();

            ChatCompletionResult response = openAiService.createChatCompletion(request);

            return response.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
//            log.error("Error generating OpenAI response", e);
            return "I apologize, but I encountered an error processing your request.";
        }
    }
}
