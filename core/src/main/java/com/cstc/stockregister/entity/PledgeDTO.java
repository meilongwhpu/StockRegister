package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "质押信息")
public class PledgeDTO {
    @ApiModelProperty(value = "出质人账户地址",example="0x7f6cb1d0f7f4e1b01c52db829ee01f6fbe80841b")
    private String pledgor;
    @ApiModelProperty(value = "质押编号",example="123")
    private String serialNum;
    @ApiModelProperty(value = "质权人账户地址",example="0x1d0ddbbb996ef29675ba7716096ae25ffbcfc144")
    private String pledgee;
    @ApiModelProperty(value = "质押金额",example="10000")
    private String amount;
    @ApiModelProperty(value = "来自流通部分的质押金额",example="5000")
    private String fromCirculate;
    @ApiModelProperty(value = "来自限售部分的质押金额",example="5000")
    private String fromRestrictedSales;
    @ApiModelProperty(value = "质押到期时间",example="1562120")
    private String releaseTime;

}
