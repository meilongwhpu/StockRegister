package com.cstc.stockregister.controller;


import com.cstc.stockregister.entity.Book;
import com.cstc.stockregister.response.ResponseData;
import com.cstc.stockregister.service.BookService;
import com.cstc.stockregister.service.TestDataService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/rest/testData")
@Slf4j
@ApiIgnore
public class TestDataController {

    @Autowired
    private TestDataService testDataService;

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

    @ApiOperation("存入键值对")
    @RequestMapping(value="addValue",method= RequestMethod.POST)
    public boolean addValue(@RequestParam(name = "key") @ApiParam(value = "key") String key,@RequestParam(name = "value") @ApiParam(value = "value") String value) {
        try {
          return  testDataService.addValue(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @ApiOperation("获取键值对")
    @RequestMapping(value="getValue/{key}",method=RequestMethod.GET)
    public String getValue(@PathVariable @ApiParam(value = "key") String key) {
        try {
            return  testDataService.getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation("触发测试")
    @RequestMapping(value="doTest",method=RequestMethod.GET)
    public String doTest() {
        try {
            System.out.println("TestView result: "+testDataService.TestView(33));
            List<String> key=new ArrayList<>();
/*            key.add("111122222222");
            key.add("2222测试1111");
            key.add("3333测试111");
            key.add("4444测试1111");
            key.add("5555测试1111");
            key.add("6666111111111");
            key.add("测试71111111111");
            testDataService.updateValueArrays(key);*/
           key.add("1111222");
            key.add("2222测试");
            key.add("3333测试2222");
            key.add("4444测试222");
            key.add("5555测试");
            key.add("666611112222");
            key.add("测试711112222");
            key.add("666611113333");
            key.add("测试711113333");
           testDataService.setValueArrays(key);
/*             testDataService.insertValueSet("set1111");
            testDataService.insertValueSet("set1112测试");
            testDataService.insertValueSet("set1113测试");
            testDataService.insertValueSet("set1114测试");
            testDataService.insertValueSet("set1115测试");
            testDataService.insertValueSet("set1116");
            testDataService.insertValueSet("set1117");
            testDataService.getAllValueSet();*/
            System.out.println("================================");
            testDataService.getValueArrays();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
