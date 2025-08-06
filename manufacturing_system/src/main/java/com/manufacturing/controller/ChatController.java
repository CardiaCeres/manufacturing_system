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

        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");

        // 把引導語 + 使用者問題合併成一段話
        String prompt = """
你是一個智慧客服助理，負責回答有關訂單管理系統的問題，請用中文正確回覆問題，僅回答與問題直接相關的部分，功能包括：登入、登出、註冊、查詢、新增、修改、刪除訂單，帳號密碼保護，資料加密，安全有保障。登入:輸入帳號與密碼即可登入系統。登出:點選登出，註冊：填寫帳號、密碼與Email建立帳戶。查詢訂單:登入系統後可查詢訂單狀態。新增訂單:登入後點選新增訂單，填寫商品與數量後送出。修改訂單:於訂單列表點選編輯進行修改。刪除訂單:點選刪除後確認即可移除該筆訂單。
問題：""" + userMessage;

        content.put("parts", List.of(Map.of("text", prompt)));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

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
                     reply = reply.replaceAll("\\*\\*", ""); // <-- 這行是重點
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
