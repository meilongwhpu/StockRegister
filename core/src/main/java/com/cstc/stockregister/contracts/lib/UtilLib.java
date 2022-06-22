package com.cstc.stockregister.contracts.lib;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class UtilLib extends Contract {
    public static final String[] BINARY_ARRAY = {"605a602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a265627a7a7230582026f88333659f443e91af00ffe300f44f4a22be3ef4c8c9f3a4832e6bf103d8206c6578706572696d656e74616cf50037"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"605a602c600b82828239805160001a60731460008114601c57601e565bfe5b5030600052607381538281f30073000000000000000000000000000000000000000030146080604052600080fd00a265627a7a72305820b634879e6825237702bb3aa8403c9853cedd50ace7107d29f1095a35d2e77ed26c6578706572696d656e74616cf50037"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    protected UtilLib(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static UtilLib load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new UtilLib(contractAddress, client, credential);
    }

    public static UtilLib deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(UtilLib.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
