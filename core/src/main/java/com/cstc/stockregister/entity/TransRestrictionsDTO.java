package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
@ApiModel(value = "交易限售集")
public class TransRestrictionsDTO {
    @ApiModelProperty(value = "企业股权代码",example="600005")
    private String stockCode;
    @ApiModelProperty(value = "账户地址",example="0x1d0ddbbb996ef29675ba7716096ae25ffbcfc144")
    private String externalAccount;
    @ApiModelProperty(value = "交易编码列表",example="[123,125]")
    private List<BigInteger> transNumber;
}
