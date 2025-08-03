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

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();

        // 準備 contents 陣列（符合 Gemini API 規格）
        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");

        List<Map<String, Object>> parts = new ArrayList<>();
        parts.add(Map.of("text",
               "你是一個智慧客服助理，請用繁體中文回答使用者有關訂單管理系統的問題，並僅回答與問題直接相關的部分，避免多餘的客套話、重複說明、英文或無關資訊。" +
    "功能包括：登入、註冊、查詢、新增、修改、刪除訂單。" +
    "各功能簡要說明如下：" +
    "登入：輸入帳號與密碼即可登入系統。" +
    "註冊：填寫帳號、密碼與Email建立帳戶。" +
    "查詢訂單：登入後可查詢訂單。" +
    "新增訂單：登入後點選新增訂單，填寫商品與數量後送出。" +
    "修改訂單：於訂單列表點選編輯進行修改。" +
    "刪除訂單：點選刪除後確認即可移除該筆訂單。" +
    "請根據使用者的問題，僅提供對應功能的具體說明，不需額外自我介紹或重複說明其他功能。"));
        content.put("parts", parts);

        List<Map<String, Object>> contents = new ArrayList<>();
        contents.add(content);

        requestBody.put("contents", contents);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            Map<?, ?> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("candidates")) {
                List<?> candidates = (List<?>) responseBody.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);
                    Map<?, ?> contentMap = (Map<?, ?>) candidate.get("content");
                    List<?> partsList = (List<?>) contentMap.get("parts");
                    if (!partsList.isEmpty()) {
                        Map<?, ?> part = (Map<?, ?>) partsList.get(0);
                        String reply = part.get("text").toString();
                        return reply.trim();
                    }
                }
            }
            return "抱歉，未收到有效回覆。";
        } catch (Exception e) {
            e.printStackTrace();
            return "呼叫 Gemini API 發生錯誤：" + e.getMessage();
        }
    }
}