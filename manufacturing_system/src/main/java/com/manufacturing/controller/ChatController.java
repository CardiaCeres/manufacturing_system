package com.manufacturing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@CrossOrigin(origins = "frontendUrl")  // 可改成你的前端網址
@RequestMapping("/api")
public class ChatController {

    @Value("${google.gemini.api_key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");

        String url = "https://openrouter.ai/api/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "google/gemini-2.0-flash-exp:free");

        List<Map<String, Object>> messages = new ArrayList<>();

        // 系統提示，定義 AI 角色和回答範圍
        messages.add(Map.of(
                "role", "system",
                "content", List.of(
                        Map.of("type", "text", "text",
                                "你是一個智慧客服助理，負責回答使用者有關訂單管理系統的問題，這個系統提供登入、註冊、查詢、新增、修改、刪除訂單等功能，請用簡單中文回覆問題。")
                )
        ));

        // 使用者輸入訊息
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
                    String rawReply = message.get("content").toString();

                    // 清理回覆文字，去除 **、項目編號，換行符號轉換
                    String cleanedReply = rawReply.replace("\\n", "\n")
                            .replace("**", "")
                            .replaceAll("(?m)^\\d+\\.\\s*", "")
                            .trim();

                    return Map.of("response", cleanedReply);
                }
            }
            return Map.of("response", "抱歉，未收到有效回覆。");
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("response", "呼叫 OpenRouter API 發生錯誤：" + e.getMessage());
        }
    }
}