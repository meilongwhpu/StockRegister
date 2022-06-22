package com.cstc.stockregister.rpc.service;

import com.cstc.stockregister.entity.Book;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("rpc/books")
public interface BookRpcService {
	public Book findById(@JsonRpcParam(value = "id")String id);
}
