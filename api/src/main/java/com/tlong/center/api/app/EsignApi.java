package com.tlong.center.api.app;

import com.tlong.center.api.dto.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api("E签宝接口")
public interface EsignApi {

    @ApiOperation("E签宝个人认证第一步Api")
    @PostMapping("/approvePersonStep1")
    Result approvePersonStep1(@RequestParam Long peopleid, @RequestParam String cardno, @RequestParam String mobile,
                              @RequestParam String name, @RequestParam String idno);

    @ApiOperation("E签宝个人认证第二步Api")
    @PostMapping("/approvePersonStep2")
    Result approvePersonStep2(@RequestParam Long peopleid,@RequestParam String cardno, @RequestParam String mobile,
                              @RequestParam String name, @RequestParam String idno, @RequestParam String code);

    @ApiOperation("E签宝企业认证第一步Api")
    @PostMapping("/approveCompanyStep1")
    Result approveCompanyStep1(@RequestParam Long peopleid,@RequestParam String companyname,
                               @RequestParam String codeorg, @RequestParam String codeusc,
                               @RequestParam String legalname,String legalidno,
                               @RequestParam String name, @RequestParam String cardno, @RequestParam String subbranch,
                               @RequestParam String bank, @RequestParam String province, @RequestParam String city);

    @ApiOperation("E签宝企业认证第二步Api")
    @PostMapping("/approveCompanyStep2")
    Result approveCompanyStep2(@RequestParam Long peopleid, @RequestParam String cash);


    @ApiOperation("合同签署Api")
    @PostMapping("/signContrac")
    Result eSign(@RequestParam String userid);
}
