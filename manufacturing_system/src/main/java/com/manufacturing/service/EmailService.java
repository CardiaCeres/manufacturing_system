package com.manufacturing.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String resetUrl) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            
            // 第二個參數 true 代表這是 multipart (支援 HTML)
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("no-reply@yourapp.com"); // ✅ 寄件人 (要在 SendGrid 驗證過)
            helper.setTo(toEmail);
            helper.setSubject("重設您的密碼");

            // HTML 內容
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

            helper.setText(htmlContent, true); // 第二個參數 true = HTML

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("寄送郵件失敗: " + e.getMessage(), e);
        }
    }
}