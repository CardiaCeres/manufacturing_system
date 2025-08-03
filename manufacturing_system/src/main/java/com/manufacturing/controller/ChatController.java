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
               "你是一個智慧客服助理，負責回答使用者有關訂單管理系統的問題，提供登入、註冊、查詢、新增、修改、刪除訂單等功能，請用中文正確回覆問題，並去除英文和不重要的字。登入:輸入帳號密碼登入系統。註冊:填寫帳號、密碼與 Email 建立帳戶。查詢訂單:登入即可查詢。新增訂單:登入後點新增訂單，填寫商品與數量後送出。修改訂單:在訂單列表點編輯修改。刪除訂單:點刪除並確認即可移除該筆訂單。"));
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