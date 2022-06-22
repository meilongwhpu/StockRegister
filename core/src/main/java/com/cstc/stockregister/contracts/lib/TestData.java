package com.cstc.stockregister.contracts.lib;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.DynamicBytes;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class TestData extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5061126e806100206000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806311aecc0c146100935780631adc1c75146100be5780634d1ad868146100fb5780635e1f95aa1461013857806397fc2da514610163578063b6576bdf146101a0578063c2cae6c0146101de578063db0577a81461021b575b600080fd5b34801561009f57600080fd5b506100a8610258565b6040516100b5919061102c565b60405180910390f35b3480156100ca57600080fd5b506100e560048036036100e09190810190610daf565b610341565b6040516100f2919061104e565b60405180910390f35b34801561010757600080fd5b50610122600480360361011d9190810190610e9d565b61044a565b60405161012f919061104e565b60405180910390f35b34801561014457600080fd5b5061014d61049a565b60405161015a919061102c565b60405180910390f35b34801561016f57600080fd5b5061018a60048036036101859190810190610df0565b6104ab565b604051610197919061104e565b60405180910390f35b3480156101ac57600080fd5b506101c760048036036101c29190810190610df0565b6104c8565b6040516101d5929190611069565b60405180910390f35b3480156101ea57600080fd5b5061020560048036036102009190810190610daf565b6104fe565b604051610212919061104e565b60405180910390f35b34801561022757600080fd5b50610242600480360361023d9190810190610e31565b61056b565b60405161024f919061104e565b60405180910390f35b60606005805480602002602001604051908101604052809291908181526020016000905b82821015610338578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103245780601f106102f957610100808354040283529160200191610324565b820191906000526020600020905b81548152906001019060200180831161030757829003601f168201915b50505050508152602001906001019061027c565b50505050905090565b60008060006005805490508451111561035f57600580549050610362565b83515b9150600090505b818110156103c357838181518110151561037f57fe5b9060200190602002015160058281548110151561039857fe5b9060005260206000200190805190602001906103b5929190610b17565b508080600101915050610369565b6005805490508451111561043f575b835181101561043e57600584828151811015156103eb57fe5b90602001906020020151908060018154018082558091505090600182039060005260206000200160009091929091909150908051906020019061042f929190610b17565b505080806001019150506103d2565b5b600192505050919050565b6000816001141515610491576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161048890611099565b60405180910390fd5b60019050919050565b60606104a6600361058b565b905090565b60006104c182600361067890919063ffffffff16565b9050919050565b606060006104e083600061079490919063ffffffff16565b6104f484600061079490919063ffffffff16565b5191509150915091565b600080600090505b825181101561056157828181518110151561051d57fe5b9060200190602002015160058281548110151561053657fe5b906000526020600020019080519060200190610553929190610b17565b508080600101915050610506565b6001915050919050565b6000610583838360006108a79092919063ffffffff16565b905092915050565b606081600101805480602002602001604051908101604052809291908181526020016000905b8282101561066d578382906000526020600020018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106595780601f1061062e57610100808354040283529160200191610659565b820191906000526020600020905b81548152906001019060200180831161063c57829003601f168201915b5050505050815260200190600101906105b1565b505050509050919050565b6000808251141515156106c0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106b7906110b9565b60405180910390fd5b6106ca8383610a9c565b156106d8576001905061078e565b82600101829080600181540180825580915050906001820390600052602060002001600090919290919091509080519060200190610717929190610b17565b5083600001836040518082805190602001908083835b602083101515610752578051825260208201915060208101905060208303925061072d565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902081905550600090505b92915050565b606082600001826040518082805190602001908083835b6020831015156107d057805182526020820191506020810190506020830392506107ab565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206001018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561089a5780601f1061086f5761010080835404028352916020019161089a565b820191906000526020600020905b81548152906001019060200180831161087d57829003601f168201915b5050505050905092915050565b60008084600001846040518082805190602001908083835b6020831015156108e457805182526020820191506020810190506020830392506108bf565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600001549050600081111561092f5760009150610a94565b8285600001856040518082805190602001908083835b60208310151561096a5780518252602082019150602081019050602083039250610945565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060010190805190602001906109b3929190610b17565b508460010180548091906001016109ca9190610b97565b90506001810185600001856040518082805190602001908083835b602083101515610a0a57805182526020820191506020810190506020830392506109e5565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060000181905550838560010182815481101515610a5657fe5b90600052602060002090600202016000019080519060200190610a7a929190610b17565b508460020160008154809291906001019190505550600191505b509392505050565b60008083600001836040518082805190602001908083835b602083101515610ad95780518252602082019150602081019050602083039250610ab4565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020541415905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610b5857805160ff1916838001178555610b86565b82800160010185558215610b86579182015b82811115610b85578251825591602001919060010190610b6a565b5b509050610b939190610bc9565b5090565b815481835581811115610bc457600202816002028360005260206000209182019101610bc39190610bee565b5b505050565b610beb91905b80821115610be7576000816000905550600101610bcf565b5090565b90565b610c2e91905b80821115610c2a5760008082016000610c0d9190610c31565b6001820160006101000a81549060ff021916905550600201610bf4565b5090565b90565b50805460018160011615610100020316600290046000825580601f10610c575750610c76565b601f016020900490600052602060002090810190610c759190610bc9565b5b50565b600082601f8301121515610c8c57600080fd5b8135610c9f610c9a82611106565b6110d9565b9150818183526020840193506020810190508360005b83811015610ce55781358601610ccb8882610cef565b845260208401935060208301925050600181019050610cb5565b5050505092915050565b600082601f8301121515610d0257600080fd5b8135610d15610d108261112e565b6110d9565b91508082526020830160208301858383011115610d3157600080fd5b610d3c8382846111e1565b50505092915050565b600082601f8301121515610d5857600080fd5b8135610d6b610d668261115a565b6110d9565b91508082526020830160208301858383011115610d8757600080fd5b610d928382846111e1565b50505092915050565b6000610da782356111d7565b905092915050565b600060208284031215610dc157600080fd5b600082013567ffffffffffffffff811115610ddb57600080fd5b610de784828501610c79565b91505092915050565b600060208284031215610e0257600080fd5b600082013567ffffffffffffffff811115610e1c57600080fd5b610e2884828501610d45565b91505092915050565b60008060408385031215610e4457600080fd5b600083013567ffffffffffffffff811115610e5e57600080fd5b610e6a85828601610d45565b925050602083013567ffffffffffffffff811115610e8757600080fd5b610e9385828601610d45565b9150509250929050565b600060208284031215610eaf57600080fd5b6000610ebd84828501610d9b565b91505092915050565b6000610ed182611193565b80845260208401935083602082028501610eea85611186565b60005b84811015610f23578383038852610f05838351610f79565b9250610f10826111b4565b9150602088019750600181019050610eed565b508196508694505050505092915050565b610f3d816111c1565b82525050565b6000610f4e826111a9565b808452610f628160208601602086016111f0565b610f6b81611223565b602085010191505092915050565b6000610f848261119e565b808452610f988160208601602086016111f0565b610fa181611223565b602085010191505092915050565b6000600782527f6572726f32323200000000000000000000000000000000000000000000000000602083015260408201","9050919050565b6000601d82527f416464726573735365743a204b65792063616e6e6f74206265203078300000006020830152604082019050919050565b611026816111cd565b82525050565b600060208201905081810360008301526110468184610ec6565b905092915050565b60006020820190506110636000830184610f34565b92915050565b600060408201905081810360008301526110838185610f43565b9050611092602083018461101d565b9392505050565b600060208201905081810360008301526110b281610faf565b9050919050565b600060208201905081810360008301526110d281610fe6565b9050919050565b6000604051905081810181811067ffffffffffffffff821117156110fc57600080fd5b8060405250919050565b600067ffffffffffffffff82111561111d57600080fd5b602082029050602081019050919050565b600067ffffffffffffffff82111561114557600080fd5b601f19601f8301169050602081019050919050565b600067ffffffffffffffff82111561117157600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b600081519050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b60008115159050919050565b6000819050919050565b6000819050919050565b82818337600083830152505050565b60005b8381101561120e5780820151818401526020810190506111f3565b8381111561121d576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a72305820165bc4369d47c2a8c200bea6be17f8d8036aded81129544cf39e35dd4294e94a6c6578706572696d656e74616cf50037"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5061126e806100206000396000f30060806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630dcc9758146100935780631bb54eac146100d057806326c0e5371461010d5780633903edb31461014a57806340a707af1461018757806399b5ec2a146101c4578063a15ebc38146101ef578063d741892b1461022d575b600080fd5b34801561009f57600080fd5b506100ba60048036036100b59190810190610e9d565b610258565b6040516100c7919061104e565b60405180910390f35b3480156100dc57600080fd5b506100f760048036036100f29190810190610df0565b6102a8565b604051610104919061104e565b60405180910390f35b34801561011957600080fd5b50610134600480360361012f9190810190610daf565b6102c5565b604051610141919061104e565b60405180910390f35b34801561015657600080fd5b50610171600480360361016c9190810190610daf565b6103ce565b60405161017e919061104e565b60405180910390f35b34801561019357600080fd5b506101ae60048036036101a99190810190610e31565b61043b565b6040516101bb919061104e565b60405180910390f35b3480156101d057600080fd5b506101d961045b565b6040516101e6919061102c565b60405180910390f35b3480156101fb57600080fd5b5061021660048036036102119190810190610df0565b61046c565b604051610224929190611069565b60405180910390f35b34801561023957600080fd5b506102426104a2565b60405161024f919061102c565b60405180910390f35b600081600114151561029f576040517fc703cb12000000000000000000000000000000000000000000000000000000008152600401610296906110b9565b60405180910390fd5b60019050919050565b60006102be82600361058b90919063ffffffff16565b9050919050565b6000806000600580549050845111156102e3576005805490506102e6565b83515b9150600090505b8181101561034757838181518110151561030357fe5b9060200190602002015160058281548110151561031c57fe5b906000526020600020019080519060200190610339929190610b17565b5080806001019150506102ed565b600580549050845111156103c3575b83518110156103c2576005848281518110151561036f57fe5b9060200190602002015190806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906103b3929190610b17565b50508080600101915050610356565b5b600192505050919050565b600080600090505b82518110156104315782818151811015156103ed57fe5b9060200190602002015160058281548110151561040657fe5b906000526020600020019080519060200190610423929190610b17565b5080806001019150506103d6565b6001915050919050565b6000610453838360006106a79092919063ffffffff16565b905092915050565b6060610467600361089c565b905090565b6060600061048483600061098990919063ffffffff16565b61049884600061098990919063ffffffff16565b5191509150915091565b60606005805480602002602001604051908101604052809291908181526020016000905b82821015610582578382906000526020600020018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561056e5780601f106105435761010080835404028352916020019161056e565b820191906000526020600020905b81548152906001019060200180831161055157829003601f168201915b5050505050815260200190600101906104c6565b50505050905090565b6000808251141515156105d3576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004016105ca90611099565b60405180910390fd5b6105dd8383610a9c565b156105eb57600190506106a1565b8260010182908060018154018082558091505090600182039060005260206000200160009091929091909150908051906020019061062a929190610b17565b5083600001836040518082805190602001908083835b6020831015156106655780518252602082019150602081019050602083039250610640565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902081905550600090505b92915050565b60008084600001846040518082805190602001908083835b6020831015156106e457805182526020820191506020810190506020830392506106bf565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600001549050600081111561072f5760009150610894565b8285600001856040518082805190602001908083835b60208310151561076a5780518252602082019150602081019050602083039250610745565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060010190805190602001906107b3929190610b17565b508460010180548091906001016107ca9190610b97565b90506001810185600001856040518082805190602001908083835b60208310151561080a57805182526020820191506020810190506020830392506107e5565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206000018190555083856001018281548110151561085657fe5b9060005260206000209060020201600001908051906020019061087a929190610b17565b508460020160008154809291906001019190505550600191505b509392505050565b606081600101805480602002602001604051908101604052809291908181526020016000905b8282101561097e578382906000526020600020018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561096a5780601f1061093f5761010080835404028352916020019161096a565b820191906000526020600020905b81548152906001019060200180831161094d57829003601f168201915b5050505050815260200190600101906108c2565b505050509050919050565b606082600001826040518082805190602001908083835b6020831015156109c557805182526020820191506020810190506020830392506109a0565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a8f5780601f10610a6457610100808354040283529160200191610a8f565b820191906000526020600020905b815481529060010190602001808311610a7257829003601f168201915b5050505050905092915050565b60008083600001836040518082805190602001908083835b602083101515610ad95780518252602082019150602081019050602083039250610ab4565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020541415905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610b5857805160ff1916838001178555610b86565b82800160010185558215610b86579182015b82811115610b85578251825591602001919060010190610b6a565b5b509050610b939190610bc9565b5090565b815481835581811115610bc457600202816002028360005260206000209182019101610bc39190610bee565b5b505050565b610beb91905b80821115610be7576000816000905550600101610bcf565b5090565b90565b610c2e91905b80821115610c2a5760008082016000610c0d9190610c31565b6001820160006101000a81549060ff021916905550600201610bf4565b5090565b90565b50805460018160011615610100020316600290046000825580601f10610c575750610c76565b601f016020900490600052602060002090810190610c759190610bc9565b5b50565b600082601f8301121515610c8c57600080fd5b8135610c9f610c9a82611106565b6110d9565b9150818183526020840193506020810190508360005b83811015610ce55781358601610ccb8882610cef565b845260208401935060208301925050600181019050610cb5565b5050505092915050565b600082601f8301121515610d0257600080fd5b8135610d15610d108261112e565b6110d9565b91508082526020830160208301858383011115610d3157600080fd5b610d3c8382846111e1565b50505092915050565b600082601f8301121515610d5857600080fd5b8135610d6b610d668261115a565b6110d9565b91508082526020830160208301858383011115610d8757600080fd5b610d928382846111e1565b50505092915050565b6000610da782356111d7565b905092915050565b600060208284031215610dc157600080fd5b600082013567ffffffffffffffff811115610ddb57600080fd5b610de784828501610c79565b91505092915050565b600060208284031215610e0257600080fd5b600082013567ffffffffffffffff811115610e1c57600080fd5b610e2884828501610d45565b91505092915050565b60008060408385031215610e4457600080fd5b600083013567ffffffffffffffff811115610e5e57600080fd5b610e6a85828601610d45565b925050602083013567ffffffffffffffff811115610e8757600080fd5b610e9385828601610d45565b9150509250929050565b600060208284031215610eaf57600080fd5b6000610ebd84828501610d9b565b91505092915050565b6000610ed182611193565b80845260208401935083602082028501610eea85611186565b60005b84811015610f23578383038852610f05838351610f79565b9250610f10826111b4565b9150602088019750600181019050610eed565b508196508694505050505092915050565b610f3d816111c1565b82525050565b6000610f4e826111a9565b808452610f628160208601602086016111f0565b610f6b81611223565b602085010191505092915050565b6000610f848261119e565b808452610f988160208601602086016111f0565b610fa181611223565b602085010191505092915050565b6000601d82527f416464726573735365743a204b65792063616e6e6f7420626520307830000000602083015260408201","9050919050565b6000600782527f6572726f323232000000000000000000000000000000000000000000000000006020830152604082019050919050565b611026816111cd565b82525050565b600060208201905081810360008301526110468184610ec6565b905092915050565b60006020820190506110636000830184610f34565b92915050565b600060408201905081810360008301526110838185610f43565b9050611092602083018461101d565b9392505050565b600060208201905081810360008301526110b281610faf565b9050919050565b600060208201905081810360008301526110d281610fe6565b9050919050565b6000604051905081810181811067ffffffffffffffff821117156110fc57600080fd5b8060405250919050565b600067ffffffffffffffff82111561111d57600080fd5b602082029050602081019050919050565b600067ffffffffffffffff82111561114557600080fd5b601f19601f8301169050602081019050919050565b600067ffffffffffffffff82111561117157600080fd5b601f19601f8301169050602081019050919050565b6000602082019050919050565b600081519050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b60008115159050919050565b6000819050919050565b6000819050919050565b82818337600083830152505050565b60005b8381101561120e5780820151818401526020810190506111f3565b8381111561121d576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a723058202160a3bc4a813d5ac32416dc2acfa46f2201b05e46d4b82c0b282e669bf6695b6c6578706572696d656e74616cf50037"};

    public static final String SM_BINARY = String.join("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[],\"name\":\"getAttrValues\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"orgInfoStr\",\"type\":\"bytes[]\"}],\"name\":\"setValueArrays\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"instr\",\"type\":\"uint256\"}],\"name\":\"TestView\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"getAllValueSet\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"key\",\"type\":\"bytes\"}],\"name\":\"setValueSet\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"key\",\"type\":\"bytes\"}],\"name\":\"getValue\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes\"},{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"key\",\"type\":\"bytes[]\"}],\"name\":\"updateAttrValues\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"key\",\"type\":\"bytes\"},{\"name\":\"value\",\"type\":\"bytes\"}],\"name\":\"setValue\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final String FUNC_GETATTRVALUES = "getAttrValues";

    public static final String FUNC_SETVALUEARRAYS = "setValueArrays";

    public static final String FUNC_TESTVIEW = "TestView";

    public static final String FUNC_GETALLVALUESET = "getAllValueSet";

    public static final String FUNC_SETVALUESET = "setValueSet";

    public static final String FUNC_GETVALUE = "getValue";

    public static final String FUNC_UPDATEATTRVALUES = "updateAttrValues";

    public static final String FUNC_SETVALUE = "setValue";

    protected TestData(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List getAttrValues() throws ContractException {
        final Function function = new Function(FUNC_GETATTRVALUES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<DynamicBytes>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public TransactionReceipt setValueArrays(List<byte[]> orgInfoStr) {
        final Function function = new Function(
                FUNC_SETVALUEARRAYS, 
                Arrays.<Type>asList(orgInfoStr.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.DynamicBytes>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(orgInfoStr, org.fisco.bcos.sdk.abi.datatypes.DynamicBytes.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void setValueArrays(List<byte[]> orgInfoStr, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETVALUEARRAYS, 
                Arrays.<Type>asList(orgInfoStr.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.DynamicBytes>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(orgInfoStr, org.fisco.bcos.sdk.abi.datatypes.DynamicBytes.class))), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetValueArrays(List<byte[]> orgInfoStr) {
        final Function function = new Function(
                FUNC_SETVALUEARRAYS, 
                Arrays.<Type>asList(orgInfoStr.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.DynamicBytes>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(orgInfoStr, org.fisco.bcos.sdk.abi.datatypes.DynamicBytes.class))), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<List<byte[]>> getSetValueArraysInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETVALUEARRAYS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<DynamicBytes>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<List<byte[]>>(

                convertToNative((List<DynamicBytes>) results.get(0).getValue())
                );
    }

    public Tuple1<Boolean> getSetValueArraysOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_SETVALUEARRAYS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public Boolean TestView(BigInteger instr) throws ContractException {
        final Function function = new Function(FUNC_TESTVIEW, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(instr)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public TransactionReceipt getAllValueSet() {
        final Function function = new Function(
                FUNC_GETALLVALUESET, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getAllValueSet(TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETALLVALUESET, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetAllValueSet() {
        final Function function = new Function(
                FUNC_GETALLVALUESET, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<List<byte[]>> getGetAllValueSetOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETALLVALUESET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<DynamicBytes>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<List<byte[]>>(

                convertToNative((List<DynamicBytes>) results.get(0).getValue())
                );
    }

    public TransactionReceipt setValueSet(byte[] key) {
        final Function function = new Function(
                FUNC_SETVALUESET, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void setValueSet(byte[] key, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETVALUESET, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetValueSet(byte[] key) {
        final Function function = new Function(
                FUNC_SETVALUESET, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<byte[]> getSetValueSetInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETVALUESET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public Tuple1<Boolean> getSetValueSetOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_SETVALUESET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public TransactionReceipt getValue(byte[] key) {
        final Function function = new Function(
                FUNC_GETVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void getValue(byte[] key, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_GETVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForGetValue(byte[] key) {
        final Function function = new Function(
                FUNC_GETVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<byte[]> getGetValueInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_GETVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<byte[]>(

                (byte[]) results.get(0).getValue()
                );
    }

    public Tuple2<byte[], BigInteger> getGetValueOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_GETVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<byte[], BigInteger>(

                (byte[]) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public TransactionReceipt updateAttrValues(List<byte[]> key) {
        final Function function = new Function(
                FUNC_UPDATEATTRVALUES, 
                Arrays.<Type>asList(key.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.DynamicBytes>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(key, org.fisco.bcos.sdk.abi.datatypes.DynamicBytes.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void updateAttrValues(List<byte[]> key, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPDATEATTRVALUES, 
                Arrays.<Type>asList(key.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.DynamicBytes>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(key, org.fisco.bcos.sdk.abi.datatypes.DynamicBytes.class))), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForUpdateAttrValues(List<byte[]> key) {
        final Function function = new Function(
                FUNC_UPDATEATTRVALUES, 
                Arrays.<Type>asList(key.isEmpty()?org.fisco.bcos.sdk.abi.datatypes.DynamicArray.empty("bytes[]"):new org.fisco.bcos.sdk.abi.datatypes.DynamicArray<org.fisco.bcos.sdk.abi.datatypes.DynamicBytes>(
                        org.fisco.bcos.sdk.abi.Utils.typeMap(key, org.fisco.bcos.sdk.abi.datatypes.DynamicBytes.class))), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<List<byte[]>> getUpdateAttrValuesInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPDATEATTRVALUES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<DynamicBytes>>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<List<byte[]>>(

                convertToNative((List<DynamicBytes>) results.get(0).getValue())
                );
    }

    public Tuple1<Boolean> getUpdateAttrValuesOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_UPDATEATTRVALUES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public TransactionReceipt setValue(byte[] key, byte[] value) {
        final Function function = new Function(
                FUNC_SETVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key), 
                new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void setValue(byte[] key, byte[] value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key), 
                new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(value)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetValue(byte[] key, byte[] value) {
        final Function function = new Function(
                FUNC_SETVALUE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(key), 
                new org.fisco.bcos.sdk.abi.datatypes.DynamicBytes(value)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<byte[], byte[]> getSetValueInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<byte[], byte[]>(

                (byte[]) results.get(0).getValue(), 
                (byte[]) results.get(1).getValue()
                );
    }

    public Tuple1<Boolean> getSetValueOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_SETVALUE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public static TestData load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new TestData(contractAddress, client, credential);
    }

    public static TestData deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(TestData.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
