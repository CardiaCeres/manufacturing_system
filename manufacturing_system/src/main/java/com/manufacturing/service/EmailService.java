package com.manufacturing.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    // 讀取 application.properties 裡的 API Key
    public EmailService(@Value("${resend.api.key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    /* =========================
       註冊驗證信
       ========================= */
    public void sendVerifyEmail(String toEmail, String verifyUrl) {

        String senderEmail = System.getenv("VERIFY_EMAIL_FROM");

        String htmlContent = """
                <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                    <h2>📧 Email 驗證通知</h2>
                    <p>您好，</p>
                    <p>感謝您註冊智慧訂單管理系統，請點擊下方按鈕完成 Email 驗證：</p>
                    <p>
                        <a href="%s" style="display:inline-block; padding:10px 20px; 
                            background-color:#667eea; color:#fff; 
                            text-decoration:none; border-radius:8px;">
                            ✅ 驗證 Email
                        </a>
                    </p>
                    <p>若非您本人操作，請忽略此信件。</p>
                    <hr/>
                    <small>智慧訂單管理系統 · 請勿回覆此信件</small>
                </div>
                """.formatted(verifyUrl);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(senderEmail) // 寄件人
                .to(toEmail)       // 使用者註冊信箱
                .subject("完成 Email 驗證")
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("📧 驗證郵件已送出, ID: " + data.getId());
        } catch (ResendException e) {
            throw new RuntimeException("寄送驗證郵件失敗: " + e.getMessage(), e);
        }
    }

    /* =========================
       重設密碼信
       ========================= */
    public void sendResetPasswordEmail(String toEmail, String resetUrl) {

        String senderEmail = System.getenv("RESET_EMAIL_FROM");

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
                .from(senderEmail) // 寄件人
                .to(toEmail)       // 使用者註冊信箱
                .subject("重設您的密碼")
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("📧 密碼重設郵件已送出, ID: " + data.getId());
        } catch (ResendException e) {
            throw new RuntimeException("寄送密碼重設郵件失敗: " + e.getMessage(), e);
        }
    }
}
