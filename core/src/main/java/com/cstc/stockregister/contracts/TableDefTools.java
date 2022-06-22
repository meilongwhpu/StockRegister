package com.cstc.stockregister.contracts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class TableDefTools extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052348015600f57600080fd5b50604380601d6000396000f3006080604052600080fd00a265627a7a72305820a4a2ccbc52e1eb8edcd2670ef207e5ff4a24abe3edc8244f5fcdc7fedb77ecd56c6578706572696d656e74616cf50037"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052348015600f57600080fd5b50604380601d6000396000f3006080604052600080fd00a265627a7a7230582054fa4f4b8b6f8e37b16d6c4d535e85a085792e9448c0ece05daf10834165031b6c6578706572696d656e74616cf50037"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tableName\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"primaryKey\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"fields\",\"type\":\"string\"}],\"name\":\"InsertRecord\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tableName\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"primaryKey\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"fields\",\"type\":\"string\"}],\"name\":\"UpdateRecord\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tableName\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"primaryKey\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"fields\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"msg\",\"type\":\"string\"}],\"name\":\"UpdateRecordError\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"msg\",\"type\":\"string\"}],\"name\":\"Debug\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final Event INSERTRECORD_EVENT = new Event("InsertRecord", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event UPDATERECORD_EVENT = new Event("UpdateRecord", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event UPDATERECORDERROR_EVENT = new Event("UpdateRecordError", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event DEBUG_EVENT = new Event("Debug", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    protected TableDefTools(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<InsertRecordEventResponse> getInsertRecordEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(INSERTRECORD_EVENT, transactionReceipt);
        ArrayList<InsertRecordEventResponse> responses = new ArrayList<InsertRecordEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            InsertRecordEventResponse typedResponse = new InsertRecordEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tableName = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.primaryKey = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.fields = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeInsertRecordEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(INSERTRECORD_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeInsertRecordEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(INSERTRECORD_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<UpdateRecordEventResponse> getUpdateRecordEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPDATERECORD_EVENT, transactionReceipt);
        ArrayList<UpdateRecordEventResponse> responses = new ArrayList<UpdateRecordEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UpdateRecordEventResponse typedResponse = new UpdateRecordEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tableName = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.primaryKey = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.fields = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeUpdateRecordEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(UPDATERECORD_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeUpdateRecordEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(UPDATERECORD_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<UpdateRecordErrorEventResponse> getUpdateRecordErrorEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPDATERECORDERROR_EVENT, transactionReceipt);
        ArrayList<UpdateRecordErrorEventResponse> responses = new ArrayList<UpdateRecordErrorEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UpdateRecordErrorEventResponse typedResponse = new UpdateRecordErrorEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tableName = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.primaryKey = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.fields = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.msg = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeUpdateRecordErrorEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(UPDATERECORDERROR_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeUpdateRecordErrorEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(UPDATERECORDERROR_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<DebugEventResponse> getDebugEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DEBUG_EVENT, transactionReceipt);
        ArrayList<DebugEventResponse> responses = new ArrayList<DebugEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DebugEventResponse typedResponse = new DebugEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.msg = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeDebugEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(DEBUG_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeDebugEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(DEBUG_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static TableDefTools load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new TableDefTools(contractAddress, client, credential);
    }

    public static TableDefTools deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(TableDefTools.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class InsertRecordEventResponse {
        public TransactionReceipt.Logs log;

        public String tableName;

        public String primaryKey;

        public String fields;
    }

    public static class UpdateRecordEventResponse {
        public TransactionReceipt.Logs log;

        public String tableName;

        public String primaryKey;

        public String fields;
    }

    public static class UpdateRecordErrorEventResponse {
        public TransactionReceipt.Logs log;

        public String tableName;

        public String primaryKey;

        public String fields;

        public String msg;
    }

    public static class DebugEventResponse {
        public TransactionReceipt.Logs log;

        public String msg;
    }
}
