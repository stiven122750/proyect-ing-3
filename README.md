# Spring Boot CRUD Demo

Project with 4 CRUDs: Customer, Product, Category, Order.
- Architecture: model, repository, service, controller.
- Business rules implemented in services:
  1. Product price must be positive and product name unique (ProductService.create/update).
  2. When creating an order: product must have enough stock and a customer cannot have more than 5 orders (OrderService.create).
- H2 in-memory DB configured in `src/main/resources/application.properties` (H2 console at `/h2-console`).

Run with Gradle:
```
./gradlew bootRun
```

API endpoints (JSON):
- Customers: `GET/POST/PUT/DELETE /api/customers`
- Products: `GET/POST/PUT/DELETE /api/products`
- Categories: `GET/POST/PUT/DELETE /api/categories`
- Orders: `GET/POST/DELETE /api/orders` (POST body: {"customerId":1,"productId":2,"quantity":3})

