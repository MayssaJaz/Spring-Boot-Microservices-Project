# The Book Boutique is a website that manages different Books!

We have one model which is `Book`
```
Book(int id, String title, String author, int quantity, String genre)
```

Users can search books by title, add a new book, edit or delete an existing one!

The application is developped with Microservice architecture along with CQRS. 



We have two Microservices: Query and Command.


We also have an API GAteway to redirect incoming requests from the frontend to the different microservices.
# Spring-Boot-Microservices-Project
