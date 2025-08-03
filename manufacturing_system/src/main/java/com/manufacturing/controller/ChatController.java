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

    // 建立 contents 陣列，分別是 system 與 user 角色
    List<Map<String, Object>> contents = new ArrayList<>();

    // system 角色的系統提示
    Map<String, Object> system = new HashMap<>();
    system.put("role", "system");
    system.put("parts", List.of(
        Map.of("text", "你是一個智慧客服助理，請用繁體中文簡潔回覆與訂單管理系統有關的問題。"
            + "功能包括登入、註冊、查詢、新增、修改、刪除訂單，"
            + "請僅針對問題回覆相關功能，不要加入歡迎詞或說明其他功能。")
    ));
    contents.add(system);

    // user 角色的使用者訊息
    Map<String, Object> user = new HashMap<>();
    user.put("role", "user");
    user.put("parts", List.of(
        Map.of("text", userMessage)
    ));
    contents.add(user);

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