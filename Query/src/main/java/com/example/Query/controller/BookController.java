package com.example.Query.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Query.model.Book;
import com.example.Query.service.BookService;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@RestController()
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	BookService service;
	
	public BookController() {
		Gson gson = new Gson();
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try {
		com.rabbitmq.client.Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare("save_book", false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println("message   " + message);
			Book book = gson.fromJson(message, Book.class);
			System.out.println(book);
			System.out.println(service);
			Book savedbook = service.save(book);
			System.out.println(savedbook);
		};

		channel.basicConsume("save_book", true, deliverCallback, consumerTag -> {
		});

		Channel channel2 = connection.createChannel();

		channel2.queueDeclare("delete_book", false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
			String id = new String(delivery.getBody(), "UTF-8");
			System.out.println("message   " + id);
			Book book = service.findOne(id);
			service.delete(book);
			System.out.println("deleted");

		};
		channel2.basicConsume("delete_book", true, deliverCallback2, consumerTag -> {
		});
		} catch(Exception e) {
			
		}

	}
	
	@GetMapping("/")
    public Iterable<Book> getBooks(){
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public Book getBooksById(@PathVariable String id){
        return this.service.findOne(id);
    }
    
    @GetMapping("/search")
    public Iterable<Book> searchBooksByTitle(@RequestParam("title") String title)
    {
        return this.service.findByTitle(title);
    }
    

	@PostMapping("/create")
	Book create(@RequestBody Book book) {
		Book savedBook = service.save(book);
		return savedBook;

	}

	@PutMapping("/update/{id}")
	Book update(@RequestBody Book newbook, @PathVariable String id) {
		Book book = service.findOne(id);
		book.setTitle(newbook.getTitle());
		book.setAuthor(newbook.getAuthor());
		book.setQuantity(newbook.getQuantity());
		book.setGenre(newbook.getGenre());
		return service.save(book);

	}

	@DeleteMapping("/delete/{id}")
	ResponseEntity<Void> delete(@PathVariable String id) {
		Book book = service.findOne(id);
		service.delete(book);
		return ResponseEntity.noContent().build();
	}
}