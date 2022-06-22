package com.cstc.stockregister.response;


import lombok.Data;

@Data
public class ResponseData<T> {
    /**
     * The generic type result object.
     */
    private T result;

    /**
     * The result code.
     */
    private Integer resultCode;

    /**
     * The error message.
     */
    private String errorMessage;

}
