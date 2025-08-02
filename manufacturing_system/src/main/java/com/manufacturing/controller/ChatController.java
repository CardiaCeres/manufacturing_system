import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Value("${google.gemini.api_key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");

        // Google Gemini API URL（示意）
        String url = "https://gemini.googleapis.com/v1/chat:generateMessage?key=" + apiKey;

        // 組裝請求內容（範例格式）
        Map<String, Object> requestBody = Map.of(
            "model", "gemini-2.0-flash",
            "messages", Collections.singletonList(
                Map.of("role", "user", "content", userMessage)
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

            // 解析回傳內容 (需依官方回傳結構做調整)
            Map<?, ?> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                var choices = (java.util.List<?>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                    Map<?, ?> message = (Map<?, ?>) choice.get("message");
                    return message.get("content").toString();
                }
            }
            return "抱歉，未收到有效回覆。";
        } catch (Exception e) {
            e.printStackTrace();
            return "呼叫 Gemini API 發生錯誤：" + e.getMessage();
        }
    }
}