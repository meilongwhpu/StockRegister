package com.cstc.stockregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerApp {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.cstc.stockregister.controller"))
                .paths(PathSelectors.any())
                .build();
//        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
    }
    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("基于区块链的股权登记系统")
                //创建人
                .contact(new Contact("枚龙", "https://github.com/meilongwhpu/StockRegister.git", "meilong@chnc-stc.com"))
                //版本号
                .version("1.0")
                //描述
                .description("该系统是基于FISCO BCOS区块链底层平台研发的股权登记系统，提供了合约治理、账户管理、企业管理、股权管理四大类功能。" +
                        "合约治理主要功能包括创建企业信息合约、创建企业股权合约、创建账户、合约的状态及归属者管理等；账户管理主要功能包括内外部账户映射管理、" +
                        "添加及查询账户持股合约等；企业管理主要功能包括企业信息管理、董监高信息管理、查询企业信息变更历史记录等；股权管理主要包括股份发行、" +
                        "股份增资及、股份减资、股权登记、冻结登记、质押登记、交易登记、历史记录以及各类数据的查询等。")
                .build();
    }
}
