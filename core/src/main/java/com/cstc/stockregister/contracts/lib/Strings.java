package com.cstc.stockregister.contracts.lib;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class Strings extends Contract {
    public static final String[] BINARY_ARRAY = {"604c602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a165627a7a72305820dbb20223997f5daf5fdb3fc70d96f7ef52f46222843b841f1dbd58b736d8143c0029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"604c602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a165627a7a72305820f79e82c81e28f3510407b7abfbcac53d0f096670474a725ba056a0aa2a4d30290029"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    protected Strings(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static Strings load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Strings(contractAddress, client, credential);
    }

    public static Strings deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(Strings.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
