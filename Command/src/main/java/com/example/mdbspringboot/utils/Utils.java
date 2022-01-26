package com.example.mdbspringboot.utils;

import org.springframework.stereotype.Service;

import com.example.mdbspringboot.model.Book;
import com.google.gson.Gson;
import com.rabbitmq.client.*;

@Service
public class Utils {
	private Gson gson = new Gson();

	public void sendData(Book obj) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (com.rabbitmq.client.Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.queueDeclare("save_book", false, false, false, null);
			String jsonmessage = gson.toJson(obj);
			channel.basicPublish("", "save_book", null, jsonmessage.getBytes());
			System.out.println(" [x] Sent '" + jsonmessage + "'");
		}
	}

	public void deleteData(int id) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (com.rabbitmq.client.Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			channel.queueDeclare("delete_book", false, false, false, null);
			String jsonmessage = id + "";
			channel.basicPublish("", "delete_book", null, jsonmessage.getBytes());
			System.out.println(" [x] Sent '" + jsonmessage + "'");
		}
	}

}
