# 🧠 智慧訂單管理系統（Smart Order Management System）

## 🌐 線上展示（Live Demo）

本系統已完成部署，可透過以下網址體驗實際功能：  
**線上展示網址**：https://manufacturing-system-latest.onrender.com

## 網站使用時間 為保障系統效能與資料完整性，本網站設定使用時間如下： 
- **星期一至星期五**：上午 8:00 至晚上 6:00

---
 
[![Live on Render](https://img.shields.io/badge/render-live-success?logo=render&style=flat)](https://your-app-name.onrender.com)
[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/yourusername/smart-order-system)
 
一套基於 **Vue.js 前端** 和 **Spring Boot 後端** 的智慧訂單管理系統，支援透過 Docker 和 GitHub Actions 自動部署至 [Render](https://render.com)。  
資料庫使用雲端 PostgreSQL 服務 [Neon](https://neon.tech) 託管，實現彈性、高效能的資料儲存。

---
 
## 🚀 技術棧（Tech Stack）
 
- **Frontend**：Vue.js 3, HTML5, CSS3, JavaScript (ES6), Axios 
- **Backend**：Java 21, Spring Boot 3.5.4 + REST API, Spring MVC, Spring Data JPA, Spring Security (JWT 驗證機制)
- **Database**：PostgreSQL（託管於 Neon，JDBC 連線，SSL）
- **CI/CD**：GitHub + GitHub Actions (自動編譯、測試與部署)
- **Container**：Docker + Docker Compose
- **建構工具**：Maven
- **部署平台**：Render（自動部署）
- **郵件服務**：Resend + Cloudflare（支援註冊驗證、密碼重置與通知）
- **排程**：Cronjobs（定時任務與網站運維）

---

## 📦 功能特色（Features）
 
👤 使用者註冊與登入：提供帳號註冊、登入、登出與密碼重置功能，結合 Spring Security + JWT 無狀態驗證，安全管理使用者權限。

📝 訂單管理：支援新增、查詢、修改與刪除（CRUD）訂單操作，依使用者權限管控，透過 RESTful API 與後端資料庫互動。

🖥️ 前端視覺化介面：採 Vue.js 響應式設計與元件化架構，使用 Vue Router 管理頁面導向，並透過 Axios 與後端動態串接資料。

🤖 智慧客服小幫手：整合 Google Gemini 2.0 Flash 與自然語言處理 (NLP)，提供即時聊天互動，協助使用者查詢訂單與操作指引，提升客服效率與使用者體驗。

🔐 權限保護：結合 JWT 與 Spring Security，保護敏感 API，確保系統安全性。

☁️ 雲端資料管理：資料儲存於 Neon PostgreSQL 雲端資料庫，確保資料安全、快速且穩定。

🔄 自動化部署：採 GitHub Actions + Docker + Render 完整 CI/CD 流程，自動建置與部署前後端應用，提升系統發布效率與穩定性。

---


## 🖼️ 系統架構圖（Architecture）
 
```text
project (Frontend Vue)
   |
   |  REST API
   ▼
manufacturing_system (Backend Spring Boot) ——> PostgreSQL (hosted on Neon)
