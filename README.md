User:
http://localhost:8080/users
{
  "username": "suzii",
  "email": "suzi@example.com",
  "password": "123456",
  "storeName": "New User's Store",
  "role": "RETAILER"
}

_______________________________________
Cateogry:
http://localhost:8080/categories

{
  "name": "Baby"
}


_________________________________________
Product:
http://localhost:8080/users/1/products


{
  "name": "Product Name33",
  "description": "Detailed description of the product",
  "price": 19.99,
  "stockQuantity": 100,
  "categoryId": 1,
  "availableSizes": ["SMALL", "MEDIUM"],
  "availableColors": ["RED","GREEN"]
}






___________________________________

Cart:
http://localhost:8080/carts/2  user_id
{
  "items": []
}
________________________
CartItem:

http://localhost:8080/cartItems/add/2   cart_id
{
  "productId": 2,
  "quantity": 2,
  "price": 19.99,
  "productName": "Product2 Name",
  "color": "GREEN",
  "total": 19.99
}
///
{
    "cartItemId": 17,
    "product": {
        "productId": 2,
        "productName": "T-shirt",
        "description": "Product Description",
        "price": 99.99,
        "category": {
            "categoryId": 1,
            "name": "Baby",
            "hibernateLazyInitializer": {}
        },
        "user": {
            "userId": 1,
            "username": "suzii",
            "password": "123456",
            "email": "suzi@example.com",
            "storeName": "New User's Store",
            "role": "RETAILER",
            "hibernateLazyInitializer": {}
        },
        "stockQuantity": 100,
        "availableSizes": [],
        "availableColors": [],
        "hibernateLazyInitializer": {}
    },
    "quantity": 2,
    "price": 19.99,
    "color": "GREEN",
    "total": 39.98,
    "productName": "Product2 Name"
}
///__________________________________

Order:
http://localhost:8080/orders

{
  "userId": 2, 
  "orderDate": "2024-04-12T12:00:00", 
  "status": "PENDING", 
  "totalPrice": 500.00, 
  "orderItems": [ 
    {
      "productId": 3,
      "quantity": 2,
      "pricePerUnit": 100.00
    }, {
      "productId": 2,
      "quantity": 3,
      "pricePerUnit": 100.00
    }
  ]
}


