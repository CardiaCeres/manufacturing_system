package com.manufacturing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "frontendUrl")
public class ChatController {

    @Value("${google.gemini.api_key}")
    private String apiKey;

    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        // 系統提示：指導 AI 聚焦在你的訂單管理系統操作流程
        String systemPrompt = """
你是訂單管理系統的 AI 助手，請根據以下說明以簡潔語句回答使用者問題：

- 登入：輸入帳號密碼登入系統。
- 註冊：填寫帳號、密碼與 Email 建立帳戶。
- 查詢訂單：登入即可查詢。
- 新增訂單：登入後點「新增訂單」，填寫商品與數量後送出。
- 修改訂單：在訂單列表點「編輯」，修改後儲存。
- 刪除訂單：點「刪除」並確認即可移除該筆訂單。

請直接回應步驟，不需問候語或額外說明。
""";

        // 封裝請求訊息
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistralai/mixtral-8x7b");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
        ));

        // 設定 Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        // headers.add("HTTP-Referer", "https://your-site-url.example.com"); // 可選
        // headers.add("X-Title", "Your Site Name"); // 可選

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 發送 POST 請求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(
                    OPENROUTER_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String reply = (String) message.get("content");

            // 回傳 AI 回應
            return ResponseEntity.ok(Map.of("response", reply));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "AI 回覆失敗：" + e.getMessage()));
        }
    }
}
