import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import OrderManagement from '../components/OrderManagement.vue';
import RegisterPage from '../components/RegisterPage.vue';
import HomeView from '../components/HomeView.vue';

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/login', name: 'Login', component: LoginPage },
  { path: '/register', name: 'Register', component: RegisterPage },
  {
    path: '/orders',
    name: 'Orders',
    component: OrderManagement,
    meta: { requiresAuth: true } // 需要登入才能訪問
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守衛：檢查是否有 token
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router;
