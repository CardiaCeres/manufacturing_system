<template>
  <div class="home">
    <div class="background-wrapper">
      <header class="hero">
        <div class="container content-box">
          <h1>📦 智慧訂單管理系統</h1>
          <p>全方位的企業訂單、客戶與庫存解決方案</p>
          <router-link to="/login" class="btn-login">登入系統</router-link>
        </div>
      </header>

      <section class="features">
        <div class="container content-box">
          <h2>✨ 核心功能</h2>
          <div class="feature-grid">
            <div class="card">
              <h3>🧾 智慧訂單管理</h3>
              <p>支援新增、修改、刪除、查詢等全套功能。</p>
            </div>
            <div class="card">
              <h3>📊 資料儀表板</h3>
              <p>即時掌握訂單量、出貨狀況與營收走勢。</p>
            </div>
            <div class="card">
              <h3>👤 客戶管理</h3>
              <p>彙整客戶資訊，提升關係維護與行銷效率。</p>
            </div>
            <div class="card">
              <h3>🔐 高度安全</h3>
              <p>帳號密碼保護，資料加密，安全有保障。</p>
            </div>
          </div>
        </div>
      </section>

      <footer class="footer content-box">
        <p>© 2025 訂單管理系統 | 由你的企業專屬打造</p>
      </footer>
    </div>

    <!-- 智慧客服聊天視窗元件 -->
    <ChatWidget />
  </div>
</template>

<script>
import ChatWidget from "@/components/ChatWidget.vue";

export default {
  name: "HomeView",
  components: {
    ChatWidget,
  },
};
</script>

<style scoped>
/* 背景設定 */
.home {
  font-family: 'Segoe UI', sans-serif;
  color: #333;
  margin: 0;
}

.background-wrapper {
  background-image: url('/public/photo.png'); /* 自行替換背景圖路徑 */
  background-size: cover;
  background-position: center;
  min-height: 100vh;
  width: 100%;
}

/* wrapper 讓內容不完全蓋住背景 */
.content-box {
  background-color: rgba(255, 255, 255, 0.85);
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

/* 頁首區塊 */
.hero {
  padding: 100px 20px 60px;
  text-align: center;
}

.hero h1 {
  font-size: 3rem;
  margin-bottom: 10px;
  animation: fadeInDown 1s ease-out;
}

.hero p {
  font-size: 1.3rem;
  margin-bottom: 30px;
  animation: fadeInUp 1.2s ease-out;
}

.btn-login {
  padding: 12px 30px;
  background: #674f4f;
  color: white;
  font-weight: bold;
  text-decoration: none;
  border-radius: 30px;
  transition: 0.3s;
  animation: fadeInUp 1.5s ease-out;
}

.btn-login:hover {
  background: #563f3f;
}

/* 功能介紹區塊 */
.features {
  padding: 60px 20px;
  text-align: center;
}

.features h2 {
  font-size: 2rem;
  margin-bottom: 40px;
  color: #444;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 25px;
  padding: 0 20px;
}

.card {
  background: #ffffff;
  padding: 25px;
  border-radius: 15px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.card h3 {
  font-size: 1.3rem;
  margin-bottom: 10px;
  color: #000000;
}

.card p {
  font-size: 1rem;
  color: #555;
}

/* 頁尾 */
.footer {
  background: rgba(255, 255, 255, 0.9);
  padding: 20px;
  font-size: 0.9rem;
  color: #666;
  text-align: center;
  margin-top: 40px;
  border-radius: 0 0 15px 15px;
}

/* 動畫 */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 容器 */
.container {
  max-width: 1100px;
  margin: 0 auto;
}
</style>