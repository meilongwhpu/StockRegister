package com.cstc.stockregister.exception;

import com.cstc.stockregister.constant.ErrorCode;

public class StockRegisterBaseException extends Exception {

    /**
     * Error code.
     */
    protected int code;

    /**
     * Error message.
     */
    protected String message;

    /**
     * Construction.
     *
     * @param message the message
     * @param cause the cause
     */
    public StockRegisterBaseException(String message, Throwable cause) {
        super(getText(-1, message), cause);
        this.code = -1;
        this.message = message;
    }
    /**
     * constructor.
     *
     * @param errorCode exception error code.
     * @param errorMessage exception error message.
     * @param e the throwable.
     */
    public StockRegisterBaseException(Integer errorCode, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.code = errorCode;
        this.message = errorMessage;
    }

    /**
     * Construction.
     *
     * @param message the message
     */
    public StockRegisterBaseException(String message) {
        super(getText(-1, message));
        this.code = -1;
        this.message = message;
    }

    /**
     * Construction.
     *
     * @param errorCode the code and message
     */
    public StockRegisterBaseException(ErrorCode errorCode) {
        super(getText(errorCode.getCode(), errorCode.getErrMsg()));
        this.code = errorCode.getCode();
        this.message = errorCode.getErrMsg();
    }

    /**
     * Construction.
     *
     * @param code the code
     * @param message reason
     */
    public StockRegisterBaseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * getText
     *
     * @param code the code
     * @param message the message
     * @return java.lang.String
     */
    private static String getText(int code, String message) {
        return "Code: " +
                code +
                ", Message: " +
                message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }
}
