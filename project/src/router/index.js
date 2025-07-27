import { createRouter, createWebHistory } from "vue-router";
import Login from "@/views/Login.vue";
import Register from "@/views/Register.vue";
import OrderManagement from "@/views/OrderManagement.vue";

const routes = [
  {
    path: "/login",
    name: "Login",
    component: Login
  },
  {
    path: "/register",
    name: "Register",
    component: Register
  },
  {
    path: "/orders",
    name: "OrderManagement",
    component: OrderManagement,
    meta: { requiresAuth: true } // 需要登入才能訪問
  },
  {
    path: "/",
    redirect: "/orders"
  },
  {
    path: "/:pathMatch(.*)*", // 404 頁面可自行建立
    redirect: "/login"
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 全域路由守衛：檢查 token，沒有就導到登入頁
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");

  if (to.meta.requiresAuth && !token) {
    next({ name: "Login" });
  } else {
    next();
  }
});

export default router;