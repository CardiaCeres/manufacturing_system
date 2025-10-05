<template>
  <div class="p-6">
    <h2 class="text-2xl font-bold mb-4">📦 訂單總覽（管理員）</h2>

    <div v-if="loading" class="text-gray-500">載入中...</div>

    <div v-else>
      <table border="1" cellpadding="8" cellspacing="0" width="100%">
        <thead>
          <tr class="bg-gray-100">
            <th>ID</th>
            <th>訂單編號</th>
            <th>客戶名稱</th>
            <th>產品名稱</th>
            <th>數量</th>
            <th>總金額</th>
            <th>狀態</th>
            <th>建立日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.id }}</td>
            <td>{{ order.orderNumber }}</td>
            <td>{{ order.customerName }}</td>
            <td>{{ order.productName }}</td>
            <td>{{ order.quantity }}</td>
            <td>{{ order.totalAmount }}</td>
            <td>
              <select v-model="order.status" @change="updateStatus(order)">
                <option>待處理</option>
                <option>已出貨</option>
                <option>已完成</option>
                <option>已取消</option>
              </select>
            </td>
            <td>{{ order.orderDate }}</td>
            <td>
              <button @click="deleteOrder(order.id)" class="bg-red-500 text-white px-2 py-1 rounded">刪除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Admin",
  data() {
    return {
      orders: [],
      loading: true,
    };
  },
  async created() {
    const role = localStorage.getItem("role");
    if (role !== "ADMIN") {
      alert("您沒有權限訪問此頁面");
      this.$router.push("/orders");
      return;
    }
    this.fetchOrders();
  },
  methods: {
    async fetchOrders() {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get("/api/admin/orders", {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.orders = res.data;
      } catch (err) {
        console.error("載入失敗:", err);
        alert("無法載入訂單資料");
      } finally {
        this.loading = false;
      }
    },
    async updateStatus(order) {
      try {
        const token = localStorage.getItem("token");
        await axios.put(`/api/admin/orders/${order.id}`, order, {
          headers: { Authorization: `Bearer ${token}` },
        });
        alert("訂單狀態已更新");
      } catch (err) {
        alert("更新失敗");
      }
    },
    async deleteOrder(id) {
      if (!confirm("確定要刪除此訂單？")) return;
      try {
        const token = localStorage.getItem("token");
        await axios.delete(`/api/admin/orders/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.orders = this.orders.filter((o) => o.id !== id);
      } catch (err) {
        alert("刪除失敗");
      }
    },
  },
};
</script>

<style scoped>
table {
  border-collapse: collapse;
}
th, td {
  border: 1px solid #ddd;
  text-align: center;
}
select {
  padding: 4px;
}
button {
  cursor: pointer;
}
</style>
