package com.cstc.stockregister.contracts.lib;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class StringUtil extends Contract {
    public static final String[] BINARY_ARRAY = {"605a602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a265627a7a72305820e7a8e56572b51d6f6c9b4d622b2cfa0fb1c298b5ff7dfdf2cb2b03cce9267baa6c6578706572696d656e74616cf50037"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"605a602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a265627a7a72305820744d34069a168c0a43d101969c5e6d78f6a0c4f28ed769e9ace45f354b8b46766c6578706572696d656e74616cf50037"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    protected StringUtil(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static StringUtil load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new StringUtil(contractAddress, client, credential);
    }

    public static StringUtil deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(StringUtil.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
