package com.cstc.stockregister.controller;


import com.cstc.stockregister.entity.Book;
import com.cstc.stockregister.response.ResponseData;
import com.cstc.stockregister.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/rest/testData")
@Slf4j
@ApiIgnore
public class TestDataController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value="get/{id}",method=RequestMethod.GET)
    public ResponseData<Book> getBookById(@PathVariable String id){
        ResponseData<Book> responseData=new ResponseData<>();
        log.info("访问服务端,参数:{}",id);
        Book book= bookService.findById(id);
        responseData.setResult(book);
        responseData.setResultCode(0);
        return responseData;
    }

}
