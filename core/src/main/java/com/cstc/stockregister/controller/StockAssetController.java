package com.cstc.stockregister.controller;

import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.constant.SysConstant;
import com.cstc.stockregister.entity.*;
import com.cstc.stockregister.exception.ContractBaseException;
import com.cstc.stockregister.response.ResponseData;
import com.cstc.stockregister.service.AccountService;
import com.cstc.stockregister.service.StockAssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/stockAsset")
@Slf4j
@Api(value="股权登记管理API",tags="股权登记管理API（19个）")
public class StockAssetController {

    @Autowired
    private StockAssetService stockAssetService;

    @Autowired
    private AccountService accountService;

    @ApiOperation("更新总股本")
    @RequestMapping(value="updateTotalBalance",method= RequestMethod.POST)
    public ResponseData<Boolean>  updateTotalBalance(@RequestParam(name = "stockCode") @ApiParam(value = "股权代码") String stockCode,
                                 @RequestParam(name = "totalBalances") @ApiParam(value = "新的总股本") int totalBalances){
        ResponseData<Boolean> responseData=new ResponseData<>();
        boolean result;
        try {
            result=stockAssetService.updateTotalSupply(stockCode, BigInteger.valueOf(totalBalances));
        }  catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
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
    @ApiOperation("获取企业发行的总股本")
    @RequestMapping(value="getTotalBalance/{stockCode}",method=RequestMethod.GET)
    public ResponseData<Integer> getTotalBalance(@PathVariable String stockCode){
        ResponseData<Integer> responseData=new ResponseData<>();
        BigInteger result;
        try {
            result=stockAssetService.totalSupply(stockCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        responseData.setResult(result.intValue());
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation("获取用户的持有某股权的登记信息")
    @RequestMapping(value="balanceByAccount/{stockCode}/{externalAccount}",method=RequestMethod.GET)
    public ResponseData<AccountBookDTO> balanceByAccount(@PathVariable String stockCode,@PathVariable String externalAccount){
        ResponseData<AccountBookDTO> responseData=new ResponseData<>();
        AccountBookDTO result;
        try {
            result=stockAssetService.balanceByAccount(stockCode,externalAccount);
        }  catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
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

    @ApiOperation("获取用户的持有某股权的交易冻结信息")
    @RequestMapping(value="getTransRestrictedDetailByAccount/{stockCode}/{externalAccount}",method=RequestMethod.GET)
    public ResponseData<List<TransactionDTO>> getTransRestrictedDetailByAccount(@PathVariable String stockCode,@PathVariable String externalAccount){
        ResponseData<List<TransactionDTO>> responseData=new ResponseData<>();
        List<TransactionDTO>  result;
        try {
            result=stockAssetService.getTransRestrictedDetailByAccount(stockCode,externalAccount);
        } catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        }  catch (Exception e) {
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

    @ApiOperation("根据质押编号获取用户的质押信息")
    @RequestMapping(value="getPledgeDetailByNumber/{stockCode}/{externalAccount}/{pledgeNumber}",method=RequestMethod.GET)
    public ResponseData<PledgeDTO> getPledgeDetailByNumber(@PathVariable String stockCode,@PathVariable String externalAccount,
                                             @PathVariable BigInteger pledgeNumber){
        ResponseData<PledgeDTO> responseData=new ResponseData<>();
        PledgeDTO result;
        try {
            result=stockAssetService.getPledgeDetailByNumber(stockCode,externalAccount,pledgeNumber);
            result.setPledgee(accountService.getExternalAccount(result.getPledgee()));
        } catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        }  catch (Exception e) {
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

    @ApiOperation("获取用户的持有某股权的质押信息")
    @RequestMapping(value="getPledgeDetailByAccount/{stockCode}/{externalAccount}",method=RequestMethod.GET)
    public ResponseData<List<PledgeDTO>> getPledgeDetailByAccount(@PathVariable String stockCode,@PathVariable String externalAccount){
        ResponseData<List<PledgeDTO>> responseData=new ResponseData<>();
        List<PledgeDTO>  result;
        try {
            result=stockAssetService.getPledgeDetailByAccount(stockCode,externalAccount);
            if(result.size()>0){
                for(PledgeDTO pledgeDTO:result){
                    pledgeDTO.setPledgor(accountService.getExternalAccount(pledgeDTO.getPledgor()));
                    pledgeDTO.setPledgee(accountService.getExternalAccount(pledgeDTO.getPledgee()));
                }
            }
        }  catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
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

    @ApiOperation("根据冻结编号获取用户的冻结信息")
    @RequestMapping(value="getFrozenDetailByNumber/{stockCode}/{externalAccount}/{frozenNumber}",method=RequestMethod.GET)
    public ResponseData<FrozenDTO> getFrozenDetailByNumber(@PathVariable String stockCode,@PathVariable String externalAccount,@PathVariable BigInteger frozenNumber){
        ResponseData<FrozenDTO> responseData=new ResponseData<>();
        FrozenDTO result;
        try {
            result=stockAssetService.getFrozenDetailByNumber(stockCode,externalAccount,frozenNumber);
        }  catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
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

    @ApiOperation("获取用户的持有某股权的冻结信息")
    @RequestMapping(value="getFrozenDetailByAccount/{stockCode}/{externalAccount}",method=RequestMethod.GET)
    public ResponseData<List<FrozenDTO>> getFrozenDetailByAccount(@PathVariable String stockCode,@PathVariable String externalAccount){
        ResponseData<List<FrozenDTO>> responseData=new ResponseData<>();
        List<FrozenDTO>  result;
        try {
            result=stockAssetService.getFrozenDetailByAccount(stockCode,externalAccount);
            if(result.size()>0){
                for(FrozenDTO frozenDTO:result){
                    frozenDTO.setAssetOwnerAddress(externalAccount);
                }
            }
        } catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        }  catch (Exception e) {
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

    @ApiOperation("获取指定冻结编号的下一个轮候冻结编号")
    @RequestMapping(value="getNextWaitingFrozenNumber/{stockCode}/{externalAccount}/{frozenNumber}",method=RequestMethod.GET)
    public ResponseData<Integer> getNextWaitingFrozenNumber(@PathVariable String stockCode,@PathVariable String externalAccount,@PathVariable BigInteger frozenNumber){
        ResponseData<Integer> responseData=new ResponseData<>();
        int result;
        try {
            result=stockAssetService.getNextWaitingFrozenNumber(stockCode,externalAccount,frozenNumber);
        }  catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
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

    @ApiOperation("转让（管理员使用）")
    @PostMapping("/transferByAdmin")
    public ResponseData<Integer> transferByAdmin(@RequestBody @ApiParam(value = "转让信息") TransferDTO transferDTO){
        ResponseData<Integer> responseData=new ResponseData<>();
        int result;
        try {
            result=stockAssetService.transferByAdmin(transferDTO.getStockCode(),transferDTO.getExternalFromAccount(),transferDTO.getExternaltoAccount(),BigInteger.valueOf(transferDTO.getAmount()),BigInteger.valueOf(transferDTO.getTransferType()));
        }  catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
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

    @ApiOperation("解除T+N交易限售")
    @PostMapping("/cancelTransRestrictions")
    public ResponseData<Integer> cancelTransRestrictions(@RequestBody @ApiParam(value = "解除交易限售的清单") TransRestrictionsDTO transRestrictionsDTO){
        ResponseData<Integer> responseData=new ResponseData<>();
        int result;
        try {
            result=stockAssetService.cancelTransRestrictions(transRestrictionsDTO.getStockCode(), transRestrictionsDTO.getExternalAccount(),transRestrictionsDTO.getTransNumber());
        }catch (ContractBaseException e){
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(e.getErrorMessage());
            return responseData;
        }catch (Exception e) {
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

    @ApiOperation("股权增资")
    @RequestMapping(value="incrBalance",method= RequestMethod.POST)
    public ResponseData<Integer> incrBalance(@RequestParam(name = "stockCode") @ApiParam(value = "企业股权代码") String stockCode,
                        @RequestParam(name = "externalAccount") @ApiParam(value = "增资账户地址") String externalAccount,
                        @RequestParam(name = "amount") @ApiParam(value = "增资金额") int amount,
                        @RequestParam(name = "isRSales") @ApiParam(value = "限售与否（0-不限售，1-限售）") int isRSales){
        ResponseData<Integer> responseData=new ResponseData<>();
        int result = 0;
        try {
            result=stockAssetService.incrBalance(stockCode,externalAccount, BigInteger.valueOf(amount),isRSales);
        }  catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
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

    @ApiOperation("股权减资")
    @RequestMapping(value="reduceBalance",method= RequestMethod.POST)
    public ResponseData<Integer> reduceBalance(@RequestParam(name = "stockCode") @ApiParam(value = "企业股权代码") String stockCode,
                                               @RequestParam(name = "externalAccount") @ApiParam(value = "增资账户地址") String externalAccount,
                                               @RequestParam(name = "amount") @ApiParam(value = "减资金额") int amount){
        ResponseData<Integer> responseData=new ResponseData<>();
        int result=0;
        try {
            result=stockAssetService.reduceBalance(stockCode,externalAccount, BigInteger.valueOf(amount));
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        } catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
        }catch (Exception e) {
            log.error("执行合约错误： "+e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
        }
        return responseData;
    }

    @ApiOperation("股权质押登记")
    @PostMapping("/doPledge")
    public ResponseData<Integer>  doPledge(@RequestBody @ApiParam(value = "质押登记信息") DoPledgeDTO doPledgeDTO){
        ResponseData<Integer> responseData=new ResponseData<>();
        List<BigInteger> balanceData=new ArrayList<>();
        List<String> pledgeDetail=new ArrayList<>();
        if(!this.checkPledgeAmount(doPledgeDTO.getTotalPledgeAmount(),doPledgeDTO.getTotalPledgeTransRestrictedSales(),doPledgeDTO.getTotalPledgeCirculate(),doPledgeDTO.getTotalPledgeRestrictedSales())){
            responseData.setResultCode(ErrorCode.REQUEST_PARAM_ERROR.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_PARAM_ERROR.getErrMsg());
            return responseData;
        }
        balanceData.add(BigInteger.valueOf(doPledgeDTO.getTotalPledgeAmount()));
        balanceData.add(BigInteger.valueOf(doPledgeDTO.getTotalPledgeTransRestrictedSales()));
        balanceData.add(BigInteger.valueOf(doPledgeDTO.getTotalPledgeCirculate()));
        balanceData.add(BigInteger.valueOf(doPledgeDTO.getTotalPledgeRestrictedSales()));
        balanceData.add(BigInteger.valueOf(doPledgeDTO.getTotalCanTransAmount()));

        pledgeDetail.add(doPledgeDTO.getPledgor());
        pledgeDetail.add(doPledgeDTO.getPledgee());
        pledgeDetail.add(String.valueOf(doPledgeDTO.getThisRequestAmount()));
        pledgeDetail.add(String.valueOf(doPledgeDTO.getThisFromCirculate()));
        pledgeDetail.add(String.valueOf(doPledgeDTO.getThisFromRestrictedSales()));
        pledgeDetail.add(doPledgeDTO.getReleaseTime());
        int result = 0;
        try {
            result=stockAssetService.doPledge(doPledgeDTO.getStockCode(),balanceData, pledgeDetail,doPledgeDTO.getCauseDesc());
        }   catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        }catch (Exception e) {
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

    @ApiOperation("股权解押登记")
    @PostMapping("/undoPledge")
    public ResponseData<Boolean> undoPledge(@RequestBody @ApiParam(value = "解押登记信息") UndoPledgeDTO undoPledgeDTO){
        ResponseData<Boolean> responseData=new ResponseData<>();
        List<BigInteger> balanceData=new ArrayList<>();
        if(!this.checkPledgeAmount(undoPledgeDTO.getTotalPledgeAmount(),undoPledgeDTO.getTotalPledgeTransRestrictedSales(),undoPledgeDTO.getTotalPledgeCirculate(),undoPledgeDTO.getTotalPledgeRestrictedSales())){
            responseData.setResultCode(ErrorCode.REQUEST_PARAM_ERROR.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_PARAM_ERROR.getErrMsg());
            return responseData;
        }
        balanceData.add(BigInteger.valueOf(undoPledgeDTO.getTotalPledgeAmount()));
        balanceData.add(BigInteger.valueOf(undoPledgeDTO.getTotalPledgeTransRestrictedSales()));
        balanceData.add(BigInteger.valueOf(undoPledgeDTO.getTotalPledgeCirculate()));
        balanceData.add(BigInteger.valueOf(undoPledgeDTO.getTotalPledgeRestrictedSales()));
        balanceData.add(BigInteger.valueOf(undoPledgeDTO.getTotalCanTransAmount()));
        boolean result = false;
        try {
            result=stockAssetService.undoPledge(undoPledgeDTO.getStockCode(),undoPledgeDTO.getPledgor(), balanceData, BigInteger.valueOf(undoPledgeDTO.getPledgeNumber()));
        } catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        }catch (Exception e) {
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

    @ApiOperation("股权冻结登记")
    @PostMapping("/doFrozen")
    public ResponseData<Integer>  doFrozen(@RequestBody @ApiParam(value = "冻结登记信息") DoFrozenDTO doFrozenDTO){
        ResponseData<Integer> responseData=new ResponseData<>();
        List<BigInteger> balanceData=new ArrayList<>();
        List<String> frozenDetail=new ArrayList<>();
        if(!this.checkFrozenAmount(doFrozenDTO.getTotalFreezeAmount(),doFrozenDTO.getTotalWaitingFreezeAmount(),doFrozenDTO.getTotalFrozenTransRestrictedSales(),doFrozenDTO.getTotalFrozenCirculate(),doFrozenDTO.getTotalFrozenRestrictedSales())){
            responseData.setResultCode(ErrorCode.REQUEST_PARAM_ERROR.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_PARAM_ERROR.getErrMsg());
            return responseData;
        }
        balanceData.add(BigInteger.valueOf(doFrozenDTO.getTotalFreezeAmount()));
        balanceData.add(BigInteger.valueOf(doFrozenDTO.getTotalWaitingFreezeAmount()));
        balanceData.add(BigInteger.valueOf(doFrozenDTO.getTotalFrozenTransRestrictedSales()));
        balanceData.add(BigInteger.valueOf(doFrozenDTO.getTotalFrozenCirculate()));
        balanceData.add(BigInteger.valueOf(doFrozenDTO.getTotalFrozenRestrictedSales()));
        balanceData.add(BigInteger.valueOf(doFrozenDTO.getTotalCanTransAmount()));

        frozenDetail.add(doFrozenDTO.getAssetOwnerAddress());
        frozenDetail.add(String.valueOf(doFrozenDTO.getBusinessType()));
        frozenDetail.add(doFrozenDTO.getApplicant());
        frozenDetail.add(String.valueOf(doFrozenDTO.getThisRequestAmount()));
        frozenDetail.add(String.valueOf(doFrozenDTO.getThisFrozenAmount()));
        frozenDetail.add(String.valueOf(doFrozenDTO.getThisWaitingAmount()));
        frozenDetail.add(doFrozenDTO.getStartTime());
        frozenDetail.add(doFrozenDTO.getEndTime());
        frozenDetail.add(doFrozenDTO.getWaitingNumber());
        frozenDetail.add(String.valueOf(doFrozenDTO.getStatus()));
        int result=0;
        try {
            result=stockAssetService.doFrozen(doFrozenDTO.getStockCode(),balanceData, frozenDetail);
        } catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        }catch (Exception e) {
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

    @ApiOperation("股权轮候冻结登记")
    @PostMapping("/doWaitingFrozen")
    public ResponseData<Boolean>  doWaitingFrozen(@RequestBody @ApiParam(value = "轮候冻结登记信息") DoWaitingFrozenDTO doWaitingFrozenDTO){
        ResponseData<Boolean> responseData=new ResponseData<>();
        List<BigInteger> balanceData=new ArrayList<>();
        if(!this.checkFrozenAmount(doWaitingFrozenDTO.getTotalFreezeAmount(),doWaitingFrozenDTO.getTotalWaitingFreezeAmount(),doWaitingFrozenDTO.getTotalFrozenTransRestrictedSales(),doWaitingFrozenDTO.getTotalFrozenCirculate(),doWaitingFrozenDTO.getTotalFrozenRestrictedSales())){
            responseData.setResultCode(ErrorCode.REQUEST_PARAM_ERROR.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_PARAM_ERROR.getErrMsg());
            return responseData;
        }
        balanceData.add(BigInteger.valueOf(doWaitingFrozenDTO.getTotalFreezeAmount()));
        balanceData.add(BigInteger.valueOf(doWaitingFrozenDTO.getTotalWaitingFreezeAmount()));
        balanceData.add(BigInteger.valueOf(doWaitingFrozenDTO.getTotalFrozenTransRestrictedSales()));
        balanceData.add(BigInteger.valueOf(doWaitingFrozenDTO.getTotalFrozenCirculate()));
        balanceData.add(BigInteger.valueOf(doWaitingFrozenDTO.getTotalFrozenRestrictedSales()));
        balanceData.add(BigInteger.valueOf(doWaitingFrozenDTO.getTotalCanTransAmount()));

        boolean result = false;
        try {
            result=stockAssetService.doWaitingFrozen(doWaitingFrozenDTO.getStockCode(),balanceData,doWaitingFrozenDTO.getAssetOwnerAddress(), doWaitingFrozenDTO.getSerialNum(),doWaitingFrozenDTO.getThisRequestAmount());
        }catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
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

    @ApiOperation("股权解冻登记")
    @PostMapping("/undoFrozen")
    public ResponseData<Integer> undoFrozen(@RequestBody @ApiParam(value = "解冻登记信息") UndoFrozenDTO undoFrozenDTO){
        ResponseData<Integer> responseData=new ResponseData<>();
        List<BigInteger> balanceData=new ArrayList<>();
        if(!this.checkFrozenAmount(undoFrozenDTO.getTotalFreezeAmount(),undoFrozenDTO.getTotalWaitingFreezeAmount(),undoFrozenDTO.getTotalFrozenTransRestrictedSales(),undoFrozenDTO.getTotalFrozenCirculate(),undoFrozenDTO.getTotalFrozenRestrictedSales())){
            responseData.setResultCode(ErrorCode.REQUEST_PARAM_ERROR.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_PARAM_ERROR.getErrMsg());
            return responseData;
        }
        balanceData.add(BigInteger.valueOf(undoFrozenDTO.getTotalFreezeAmount()));
        balanceData.add(BigInteger.valueOf(undoFrozenDTO.getTotalWaitingFreezeAmount()));
        balanceData.add(BigInteger.valueOf(undoFrozenDTO.getTotalFrozenTransRestrictedSales()));
        balanceData.add(BigInteger.valueOf(undoFrozenDTO.getTotalFrozenCirculate()));
        balanceData.add(BigInteger.valueOf(undoFrozenDTO.getTotalFrozenRestrictedSales()));
        balanceData.add(BigInteger.valueOf(undoFrozenDTO.getTotalCanTransAmount()));

        int result=0;
        try {
            result=stockAssetService.undoFrozen(undoFrozenDTO.getStockCode(),undoFrozenDTO.getAssetOwnerAddress(),balanceData, BigInteger.valueOf(undoFrozenDTO.getFrozenNumber()));
        } catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        }catch (Exception e) {
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

    @ApiOperation("获取用户的股权业务办理记录")
    @RequestMapping(value="getBusinessRecord/{assetCode}/{externalAccount}/{businessType}",method=RequestMethod.GET)
    public ResponseData<StockBusinessRecordDTO> getBusinessRecord(@PathVariable @ApiParam(value = "股权代码")  String assetCode,@PathVariable @ApiParam(value = "持仓账户地址") String externalAccount,
                                                                  @PathVariable @ApiParam(value = "业务类型：（0-所有业务，1-资本变更，2-交易记录，3-质押解押，4-冻结解冻）") int businessType){
        ResponseData<StockBusinessRecordDTO> responseData=new ResponseData<>();
        StockBusinessRecordDTO stockBusinessRecordDTO=new StockBusinessRecordDTO();
        try {
            String interAccount=accountService.getUserAccount(externalAccount);
            if(!SysConstant.EMPTY_ADDRESS.equals(interAccount)){
                stockBusinessRecordDTO=stockAssetService.getBusinessRecord(accountService,assetCode,externalAccount,businessType);
            }
        }catch (ContractBaseException e) {
            log.error("合约返回错误信息： "+e.getMessage());
            responseData.setResultCode(e.getErrorCode());
            responseData.setErrorMessage(e.getMessage());
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        responseData.setResult(stockBusinessRecordDTO);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    /**
     * 检查冻结金额是否正确：冻结总额=等待冻结总额+交易限售部分冻结总额+流通部分冻结总额+限售部分冻结总额
     * @param totalFreezeAmount 冻结总额
     * @param totalWaitingFreezeAmount 等待冻结总额
     * @param totalFrozenTransRestrictedSales 交易限售部分冻结总额
     * @param totalFrozenCirculate 流通部分冻结总额
     * @param totalFrozenRestrictedSales 限售部分冻结总额
     * @return
     */
    private boolean checkFrozenAmount(int totalFreezeAmount,int totalWaitingFreezeAmount,int totalFrozenTransRestrictedSales,
                                      int totalFrozenCirculate,int totalFrozenRestrictedSales){
        int newTotal=totalWaitingFreezeAmount+totalFrozenTransRestrictedSales+totalFrozenCirculate+totalFrozenRestrictedSales;
        if(totalFreezeAmount==newTotal){
            return true;
        }
        return false;
    }

    /**
     * 检查质押金额是否正确：质押总额 = 交易限售的质押总额+流通部分的质押总额 +限售部分的质押总额
     * @param totalPledgeAmount 质押总额
     * @param totalPledgeTransRestrictedSales 交易限售的质押总额
     * @param totalPledgeCirculate 流通部分的质押总额
     * @param totalPledgeRestrictedSales 限售部分的质押总额
     * @return
     */
    private boolean checkPledgeAmount(int totalPledgeAmount,int totalPledgeTransRestrictedSales,int totalPledgeCirculate,
                                      int totalPledgeRestrictedSales){

        int newTotal=totalPledgeTransRestrictedSales+totalPledgeCirculate+totalPledgeRestrictedSales;
        if(totalPledgeAmount==newTotal){
            return true;
        }
        return false;
    }
}
