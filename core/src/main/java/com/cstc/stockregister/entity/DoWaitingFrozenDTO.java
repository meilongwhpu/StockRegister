package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "轮候冻结执行信息")
public class DoWaitingFrozenDTO {
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
    @ApiModelProperty(value = "轮候冻结业务的编号",example="")
    private int serialNum;
    @ApiModelProperty(value = "资产所有者账户地址",example="0x1d0ddbbb996ef29675ba7716096ae25ffbcfc144")
    private String assetOwnerAddress;
    @ApiModelProperty(value = "本次申请执行的等待冻结金额（轮候冻结转冻结）",example="1000")
    private int thisRequestAmount;

}
