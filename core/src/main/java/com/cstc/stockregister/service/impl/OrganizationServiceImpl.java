package com.cstc.stockregister.service.impl;

import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.constant.ResolveEventLogStatus;
import com.cstc.stockregister.constant.SysConstant;
import com.cstc.stockregister.contracts.Governor;
import com.cstc.stockregister.contracts.Organization;
import com.cstc.stockregister.entity.*;
import com.cstc.stockregister.exception.ContractBaseException;
import com.cstc.stockregister.response.ResolveEventLogResult;
import com.cstc.stockregister.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.protocol.response.BcosTransactionReceiptsDecoder;
import org.fisco.bcos.sdk.client.protocol.response.BcosTransactionReceiptsInfo;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderInterface;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.*;

@Service
@Slf4j
public class OrganizationServiceImpl extends BaseService implements OrganizationService {
    private static final int STOP_RESOLVE_BLOCK_NUMBER = 0;

    @Autowired
    private Governor governor;
    @Autowired
    @Qualifier("decoder")
    private TransactionDecoderInterface decoder;

    @Override
    public int updateOrgInfo(String orgCreditCode,List<String> orgInfoStr) throws Exception {
        List<byte[]> orgInfoList=new ArrayList<>();
        for(String str:orgInfoStr){
            orgInfoList.add(str.getBytes());
        }
        Organization organization=this.getOrgContract(orgCreditCode);
        TransactionReceipt receipt=organization.updateOrgInfo(orgInfoList);
        Tuple1<BigInteger> result=organization.getUpdateOrgInfoOutput(receipt);
        log.info("调用Organization合约的updateOrgInfo方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        if(result.getValue1().intValue()==0){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1().intValue();
    }

    @Override
    public OrganizationDTO getOrgInfo(String orgCreditCode) throws Exception {
        OrganizationDTO organizationDTO=new OrganizationDTO();
        Organization organization=this.getOrgContract(orgCreditCode);
        Tuple2<List<byte[]>, String> result=organization.getOrgInfo();
        List<byte[]> orgStr=result.getValue1();
        organizationDTO.setOrgName(new String(orgStr.get(0)));
        organizationDTO.setOrgCreditCode(new String(orgStr.get(1)));
        organizationDTO.setLegalPerson(new String(orgStr.get(2)));
        organizationDTO.setLegalIdNumber(new String(orgStr.get(3)));
        organizationDTO.setRegisteredAddress(new String(orgStr.get(4)));
        organizationDTO.setRegistryDate(new String(orgStr.get(5)));
        organizationDTO.setOfficeAddress(new String(orgStr.get(6)));
        organizationDTO.setContactNumber(new String(orgStr.get(7)));
        organizationDTO.setCredType(Integer.valueOf(new String(orgStr.get(8))));
        organizationDTO.setOrgType(Integer.valueOf(new String(orgStr.get(9))));
        organizationDTO.setRegisteredCapital(Integer.valueOf(new String(orgStr.get(10))));
        organizationDTO.setPaidInCapital(Integer.valueOf(new String(orgStr.get(11))));
        organizationDTO.setShareholdersNumber(Integer.valueOf(new String(orgStr.get(12))));
        organizationDTO.setLegalIdType(Integer.valueOf(new String(orgStr.get(13))));
        organizationDTO.setStockAsset(result.getValue2());
        return organizationDTO;
    }

    @Override
    public boolean setStockAsset(String orgCreditCode,String stockAssetAddress) throws Exception {
        Organization organization=this.getOrgContract(orgCreditCode);
        TransactionReceipt receipt=organization.setStockAsset(stockAssetAddress);
        Tuple1<Boolean> result=organization.getSetStockAssetOutput(receipt);
        log.info("调用Organization合约的setStockAsset方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        if(!result.getValue1()){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }
    @Override
    public boolean setBondAsset(String orgCreditCode,String bondAssetCode, String bondAssetAddress) throws Exception {
        Organization organization=this.getOrgContract(orgCreditCode);
        TransactionReceipt receipt=organization.setBondAsset(bondAssetCode.getBytes(),bondAssetAddress);
        Tuple1<Boolean> result=organization.getSetBondAssetOutput(receipt);
        log.info("调用Organization合约的setBondAsset方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        return result.getValue1();
    }

    @Override
    public String getBondAsset(String orgCreditCode,String bondAssetCode) throws Exception {
        Organization organization=this.getOrgContract(orgCreditCode);
        String bondAssetAddress=organization.getBondAsset(bondAssetCode.getBytes());
        return bondAssetAddress;
    }

    @Override
    public int addManager(String orgCreditCode,List<String> managers) throws Exception {
        Organization organization=this.getOrgContract(orgCreditCode);
        TransactionReceipt receipt=organization.addManager(managers);
        Tuple1<BigInteger> result=organization.getAddManagerOutput(receipt);
        log.info("调用Organization合约的addManager方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        int count=result.getValue1().intValue();
        if(count<=0){
            TransactionResponse response=decoder.decodeReceiptStatus(receipt);
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return count;
    }

    @Override
    public boolean deleteManager(String orgCreditCode,String managerAddress) throws Exception {
        Organization organization=this.getOrgContract(orgCreditCode);
        TransactionReceipt receipt=organization.deleteManager(managerAddress);
        Tuple1<Boolean> result=organization.getDeleteManagerOutput(receipt);
        log.info("调用Organization合约的deleteManager方法，交易结果：区块高度="+Integer.parseInt(receipt.getBlockNumber().substring(2),16)+" ,区块hash="+receipt.getBlockHash()+" ,交易HASH="+receipt.getTransactionHash());
        TransactionResponse response=decoder.decodeReceiptStatus(receipt);
        if(response.getReturnCode()==22){
            throw new ContractBaseException(ErrorCode.CONTRACT_CALL_FAIL.getCode(),response.getReturnMessage());
        }
        return result.getValue1();
    }

    @Override
    public List<String> getManager(String orgCreditCode) throws Exception {
        List<String> respone=new ArrayList<>();
        Organization organization=this.getOrgContract(orgCreditCode);
        List<String> result=organization.getManager();
        for(String str:result){
            respone.add(str);
        }
        return respone;
    }


    @Override
    public List<OrgInfoChangeDTO> getOrgInfoChangeRecord(String orgCreditCode) throws Exception {
        Organization organization=this.getOrgContract(orgCreditCode);
        int latestBlockNumber=organization.getLastBlock().intValue();
        List<OrgInfoChangeDTO> orgInfoChangeDTOList=new ArrayList<>();
        Map<Integer, List<Organization.UpdateOrgInfoRecordEventResponse>> blockEventMap = new HashMap<>();
        List<Integer> blockList = new ArrayList<>();

        if (0 == latestBlockNumber) {
            return  orgInfoChangeDTOList;
        }

        // Step 1: fetch all blocks in this event link in REVERSE order from chain
        resolveEventHistory(organization,latestBlockNumber,blockList,blockEventMap);

        // Step 2: reverse this the block list (so it is ascending order now)
        Collections.reverse(blockList);

        // Step 3: construct OrgInfoChangeDTO in NORMAL order off-chain
        constructOrgInfoChangeRecord(blockList, blockEventMap, orgInfoChangeDTOList);

        return orgInfoChangeDTOList;
    }

    @Override
    public String getGovernor(String orgCreditCode) throws Exception {
        Organization organization=this.getOrgContract(orgCreditCode);
        String result=organization.getGovernor();
        return result;
    }

    @Override
    public String getCreator(String orgCreditCode) throws Exception {
        Organization organization=this.getOrgContract(orgCreditCode);
        String result=organization.getCreator();
        return result;
    }

    private void resolveEventHistory(Organization organization,
                                     int blockNumber,
                                     List<Integer> blockList,
                                     Map<Integer, List<Organization.UpdateOrgInfoRecordEventResponse>> blockEventMap
    ) {
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
                                resolveSingleEventLog(organization, log, receipt, currentBlockNumber,
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

    private ResolveEventLogResult resolveSingleEventLog(Organization organization,
                                                        TransactionReceipt.Logs log,
                                                        TransactionReceipt receipt,
                                                        int currentBlockNumber,
                                                        Map<Integer, List<Organization.UpdateOrgInfoRecordEventResponse>> blockEventMap,
                                                        HashSet<Integer> isExist) {
        String topic = log.getTopics().get(0);
        String event = this.getOrganizationTopicMap().get(topic);

        if (StringUtils.isNotBlank(event)) {
            return extractEventsFromBlock(organization, receipt, currentBlockNumber, blockEventMap,isExist);
        }
        ResolveEventLogResult response = new ResolveEventLogResult();
        response.setResolveEventLogStatus(ResolveEventLogStatus.STATUS_EVENT_NULL);
        return response;
    }

    private ResolveEventLogResult extractEventsFromBlock(Organization organization,
                                                         TransactionReceipt receipt,
                                                         int currentBlockNumber,
                                                         Map<Integer, List<Organization.UpdateOrgInfoRecordEventResponse>> blockEventMap,
                                                         HashSet<Integer> isExist
    ) {
        List<Organization.UpdateOrgInfoRecordEventResponse> eventlog =organization.getUpdateOrgInfoRecordEvents(receipt);
        ResolveEventLogResult response = new ResolveEventLogResult();

        if (CollectionUtils.isEmpty(eventlog)) {
            response.setResolveEventLogStatus(ResolveEventLogStatus.STATUS_EVENTLOG_NULL);
            return response;
        }

        int previousBlock = 0;
        for (Organization.UpdateOrgInfoRecordEventResponse res : eventlog) {
            // Fill-in blockEventMap
            List<Organization.UpdateOrgInfoRecordEventResponse> events = blockEventMap.get(currentBlockNumber);
            if(isExist.contains(res.number)){
                continue;
            }else{
                isExist.add(res.number.intValue());
            }
            if (CollectionUtils.isEmpty(events)) {
                List<Organization.UpdateOrgInfoRecordEventResponse> newEvents = new ArrayList<>();
                newEvents.add(res);
                blockEventMap.put(currentBlockNumber, newEvents);
            } else {
                events.add(res);
            }
            previousBlock = res.previousBlock.intValue();
        }

        if(previousBlock==0){
            response.setResolveEventLogStatus(ResolveEventLogStatus.STATUS_KEY_NOT_MATCH);
            return response;
        }
        response.setPreviousBlock(previousBlock);
        response.setResolveEventLogStatus(ResolveEventLogStatus.STATUS_SUCCESS);
        return response;
    }

    private void constructOrgInfoChangeRecord(List<Integer> blockList,
                                              Map<Integer, List<Organization.UpdateOrgInfoRecordEventResponse>> blockEventMap,
                                              List<OrgInfoChangeDTO> orgInfoChangeDTOList) {
        for (int block : blockList) {
            List<Organization.UpdateOrgInfoRecordEventResponse> eventList = blockEventMap.get(block);
            if(null!=eventList){
                for (Organization.UpdateOrgInfoRecordEventResponse event : eventList) {
                    OrgInfoChangeDTO orgInfoChangeDTO=new OrgInfoChangeDTO();
                    orgInfoChangeDTO.setSerialNum(event.number.intValue());
                    orgInfoChangeDTO.setOrgName(new String(event.orgInfoStr.get(0).getValue()));
                    orgInfoChangeDTO.setOrgCreditCode(new String(event.orgInfoStr.get(1).getValue()));
                    orgInfoChangeDTO.setLegalPerson(new String(event.orgInfoStr.get(2).getValue()));
                    orgInfoChangeDTO.setLegalIdNumber(new String(event.orgInfoStr.get(3).getValue()));
                    orgInfoChangeDTO.setRegisteredAddress(new String(event.orgInfoStr.get(4).getValue()));
                    orgInfoChangeDTO.setRegistryDate(new String(event.orgInfoStr.get(5).getValue()));
                    orgInfoChangeDTO.setOfficeAddress(new String(event.orgInfoStr.get(6).getValue()));
                    orgInfoChangeDTO.setContactNumber(new String(event.orgInfoStr.get(7).getValue()));
                    orgInfoChangeDTO.setCredType(Integer.parseInt(new String(event.orgInfoStr.get(8).getValue())));
                    orgInfoChangeDTO.setOrgType(Integer.parseInt(new String(event.orgInfoStr.get(9).getValue())));
                    orgInfoChangeDTO.setRegisteredCapital(Integer.parseInt(new String(event.orgInfoStr.get(10).getValue())));
                    orgInfoChangeDTO.setPaidInCapital(Integer.parseInt(new String(event.orgInfoStr.get(11).getValue())));
                    orgInfoChangeDTO.setShareholdersNumber(Integer.parseInt(new String(event.orgInfoStr.get(12).getValue())));
                    orgInfoChangeDTO.setLegalIdType(Integer.parseInt(new String(event.orgInfoStr.get(13).getValue())));
                    orgInfoChangeDTOList.add(orgInfoChangeDTO);
                }
            }
        }
    }


    private Organization getOrgContract(String orgCreditCode)throws Exception {
        String orgAddress=governor.getOrgAddress(orgCreditCode.getBytes());
        if(SysConstant.EMPTY_ADDRESS.equals(orgAddress)||"".equals(orgAddress)||orgAddress==null){
            throw new ContractBaseException(ErrorCode.LOAD_CONTRACT_ERROR.getCode(),ErrorCode.LOAD_CONTRACT_ERROR.getErrMsg());
        }
        Organization organization=this.getContract(orgAddress,Organization.class);
        return  organization;
    }
}
