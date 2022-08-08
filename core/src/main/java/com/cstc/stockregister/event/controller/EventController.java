package com.cstc.stockregister.event.controller;

import com.cstc.stockregister.event.EventService;
import com.cstc.stockregister.event.entity.ContractEventInfo;
import com.cstc.stockregister.event.entity.ReqContractEventRegister;
import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.event.entity.ReqUnregister;
import com.cstc.stockregister.response.ResponseData;
import com.cstc.stockregister.util.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.utils.AddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/event")
@Slf4j
@Api(value = "订阅合约事件", tags = "event push manage controller")
public class EventController {

    @Autowired
    private EventService eventService;

    @ApiOperation(value = "registerContractEvent",
            notes = "register contract event callback and push message to mq")
    @PostMapping("contractEvent")
    public ResponseData<String> registerContractEvent(@Valid @RequestBody  @ApiParam(value = "注册合约事件DTO") ReqContractEventRegister reqContractEventRegister){
        log.debug("start registerContractEvent. {}", reqContractEventRegister);
        ResponseData<String> responseData=new ResponseData<>();
        int groupId = reqContractEventRegister.getGroupId();
        String appId = reqContractEventRegister.getAppId();
        if (!CommonUtils.isLetterDigit(appId)) {
            responseData.setResultCode(ErrorCode.PARAM_INVALID_LETTER_DIGIT.getCode());
            responseData.setErrorMessage(ErrorCode.PARAM_INVALID_LETTER_DIGIT.getErrMsg());
            return responseData;
        }
        String fromBlock = reqContractEventRegister.getFromBlock();
        String toBlock = reqContractEventRegister.getToBlock();
        // 0 < fromBlock <= toBlock, latest means latest block
        if ("0".equals(fromBlock) || "0".equals(toBlock)) {
            responseData.setResultCode(ErrorCode.BLOCK_RANGE_PARAM_INVALID.getCode());
            responseData.setErrorMessage(ErrorCode.BLOCK_RANGE_PARAM_INVALID.getErrMsg());
            return responseData;
        }
        if ("latest".equals(fromBlock) && !"latest".equals(toBlock)) {
            responseData.setResultCode(ErrorCode.BLOCK_RANGE_PARAM_INVALID.getCode());
            responseData.setErrorMessage(ErrorCode.BLOCK_RANGE_PARAM_INVALID.getErrMsg());
            return responseData;
        }
        if (!"latest".equals(fromBlock) && !"latest".equals(toBlock) &&
                Integer.parseInt(toBlock) < Integer.parseInt(fromBlock)) {
            responseData.setResultCode(ErrorCode.BLOCK_RANGE_PARAM_INVALID.getCode());
            responseData.setErrorMessage(ErrorCode.BLOCK_RANGE_PARAM_INVALID.getErrMsg());
            return responseData;
        }
        String contractAddress = reqContractEventRegister.getContractAddress();

        if (contractAddress!=null && !"".equals(contractAddress) && !AddressUtils.isValidAddress(contractAddress)) {
            responseData.setResultCode(ErrorCode.PARAM_ADDRESS_IS_INVALID.getCode());
            responseData.setErrorMessage(ErrorCode.PARAM_ADDRESS_IS_INVALID.getErrMsg());
            return responseData;
        }

        List<String> topicList = reqContractEventRegister.getTopicList();
        String exchangeName = reqContractEventRegister.getExchangeName();
        // username as queue name
        String queueName = reqContractEventRegister.getQueueName();
        try {
            // register contract event log push in service
            eventService.registerContractEvent(appId, groupId,
                    exchangeName, queueName, fromBlock, toBlock,
                    contractAddress, topicList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        log.debug("end registerContractEvent. ");
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation(value = "unregisterContractEvent",
            notes = "unregister contract event")
    @ApiImplicitParam(name = "reqUnregister", value = "注册出块通知所需配置与数据表的id值",
            required = true, dataType = "ReqContractEventRegister")
    @DeleteMapping("contractEvent")
    public ResponseData<Boolean> unregisterContractEvent(
            @Valid @RequestBody ReqUnregister reqUnregister) {
        log.debug("start unregisterContractEvent reqUnregister. {}", reqUnregister);
        ResponseData<Boolean> responseData=new ResponseData<>();
        String infoId = reqUnregister.getId();
        String appId = reqUnregister.getAppId();
        int groupId = reqUnregister.getGroupId();
        String exchangeName = reqUnregister.getExchangeName();
        // username as queue name
        String queueName = reqUnregister.getQueueName();
        try {
            eventService.unregisterContractEvent(infoId, appId, groupId,
                    exchangeName, queueName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        log.debug("end unregisterContractEvent. ");
        responseData.setResult(true);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation(value = "getContractEventInfo",
            notes = "get registered contract event info")
    @GetMapping(value = {"contractEvent/list/{groupId}"})
    public ResponseData<List<ContractEventInfo>> getContractEventInfo(@PathVariable("groupId") Integer groupId) {
        List<ContractEventInfo> resList=eventService.getContractEventInfoList(groupId);
        ResponseData<List<ContractEventInfo>> responseData=new ResponseData<>();
        responseData.setResult(resList);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

}
