package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "冻结信息")
public class FrozenDTO {
    @ApiModelProperty(value = "资产所有者账户地址",example="0x7f6cb1d0f7f4e1b01c52db829ee01f6fbe80841b")
    private String assetOwnerAddress;
    @ApiModelProperty(value = "冻结编号",example="123")
    private String serialNum;
    @ApiModelProperty(value = "冻结类型（1-司法冻结，2-手工冻结，3-轮候冻结）",example="1")
    private String businessType;
    @ApiModelProperty(value = "冻结申请人",example="渝中法院")
    private String applicant;
    @ApiModelProperty(value = "申请冻结金额",example="10000")
    private String requestAmount;
    @ApiModelProperty(value = "执行冻结金额",example="9000")
    private String frozenAmount;
    @ApiModelProperty(value = "轮候冻结金额",example="1000")
    private String waitingAmount;
    @ApiModelProperty(value = "冻结开始时间",example="1652000")
    private String startTime;
    @ApiModelProperty(value = "冻结结束时间",example="6210421")
    private String endTime;
    @ApiModelProperty(value = "轮候冻结编号",example="123")
    private String waitingNumber;
    @ApiModelProperty(value = "冻结状态(1-全部冻结,2-部分冻结,3-全部轮候,4-已解冻)",example="1")
    private String status;
}
