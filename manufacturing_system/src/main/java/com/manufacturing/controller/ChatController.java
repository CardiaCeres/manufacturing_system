package com.manufacturing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@CrossOrigin(origins = "frontend.url")
@RequestMapping("/api")
public class ChatController {

    @Value("${google.gemini.api_key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");

        String url = "https://openrouter.ai/api/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "google/gemini-2.0-flash-exp:free");

        List<Map<String, Object>> messages = new ArrayList<>();

        messages.add(Map.of(
            "role", "system",
            "content", List.of(
                Map.of("type", "text", "text",
                    "你是一個智慧客服助理，負責回答使用者有關訂單管理系統的問題，提供登入、註冊、查詢、新增、修改、刪除訂單等功能，請用中文正確回覆問題，並去除英文和不重要的字。登入:輸入帳號密碼登入系統。註冊:填寫帳號、密碼與 Email 建立帳戶。查詢訂單:登入即可查詢。新增訂單:登入後點新增訂單，填寫商品與數量後送出。修改訂單:在訂單列表點編輯修改。刪除訂單:點刪除並確認即可移除該筆訂單。")
            )
        ));

        messages.add(Map.of(
            "role", "user",
            "content", List.of(
                Map.of("type", "text", "text", userMessage)
            )
        ));

        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            Map<?, ?> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("choices")) {
                List<?> choices = (List<?>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                    Map<?, ?> message = (Map<?, ?>) choice.get("message");
                    String reply = message.get("content").toString();

                    // 去除多餘字元和換行符號
                    reply = reply.replaceAll("\\\\n", " ")
                                 .replaceAll("\\n", " ")
                                 .replaceAll("<br>", " ")
                                 .replaceAll("\\{.*?\\}", "")
                                 .replaceAll("\"", "")
                                 .trim();
                    return reply;
                }
            }
            return "抱歉，未收到有效回覆。";
        } catch (Exception e) {
            e.printStackTrace();
            return "呼叫 OpenRouter API 發生錯誤：" + e.getMessage();
        }
    }
}