package com.tlong.center.app;

import com.tlong.center.api.app.EsignApi;
import com.tlong.center.api.dto.Result;
import com.tlong.center.service.EsignService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/esign")
public class EsignAllController implements EsignApi{

    final EsignService esignService;

    public EsignAllController(EsignService esignService) {
        this.esignService = esignService;
    }

    /**
     * 个人认证接口第1步，发送验证码
     * @param peopleid	人员id
     * @param cardno	银行卡号
     * @param mobile	手机号
     * @param name	姓名
     * @param idno	身份证号
     * @return
     */
    @Override
    @RequestMapping(value = "/approvePersonStep1")
    public Result approvePersonStep1(@RequestParam Long peopleid, @RequestParam String cardno, @RequestParam String mobile,
                                     @RequestParam String name, @RequestParam String idno) {
        return esignService.approvePersonStep1( peopleid,cardno,  mobile,  name,  idno);
    }



    /**
     * 个人认证接口第2步，验证验证码
     * @param peopleid	人员id
     * @param cardno	银行卡号
     * @param mobile	手机号
     * @param name	姓名
     * @param idno	身份证号
     * @param code	验证码
     * @return
     */
    @Override
    @RequestMapping(value = "/approvePersonStep2")
    public Result approvePersonStep2(@RequestParam Long peopleid,@RequestParam String cardno, @RequestParam String mobile,
                                     @RequestParam String name, @RequestParam String idno, @RequestParam String code) {
        return esignService.approvePersonStep2( peopleid,cardno,  mobile,  name,  idno, code);
    }

    /**
     * 企业认证接口第1步，验证企业真实性，成功后打款到对公账户
     * @param peopleid 人员id
     * @param companyname	企业名
     * @param codeorg	组织机构代码
     * @param codeusc	社会统一信用代码
     * @param legalname	法人姓名
     * @param legalidno	法人身份证
     * @param name	对公账户户名（一般来说即企业名称）
     * @param cardno	企业对公银行账号
     * @param subbranch	企业银行账号开户行支行全称
     * @param bank	企业银行账号开户行名称
//     * @param provice	企业银行账号开户行所在省份
     * @param city	企业银行账号开户行所在城市
     * @return
     */
    @RequestMapping(value = "/approveCompanyStep1")
    public Result approveCompanyStep1(@RequestParam Long peopleid,@RequestParam String companyname,
                                      @RequestParam String codeorg, @RequestParam String codeusc,
                                      @RequestParam String legalname,String legalidno,
                                      @RequestParam String name, @RequestParam String cardno, @RequestParam String subbranch,
                                      @RequestParam String bank, @RequestParam String province, @RequestParam String city) {

        return esignService.approveCompanyStep1( peopleid, companyname,  codeorg,  codeusc,  legalname, legalidno,
                name, cardno, subbranch, bank, province, city);
    }

    /**
     * 企业认证接口第2步，验证打款金额是否正确
     * @param peopleid 人员id
     * @param cash	打款金额
     * @return
     */
    @RequestMapping(value = "/approveCompanyStep2")
    public Result approveCompanyStep2(@RequestParam Long peopleid, @RequestParam String cash) {
        return esignService.approveCompanyStep2( peopleid, cash);
    }

    /**
     * 合同签署
     */
    @RequestMapping(value = "/signContract")
    public Result eSign(@RequestParam String userid) {
        return esignService.eSign(userid);
    }

}
