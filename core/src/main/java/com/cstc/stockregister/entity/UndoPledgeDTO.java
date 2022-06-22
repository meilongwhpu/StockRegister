package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "解押信息")
public class UndoPledgeDTO {
    @ApiModelProperty(value = "企业股权代码",example="600005")
    private String stockCode;
    @ApiModelProperty(value = "出质人账户地址",example="0x7f6cb1d0f7f4e1b01c52db829ee01f6fbe80841b")
    private String pledgor;
    @ApiModelProperty(value = "质押编号",example="123")
    private int pledgeNumber;
    @ApiModelProperty(value = "最新的质押总金额",example="1000")
    private int totalPledgeAmount;
    @ApiModelProperty(value = "最新的交易限售部分质押总金额",example="1000")
    private int totalPledgeTransRestrictedSales;
    @ApiModelProperty(value = "最新的流通部分质押总金额",example="1000")
    private int totalPledgeCirculate;
    @ApiModelProperty(value = "最新的限售部分质押总金额",example="1000")
    private int totalPledgeRestrictedSales;
    @ApiModelProperty(value = "最新的可交易部分质押总金额",example="1000")
    private int totalCanTransAmount;

}
