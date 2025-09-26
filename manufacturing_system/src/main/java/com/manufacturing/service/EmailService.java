package com.manufacturing.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key:}")  // 如果沒設定，會是空字串
    private String apiKey;

    private Resend resend;

    /**
     * 延遲初始化 Resend
     */
    private Resend getResend() {
        if (resend == null) {
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException(
                        "❌ Resend API Key 未設定，請先在環境變數或 application.properties 設定 RESEND_API_KEY"
                );
            }
            resend = new Resend(apiKey);
        }
        return resend;
    }

    /**
     * 寄送重設密碼信件
     * 如果沒設定 API Key，會在這裡丟例外，而不是在 Spring 啟動時 crash
     */
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
                .from("智慧訂單管理系統 <no-reply@yourdomain.com>")
                .to(toEmail)
                .subject("重設您的密碼")
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse data = getResend().emails().send(params);
            System.out.println("📧 郵件已送出, ID: " + data.getId());
        } catch (ResendException e) {
            throw new RuntimeException("寄送郵件失敗", e);
        }
    }
}
