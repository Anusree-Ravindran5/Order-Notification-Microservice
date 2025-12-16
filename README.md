# Order-Notification-Microservice

- URL: http://localhost:8080/api/orders
 - Method: get/post
### Request Body for post:
```Json{
  "customerName": "test1",
  "totalAmount": 20,
  "items": [
    {
      "productName": "Color Pencil",
      "quantity": 1,
      "price": 20
    }
  ]
}
```
