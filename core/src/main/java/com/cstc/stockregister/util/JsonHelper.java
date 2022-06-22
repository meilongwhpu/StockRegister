package com.cstc.stockregister.util;

import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.exception.StockRegisterBaseException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@Slf4j
public class JsonHelper {

    private static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();

        // Include.NON_NULL Property is NULL and not serialized
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // DO NOT convert inconsistent fields
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * convert object to String
     *
     * @param object java object
     * @return json data
     * @throws StockRegisterBaseException OpenLedgerBaseException
     */
    public static String object2Json(Object object) throws StockRegisterBaseException {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("json encode failed", e);
            throw new StockRegisterBaseException(ErrorCode.JSON_ENCODE_EXCEPTION);
        }
    }

    /**
     * convert object to byte[]
     *
     * @param object java object
     * @return json data
     * @throws StockRegisterBaseException StockRegisterBaseException
     */
    public static byte[] object2JsonBytes(Object object) throws StockRegisterBaseException {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.error("json encode failed", e);
            throw new StockRegisterBaseException(ErrorCode.JSON_ENCODE_EXCEPTION);
        }
    }

    /**
     * convert jsonString to object
     *
     * @param jsonString json String
     * @param valueType java object type
     * @param <T> template type
     * @return Object java object
     * @throws Exception
     */
    public static <T> T json2Object(String jsonString, Class<T> valueType) throws StockRegisterBaseException {
        try {
            return OBJECT_MAPPER.readValue(jsonString, valueType);
        } catch (IOException e) {
            log.error("json decode String failed, valueType:{}", e);
            throw new StockRegisterBaseException(ErrorCode.JSON_DECODE_EXCEPTION);
        }
    }

    /**
     * convert json String to Object
     *
     * @param jsonString json String
     * @param typeReference typeReference
     * @param <T> template type
     * @return class instance
     * @throws Exception
     */
    public static <T> T json2Object(String jsonString, TypeReference<T> typeReference) throws StockRegisterBaseException {
        try {
            return OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (IOException e) {
            log.error("json decode String failed, typeReference:{}", e);
            throw new StockRegisterBaseException(ErrorCode.JSON_DECODE_EXCEPTION);
        }
    }

    /**
     * convert json byte[] to Object
     *
     * @param json json data
     * @param valueType java object type
     * @param <T> template type
     * @return Object java object
     * @throws StockRegisterBaseException StockRegisterBaseException
     */
    public static <T> T json2Object(byte[] json, Class<T> valueType) throws StockRegisterBaseException {
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            log.error("json decode byte[] failed, valueType : {}", e);
            throw new StockRegisterBaseException(ErrorCode.JSON_DECODE_EXCEPTION);
        }
    }

    /**
     * convert json byte[] to Object
     *
     * @param json json data
     * @param typeReference typeReference
     * @param <T> template type
     * @return class instance
     * @throws StockRegisterBaseException StockRegisterBaseException
     */
    public static <T> T json2Object(byte[] json, TypeReference<T> typeReference) throws StockRegisterBaseException {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("json decode byte[] failed, typeReference:", e);
            throw new StockRegisterBaseException(ErrorCode.JSON_DECODE_EXCEPTION);
        }
    }

    public static boolean isValid(String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return false;
        }
        try {
            OBJECT_MAPPER.readTree(jsonString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
