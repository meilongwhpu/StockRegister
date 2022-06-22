package com.cstc.stockregister.contractcode;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int8;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class AccountController extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405160208061114483398101806040528101908080519060200190929190505050806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506110c1806100836000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063343fce851461007257806344a2a2631461013b5780636e39f8db1461022a5780636e6fceb614610319578063a20bc1c4146103e2575b600080fd5b34801561007e57600080fd5b5061011f600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506104fd565b604051808260000b60000b815260200191505060405180910390f35b34801561014757600080fd5b506101a2600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506106a4565b604051808360000b60000b815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101ee5780820151818401526020810190506101d3565b50505050905090810190601f16801561021b5780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b34801561023657600080fd5b50610291600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610841565b604051808360000b60000b815260200180602001828103825283818151815260200191508051906020019080838360005b838110156102dd5780820151818401526020810190506102c2565b50505050905090810190601f16801561030a5780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b34801561032557600080fd5b506103c6600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506109de565b604051808260000b60000b815260200191505060405180910390f35b3480156103ee57600080fd5b506104e1600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803515159060200190929190505050610b85565b604051808260000b60000b815260200191505060405180910390f35b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c4305c3784846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835285818151815260200191508051906020019080838360005b838110156105ad578082015181840152602081019050610592565b50505050905090810190601f1680156105da5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156106135780820151818401526020810190506105f8565b50505050905090810190601f1680156106405780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b15801561066157600080fd5b505af1158015610675573d6000803e3d6000fd5b505050506040513d602081101561068b57600080fd5b8101908080519060200190929190505050905092915050565b600060606000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166344a2a263846040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b83811015610751578082015181840152602081019050610736565b50505050905090810190601f16801561077e5780820380516001836020036101000a031916815260200191505b5092505050600060405180830381600087803b15801561079d57600080fd5b505af11580156107b1573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060408110156107db57600080fd5b810190808051906020019092919080516401000000008111156107fd57600080fd5b8281019050602081018481111561081357600080fd5b815185600182028301116401000000008211171561083057600080fd5b505092919050505091509150915091565b600060606000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16636e39f8db846040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156108ee5780820151818401526020810190506108d3565b50505050905090810190601f16801561091b5780820380516001836020036101000a031916815260200191505b5092505050600060405180830381600087803b15801561093a57600080fd5b505af115801561094e573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250604081101561097857600080fd5b8101908080519060200190929190805164010000000081111561099a57600080fd5b828101905060208101848111156109b057600080fd5b81518560018202830111640100000000821117156109cd57600080fd5b505092919050505091509150915091565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16636e6fceb684846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835285818151815260200191508051906020019080838360005b83811015610a8e578082015181840152602081019050610a73565b50505050905090810190601f168015610abb5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610af4578082015181840152602081019050610ad9565b50505050905090810190601f168015610b215780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b158015610b4257600080fd5b505af1158015610b56573d6000803e3d6000fd5b505050506040513d6020811015610b6c57600080fd5b8101908080519060200190929190505050905092915050565b6000606060008315610ce057610b9b8787610e8a565b91506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631cc10e39836040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b83811015610c46578082015181840152602081019050610c2b565b50505050905090810190601f168015610c735780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b158015610c9257600080fd5b505af1158015610ca6573d6000803e3d6000fd5b505050506040513d6020811015610cbc57600080fd5b81019080805190602001909291905050509050801515610cdf57606e9250610e80565b5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c7f1752588876040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835285818151815260200191508051906020019080838360005b83811015610d8e578082015181840152602081019050610d73565b50505050905090810190601f168015610dbb5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610df4578082015181840152602081019050610dd9565b50505050905090810190601f168015610e215780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b158015610e4257600080fd5b505af1158015610e56573d6000803e3d6000fd5b505050506040513d6020811015610e6c57600080fd5b810190808051906020019092919050505092505b5050949350505050565b6060806002604051908082528060200260200182016040528015610ec257816020015b6060815260200190600190039081610ead5790505b50905083816000815181101515610ed557fe5b9060200190602002018190525082816001815181101515610ef257fe5b90602001906020020181905250610f0881610f11565b91505092915050565b6060600080606080600060606000809650600095505b8851861015610f5a578886815181101515610f3e57fe5b9060200190602002015151870196508580600101965050610f27565b866040519080825280601f01601f191660200182016040528015610f8d5781602001","602082028038833980820191505090505b50945084935060009250600095505b8851861015611086578886815181101515610fb357fe5b906020019060200201519150600090505b8151811015611079578181815181101515610fdb57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002848481518110151561103457fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535082806001019350508080600101915050610fc4565b8580600101965050610f9c565b839750505050505050509190505600a165627a7a7230582007bac1df9cbde9fb4f3d5da3af0678a5e1f1727d3c780bcea12e40d034722c400029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405160208061114483398101806040528101908080519060200190929190505050806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506110c1806100836000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630298dbaa146100725780634dcb1e781461013b5780639300e8141461022a578063bae99a9314610345578063f7d393651461040e575b600080fd5b34801561007e57600080fd5b5061011f600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506104fd565b604051808260000b60000b815260200191505060405180910390f35b34801561014757600080fd5b506101a2600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506106a4565b604051808360000b60000b815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101ee5780820151818401526020810190506101d3565b50505050905090810190601f16801561021b5780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b34801561023657600080fd5b50610329600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803515159060200190929190505050610841565b604051808260000b60000b815260200191505060405180910390f35b34801561035157600080fd5b506103f2600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610b46565b604051808260000b60000b815260200191505060405180910390f35b34801561041a57600080fd5b50610475600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610ced565b604051808360000b60000b815260200180602001828103825283818151815260200191508051906020019080838360005b838110156104c15780820151818401526020810190506104a6565b50505050905090810190601f1680156104ee5780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c8a3d81284846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835285818151815260200191508051906020019080838360005b838110156105ad578082015181840152602081019050610592565b50505050905090810190601f1680156105da5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156106135780820151818401526020810190506105f8565b50505050905090810190601f1680156106405780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b15801561066157600080fd5b505af1158015610675573d6000803e3d6000fd5b505050506040513d602081101561068b57600080fd5b8101908080519060200190929190505050905092915050565b600060606000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16634dcb1e78846040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b83811015610751578082015181840152602081019050610736565b50505050905090810190601f16801561077e5780820380516001836020036101000a031916815260200191505b5092505050600060405180830381600087803b15801561079d57600080fd5b505af11580156107b1573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f8201168201806040525060408110156107db57600080fd5b810190808051906020019092919080516401000000008111156107fd57600080fd5b8281019050602081018481111561081357600080fd5b815185600182028301116401000000008211171561083057600080fd5b505092919050505091509150915091565b600060606000831561099c576108578787610e8a565b91506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d3f3f8ee836040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156109025780820151818401526020810190506108e7565b50505050905090810190601f16801561092f5780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b15801561094e57600080fd5b505af1158015610962573d6000803e3d6000fd5b505050506040513d602081101561097857600080fd5b8101908080519060200190929190505050905080151561099b57606e9250610b3c565b5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c5fb421c88876040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835285818151815260200191508051906020019080838360005b83811015610a4a578082015181840152602081019050610a2f565b50505050905090810190601f168015610a775780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610ab0578082015181840152602081019050610a95565b50505050905090810190601f168015610add5780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b158015610afe57600080fd5b505af1158015610b12573d6000803e3d6000fd5b505050506040513d6020811015610b2857600080fd5b810190808051906020019092919050505092505b5050949350505050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663bae99a9384846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835285818151815260200191508051906020019080838360005b83811015610bf6578082015181840152602081019050610bdb565b50505050905090810190601f168015610c235780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610c5c578082015181840152602081019050610c41565b50505050905090810190601f168015610c895780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b158015610caa57600080fd5b505af1158015610cbe573d6000803e3d6000fd5b505050506040513d6020811015610cd457600080fd5b8101908080519060200190929190505050905092915050565b600060606000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663f7d39365846040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b83811015610d9a578082015181840152602081019050610d7f565b50505050905090810190601f168015610dc75780820380516001836020036101000a031916815260200191505b5092505050600060405180830381600087803b158015610de657600080fd5b505af1158015610dfa573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506040811015610e2457600080fd5b81019080805190602001909291908051640100000000811115610e4657600080fd5b82810190506020810184811115610e5c57600080fd5b8151856001820283011164010000000082111715610e7957600080fd5b505092919050505091509150915091565b6060806002604051908082528060200260200182016040528015610ec257816020015b6060815260200190600190039081610ead5790505b50905083816000815181101515610ed557fe5b9060200190602002018190525082816001815181101515610ef257fe5b90602001906020020181905250610f0881610f11565b91505092915050565b6060600080606080600060606000809650600095505b8851861015610f5a578886815181101515610f3e57fe5b9060200190602002015151870196508580600101965050610f27565b866040519080825280601f01601f191660200182016040528015610f8d5781602001","602082028038833980820191505090505b50945084935060009250600095505b8851861015611086578886815181101515610fb357fe5b906020019060200201519150600090505b8151811015611079578181815181101515610fdb57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002848481518110151561103457fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535082806001019350508080600101915050610fc4565b8580600101965050610f9c565b839750505050505050509190505600a165627a7a7230582051dfd9ef290eb7de9e578aa974409a2fb05f826aea639849674814bf864efb4a0029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"account_obj_id_v\",\"type\":\"string\"},{\"name\":\"_values\",\"type\":\"string\"}],\"name\":\"insertAccountInfoSnapshot\",\"outputs\":[{\"name\":\"\",\"type\":\"int8\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account_obj_id_v\",\"type\":\"string\"}],\"name\":\"getAccountInfoSnapshot\",\"outputs\":[{\"name\":\"\",\"type\":\"int8\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account_obj_id\",\"type\":\"string\"}],\"name\":\"getAccountInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"int8\"},{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account_obj_id\",\"type\":\"string\"},{\"name\":\"_values\",\"type\":\"string\"}],\"name\":\"insertAccountInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"int8\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account_obj_id\",\"type\":\"string\"},{\"name\":\"_oldVersion\",\"type\":\"string\"},{\"name\":\"_values\",\"type\":\"string\"},{\"name\":\"_isCheckSnapshot\",\"type\":\"bool\"}],\"name\":\"updateAccountInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"int8\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"accountStorageAddress\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_INSERTACCOUNTINFOSNAPSHOT = "insertAccountInfoSnapshot";

    public static final String FUNC_GETACCOUNTINFOSNAPSHOT = "getAccountInfoSnapshot";

    public static final String FUNC_GETACCOUNTINFO = "getAccountInfo";

    public static final String FUNC_INSERTACCOUNTINFO = "insertAccountInfo";

    public static final String FUNC_UPDATEACCOUNTINFO = "updateAccountInfo";

    protected AccountController(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt insertAccountInfoSnapshot(String account_obj_id_v, String _values) {
        final Function function = new Function(
                FUNC_INSERTACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id_v), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void insertAccountInfoSnapshot(String account_obj_id_v, String _values, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_INSERTACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id_v), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForInsertAccountInfoSnapshot(String account_obj_id_v, String _values) {
        final Function function = new Function(
                FUNC_INSERTACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id_v), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getInsertAccountInfoSnapshotInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INSERTACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple1<BigInteger> getInsertAccountInfoSnapshotOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_INSERTACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int8>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public TransactionReceipt getAccountInfoSnapshot(String account_obj_id_v) {
        final Function function = new Function(
                FUNC_GETACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id_v)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getAccountInfoSnapshot(String account_obj_id_v, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id_v)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetAccountInfoSnapshot(String account_obj_id_v) {
        final Function function = new Function(
                FUNC_GETACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id_v)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getGetAccountInfoSnapshotInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public Tuple2<BigInteger, String> getGetAccountInfoSnapshotOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETACCOUNTINFOSNAPSHOT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int8>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, String>(

                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public TransactionReceipt getAccountInfo(String account_obj_id) {
        final Function function = new Function(
                FUNC_GETACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getAccountInfo(String account_obj_id, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetAccountInfo(String account_obj_id) {
        final Function function = new Function(
                FUNC_GETACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getGetAccountInfoInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETACCOUNTINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public Tuple2<BigInteger, String> getGetAccountInfoOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETACCOUNTINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int8>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, String>(

                (BigInteger) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public TransactionReceipt insertAccountInfo(String account_obj_id, String _values) {
        final Function function = new Function(
                FUNC_INSERTACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void insertAccountInfo(String account_obj_id, String _values, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_INSERTACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForInsertAccountInfo(String account_obj_id, String _values) {
        final Function function = new Function(
                FUNC_INSERTACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getInsertAccountInfoInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INSERTACCOUNTINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public Tuple1<BigInteger> getInsertAccountInfoOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_INSERTACCOUNTINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int8>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public TransactionReceipt updateAccountInfo(String account_obj_id, String _oldVersion, String _values, Boolean _isCheckSnapshot) {
        final Function function = new Function(
                FUNC_UPDATEACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_oldVersion), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values), 
                new org.fisco.bcos.sdk.abi.datatypes.Bool(_isCheckSnapshot)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void updateAccountInfo(String account_obj_id, String _oldVersion, String _values, Boolean _isCheckSnapshot, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPDATEACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_oldVersion), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values), 
                new org.fisco.bcos.sdk.abi.datatypes.Bool(_isCheckSnapshot)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUpdateAccountInfo(String account_obj_id, String _oldVersion, String _values, Boolean _isCheckSnapshot) {
        final Function function = new Function(
                FUNC_UPDATEACCOUNTINFO, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(account_obj_id), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_oldVersion), 
                new org.fisco.bcos.sdk.abi.datatypes.Utf8String(_values), 
                new org.fisco.bcos.sdk.abi.datatypes.Bool(_isCheckSnapshot)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<String, String, String, Boolean> getUpdateAccountInfoInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPDATEACCOUNTINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<String, String, String, Boolean>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (Boolean) results.get(3).getValue()
                );
    }

    public Tuple1<BigInteger> getUpdateAccountInfoOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_UPDATEACCOUNTINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int8>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public static AccountController load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new AccountController(contractAddress, client, credential);
    }

    public static AccountController deploy(Client client, CryptoKeyPair credential, String accountStorageAddress) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Address(accountStorageAddress)));
        return deploy(AccountController.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }
}
