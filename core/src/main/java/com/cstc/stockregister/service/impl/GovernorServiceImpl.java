package com.cstc.stockregister.service.impl;

import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.constant.SysConstant;
import com.cstc.stockregister.contracts.Governor;
import com.cstc.stockregister.exception.ContractBaseException;
import com.cstc.stockregister.service.GovernorService;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderInterface;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GovernorServiceImpl extends BaseService implements GovernorService {

    @Autowired
    private Governor governor;

    @Autowired
    @Qualifier("decoder")
    private TransactionDecoderInterface decoder;

    @Override
    public String createAccount(String externalAccount) throws Exception {
        TransactionReceipt receipt=governor.createAccount(externalAccount);
        Tuple2<Boolean, String> result=governor.getCreateAccountOutput(receipt);
        log.info("调用Governor合约的createAccount方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        if(response.getReturnCode()==22){
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return  result.getValue2();
    }

    @Override
    public String createOrganization(List<String> orgInfoStr) throws Exception {
        List<byte[]> orgInfoList=new ArrayList<>();
        for(String str:orgInfoStr){
            orgInfoList.add(str.getBytes());
        }
        TransactionReceipt receipt=governor.createOrganization(orgInfoList);
        Tuple2<Boolean, String> result=governor.getCreateOrganizationOutput(receipt);
        log.info("调用Governor合约的createOrganization方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        if(!result.getValue1()){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return  result.getValue2();
    }

    @Override
    public String createStockAsset( String orgCreditCode, List<String> inDataStr, List<BigInteger> inDataUint) throws Exception {
        List<byte[]> inDataList=new ArrayList<>();
        for(String str:inDataStr){
            inDataList.add(str.getBytes());
        }
        TransactionReceipt receipt=governor.createStockAsset(orgCreditCode.getBytes(),inDataList,inDataUint);
        Tuple2<Boolean, String>  result=governor.getCreateStockAssetOutput(receipt);
        log.info("调用Governor合约的createStockAsset方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        if(!result.getValue1()){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return  result.getValue2();
    }

    @Override
    public String getOrgAddress(String orgCreditCode) throws Exception {
        String orgAddress=governor.getOrgAddress(orgCreditCode.getBytes());
        return orgAddress;
    }

    @Override
    public String getStockAssetAddress(String assetCode) throws Exception {
        String stockAssetAddress=governor.getStockAssetAddress(assetCode.getBytes());
        return stockAssetAddress;
    }

    @Override
    public String updateGovernorOfOrganization(String orgCreditCode, String newGovernor) throws Exception {
        TransactionReceipt receipt=governor.updateGovernorOfOrganization(orgCreditCode.getBytes(),newGovernor);
        Tuple1<String> result=governor.getUpdateGovernorOfOrganizationOutput(receipt);
        if(result.getValue1()==null||result.getValue1().equals("")||result.getValue1().equals(SysConstant.EMPTY_ADDRESS)){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

    @Override
    public String updateCreatorOfOrganization(String orgCreditCode, String newCreator) throws Exception {
        TransactionReceipt receipt=governor.updateCreatorOfOrganization(orgCreditCode.getBytes(),newCreator);
        Tuple1<String> result=governor.getUpdateCreatorOfOrganizationOutput(receipt);
        if(result.getValue1()==null||result.getValue1().equals("")||result.getValue1().equals(SysConstant.EMPTY_ADDRESS)){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

    @Override
    public String updateGovernorOfStockAsset(String assetCode, String newGovernor) throws Exception {
        TransactionReceipt receipt=governor.updateGovernorOfStockAsset(assetCode.getBytes(),newGovernor);
        Tuple1<String> result=governor.getUpdateGovernorOfStockAssetOutput(receipt);
        if(result.getValue1()==null||result.getValue1().equals("")||result.getValue1().equals(SysConstant.EMPTY_ADDRESS)){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

    @Override
    public String updateCreatorOfStockAsset(String assetCode, String newCreator) throws Exception {
        TransactionReceipt receipt=governor.updateCreatorOfStockAsset(assetCode.getBytes(),newCreator);
        Tuple1<String> result=governor.getUpdateCreatorOfStockAssetOutput(receipt);
        if(result.getValue1()==null||result.getValue1().equals("")||result.getValue1().equals(SysConstant.EMPTY_ADDRESS)){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

    @Override
    public boolean updateOrgContractStatus(String orgCreditCode, int newStatus) throws Exception {
        TransactionReceipt receipt=governor.updateOrgContractStatus(orgCreditCode.getBytes(), BigInteger.valueOf(newStatus));
        Tuple1<Boolean> result=governor.getUpdateOrgContractStatusOutput(receipt);
        if(!result.getValue1()){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

    @Override
    public boolean updateStockContractStatus(String assetCode, int newStatus) throws Exception {
        TransactionReceipt receipt=governor.updateStockContractStatus(assetCode.getBytes(), BigInteger.valueOf(newStatus));
        Tuple1<Boolean> result=governor.getUpdateStockContractStatusOutput(receipt);
        if(!result.getValue1()){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

}
