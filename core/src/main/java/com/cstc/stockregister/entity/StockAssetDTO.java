package com.cstc.stockregister.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "股权信息")
public class StockAssetDTO {
    @ApiModelProperty(value = "企业统一信用代码",example="91511111159875100U")
    private String orgCreditCode;
    @ApiModelProperty(value = "股权代码",example="600005")
    private String assetCode;
    @ApiModelProperty(value = "股权名称",example="测试公司")
    private String assetName;
    @ApiModelProperty(value = "总股本",example="1000000")
    private Integer totalBalances;
    @ApiModelProperty(value = "董监高限售比例",example="70")
    private Integer salesRatio;
    @ApiModelProperty(value = "交易限售天数",example="5")
    private Integer tradeRestrictDay;

}

