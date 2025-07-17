# 🧠 智慧訂單管理系統（Smart Order Management System）
 
[![Live on Render](https://img.shields.io/badge/render-live-success?logo=render&style=flat)](https://your-app-name.onrender.com)
[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/yourusername/smart-order-system)
 
一套基於 **Vue.js 前端** 和 **Spring Boot 後端** 的智慧訂單管理系統，支援透過 Docker 和 GitHub Actions 自動部署至 [Render](https://render.com)。  
資料庫使用雲端 PostgreSQL 服務 [Neon](https://neon.tech) 託管，實現彈性、高效能的資料儲存。
 
---
 
## 🚀 技術棧（Tech Stack）
 
- **Frontend**：Vue.js 3 + Vite
- **Backend**：Spring Boot + REST API
- **Database**：PostgreSQL（託管於 [Neon](https://neon.tech)）
- **CI/CD**：GitHub Actions
- **Container**：Docker + Docker Compose
- **部署平台**：Render（自動部署）
 
---
 
## 📦 功能特色（Features）
 
- 📋 客戶可建立、查詢與追蹤訂單
- 🛠️ 後台可編輯商品、管理庫存與審核訂單
- 🔍 即時查詢與篩選訂單狀態
- 📈 管理者儀表板，提供關鍵數據分析
- ☁️ 資料儲存於雲端 Neon PostgreSQL，安全又快速
- 🔄 自動部署至 Render，開發更流暢
 
---
 
## 🖼️ 系統架構圖（Architecture）
 
```text
Vue (Frontend)
   |
   |  REST API
   ▼
Spring Boot (Backend) ——> PostgreSQL (hosted on Neon)
