# 校园图书借阅管理系统

## 系统部署说明

| 学号 | 姓名 |
|------|------|
| 233428010133 | 徐天成 |
| 233428010123 | 王博洋 |

项目地址：`https://github.com/YYYYPPEE/library_management_system.git`

## 技术栈声明

- **后端**：Spring Boot 3.2.0 + Spring Data JPA + Spring Security + JWT + MySQL
- **前端**：HTML5 + CSS3 + JavaScript + Bootstrap 5

## 本地部署步骤

### 1. 数据库初始化

```sql
CREATE DATABASE IF NOT EXISTS library_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_db;
-- 执行 database/init.sql 建表
```

### 2. 后端启动

```bash
cd backend
# 修改 src/main/resources/application.yml 中的数据库账号密码
mvn clean install
mvn spring-boot:run
# 后端服务将在 http://localhost:8081 启动
```

### 3. 前端访问

本项目前端文件已集成在后端静态资源中，启动后端后直接访问：

```
http://localhost:8081
```

或者直接打开 `frontend/index.html` 也可以访问（注意：此时需要后端服务正常运行）。

### 4. 测试账号

请先访问注册页面自行注册账号，支持注册学生和管理员两种角色。

## 功能特性

- 用户注册/登录（支持学生和管理员角色）
- 图书管理（添加、编辑、删除、查询图书）
- 图书借阅/归还
- 借阅记录查询
- 数据统计（仅管理员）
- 日间/夜间模式切换
