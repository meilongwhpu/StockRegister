package com.cstc.stockregister.util;

import com.cstc.stockregister.entity.ManagerAddressDTO;
import com.cstc.stockregister.entity.TransactionDTO;
import com.cstc.stockregister.exception.StockRegisterBaseException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        System.out.println(property);
/*
        String test="11111111111111111111111111111111";
        byte[] bys=test.getBytes();
        System.out.println(bys.length);

       ManagerAddressDTO dto=new ManagerAddressDTO();
        dto.setOrgCreditCode("123456");
        List<String> manager=new ArrayList<>();
        manager.add("111111");
        dto.setManagers(manager);
        try {
            String result=JsonHelper.object2Json(dto);
            System.out.println(result);
        } catch (StockRegisterBaseException e) {
            e.printStackTrace();
        }
        List<TransactionDTO> list=new ArrayList<>();
        TransactionDTO transactionDTO1=new TransactionDTO();
        transactionDTO1.setAccount("123");
        transactionDTO1.setAmount("100000");
        transactionDTO1.setReleaseTime("12353124");
        transactionDTO1.setSerialNum("1111");

        TransactionDTO transactionDTO2=new TransactionDTO();
        transactionDTO2.setAccount("432");
        transactionDTO2.setAmount("200000");
        transactionDTO2.setReleaseTime("432123");
        transactionDTO2.setSerialNum("2222");
        list.add(transactionDTO1);
        list.add(transactionDTO2);
        try {
            String result=JsonHelper.object2Json(list);
            System.out.println(result);
        } catch (StockRegisterBaseException e) {
            e.printStackTrace();
        }
        String testStr="[{\"account\":\"123\",\"amount\":\"100000\",\"serialNum\":\"1111\",\"releaseTime\":\"12353124\"},{\"account\":\"432\",\"amount\":\"200000\",\"serialNum\":\"2222\",\"releaseTime\":\"432123\"}]";
        try {
            List<TransactionDTO> list2=JsonHelper.json2Object(testStr,new TypeReference<List<TransactionDTO>>(){});
            System.out.println(list2.size());
        } catch (StockRegisterBaseException e) {
            e.printStackTrace();
        }*/
        System.out.println(System.currentTimeMillis());
        byte[] bytes2="1".getBytes();
        System.out.println("输入"+1+"转换byte后得到："+Integer.parseInt(new String(bytes2)));
        System.out.println(System.currentTimeMillis());
        byte[] bytes1=Test.intToByteArrayBest(1);
        System.out.println("输入"+1+"转换byte后得到："+Test.byteArrayToIntBest(bytes1));
        System.out.println(System.currentTimeMillis());
        int input=-100;
        while (input<5){
            byte[] bytes=Test.intToByteArrayBest(input);
            System.out.println("输入"+input+"转换byte后得到："+Test.byteArrayToIntBest(bytes));
            input+=100;
        }
    }

    public static byte[] intToByteArrayBest(int i){
        return new byte[]{
                (byte)i,
                (byte)(i >>> 8),
                (byte)(i >>> 16),
                (byte)(i >>> 24)};
    }

    //循环累加、或运算都可以
    public static int byteArrayToIntBest(byte[] bytes){
        return (bytes[0] & 0xff)
                |((bytes[1] & 0xff) << 8)
                |((bytes[2] & 0xff) << 16)
                |((bytes[3] & 0xff) << 24);
    }
}
