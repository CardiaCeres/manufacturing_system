import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import OrderManagement from '../components/OrderManagement.vue';
import RegisterPage from '../components/RegisterPage.vue';
import HomeView from '../components/HomeView.vue';

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/login', name: 'Login', component: LoginPage },
  { path: '/orders', name: 'Orders', component: OrderManagement, meta: { requiresAuth: true } },
  { path: '/register', name: 'Register', component: RegisterPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 全局路由守衛
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');

  // 需要登入才能進入的頁面
  if (to.meta.requiresAuth && !token) {
    next('/login'); // 沒 token 導向登入頁
  } else if ((to.path === '/login' || to.path === '/register') && token) {
    // 如果已登入，避免回到登入或註冊頁，導向首頁或訂單頁
    next('/orders');
  } else {
    next(); // 其他路由正常放行
  }
});

export default router;