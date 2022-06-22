package com.cstc.stockregister.contracts.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.DynamicBytes;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class BytesMapLib extends Contract {
    public static final String[] BINARY_ARRAY = {"605a602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a265627a7a723058207bdf0ab04c0184d1d4fb1229abfafc29bbf8181ef7eea07006b37a677d5a38626c6578706572696d656e74616cf50037"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"605a602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a265627a7a723058200068fd5295c628d019270f7293c08131ba3b5640b631524818c8614a51f6206f6c6578706572696d656e74616cf50037"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"txOrigin\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"msgSender\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"cmd\",\"type\":\"bytes\"},{\"indexed\":false,\"name\":\"key\",\"type\":\"bytes\"},{\"indexed\":false,\"name\":\"value\",\"type\":\"bytes\"}],\"name\":\"LogHistory\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final Event LOGHISTORY_EVENT = new Event("LogHistory", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    protected BytesMapLib(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<LogHistoryEventResponse> getLogHistoryEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGHISTORY_EVENT, transactionReceipt);
        ArrayList<LogHistoryEventResponse> responses = new ArrayList<LogHistoryEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogHistoryEventResponse typedResponse = new LogHistoryEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.txOrigin = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.msgSender = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.cmd = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.key = (byte[]) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.value = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeLogHistoryEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGHISTORY_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeLogHistoryEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(LOGHISTORY_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static BytesMapLib load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new BytesMapLib(contractAddress, client, credential);
    }

    public static BytesMapLib deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(BytesMapLib.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class LogHistoryEventResponse {
        public TransactionReceipt.Logs log;

        public String txOrigin;

        public String msgSender;

        public byte[] cmd;

        public byte[] key;

        public byte[] value;
    }
}
