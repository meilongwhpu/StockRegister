package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "董监高集合")
public class ManagerAddressDTO {
    @ApiModelProperty(value = "企业统一信用代码",example="91511111159875100U")
    private String orgCreditCode;
    @ApiModelProperty(value = "董监高账户列表",example="[0x7f6cb1d0f7f4e1b01c52db829ee01f6fbe80841b,0x7f6cb1d0f7f4e1b01c52db829ee01f6fbe808432]")
    private List<String> managers;

}
