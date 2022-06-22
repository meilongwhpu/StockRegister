package com.cstc.stockregister.util;

public class IntegerToByte {

    public static byte[] intToByteArray(int i){
        return new byte[]{
                (byte)i,
                (byte)(i >>> 8),
                (byte)(i >>> 16),
                (byte)(i >>> 24)};
    }

    //循环累加、或运算都可以
    public static int byteArrayToInt(byte[] bytes){
        return (bytes[0] & 0xff)
                |((bytes[1] & 0xff) << 8)
                |((bytes[2] & 0xff) << 16)
                |((bytes[3] & 0xff) << 24);
    }
}
