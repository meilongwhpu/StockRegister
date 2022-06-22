package com.cstc.stockregister.controller;

import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.entity.Book;
import com.cstc.stockregister.response.ResponseData;
import com.cstc.stockregister.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/account")
@Slf4j
@Api(value="账户信息管理API",tags="账户信息管理API（5个）")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @ApiOperation("获取外部账户映射的内部账户地址")
    @RequestMapping(value="getUserAccount/{externalAccount}",method=RequestMethod.GET)
    public ResponseData<String> getUserAccount(@PathVariable @ApiParam(value = "外部账户地址") String externalAccount){
        ResponseData<String> responseData=new ResponseData<>();
        String result;
        try {
            result=accountService.getUserAccount(externalAccount);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        responseData.setResult(result);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation("更新外部账户地址的映射（仅限治理者使用）")
    @RequestMapping(value="setExternalAccountByGovernance",method= RequestMethod.POST)
    public ResponseData<Boolean> setExternalAccountByGovernance(@RequestParam(name = "newExternalAccount") @ApiParam(value = "新区块链账户地址") String newExternalAccount,
                                                 @RequestParam(name = "oldExternalAccount") @ApiParam(value = "旧区块链账户地址") String oldExternalAccount){
        ResponseData<Boolean> responseData=new ResponseData<>();
        boolean result;
        try {
            result=accountService.setExternalAccountByGovernance(newExternalAccount,oldExternalAccount);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        responseData.setResult(result);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation("更新外部账户地址的映射（仅限账户自己使用）")
    @RequestMapping(value="setExternalAccountByUser",method= RequestMethod.POST)
    public ResponseData<Boolean>  setExternalAccountByUser(@RequestParam(name = "senderAddress") @ApiParam(value = "外部账户地址") String senderAddress,
                                            @RequestParam(name = "newExternalAccount") @ApiParam(value = "新区块链账户地址") String newExternalAccount){
        ResponseData<Boolean> responseData=new ResponseData<>();
        boolean result;
        try {
            result=accountService.setExternalAccountByUser(senderAddress,newExternalAccount);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        responseData.setResult(result);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation("设置账户持有的股权合约地址")
    @RequestMapping(value="addStockAsset",method= RequestMethod.POST)
    public ResponseData<Boolean> addStockAsset(@RequestParam(name = "externalAccount") @ApiParam(value = "外部账户地址") String externalAccount,
                                 @RequestParam(name = "stockAssetCode") @ApiParam(value = "股权代码") String stockAssetCode,
                                 @RequestParam(name = "assetAddress") @ApiParam(value = "股权登记合约地址") String assetAddress){
        ResponseData<Boolean> responseData=new ResponseData<>();
        boolean result;
        try {
            result=accountService.addStockAsset(externalAccount,stockAssetCode,assetAddress);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        responseData.setResult(result);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation("根据股权代码获取外部账户持有的股权登记合约地址")
    @RequestMapping(value="getStockAssets/{externalAccount}/{stockAssetCode}",method=RequestMethod.GET)
    public ResponseData<String> getStockAssets(@PathVariable @ApiParam(value = "外部账户地址") String externalAccount,@PathVariable @ApiParam(value = "股权代码") String stockAssetCode){
        ResponseData<String> responseData=new ResponseData<>();
        String result;
        try {
            result=accountService.getStockAssets(externalAccount,stockAssetCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        }
        responseData.setResult(result);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

}
