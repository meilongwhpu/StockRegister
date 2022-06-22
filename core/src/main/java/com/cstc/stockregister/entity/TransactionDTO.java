package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "交易信息")
public class TransactionDTO {
    @ApiModelProperty(value = "账户地址",example="0x7f6cb1d0f7f4e1b01c52db829ee01f6fbe80841b")
    private String account;
    @ApiModelProperty(value = "交易编码",example="1234")
    private String serialNum;
    @ApiModelProperty(value = "交易金额",example="1000")
    private String amount;
    @ApiModelProperty(value = "交易份额接触冻结时间",example="16522200")
    private String releaseTime;
}
