package com.cstc.stockregister.controller;

import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.constant.SysConstant;
import com.cstc.stockregister.entity.OrganizationDTO;
import com.cstc.stockregister.entity.StockAssetDTO;
import com.cstc.stockregister.exception.ContractBaseException;
import com.cstc.stockregister.response.ResponseData;
import com.cstc.stockregister.service.AccountService;
import com.cstc.stockregister.service.GovernorService;
import com.cstc.stockregister.service.OrganizationService;
import com.cstc.stockregister.service.StockAssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("/rest/governor")
@Slf4j
@Api(value="合约治理",tags="合约治理（15个API接口）")
public class GovernorController {

    @Autowired
    private GovernorService governorService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private StockAssetService stockAssetService;

    @ApiOperation("创建账户信息")
    @RequestMapping(value="createAccount",method= RequestMethod.POST)
    public ResponseData<String> createAccount(@RequestParam(name = "externalAccount") @ApiParam(value = "区块链账户的地址") String externalAccount){
        ResponseData<String> responseData=new ResponseData<>();
        String responseContract=null;
        try {
            if(accountService.hasAccount(externalAccount)){
                responseData.setResultCode(ErrorCode.ACCOUNT_EXIST.getCode());
                responseData.setErrorMessage(ErrorCode.ACCOUNT_EXIST.getErrMsg());
                return responseData;
            }else{
                responseContract=governorService.createAccount(externalAccount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
        responseData.setResult(responseContract);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation("创建企业信息合约")
    @PostMapping("/createOrganization")
    public ResponseData<String> createOrganization(@RequestBody @ApiParam(value = "企业信息") OrganizationDTO orgInfo){
        ResponseData<String> responseData=new ResponseData<>();
        String responseContract;
        List<String> orgInfoStr=new ArrayList<>();
        orgInfoStr.add(orgInfo.getOrgName());
        orgInfoStr.add(orgInfo.getOrgCreditCode());
        orgInfoStr.add(orgInfo.getLegalPerson());
        orgInfoStr.add(orgInfo.getLegalIdNumber());
        orgInfoStr.add(orgInfo.getRegisteredAddress());
        orgInfoStr.add(orgInfo.getRegistryDate());
        orgInfoStr.add(orgInfo.getOfficeAddress());
        orgInfoStr.add(orgInfo.getContactNumber());
        orgInfoStr.add(String.valueOf(orgInfo.getCredType()));
        orgInfoStr.add(String.valueOf(orgInfo.getOrgType()));
        orgInfoStr.add(String.valueOf(orgInfo.getRegisteredCapital()));
        orgInfoStr.add(String.valueOf(orgInfo.getPaidInCapital()));
        orgInfoStr.add(String.valueOf(orgInfo.getShareholdersNumber()));
        orgInfoStr.add(String.valueOf(orgInfo.getLegalIdType()));

        try {
            String orgAddress=governorService.getOrgAddress(orgInfo.getOrgCreditCode());
            if(SysConstant.EMPTY_ADDRESS.equalsIgnoreCase(orgAddress)){
                responseContract=governorService.createOrganization(orgInfoStr);
            }else{
                responseData.setResultCode(ErrorCode.COMPANY_CODE_EXIST.getCode());
                responseData.setErrorMessage(ErrorCode.COMPANY_CODE_EXIST.getErrMsg());
                return responseData;
            }

        }  catch (ContractBaseException e) {
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
        responseData.setResult(responseContract);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation("创建股权合约")
    @PostMapping("/createStockAsset")
    public ResponseData<String> createStockAsset(@RequestBody @ApiParam(value = "企业股权信息") StockAssetDTO stockInfo){
        ResponseData<String> responseData=new ResponseData<>();
        List<String> inDataStr=new ArrayList<>();
        inDataStr.add(stockInfo.getAssetCode());
        inDataStr.add(stockInfo.getAssetName());
        List<BigInteger> inDataUint=new ArrayList<>();
        inDataUint.add(BigInteger.valueOf(stockInfo.getTotalBalances()));
        inDataUint.add(BigInteger.valueOf(stockInfo.getSalesRatio()));
        inDataUint.add(BigInteger.valueOf(stockInfo.getTradeRestrictDay()));
        String responseContract=null;
        try {
            String stockAssetAddress=governorService.getStockAssetAddress(stockInfo.getAssetCode());
            if(SysConstant.EMPTY_ADDRESS.equalsIgnoreCase(stockAssetAddress)){
                responseContract=governorService.createStockAsset(stockInfo.getOrgCreditCode(),inDataStr,inDataUint);
            }else{
                responseData.setResultCode(ErrorCode.STOCK_CODE_EXIST.getCode());
                responseData.setErrorMessage(ErrorCode.STOCK_CODE_EXIST.getErrMsg());
                return responseData;
            }
        }  catch (ContractBaseException e) {
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
        responseData.setResult(responseContract);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }

    @ApiOperation("根据企业信用代码获取企业合约地址")
    @RequestMapping(value="getOrgAddress/{orgCreditCode}",method=RequestMethod.GET)
    public ResponseData<String> getOrgAddress(@PathVariable @ApiParam(value = "企业统一信用代码") String orgCreditCode){
        ResponseData<String> responseData=new ResponseData<>();
        try {
            String result=governorService.getOrgAddress(orgCreditCode);
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
    }

    @ApiOperation("根据股权代码获取股权合约地址")
    @RequestMapping(value="getStockAssetAddress/{assetCode}",method=RequestMethod.GET)
    public ResponseData<String> getStockAssetAddress(@PathVariable @ApiParam(value = "股权代码") String assetCode){
        ResponseData<String> responseData=new ResponseData<>();
        try {
            String result=governorService.getStockAssetAddress(assetCode);
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseData.setResultCode(ErrorCode.REQUEST_FAIL.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_FAIL.getErrMsg());
            return responseData;
        }
    }

    @ApiOperation("变更企业合约所属的治理合约地址")
    @RequestMapping(value="changeGovernorOfOrganization",method= RequestMethod.POST)
    public ResponseData<String> changeGovernorOfOrganization(@RequestParam(name = "orgCreditCode") @ApiParam(value = "企业统一信用代码") String orgCreditCode,
                                                             @RequestParam(name = "newGovernor") @ApiParam(value = "新的治理合约地址") String newGovernor){
        ResponseData<String> responseData=new ResponseData<>();
        try {
            String result=governorService.updateGovernorOfOrganization(orgCreditCode,newGovernor);
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
            return responseData;
        }  catch (ContractBaseException e) {
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
    }
    @ApiOperation("变更企业合约的创建者账户地址")
    @RequestMapping(value="changeCreatorOfOrganization",method= RequestMethod.POST)
    public ResponseData<String> changeCreatorOfOrganization(@RequestParam(name = "orgCreditCode") @ApiParam(value = "企业统一信用代码") String orgCreditCode,
                                                             @RequestParam(name = "newCreator") @ApiParam(value = "新的创建者账户地址") String newCreator){
        ResponseData<String> responseData=new ResponseData<>();
        try {
            String result=governorService.updateCreatorOfOrganization(orgCreditCode,newCreator);
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
            return responseData;
        }  catch (ContractBaseException e) {
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
    }
    @ApiOperation("变更股权合约所属的治理合约地址")
    @RequestMapping(value="changeGovernorOfStockAsset",method= RequestMethod.POST)
    public ResponseData<String> changeGovernorOfStockAsset(@RequestParam(name = "assetCode") @ApiParam(value = "股权代码") String assetCode,
                                                            @RequestParam(name = "newGovernor") @ApiParam(value = "新的治理合约地址") String newGovernor){
        ResponseData<String> responseData=new ResponseData<>();
        try {
            String result=governorService.updateGovernorOfStockAsset(assetCode,newGovernor);
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
            return responseData;
        }  catch (ContractBaseException e) {
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
    }
    @ApiOperation("变更股权合约的创建者账户地址")
    @RequestMapping(value="changeCreatorOfStockAsset",method= RequestMethod.POST)
    public ResponseData<String> changeCreatorOfStockAsset(@RequestParam(name = "assetCode") @ApiParam(value = "股权代码") String assetCode,
                                                           @RequestParam(name = "newCreator") @ApiParam(value = "新的创建者账户地址") String newCreator){
        ResponseData<String> responseData=new ResponseData<>();
        try {
            String result=governorService.updateCreatorOfStockAsset(assetCode,newCreator);
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
            return responseData;
        }  catch (ContractBaseException e) {
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
    }

    @ApiOperation("更新企业合约的状态")
    @RequestMapping(value="updateOrgContractStatus",method= RequestMethod.POST)
    public ResponseData<Boolean> updateOrgContractStatus(@RequestParam(name = "orgCreditCode") @ApiParam(value = "企业统一信用代码") String orgCreditCode,
                                                          @RequestParam(name = "newStatus") @ApiParam(value = "新的合约状态（0-正常，1-停用）") int newStatus){
        ResponseData<Boolean> responseData=new ResponseData<>();
        try {
            boolean result=governorService.updateOrgContractStatus(orgCreditCode,newStatus);
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
            return responseData;
        }  catch (ContractBaseException e) {
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
    }
    @ApiOperation("更新股权合约的状态")
    @RequestMapping(value="updateStockContractStatus",method= RequestMethod.POST)
    public ResponseData<Boolean> updateStockContractStatus(@RequestParam(name = "assetCode") @ApiParam(value = "股权代码") String assetCode,
                                                        @RequestParam(name = "newStatus") @ApiParam(value = "新的合约状态（0-正常，1-停用）") int newStatus){
        ResponseData<Boolean> responseData=new ResponseData<>();
        try {
            boolean result=governorService.updateStockContractStatus(assetCode,newStatus);
            responseData.setResult(result);
            responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
            responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
            return responseData;
        }  catch (ContractBaseException e) {
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
    }
    @ApiOperation("获取企业合约所属的治理合约地址")
    @RequestMapping(value="getGovernorOfOrganization/{orgCreditCode}",method=RequestMethod.GET)
    public ResponseData<String> getGovernorOfOrganization(@PathVariable @ApiParam(value = "企业统一信用代码") String orgCreditCode){
        ResponseData<String> responseData=new ResponseData<>();
        String result;
        try {
            result=organizationService.getGovernor(orgCreditCode);
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
    @ApiOperation("获取企业合约的创建者地址")
    @RequestMapping(value="getCreatorOfOrganization/{orgCreditCode}",method=RequestMethod.GET)
    public ResponseData<String> getCreatorOfOrganization(@PathVariable @ApiParam(value = "企业统一信用代码") String orgCreditCode){
        ResponseData<String> responseData=new ResponseData<>();
        String result;
        try {
            result=organizationService.getCreator(orgCreditCode);
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

    @ApiOperation("获取股权合约所属的治理合约地址")
    @RequestMapping(value="getGovernorOfStockAsset/{assetCode}",method=RequestMethod.GET)
    public ResponseData<String> getGovernorOfStockAsset(@PathVariable @ApiParam(value = "股权代码") String assetCode){
        ResponseData<String> responseData=new ResponseData<>();
        String result;
        try {
            result=stockAssetService.getGovernor(assetCode);
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

    @ApiOperation("获取股权合约的创建者地址")
    @RequestMapping(value="getCreatorOfStockAsset/{assetCode}",method=RequestMethod.GET)
    public ResponseData<String> getCreatorOfStockAsset(@PathVariable @ApiParam(value = "股权代码") String assetCode){
        ResponseData<String> responseData=new ResponseData<>();
        String result;
        try {
            result=stockAssetService.getCreator(assetCode);
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

}
