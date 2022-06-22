package com.cstc.stockregister.exception;

public class ContractBaseException extends RuntimeException  {

    private Integer errorCode;
    private String errorMessage;

    /**
     * constructor.
     *
     * @param errorCode exception error code.
     * @param errorMessage exception error message.
     */
    public ContractBaseException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * constructor.
     *
     * @param errorCode exception error code.
     * @param errorMessage exception error message.
     * @param e the throwable.
     */
    public ContractBaseException(Integer errorCode, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
