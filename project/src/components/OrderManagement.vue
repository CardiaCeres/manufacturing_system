<template>
  <div class="wrapper">
    <div class="orders">
      <div class="header">
        <button class="logout-button" @click="logout">登出</button>
      </div>
      <h2>訂單管理</h2>

      <div class="create-form">
        <input v-model="newOrder.orderNumber" placeholder="訂單編號" />
        <input v-model="newOrder.customerName" placeholder="客戶名稱" />
        <input v-model="newOrder.materialName" placeholder="材料名稱" />
        <input v-model="newOrder.productName" placeholder="產品名稱" />
        <input v-model.number="newOrder.quantity" placeholder="數量" type="number" />
        <input v-model.number="newOrder.price" placeholder="單價" type="number" />
        <input v-model="newOrder.orderDate" type="date" />
        <select v-model="newOrder.status">
          <option value="處理中">處理中</option>
          <option value="已完成">已完成</option>
          <option value="已取消">已取消</option>
        </select>
        <button @click="createOrder">{{ isEditing ? "更新訂單" : "新增訂單" }}</button>
        <button v-if="isEditing" @click="cancelEdit" style="margin-left:10px;">取消編輯</button>
      </div>

      <div v-if="orders.length === 0">
        <p>目前沒有任何訂單。</p>
      </div>

      <table v-else>
        <thead>
          <tr>
            <th>訂單編號</th>
            <th>客戶名稱</th>
            <th>材料名稱</th>
            <th>產品名稱</th>
            <th>數量</th>
            <th>單價</th>
            <th>總金額</th>
            <th>狀態</th>
            <th>訂單日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.orderNumber }}</td>
            <td>{{ order.customerName }}</td>
            <td>{{ order.materialName }}</td>
            <td>{{ order.productName }}</td>
            <td>{{ order.quantity }}</td>
            <td>{{ order.price }}</td>
            <td>{{ order.totalAmount }}</td>
            <td>{{ order.status }}</td>
            <td>{{ order.orderDate }}</td>
            <td>
              <button @click="editOrder(order)">編輯</button>
              <button @click="deleteOrder(order.id)">刪除</button>
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
  name: "OrderManagement",
  data() {
    return {
      orders: [],
      newOrder: {
        orderNumber: "",
        customerName: "",
        materialName: "",
        productName: "",
        quantity: null,
        price: null,
        orderDate: "",
        status: "處理中"
      },
      isEditing: false,
      editingOrderId: null
    };
  },
  created() {
    const token = localStorage.getItem('token');
    if (!token) {
      this.$router.push('/login');
      return;
    }

    axios.defaults.baseURL = "/api";
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    axios.defaults.withCredentials = true;

    this.fetchOrders();
  },
  methods: {
    async fetchOrders() {
      try {
        const response = await axios.get("/orders/my", { withCredentials: true });
        this.orders = response.data;
        this.sortOrders();
      } catch (error) {
        console.error("取得訂單資料失敗", error);
        alert("取得訂單資料失敗，請稍後再試。");
        if (error.response && error.response.status === 401) {
          this.logout();
        }
      }
    },
    async createOrder() {
      try {
        if (this.isEditing) {
          const response = await axios.put(`/orders/update/${this.editingOrderId}`, this.newOrder, { withCredentials: true });
          const index = this.orders.findIndex(o => o.id === this.editingOrderId);
          if (index !== -1) this.orders.splice(index, 1, response.data);
          alert("訂單更新成功！");
          this.isEditing = false;
          this.editingOrderId = null;
        } else {
          const response = await axios.post("/orders/create", this.newOrder, { withCredentials: true });
          this.orders.push(response.data);
          alert("訂單新增成功！");
        }
        this.sortOrders();
        this.resetForm();
      } catch (error) {
        console.error("操作失敗", error);
        alert("請填寫完整的訂單資料");
      }
    },
    editOrder(order) {
      this.isEditing = true;
      this.editingOrderId = order.id;
      this.newOrder = { ...order };
    },
    async deleteOrder(orderId) {
      const confirmDelete = confirm("您確定要刪除此訂單嗎？此操作無法復原！");
      if (!confirmDelete) return;

      try {
        await axios.delete(`/orders/delete/${orderId}`, { withCredentials: true });
        this.orders = this.orders.filter(order => order.id !== orderId);
        this.sortOrders();
        alert("訂單刪除成功！");
      } catch (error) {
        console.error("刪除失敗", error);
        alert("刪除訂單失敗，請稍後再試！");
      }
    },
    logout() {
      const confirmed = window.confirm("確定要登出嗎？");
      if (!confirmed) return;

      localStorage.removeItem("token");
      localStorage.removeItem("user");
      delete axios.defaults.headers.common["Authorization"];
      this.$router.push("/login");
    },
    resetForm() {
      this.newOrder = {
        orderNumber: "",
        customerName: "",
        materialName: "",
        productName: "",
        quantity: null,
        price: null,
        orderDate: "",
        status: "處理中"
      };
    },
    cancelEdit() {
      this.isEditing = false;
      this.editingOrderId = null;
      this.resetForm();
    },
    sortOrders() {
      this.orders.sort((a, b) => {
        const numA = Number(a.orderNumber);
        const numB = Number(b.orderNumber);
        if (isNaN(numA) || isNaN(numB)) {
          return a.orderNumber.localeCompare(b.orderNumber);
        }
        return numA - numB;
      });
    }
  }
};
</script>

<style scoped>
.wrapper {
  min-height: 100vh;
  background-image: url('/public/photo.png');
  background-size: cover;
  background-position: center;
  padding: 2rem;
  box-sizing: border-box;
}

.orders {
  max-width: 1000px;
  margin: 0 auto;
  font-family: 'Segoe UI', sans-serif;
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(8px);
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.15);
}

.header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.logout-button {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.logout-button:hover {
  background-color: #2980b9;
}

.create-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.create-form input,
.create-form select {
  padding: 8px;
  flex: 1 1 150px;
}

.create-form button {
  padding: 8px 16px;
  background-color: #4caf50;
  color: white;
  border: none;
  cursor: pointer;
}

.create-form button:hover {
  background-color: #45a049;
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: #f9f9f9;
}

th,
td {
  padding: 10px;
  text-align: left;
  border: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}

button {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 6px 12px;
  cursor: pointer;
  margin-right: 5px;
}

button:hover {
  background-color: #d32f2f;
}
</style>