package com.cstc.stockregister.service.impl;

import com.cstc.stockregister.configuration.BcosConfig;
import com.cstc.stockregister.configuration.ContractConfig;
import com.cstc.stockregister.contracts.lib.TestData;
import com.cstc.stockregister.service.TestDataService;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderInterface;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderService;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TestDataServiceImpl extends BaseService implements TestDataService {

    private TestData testData;
    private TransactionDecoderInterface decoder;

    public TestDataServiceImpl(BcosConfig bcosConfig, ContractConfig contractConfig){
        super(bcosConfig,contractConfig);
        try {
            this.initialize(contractConfig.getDeployContractAccount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        testData=this.getContract("0x7df3d6f9a87b14167d4910801ffcbb92a13f768a",TestData.class);
        decoder = new TransactionDecoderService(this.getClient().getCryptoSuite());
    }

    @Override
    public boolean addValue(String key, String value) throws Exception {
        TransactionReceipt receipt=testData.setValue(key.getBytes(),value.getBytes());
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        return testData.getSetValueOutput(receipt).getValue1().booleanValue();
    }
    public boolean TestView(int input)throws Exception{
        try{
            return testData.TestView(BigInteger.valueOf(input));
        }catch (ContractException e){
            e.getErrorCode();
            System.out.println(e.getErrorCode());
            System.out.println(e.getMessage());
        }
        return false;
    }
    @Override
    public String getValue(String key) throws Exception {
        TransactionReceipt receipt=testData.getValue(key.getBytes());
        Tuple2<byte[], BigInteger> result=testData.getGetValueOutput(receipt);
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        log.info("bytes长度："+result.getValue2().intValue());
        return  new String(result.getValue1());
    }

    @Override
    public boolean insertValueSet(String key) throws Exception {
        TransactionReceipt receipt=testData.setValueSet(key.getBytes());
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        return testData.getSetValueSetOutput(receipt).getValue1().booleanValue();
    }

    @Override
    public String getAllValueSet() throws Exception {
        TransactionReceipt receipt=testData.getAllValueSet();
        Tuple1<List<byte[]>> result=testData.getGetAllValueSetOutput(receipt);
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        log.info("bytes长度："+result.getValue1());
        for(int i=0;i<result.getValue1().size();i++){
            byte[] res= result.getValue1().get(i);
            System.out.println("getAllValueSet_第"+i+"个数值："+new String(res));
        }
        return null;
    }

    @Override
    public boolean setValueArrays(List<String> key) throws Exception {
        List<byte[]> keyvalue= new ArrayList<>();
        for(int i=0;i<key.size();i++){
            keyvalue.add(key.get(i).getBytes());
        }
        TransactionReceipt receipt=testData.setValueArrays(keyvalue);
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        return testData.getSetValueArraysOutput(receipt).getValue1().booleanValue();
    }

    @Override
    public boolean updateValueArrays(List<String> key) throws Exception {
        List<byte[]> keyvalue= new ArrayList<>();
        for(int i=0;i<key.size();i++){
            keyvalue.add(key.get(i).getBytes());
        }
        TransactionReceipt receipt=testData.updateAttrValues(keyvalue);
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        return testData.getUpdateAttrValuesOutput(receipt).getValue1().booleanValue();
    }

    @Override
    public String getValueArrays() throws Exception {
        List result=testData.getAttrValues();
        for(int i=0;i<result.size();i++){
            byte[] res= (byte[]) result.get(i);
            System.out.println("getValueArrays_第"+i+"个数值："+new String(res));
        }
        return null;
    }


}
