package com.cstc.stockregister.rpc.service.impl;

import com.cstc.stockregister.entity.Book;
import com.cstc.stockregister.rpc.service.BookRpcService;
import com.cstc.stockregister.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;


import lombok.extern.slf4j.Slf4j;

@AutoJsonRpcServiceImpl
@Service
@Slf4j
public class BookRpcServiceImpl implements BookRpcService {

	@Autowired
	private BookService bookService;
	
	@Override
	public Book findById(String id) {
		log.info("访问服务端JSON-RPC,参数:{}",id);
		return bookService.findById(id);
	}

}
