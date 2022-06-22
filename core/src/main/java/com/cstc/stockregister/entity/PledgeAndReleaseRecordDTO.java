package com.cstc.stockregister.entity;

import lombok.Data;

@Data
public class PledgeAndReleaseRecordDTO {
    private String stockCode;
    private int businessType;
    private String pledgor;
    private String pledgee;
    private String serialNum;
    private String amount;
    private String fromCirculate;
    private String fromRestrictedSales;
    private String releaseTime;
    private String occurTime;
    private String causeDesc;
}
