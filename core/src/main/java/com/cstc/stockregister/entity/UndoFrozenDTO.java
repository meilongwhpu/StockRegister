package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "解冻信息")
public class UndoFrozenDTO {
    @ApiModelProperty(value = "企业股权代码",example="600005")
    private String stockCode;
    @ApiModelProperty(value = "资产所有者账户地址",example="0x1d0ddbbb996ef29675ba7716096ae25ffbcfc144")
    private String assetOwnerAddress;
    @ApiModelProperty(value = "最新总的冻结金额",example="1000")
    private int totalFreezeAmount;
    @ApiModelProperty(value = "最新总的待冻结金额",example="0")
    private int totalWaitingFreezeAmount;
    @ApiModelProperty(value = "最新的交易限售部分总的冻结金额",example="0")
    private int totalFrozenTransRestrictedSales;
    @ApiModelProperty(value = "最新的流通部分总的冻结金额",example="0")
    private int totalFrozenCirculate;
    @ApiModelProperty(value = "最新的董监高限售部分总的冻结金额",example="1000")
    private int totalFrozenRestrictedSales;
    @ApiModelProperty(value = "最新的可交易部分总的冻结金额",example="3000")
    private int totalCanTransAmount;
    @ApiModelProperty(value = "冻结编号",example="123")
    private int frozenNumber;
}

