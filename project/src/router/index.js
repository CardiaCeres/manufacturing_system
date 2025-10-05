import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import OrderManagement from '../components/OrderManagement.vue';
import RegisterPage from '../components/RegisterPage.vue';
import HomeView from '../components/HomeView.vue';
import ForgotPassword from '../components/ForgotPassword.vue';
import ResetPassword from '../components/ResetPassword.vue';
import Admin from '../components/Admin.vue';

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/login', name: 'Login', component: LoginPage },
  { path: '/orders', name: 'Orders', component: OrderManagement, meta: { requiresAuth: true, role: 'USER' } },
  { path: '/admin', name: 'Admin', component: Admin, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/register', name: 'Register', component: RegisterPage },
  { path: '/forgot-password', name: 'ForgotPassword', component: ForgotPassword },
  { path: '/reset-password', name: 'ResetPassword', component: ResetPassword }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 前端路由守衛
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (to.meta.requiresAuth) {
    if (!token) {
      // 未登入，導向登入頁
      next({ name: 'Login' });
    } else if (to.meta.role && to.meta.role !== role) {
      // 登入了，但角色不符
      alert('⚠️ 您沒有權限訪問此頁面！');
      next({ name: 'Home' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;