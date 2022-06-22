package com.cstc.stockregister.entity;


import lombok.Data;

@Data
public class ResponseContract {
    private Integer resultCode;
    private String resultMsg;
    private String transactionHash;

    public ResponseContract(int resultCode,String transactionHash){
        this.resultCode=resultCode;
        this.transactionHash=transactionHash;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
}
