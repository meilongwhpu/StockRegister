package com.cstc.stockregister.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TransferRecordDTO {
    private String stockCode;
    private int transferType;
    private String fromAccount;
    private String toAccount;
    private String serialNum;
    private String amount;
    private String occurTime;


}
