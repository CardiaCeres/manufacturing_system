package com.manufacturing.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    // 讀取 application.properties 裡的 API Key
    public EmailService(@Value("${RESEND_API_KEY}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void sendResetPasswordEmail(String toEmail, String resetUrl) {
        try {
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

            SendEmailRequest params = SendEmailRequest.builder()
                    .from("智慧訂單管理系統 <no-reply@yourdomain.com>") // ✅ 需在 Resend 驗證過的網域
                    .to(toEmail)
                    .subject("重設您的密碼")
                    .html(htmlContent)
                    .build();

            SendEmailResponse data = resend.emails().send(params);

            System.out.println("📧 郵件已送出, ID: " + data.getId());

        } catch (ResendException e) {
            throw new RuntimeException("寄送郵件失敗: " + e.getMessage(), e);
        }
    }
}
