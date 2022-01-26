package com.example.mdbspringboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mdbspringboot.model.Book;


public interface BookRepository extends MongoRepository<Book, Integer> {

}
