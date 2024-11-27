package com.example.clinic_management.service.impl;

import com.example.clinic_management.service.ChatBotLocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
@RequiredArgsConstructor
public class ChatBotLocalServiceImpl implements ChatBotLocalService {

    @Value("${chatbot.api.url}")
    private String chatBotApiUrl;

    private final RestTemplate restTemplate;

    @Override
    public String getChatBotResponse(String question) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject request = new JSONObject();
        request.put("question", question);

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                chatBotApiUrl + "/chat",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
}
