package com.cstc.stockregister.entity;

import java.io.IOException;

public enum ResponseCode {


    SUCCESS(1,"success"),
    ERROR_UNKOWN(201,"unknown error"),
    CALL_ERROR(101,"call contract error"),
    FAIL(102,"contract run fail"),
    FAIL_NULL_RETURN(103,"query the table of contract return null"),
    FAIL_ALREADY_EXIST(104,"record already exist in the table of contract"),
    FAIL_INSERT(105,"insert fail to the table of contract"),
    FAIL_NO_RECORD(108,"query no record from the table of contract"),
    FAIL_QUERY_ERROR(109,"query error from the table of contract"),
    NO_SNAPSHOT(110,"no snapshot for the table of contract");

    public Integer code;

    public String msg;

    ResponseCode(int code,  String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResponseCode getResponseCode(int code) throws IOException {
        if (code==SUCCESS.code) return SUCCESS;
        if (code==CALL_ERROR.code) return CALL_ERROR;
        if (code==FAIL.code) return FAIL;
        if (code==FAIL_NULL_RETURN.code) return FAIL_NULL_RETURN;
        if (code==FAIL_ALREADY_EXIST.code) return FAIL_ALREADY_EXIST;
        if (code==FAIL_INSERT.code) return FAIL_INSERT;
        if (code==FAIL_NO_RECORD.code) return FAIL_NO_RECORD;
        if (code==FAIL_QUERY_ERROR.code) return FAIL_QUERY_ERROR;
        if (code==NO_SNAPSHOT.code) return NO_SNAPSHOT;
        return ERROR_UNKOWN;
    }


}
