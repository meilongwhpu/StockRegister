package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FrozenAndReleaseRecordDTO {
    private String stockCode;
    private int businessType;
    private String assetOwnerAddress;
    private String serialNum;
    private String applicant;
    private String requestAmount;
    private String frozenAmount;
    private String waitingAmount;
    private String startTime;
    private String endTime;
    private String waitingNumber;
    private String status;
}
