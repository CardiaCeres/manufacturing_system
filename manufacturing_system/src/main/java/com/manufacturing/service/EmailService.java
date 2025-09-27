package com.manufacturing.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${RESEND_API_KEY:}")  // 沒設定也能啟動
    private String apiKey;

    private Resend resend;

    private Resend getResend() {
        if (resend == null) {
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("❌ Resend API Key 未設定");
            }
            resend = new Resend(apiKey);
        }
        return resend;
    }

    public void sendResetPasswordEmail(String toEmail, String resetUrl) {
        String htmlContent = """
                <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                    <h2>🔐 重設密碼通知</h2>
                    <p>您好，</p>
                    <p>我們收到了您重設密碼的請求，請點擊下方按鈕以設定新密碼：</p>
                    <p>
                        <a href="%s" style="display:inline-block; padding:10px 20px; 
                            background-color:#667eea; color:#fff; 
                            text-decoration:none; border-radius:8px;">
                            👉 重設密碼
                        </a>
                    </p>
                    <p>如果不是您本人操作，請忽略這封信件。</p>
                    <hr/>
                    <small>智慧訂單管理系統 · 請勿回覆此信件</small>
                </div>
                """.formatted(resetUrl);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("智慧訂單管理系統 <onboarding@resend.dev>")  // ✅ 無網域也能用
                .to("delivered@resend.dev")  // 例如 Gmail 信箱
                .subject("重設您的密碼")
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse data = getResend().emails().send(params);
            System.out.println("📧 郵件已送出, ID: " + data.getId());
        } catch (ResendException e) {
            System.err.println("❌ 郵件寄送失敗: " + e.getMessage());
        }
    }
}