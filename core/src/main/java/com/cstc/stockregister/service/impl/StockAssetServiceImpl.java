package com.cstc.stockregister.service.impl;

import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.constant.OperCodeSet;
import com.cstc.stockregister.constant.ResolveEventLogStatus;
import com.cstc.stockregister.constant.SysConstant;
import com.cstc.stockregister.contracts.Governor;
import com.cstc.stockregister.contracts.StockAsset;
import com.cstc.stockregister.entity.*;
import com.cstc.stockregister.exception.ContractBaseException;
import com.cstc.stockregister.response.ResolveEventLogResult;
import com.cstc.stockregister.service.AccountService;
import com.cstc.stockregister.service.StockAssetService;
import com.cstc.stockregister.util.JsonHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.protocol.response.BcosTransactionReceiptsDecoder;
import org.fisco.bcos.sdk.client.protocol.response.BcosTransactionReceiptsInfo;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderInterface;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.*;

@Service
@Slf4j
public class StockAssetServiceImpl extends BaseService implements StockAssetService {
    private static final int STOP_RESOLVE_BLOCK_NUMBER = 0;

    @Autowired
    private Governor governor;
    @Autowired
    @Qualifier("decoder")
    private TransactionDecoderInterface decoder;

    @Override
    public boolean updateTotalSupply(String assetCode, BigInteger _totalBalances) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.updateTotalSupply(_totalBalances);
        log.info("调用StockAsset合约的updateTotalSupply方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        Tuple1<Boolean> result= stockAsset.getUpdateTotalSupplyOutput(receipt);
        if(result.getValue1()){
            return true;
        }else{
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
    }

    @Override
    public BigInteger totalSupply(String assetCode) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        BigInteger result=stockAsset.totalSupply();
        return result;
    }

