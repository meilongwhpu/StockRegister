package com.cstc.stockregister.contracts;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class Constant extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052348015600f57600080fd5b50604380601d6000396000f3006080604052600080fd00a265627a7a72305820d46bd6cccb240e2d2f9b08281ae705fdc602b1e969076989c0396012180762816c6578706572696d656e74616cf50037"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052348015600f57600080fd5b50604380601d6000396000f3006080604052600080fd00a265627a7a7230582038a44f96b0e5b1c75a7fb163a2343f4966a0000ce66bdcdaf4e73448695e96556c6578706572696d656e74616cf50037"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    protected Constant(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static Constant load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Constant(contractAddress, client, credential);
    }

    public static Constant deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(Constant.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
