package com.example.Query.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Query.model.Book;
import com.example.Query.service.BookService;
import com.example.Query.service.BookServiceImpl;
import com.google.gson.Gson;
import com.rabbitmq.client.*;

@Service
public class Utils {
	
	BookService service =  new BookServiceImpl();
	
	private Gson gson = new Gson();

	public void consumeData() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
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

	}

}
