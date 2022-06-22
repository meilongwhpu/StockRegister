package com.cstc.stockregister.service;


import com.cstc.stockregister.entity.Book;

public interface BookService {
	public Book findById(String id);
}