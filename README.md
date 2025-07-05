# 🏠 RoomieHub

RoomieHub là một nền tảng giúp sinh viên và người đi làm tìm bạn cùng phòng phù hợp và nhà trọ lý tưởng. Ứng dụng tích hợp các công nghệ hiện đại như gợi ý bằng AI, tour phòng 3D, ký hợp đồng điện tử và thanh toán trực tuyến.

---

## 🚀 Tính năng chính

- 🔍 **Tìm kiếm và lọc phòng trọ**
- 👯 **Gợi ý bạn cùng phòng bằng AI (AI roommate matching)**
- 🗺️ **Xem phòng bằng Google Maps và tour phòng 3D**
- 🧾 **Quản lý hợp đồng thuê nhà online**
- 💳 **Thanh toán trực tuyến thông qua PayOS**
- 🧑‍💼 **Quản lý tài khoản người dùng và phân quyền**
- 📦 **Gói dịch vụ: Basic, Professional, VIP, VR**

---

## 🛠️ Công nghệ sử dụng

### Backend
- Java Spring Boot
- Spring Security + JWT
- MySQL
- PayOS SDK (tích hợp thanh toán)
- RESTful API

### Frontend
- React.js (hoặc React Native nếu có mobile app)
- TailwindCSS / MUI
- Axios
- React Router DOM

---

## 📦 Gói dịch vụ

| Gói dịch vụ   | Số bài đăng   |
|---------------|----------------|
| **BASIC**     | 15 bài         |
| **PROFESSIONAL** | 50 bài     |
| **VIP**       | Unlimited       |
| **VR**        | 1 bài (tour 3D) |

> Người dùng có thể mua thêm gói khi gói hiện tại còn hiệu lực, hệ thống sẽ cộng dồn lượt đăng hoặc cập nhật theo logic gói cao hơn.

---

## 🧪 Cài đặt & Chạy project

### ⚙️ Backend - Spring Boot

```bash
# Clone project
git clone https://github.com/yourname/roomiehub-backend.git
cd roomiehub-backend

# Cấu hình file application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/roomiehub
spring.datasource.username=root
spring.datasource.password=yourpassword

# Run
./mvnw spring-boot:run
