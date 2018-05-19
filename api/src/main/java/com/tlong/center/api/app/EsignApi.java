package com.tlong.center.api.app;

import com.tlong.center.api.dto.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

@Api("E签宝接口")
public interface EsignApi {

    @ApiOperation("E签宝个人认证1")
    @PostMapping("/approvePersonStep1")
    Result approvePersonStep1(Long peopleid, String cardno, String mobile, String name, String idno);

    @ApiOperation("E签宝个人认证2")
    @PostMapping("/approvePersonStep2")
    Result approvePersonStep2(Long peopleid,String cardno, String mobile, String name, String idno,String code);

    @ApiOperation("E签宝企业认证1")
    @PostMapping("/approveCompanyStep1")
    Result approveCompanyStep1(Long peopleid,String companyname, String codeorg, String codeusc, String legalname,String legalidno,
                               String name,String cardno,String subbranch,String bank,String province,String city);

    @ApiOperation("E签宝企业认证2")
    @PostMapping("/approveCompanyStep2")
    Result approveCompanyStep2(Long peopleid,String cash);
}
