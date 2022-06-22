package com.cstc.stockregister.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "企业信息")
public class OrganizationDTO {
    @ApiModelProperty(value = "企业发行股权合约地址（注册时为空）",example=" ")
    private String stockAsset;
    @ApiModelProperty(value = "企业名称",example="测试公司")
    private String orgName;
    @ApiModelProperty(value = "企业统一信用代码",example="91511111159875100U")
    private String orgCreditCode;
    @ApiModelProperty(value = "法人代表",example="张三")
    private String legalPerson;
    @ApiModelProperty(value = "法人证件编号",example="5012131452145221")
    private String legalIdNumber;
    @ApiModelProperty(value = "公司注册地",example="渝中区解放碑")
    private String registeredAddress;
    @ApiModelProperty(value = "公司注册日期",example="2011-06-13")
    private String registryDate;
    @ApiModelProperty(value = "公司办公地",example="渝中区解放碑")
    private String officeAddress;
    @ApiModelProperty(value = "公司联系电话",example="0231111111")
    private String contactNumber;
    @ApiModelProperty(value = "公司证件类型",example="1")
    private Integer credType;
    @ApiModelProperty(value = "公司类型（1—股份公司，2-有限公司）",example="1")
    private Integer orgType;
    @ApiModelProperty(value = "注册资本",example="1000000")
    private Integer registeredCapital;
    @ApiModelProperty(value = "实缴资本",example="1000000")
    private Integer paidInCapital;
    @ApiModelProperty(value = "股东人数",example="50")
    private Integer shareholdersNumber;
    @ApiModelProperty(value = "法人证件类型",example="1")
    private Integer legalIdType;

}
