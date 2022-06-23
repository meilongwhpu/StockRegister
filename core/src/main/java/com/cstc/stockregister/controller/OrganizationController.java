package com.cstc.stockregister.controller;

import com.cstc.stockregister.constant.ErrorCode;
import com.cstc.stockregister.entity.ManagerAddressDTO;
import com.cstc.stockregister.entity.OrgInfoChangeDTO;
import com.cstc.stockregister.entity.OrganizationDTO;
import com.cstc.stockregister.exception.ContractBaseException;
import com.cstc.stockregister.response.ResponseData;
import com.cstc.stockregister.service.AccountService;
import com.cstc.stockregister.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/organization")
@Slf4j
@Api(value="企业信息管理",tags="企业信息管理（7个API接口）")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @ApiOperation("更新企业信息")
    @PostMapping("/updateOrgInfo")
    public ResponseData<Integer> updateOrgInfo(@RequestBody @ApiParam(value = "企业信息") OrganizationDTO orgInfo){
        ResponseData<Integer> responseData=new ResponseData<>();
        int result = 0;
        String orgCreditCode=orgInfo.getOrgCreditCode();
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
            result=organizationService.updateOrgInfo(orgCreditCode, orgInfoStr);
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

    @ApiOperation("获取企业信息")
    @RequestMapping(value="getOrgInfo/{orgCreditCode}",method=RequestMethod.GET)
    public ResponseData<OrganizationDTO> getOrgInfo(@PathVariable @ApiParam(value = "企业统一信用代码") String orgCreditCode){
        ResponseData<OrganizationDTO> responseData=new ResponseData<>();
        OrganizationDTO result=null;
        try {
            result=organizationService.getOrgInfo(orgCreditCode);
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

    @ApiOperation("设置企业发行的股权合约地址")
    @RequestMapping(value="setStockAsset",method= RequestMethod.POST)
    public ResponseData<Boolean> setStockAsset(@RequestParam(name = "orgCreditCode") @ApiParam(value = "企业统一信用代码") String orgCreditCode,
                                @RequestParam(name = "stockAssetAddress") @ApiParam(value = "企业注册的股权合约地址") String stockAssetAddress){
        ResponseData<Boolean> responseData=new ResponseData<>();
        boolean result = false;
        try {
            result=organizationService.setStockAsset(orgCreditCode, stockAssetAddress);
        }catch (ContractBaseException e) {
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

    @ApiOperation("添加董监高的账户地址")
    @PostMapping("/addManager")
    public ResponseData<Integer> addManager(@RequestBody @ApiParam(value = "董监高的账户地址信息") ManagerAddressDTO managerAddressDTO){
        ResponseData<Integer> responseData=new ResponseData<>();
        int result = 0;
        try {
            result=organizationService.addManager(managerAddressDTO.getOrgCreditCode(), managerAddressDTO.getManagers());
        }catch (ContractBaseException e) {
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

    @ApiOperation("删除董监高的账户地址")
    @RequestMapping(value="deleteManager",method= RequestMethod.POST)
    public ResponseData<Boolean>  deleteManager(@RequestParam(name = "orgCreditCode") @ApiParam(value = "企业统一信用代码") String orgCreditCode,
                                 @RequestParam(name = "managerAddress") @ApiParam(value = "董监高的账户地址") String managerAddress){
        ResponseData<Boolean> responseData=new ResponseData<>();
        boolean result = false;
        try {
            result=organizationService.deleteManager(orgCreditCode, managerAddress);
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
    @ApiOperation("查询企业的董监高名单")
    @RequestMapping(value="getManager/{orgCreditCode}",method=RequestMethod.GET)
    public ResponseData<List<String>> getManager(@PathVariable @ApiParam(value = "企业统一信用代码") String orgCreditCode){
        ResponseData<List<String>> responseData=new ResponseData<>();
        List<String> result;
        try {
            result=organizationService.getManager(orgCreditCode);
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


    @ApiOperation("获取企业信息变更记录")
    @RequestMapping(value="getOrgInfoChangeRecord/{orgCreditCode}",method=RequestMethod.GET)
    public ResponseData<List<OrgInfoChangeDTO>> getOrgInfoChangeRecord(@PathVariable @ApiParam(value = "企业统一信用代码") String orgCreditCode) {
        ResponseData<List<OrgInfoChangeDTO>> responseData=new ResponseData<>();
        List<OrgInfoChangeDTO> orgInfoChangeDTOList = new ArrayList<>();
        try {
            orgInfoChangeDTOList=organizationService.getOrgInfoChangeRecord(orgCreditCode);
        } catch (ContractBaseException e) {
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
        responseData.setResult(orgInfoChangeDTOList);
        responseData.setResultCode(ErrorCode.REQUEST_SUCCESS.getCode());
        responseData.setErrorMessage(ErrorCode.REQUEST_SUCCESS.getErrMsg());
        return responseData;
    }


}
