package com.cstc.stockregister.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StockBusinessRecordDTO {
    private String account;
    private List<TransferRecordDTO> transferRecords=new ArrayList<>();
    private List<PledgeAndReleaseRecordDTO> pledgeAndReleaseRecords=new ArrayList<>();
    private List<FrozenAndReleaseRecordDTO> frozenAndReleaseRecords=new ArrayList<>();

}