    @Override
    public AccountBookDTO balanceByAccount(String assetCode, String externalAccount) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        AccountBookDTO accountBookDTO=new AccountBookDTO();
        try{
            List<BigInteger> result=stockAsset.balanceByAccount(externalAccount);
            if(result.size()==14){
                accountBookDTO.setTotal(result.get(0));
                accountBookDTO.setCirculate(result.get(1));
                accountBookDTO.setRestrictedSales(result.get(2));
                accountBookDTO.setCanTrans(result.get(3));
                accountBookDTO.setTransRestrictedSales(result.get(4));
                accountBookDTO.setPledgeAmount(result.get(5));
                accountBookDTO.setFreezeAmount(result.get(6));
                accountBookDTO.setWaitingFreeze(result.get(7));
                accountBookDTO.setFrozenTransRestrictedSales(result.get(8));
                accountBookDTO.setFrozenCirculate(result.get(9));
                accountBookDTO.setFrozenRestrictedSales(result.get(10));
                accountBookDTO.setPledgeTransRestrictedSales(result.get(11));
                accountBookDTO.setPledgeCirculate(result.get(12));
                accountBookDTO.setPledgeRestrictedSales(result.get(13));
            }
        }catch (ContractException e){
            log.error("调用StockAsset合约的balanceByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_ERROR.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.balanceByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }
        return accountBookDTO;
    }

    @Override
    public List<TransactionDTO> getTransRestrictedDetailByAccount(String assetCode, String externalAccount) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        List<TransactionDTO> list=new ArrayList<>();
        try{
            Tuple2<BigInteger, String> result=stockAsset.getTransRestrictedDetailByAccount(externalAccount);
            if(SysConstant.QUERY_CURD_SUCCESS==result.getValue1().intValue()){
                list=JsonHelper.json2Object(result.getValue2(),new TypeReference<List<TransactionDTO>>(){});
            }
        }catch (ContractException e){
            log.error("调用StockAsset合约的getTransRestrictedDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_ERROR.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.getTransRestrictedDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }
        return list;
    }

    @Override
    public PledgeDTO getPledgeDetailByNumber(String assetCode, String externalAccount, BigInteger pledgeNumber) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        PledgeDTO pledgeDTO=new PledgeDTO();
        try{
            Tuple2<BigInteger, List<String>> result=stockAsset.getPledgeDetailByNumber(externalAccount,pledgeNumber);
            pledgeDTO.setPledgor(externalAccount);
            List<String> content=result.getValue2();
            if(SysConstant.QUERY_CURD_SUCCESS==result.getValue1().intValue() && content.size()==6){
                pledgeDTO.setSerialNum(content.get(0));
                pledgeDTO.setPledgee(content.get(1));
                pledgeDTO.setAmount(content.get(2));
                pledgeDTO.setFromCirculate(content.get(3));
                pledgeDTO.setFromRestrictedSales(content.get(4));
                pledgeDTO.setReleaseTime(content.get(5));
            }
        }catch (ContractException e){
            log.error("调用StockAsset合约的getPledgeDetailByNumber方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_ERROR.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.getPledgeDetailByNumber方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }
        return pledgeDTO;
    }

    @Override
    public List<PledgeDTO> getPledgeDetailByAccount(String assetCode, String externalAccount) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        List<PledgeDTO> list=new ArrayList<>();
        try{
            Tuple2<BigInteger, String> result=stockAsset.getPledgeDetailByAccount(externalAccount);
            if(SysConstant.QUERY_CURD_SUCCESS==result.getValue1().intValue()){
                list=JsonHelper.json2Object(result.getValue2(),new TypeReference<List<PledgeDTO>>(){});
            }
        }catch (ContractException e){
            log.error("调用StockAsset合约的getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_ERROR.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }


        return list;
    }

    @Override
    public FrozenDTO getFrozenDetailByNumber(String assetCode, String externalAccount, BigInteger frozenNumber) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        FrozenDTO frozenDTO=new FrozenDTO();
        frozenDTO.setAssetOwnerAddress(externalAccount);
        try{
            Tuple2<BigInteger, List<String>> result=stockAsset.getFrozenDetailByNumber(externalAccount,frozenNumber);
            List<String> content=result.getValue2();
            if(SysConstant.QUERY_CURD_SUCCESS==result.getValue1().intValue()&& content.size()==10){
                frozenDTO.setSerialNum(content.get(0));
                frozenDTO.setBusinessType(content.get(1));
                frozenDTO.setApplicant(content.get(2));
                frozenDTO.setRequestAmount(content.get(3));
                frozenDTO.setFrozenAmount(content.get(4));
                frozenDTO.setWaitingAmount(content.get(5));
                frozenDTO.setStartTime(content.get(6));
                frozenDTO.setEndTime(content.get(7));
                frozenDTO.setWaitingNumber(content.get(8));
                frozenDTO.setStatus(content.get(9));
            }
        }catch (ContractException e){
            log.error("调用StockAsset合约的getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_ERROR.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }
        return frozenDTO;
    }

    @Override
    public List<FrozenDTO> getFrozenDetailByAccount(String assetCode, String externalAccount) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        List<FrozenDTO> list=new ArrayList<>();
        try{
            Tuple2<BigInteger, String> result=stockAsset.getFrozenDetailByAccount(externalAccount);
            if(SysConstant.QUERY_CURD_SUCCESS==result.getValue1().intValue()){
                list=JsonHelper.json2Object(result.getValue2(),new TypeReference<List<FrozenDTO>>(){});
            }
        }catch (ContractException e){
            log.error("调用StockAsset合约的getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_ERROR.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }
        return list;
    }

    @Override
    public int getNextWaitingFrozenNumber(String assetCode, String externalAccount, BigInteger unFrozenNumber) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        try{
            BigInteger result=stockAsset.getNextWaitingFrozenNumber(externalAccount,unFrozenNumber);
            return result.intValue();
        }catch (ContractException e){
            log.error("调用StockAsset合约的getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }
    }

    @Override
    public int transferByAdmin(String assetCode, String externalFromAccount, String externaltoAccount, BigInteger amount, BigInteger transferType) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.transferByAdmin(externalFromAccount,externaltoAccount,amount,transferType);
        log.info("调用StockAsset合约的transferByAdmin方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        Tuple1<BigInteger> result= stockAsset.getTransferByAdminOutput(receipt);
        if(result.getValue1().intValue()==0){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }else{
            return result.getValue1().intValue();
        }

    }

    @Override
    public int cancelTransRestrictions(String assetCode, String externalAccount, List<BigInteger> transNumber) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.cancelTransRestrictions(externalAccount,transNumber);
        log.info("调用StockAsset合约的cancelTransRestrictions方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        if(response.getReturnCode()==22){
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        Tuple1<BigInteger> result= stockAsset.getCancelTransRestrictionsOutput(receipt);
        return result.getValue1().intValue();
    }

    //isRSales:0-不限售，1-限售
    @Override
    public int incrBalance(String assetCode, String externalAccount, BigInteger amount, int isRSales) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.incrBalance(externalAccount,amount, BigInteger.valueOf(isRSales));
        log.info("调用StockAsset合约的incrBalance方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        Tuple1<BigInteger> result= stockAsset.getIncrBalanceOutput(receipt);
        if(result.getValue1().intValue()==0){
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }else{
            return result.getValue1().intValue();
        }
    }

    @Override
    public int reduceBalance(String assetCode, String externalAccount, BigInteger amount) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.reduceBalance(externalAccount,amount);
        log.info("调用StockAsset合约的reduceBalance方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        Tuple1<BigInteger> result= stockAsset.getReduceBalanceOutput(receipt);
        if(result.getValue1().intValue()==0){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1().intValue();
    }

    @Override
    public int doPledge(String assetCode, List<BigInteger> balanceData, List<String> pledgeDetail, String causeDesc) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.doPledge(balanceData,pledgeDetail,causeDesc);
        log.info("调用StockAsset合约的doPledge方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        Tuple1<BigInteger> result= stockAsset.getDoPledgeOutput(receipt);
        if(result.getValue1().intValue()==0){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1().intValue();
    }

    @Override
    public boolean undoPledge(String assetCode, String pledgor, List<BigInteger> balanceData, BigInteger pledgeNumber) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.undoPledge(pledgor,balanceData,pledgeNumber);
        log.info("调用StockAsset合约的undoPledge方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        Tuple1<Boolean> result= stockAsset.getUndoPledgeOutput(receipt);
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        if(response.getReturnCode()==22){
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

    @Override
    public int doFrozen(String assetCode, List<BigInteger> balanceData, List<String> frozenDetail) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.doFrozen(balanceData,frozenDetail);
        log.info("调用StockAsset合约的doFrozen方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        Tuple1<BigInteger> result= stockAsset.getDoFrozenOutput(receipt);
        if(result.getValue1().intValue()==0){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1().intValue();
    }

    @Override
    public boolean doWaitingFrozen(String assetCode, List<BigInteger> balanceData, String ownerAddress, int waitingFrozenSerilNum, int requestAmount) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        List<String> frozenDetail=new ArrayList<>();
        try{
            Tuple2<BigInteger, List<String>> waitingResult=stockAsset.getFrozenDetailByNumber(ownerAddress, BigInteger.valueOf(waitingFrozenSerilNum));
            if(SysConstant.QUERY_CURD_SUCCESS==waitingResult.getValue1().intValue()&& waitingResult.getValue2().size()==10){
                frozenDetail.add(0,waitingResult.getValue2().get(0));
                frozenDetail.add(1,waitingResult.getValue2().get(1));
                frozenDetail.add(2,waitingResult.getValue2().get(2));
                frozenDetail.add(3,waitingResult.getValue2().get(3));
                int frozenAmount=Integer.valueOf(waitingResult.getValue2().get(4))+requestAmount;
                frozenDetail.add(4, String.valueOf(frozenAmount));
                int waitingAmount=Integer.valueOf(waitingResult.getValue2().get(5))-requestAmount;
                frozenDetail.add(5, String.valueOf(waitingAmount));
                frozenDetail.add(6,waitingResult.getValue2().get(6));
                frozenDetail.add(7,waitingResult.getValue2().get(7));
                frozenDetail.add(8,waitingResult.getValue2().get(8));
                if(waitingAmount==0){
                    frozenDetail.add(9, String.valueOf(SysConstant.FREEZE_ALL));
                }else if(waitingAmount>0){
                    frozenDetail.add(9, String.valueOf(SysConstant.FREEZE_PART));
                }else{
                    throw new ContractBaseException(ErrorCode.DO_WAITING_FROZEN_PARMA_ERROR.getCode(),ErrorCode.DO_WAITING_FROZEN_PARMA_ERROR.getErrMsg());
                }
            }
        }catch (ContractException e){
            log.error("调用StockAsset合约的getFrozenDetailByNumber方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.doWaitingFrozen方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }
        TransactionReceipt receipt=stockAsset.doWaitingFrozenToFrozen(balanceData,ownerAddress,frozenDetail);
        log.info("调用StockAsset合约的doWaitingFrozenToFrozen方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        Tuple1<Boolean> result= stockAsset.getDoWaitingFrozenToFrozenOutput(receipt);
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        if(response.getReturnCode()==22){
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

    @Override
    public int undoFrozen(String assetCode, String assetOwnerAddress, List<BigInteger> balanceData, BigInteger frozenNumber) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        TransactionReceipt receipt=stockAsset.undoFrozen(assetOwnerAddress,balanceData,frozenNumber);
        log.info("调用StockAsset合约的undoFrozen方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        Tuple1<BigInteger> result= stockAsset.getUndoFrozenOutput(receipt);
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        if(response.getReturnCode()==22){
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1().intValue();
    }

    @Override
    public StockBusinessRecordDTO getBusinessRecord(AccountService accountService,String assetCode,String externalAccount,int businessType) throws Exception {
        String interAccount=accountService.getUserAccount(externalAccount);
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        StockBusinessRecordDTO stockBusinessRecords=new StockBusinessRecordDTO();
        stockBusinessRecords.setAccount(externalAccount);
        Map<Integer, List<StockAsset.StockBusinessRecordEventResponse>> blockEventMap = new HashMap<>();
        List<Integer> blockList = new ArrayList<>();
        int latestBlockNumber;
        try{
            latestBlockNumber=stockAsset.getLastBlockRecord(externalAccount).intValue();
        }catch (ContractException e){
            log.error("调用StockAsset合约的getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("调用StockAssetServiceImpl.getPledgeDetailByAccount方法失败： "+e.getMessage());
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),ErrorCode.CONTRACT_CALL_FAIL.getErrMsg());
        }

        if (0 == latestBlockNumber) {
            return  stockBusinessRecords;
        }

        // Step 1: fetch all blocks in this event link in REVERSE order from chain
        resolveEventHistory(stockAsset,interAccount,latestBlockNumber,blockList,blockEventMap);

        // Step 2: reverse this the block list (so it is ascending order now)
        Collections.reverse(blockList);

        // Step 3: construct StockBusinessRecord in NORMAL order off-chain
        constructStockBusinessRecord(accountService,assetCode,externalAccount,businessType,blockList, blockEventMap, stockBusinessRecords);

        return stockBusinessRecords;
    }

    @Override
    public String getGovernor(String assetCode) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        String result=stockAsset.getGovernor();
        return result;
    }

    @Override
    public String getCreator(String assetCode) throws Exception {
        StockAsset stockAsset=this.getStockAssetContract(assetCode);
        String result=stockAsset.getCreator();
        return result;
    }

    private void resolveEventHistory(StockAsset stockAsset,String interAccount,int blockNumber,
            List<Integer> blockList,Map<Integer, List<StockAsset.StockBusinessRecordEventResponse>> blockEventMap) {
        HashSet<Integer> isExist=new HashSet<>();
        int previousBlock = blockNumber;
        while (previousBlock != STOP_RESOLVE_BLOCK_NUMBER) {
            int currentBlockNumber = previousBlock;
            // Fill-in blockList
            blockList.add(currentBlockNumber);
            previousBlock = 0;
            try {
                BcosTransactionReceiptsDecoder bcosTransactionReceiptsDecoder =
                        this.getClient().getBatchReceiptsByBlockNumberAndRange(BigInteger.valueOf(currentBlockNumber), "0", "-1");
                // 解码交易回执信息
                BcosTransactionReceiptsInfo.TransactionReceiptsInfo receiptsInfo = bcosTransactionReceiptsDecoder.decodeTransactionReceiptsInfo();
                // 获取交易回执列表
                List<TransactionReceipt> receipts = receiptsInfo.getTransactionReceipts();

                for (TransactionReceipt receipt : receipts) {
                    List<TransactionReceipt.Logs> logs = receipt.getLogs();
                    for (TransactionReceipt.Logs log : logs) {
                        ResolveEventLogResult returnValue =
                                resolveSingleEventLog(stockAsset,interAccount, log, receipt, currentBlockNumber,
                                        blockEventMap,isExist);
                        if (returnValue.getResultStatus().equals(
                                ResolveEventLogStatus.STATUS_SUCCESS)) {
                            if (returnValue.getPreviousBlock() == currentBlockNumber) {
                                continue;
                            }
                            previousBlock = returnValue.getPreviousBlock();
                        }
                    }
                }
            } catch (Exception e) {
                throw new ContractBaseException(
                        ErrorCode.TRANSACTION_EXECUTE_ERROR.getCode(),
                        ErrorCode.TRANSACTION_EXECUTE_ERROR.getErrMsg(),
                        e);
            }
        }
    }


    private ResolveEventLogResult resolveSingleEventLog(StockAsset stockAsset,String interAccount,TransactionReceipt.Logs log,
            TransactionReceipt receipt,int currentBlockNumber,Map<Integer, List<StockAsset.StockBusinessRecordEventResponse>> blockEventMap,
                                                        HashSet<Integer> isExist) {
        String topic = log.getTopics().get(0);
        String event = this.getStockTopicMap().get(topic);

        if (StringUtils.isNotBlank(event)) {
            return extractEventsFromBlock(stockAsset,interAccount, receipt, currentBlockNumber, blockEventMap,isExist);
        }
        ResolveEventLogResult response = new ResolveEventLogResult();
        response.setResolveEventLogStatus(ResolveEventLogStatus.STATUS_EVENT_NULL);
        return response;
    }

    private ResolveEventLogResult extractEventsFromBlock(StockAsset stockAsset,String interAccount,
            TransactionReceipt receipt,int currentBlockNumber,
            Map<Integer, List<StockAsset.StockBusinessRecordEventResponse>> blockEventMap,HashSet<Integer> isExist) {
        List<StockAsset.StockBusinessRecordEventResponse> eventlog =stockAsset.getStockBusinessRecordEvents(receipt);
        ResolveEventLogResult response = new ResolveEventLogResult();

        if (CollectionUtils.isEmpty(eventlog)) {
            response.setResolveEventLogStatus(ResolveEventLogStatus.STATUS_EVENTLOG_NULL);
            return response;
        }

        int previousBlock = 0;
        for (StockAsset.StockBusinessRecordEventResponse res : eventlog) {
            if(StringUtils.equals(res.account, interAccount)){
                // Fill-in blockEventMap
                List<StockAsset.StockBusinessRecordEventResponse> events = blockEventMap.get(currentBlockNumber);
                int hashCode=res.account.concat(String.valueOf(res.businessType)).concat(res.content.toString()).hashCode();
                if(isExist.contains(hashCode)){
                    continue;
                }else{
                    isExist.add(hashCode);
                }
                if (CollectionUtils.isEmpty(events)) {
                    List<StockAsset.StockBusinessRecordEventResponse> newEvents = new ArrayList<>();
                    newEvents.add(res);
                    blockEventMap.put(currentBlockNumber, newEvents);
                } else {
                    events.add(res);
                }
                previousBlock = res.previousBlock.intValue();
            }
        }

        if(previousBlock==0){
            response.setResolveEventLogStatus(ResolveEventLogStatus.STATUS_KEY_NOT_MATCH);
            return response;
        }
        response.setPreviousBlock(previousBlock);
        response.setResolveEventLogStatus(ResolveEventLogStatus.STATUS_SUCCESS);
        return response;
    }


    private void constructStockBusinessRecord(AccountService accountService,String assetCode,String externalAccount,int businessType,
            List<Integer> blockList,Map<Integer, List<StockAsset.StockBusinessRecordEventResponse>> blockEventMap,
            StockBusinessRecordDTO stockBusinessRecords) {
        for (int block : blockList) {
            List<StockAsset.StockBusinessRecordEventResponse> eventList = blockEventMap.get(block);
            if(null!=eventList){
                for (StockAsset.StockBusinessRecordEventResponse event : eventList) {
                    if(OperCodeSet.capitalChangeTypeSet.contains(event.businessType.intValue())&& (businessType==SysConstant.ALL_RECORD_TYPE||businessType==SysConstant.CAPITAL_CHANGE_RECORD_TYPE)){
                        TransferRecordDTO transferRecord=new TransferRecordDTO();
                        transferRecord.setStockCode(assetCode);
                        transferRecord.setTransferType(event.businessType.intValue());
                        try {
                            String externalToAccount=accountService.getExternalAccount(event.content.get(0).toString());
                            transferRecord.setToAccount(externalToAccount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        transferRecord.setAmount(event.content.get(1).toString());
                        transferRecord.setOccurTime(event.content.get(2).toString());
                        transferRecord.setSerialNum(event.content.get(3).toString());
                        stockBusinessRecords.getTransferRecords().add(transferRecord);
                    }else if(OperCodeSet.tranferTypeSet.contains(event.businessType.intValue())&&(businessType==SysConstant.ALL_RECORD_TYPE||businessType==SysConstant.TRANSFER_RECORD_TYPE)){
                        TransferRecordDTO transferRecord=new TransferRecordDTO();
                        transferRecord.setStockCode(assetCode);
                        transferRecord.setTransferType(event.businessType.intValue());
                        try {
                            String externalFromAccount=accountService.getExternalAccount(event.content.get(0).toString());
                            String externalToAccount=accountService.getExternalAccount(event.content.get(1).toString());
                            transferRecord.setFromAccount(externalFromAccount);
                            transferRecord.setToAccount(externalToAccount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        transferRecord.setAmount(event.content.get(2).toString());
                        transferRecord.setSerialNum(event.content.get(3).toString());
                        transferRecord.setOccurTime(event.content.get(4).toString());
                        stockBusinessRecords.getTransferRecords().add(transferRecord);
                    }else if(OperCodeSet.pledgeTypeSet.contains(event.businessType.intValue())&&(businessType==SysConstant.ALL_RECORD_TYPE||businessType==SysConstant.PLEDGE_RECORD_TYPE)){
                        PledgeAndReleaseRecordDTO pledgeAndReleaseRecordDTO=new PledgeAndReleaseRecordDTO();
                        pledgeAndReleaseRecordDTO.setStockCode(assetCode);
                        pledgeAndReleaseRecordDTO.setBusinessType(event.businessType.intValue());
                        pledgeAndReleaseRecordDTO.setPledgor(externalAccount);
                        try {
                            String externalToAccount=accountService.getExternalAccount(event.content.get(1).toString());
                            pledgeAndReleaseRecordDTO.setPledgee(externalToAccount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pledgeAndReleaseRecordDTO.setSerialNum(event.content.get(2).toString());
                        pledgeAndReleaseRecordDTO.setAmount(event.content.get(3).toString());
                        pledgeAndReleaseRecordDTO.setFromCirculate(event.content.get(4).toString());
                        pledgeAndReleaseRecordDTO.setFromRestrictedSales(event.content.get(5).toString());
                        pledgeAndReleaseRecordDTO.setReleaseTime(event.content.get(6).toString());
                        pledgeAndReleaseRecordDTO.setOccurTime(event.content.get(7).toString());
                        pledgeAndReleaseRecordDTO.setCauseDesc(event.content.get(8).toString());
                        stockBusinessRecords.getPledgeAndReleaseRecords().add(pledgeAndReleaseRecordDTO);
                    }else if(OperCodeSet.frozenTypeSet.contains(event.businessType.intValue())&&(businessType==SysConstant.ALL_RECORD_TYPE||businessType==SysConstant.FROZEN_RECORD_TYPE)){
                        FrozenAndReleaseRecordDTO frozenAndReleaseRecordDTO=new FrozenAndReleaseRecordDTO();
                        frozenAndReleaseRecordDTO.setStockCode(assetCode);
                        frozenAndReleaseRecordDTO.setSerialNum(event.content.get(0).toString());
                        frozenAndReleaseRecordDTO.setAssetOwnerAddress(externalAccount);
                        frozenAndReleaseRecordDTO.setBusinessType(event.businessType.intValue());
                        frozenAndReleaseRecordDTO.setApplicant(event.content.get(2).toString());
                        frozenAndReleaseRecordDTO.setRequestAmount(event.content.get(3).toString());
                        frozenAndReleaseRecordDTO.setFrozenAmount(event.content.get(4).toString());
                        frozenAndReleaseRecordDTO.setWaitingAmount(event.content.get(5).toString());
                        frozenAndReleaseRecordDTO.setStartTime(event.content.get(6).toString());
                        frozenAndReleaseRecordDTO.setEndTime(event.content.get(7).toString());
                        frozenAndReleaseRecordDTO.setWaitingNumber(event.content.get(8).toString());
                        frozenAndReleaseRecordDTO.setStatus(event.content.get(9).toString());
                        stockBusinessRecords.getFrozenAndReleaseRecords().add(frozenAndReleaseRecordDTO);
                    }
                }
            }
        }
    }

    private StockAsset getStockAssetContract(String assetCode)throws Exception {
        String stockAssetAddress=governor.getStockAssetAddress(assetCode.getBytes());
        if(SysConstant.EMPTY_ADDRESS.equals(stockAssetAddress)||"".equals(stockAssetAddress)||stockAssetAddress==null){
            throw new ContractBaseException(ErrorCode.LOAD_CONTRACT_ERROR.getCode(),ErrorCode.LOAD_CONTRACT_ERROR.getErrMsg());
        }
        StockAsset stockAsset=this.getContract(stockAssetAddress,StockAsset.class);
        return  stockAsset;
    }

}
