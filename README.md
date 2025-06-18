# Paypal-Payment-Integration

Paypal Payment Integration
# PayPal Payment Integration - Java Spring Boot Microservices

## 📌 Overview

This project demonstrates the integration of PayPal Standard Checkout using Java Spring Boot in a Microservices Architecture. It showcases secure payment handling, order management, reconciliation logic, and distributed access token management.

---

## 🚀 Features

- 🔐 OAuth 2.0-based PayPal API integration
- 📦 End-to-end order lifecycle management (token generation, create order, capture order)
- 💾 Efficient Redis Cachefor access token reuse
- ⏱ Spring Scheduler for transaction reconciliation
- ⚠️ Centralized Exception Handling with custom error codes
- 🔄 Full payment reconciliation to ensure no unsettled transactions
- 📊 MySQL with Spring JDBC for persistent data storage
  

---

## 🛠 Tech Stack

| Layer              | Technology                     |
|--------------------|-------------------------------|
| Backend            | Java, Spring Boot             |
| Architecture       | Microservices                 |
| API Integration    | PayPal REST APIs              |
| Security           | OAuth 2.0                     |
| Scheduling         | Spring Scheduler              |
| Caching            | Redis                         |
| Database           | MySQL, Spring JDBC            |
| Version Control    | Git, Bitbucket                |
| Deployment         | AWS                           |
| Tools              | IntelliJ IDEA, Postman        |

---

## 🔧 Setup & Run Locally

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/paypal-payment-integration.git
   cd paypal-payment-integration
   
2. Update application.properties

Configure PayPal credentials (client_id, secret)

Add Redis and MySQL connection details

3.Run the Application
mvn clean install
mvn spring-boot:run

4.Test API Endpoints

Use Postman to test create order, capture order, get order, etc.
