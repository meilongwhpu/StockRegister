package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel(value = "转让交易实体")
public class TransferDTO {

    @ApiModelProperty(value = "企业股权代码",example="600066")
    private String stockCode;

    @ApiModelProperty(value = "转出账户地址",example="0x7f6cb1d0f7f4e1b01c52db829ee01f6fbe80841b")
    private String externalFromAccount;

    @ApiModelProperty(value = "转入账户地址",example="0xf549d64217c57e59b832faeabb59577af12d875f")
    private String externaltoAccount;

    @ApiModelProperty(value = "转让金额",example="10000")
    private int amount;

    @ApiModelProperty(value = "转让类型（1-交易过户，2-非交易过户）",example="1")
    private int transferType;
}
