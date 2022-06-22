package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
@ApiModel(value = "用户的账本信息")
public class AccountBookDTO {
    @ApiModelProperty(value = "总持仓",example="10000")
    private BigInteger total;
    @ApiModelProperty(value = "流通股数量",example="10000")
    private BigInteger circulate;
    @ApiModelProperty(value = "限售股数量",example="10000")
    private BigInteger restrictedSales;
    @ApiModelProperty(value = "可交易额",example="10000")
    private BigInteger canTrans;
    @ApiModelProperty(value = "T+N交易限售额",example="10000")
    private BigInteger transRestrictedSales;
    @ApiModelProperty(value = "质押总额",example="10000")
    private BigInteger pledgeAmount;
    @ApiModelProperty(value = "冻结总额",example="10000")
    private BigInteger freezeAmount;
    @ApiModelProperty(value = "轮候冻结额",example="10000")
    private BigInteger waitingFreeze;
    @ApiModelProperty(value = "来自交易限售部分的冻结额",example="10000")
    private BigInteger frozenTransRestrictedSales;
    @ApiModelProperty(value = "来自流通部分的冻结额",example="10000")
    private BigInteger frozenCirculate;
    @ApiModelProperty(value = "来自董监高限售部分的冻结额",example="10000")
    private BigInteger frozenRestrictedSales;
    @ApiModelProperty(value = "来自交易限售部分的质押额",example="10000")
    private BigInteger pledgeTransRestrictedSales;
    @ApiModelProperty(value = "来自流通部分的质押额",example="10000")
    private BigInteger pledgeCirculate;
    @ApiModelProperty(value = "来自董监高限售部分的质押额",example="10000")
    private BigInteger pledgeRestrictedSales;

}
