package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "冻结执行信息")
public class DoFrozenDTO {
    @ApiModelProperty(value = "企业股权代码",example="600005")
    private String stockCode;
    @ApiModelProperty(value = "最新的已冻结总金额",example="1000")
    private int totalFreezeAmount;
    @ApiModelProperty(value = "最新的等待冻结总金额",example="0")
    private int totalWaitingFreezeAmount;
    @ApiModelProperty(value = "来自交易限售部分的冻结总金额",example="0")
    private int totalFrozenTransRestrictedSales;
    @ApiModelProperty(value = "来自流通部分的冻结总金额",example="0")
    private int totalFrozenCirculate;
    @ApiModelProperty(value = "来自限售部分的冻结总金额",example="1000")
    private int totalFrozenRestrictedSales;
    @ApiModelProperty(value = "可交易总额",example="3000")
    private int totalCanTransAmount;
    @ApiModelProperty(value = "资产所有者账户地址",example="0x1d0ddbbb996ef29675ba7716096ae25ffbcfc144")
    private String assetOwnerAddress;
    @ApiModelProperty(value = "冻结类型（1-司法冻结，2-手工冻结，3-轮候冻结）",example="1")
    private int businessType;
    @ApiModelProperty(value = "冻结申请人",example="渝中法院")
    private String applicant;
    @ApiModelProperty(value = "申请冻结金额",example="1000")
    private int thisRequestAmount;
    @ApiModelProperty(value = "执行冻结金额",example="1000")
    private int thisFrozenAmount;
    @ApiModelProperty(value = "轮候冻结金额",example="0")
    private int thisWaitingAmount;
    @ApiModelProperty(value = "冻结开始时间",example="20220616")
    private String startTime;
    @ApiModelProperty(value = "冻结结束时间",example="20990616")
    private String endTime;
    @ApiModelProperty(value = "轮候冻结编号",example="")
    private String waitingNumber;
    @ApiModelProperty(value = "冻结状态(1-全部冻结,2-部分冻结,3-全部轮候,4-已解冻)",example="1")
    private int status;
}
