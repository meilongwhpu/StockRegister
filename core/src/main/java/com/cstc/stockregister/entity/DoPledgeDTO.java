package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "质押执行信息")
public class DoPledgeDTO {
    @ApiModelProperty(value = "企业股权代码",example="600005")
    private String stockCode;
    @ApiModelProperty(value = "最新的质押总金额",example="1000")
    private int totalPledgeAmount;
    @ApiModelProperty(value = "来自交易限售部分的质押总金额",example="0")
    private int totalPledgeTransRestrictedSales;
    @ApiModelProperty(value = "来自流通部分的质押总金额",example="0")
    private int totalPledgeCirculate;
    @ApiModelProperty(value = "来自限售部分的质押总金额",example="1000")
    private int totalPledgeRestrictedSales;
    @ApiModelProperty(value = "可交易总金额",example="3000")
    private int totalCanTransAmount;
    @ApiModelProperty(value = "出质人账户地址",example="0x1d0ddbbb996ef29675ba7716096ae25ffbcfc144")
    private String pledgor;
    @ApiModelProperty(value = "质权人账户地址",example="0x7f6cb1d0f7f4e1b01c52db829ee01f6fbe80841b")
    private String pledgee;
    @ApiModelProperty(value = "质押金额",example="1000")
    private int thisRequestAmount;
    @ApiModelProperty(value = "来自流通部分的质押额",example="0")
    private int thisFromCirculate;
    @ApiModelProperty(value = "来自限售部分的质押额",example="1000")
    private int thisFromRestrictedSales;
    @ApiModelProperty(value = "质押到期日",example="2099-10-1")
    private String releaseTime;
    @ApiModelProperty(value = "质押事件描述",example="测试质押")
    private String causeDesc;
}
