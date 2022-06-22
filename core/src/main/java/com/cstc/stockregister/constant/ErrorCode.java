package com.cstc.stockregister.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    CONTRACT_CALL_FAIL(-1, "call contract fail."),
    CONTRACT_CALL_ERROR(-2, "call contract back kown error."),
    LOAD_CONTRACT_ERROR(-3, "load contract error."),
    DO_WAITING_FROZEN_PARMA_ERROR(99, "The applied frozen amount exceeds the total frozen amount"),
    REQUEST_SUCCESS(100, "request success."),
    REQUEST_FAIL(101, "unknow error, request fail."),
    REQUEST_PARAM_ERROR(102, "request parameter have error."),
    COMPANY_CODE_EXIST(200, "company code already exist."),
    STOCK_CODE_EXIST(201, "stock code already exist."),
    ACCOUNT_EXIST(202, "external account already exist."),
    TRANSACTION_EXECUTE_ERROR(160001, "the transaction does not correctly executed."),
    UNKNOW_ERROR(160002, "unknow error, please check the error log."),
    JSON_ENCODE_EXCEPTION(20001, "encode Object to json failed"),
    JSON_DECODE_EXCEPTION(20002, "decode Object from json failed");

    /**
     * error code.
     */
    private int code;

    /**
     * error message.
     */
    private String errMsg;

    /**
     * Error Code Constructor.
     *
     * @param code The ErrorCode
     * @param errMsg The error message
     */
    ErrorCode(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    /**
     * get ErrorType By errcode.
     *
     * @param errorCode the ErrorCode
     * @return errorCode
     */
    public static ErrorCode getTypeByErrorCode(int errorCode) {
        for (ErrorCode type : ErrorCode.values()) {
            if (type.getCode() == errorCode) {
                return type;
            }
        }
        return ErrorCode.UNKNOW_ERROR;
    }

}
