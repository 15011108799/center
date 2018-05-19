package com.tlong.center.service;

import com.alibaba.fastjson.JSONObject;
import com.tlong.center.api.dto.Result;
import com.tlong.center.common.esignBean.*;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.esign.EsignCompany;
import com.tlong.center.domain.app.esign.EsignPerson;
import com.tlong.center.domain.app.esign.EsignRecordHistory;
import com.tlong.center.domain.repository.EsignCompanyRepository;
import com.tlong.center.domain.repository.EsignRecordHistoryRepository;
import com.tlong.center.domain.repository.EsignPersonRepository;
import com.tlong.center.domain.repository.TlongUserRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.tlong.center.domain.app.esign.QEsignPerson.esignPerson;
import static com.tlong.center.domain.app.esign.QEsignCompany.esignCompany;


@Component
@Transactional
public class EsignService {

    final EntityManager entityManager;
    final EsignRecordHistoryRepository esignRecordHistoryRepository;
    final EsignPersonRepository esignPersonRepository;
    final EsignCompanyRepository esignCompanyRepository;
    final TlongUserRepository tlongUserRepository;
    final ESignSignService eSignSignService;

    public EsignService(EntityManager entityManager, EsignRecordHistoryRepository esignRecordHistoryRepository, EsignPersonRepository esignPersonRepository, EsignCompanyRepository esignCompanyRepository, TlongUserRepository tlongUserRepository, ESignSignService eSignSignService) {
        this.entityManager = entityManager;
        this.esignRecordHistoryRepository = esignRecordHistoryRepository;
        this.esignPersonRepository = esignPersonRepository;
        this.esignCompanyRepository = esignCompanyRepository;
        this.tlongUserRepository = tlongUserRepository;
        this.eSignSignService = eSignSignService;
    }

    /**
     * 个人认证接口第1步，发送验证码
     */
    public Result approvePersonStep1(Long userId, String cardno, String mobile, String name,
                                     String idno) {
        Result r = new Result();
        try {
            if(userId==null||userId.equals("")||cardno==null||cardno.equals("")
                    ||mobile==null||mobile.equals("")||name==null||name.equals("")
                    ||idno==null||idno.equals("")){
                r.setFlag(0);
                r.setMsg("参数不完整");
                return r;
            }
            //根据参数拼对象
            PersonInfoValidVo personInfoValidVo = new PersonInfoValidVo();
            personInfoValidVo.setCardno(cardno);
            personInfoValidVo.setIdno(idno);
            personInfoValidVo.setMobile(mobile);
            personInfoValidVo.setName(name);

            //将传输记录到history表
            Long recordid=recordHistory("personInfoValidUrl",personInfoValidVo);

            //将参数传给e签宝，得到返回值字符串
            String response = ESignApiManager.postInterface(personInfoValidVo);
            //String response = "{\"errCode\":0,\"msg\":\"成功\",\"serviceId\":\"924459e6-5e33-41b0-bccf-3eb07*****\"}";

            //将传输返回值更新到history表
            updateHistory(recordid,response);

            //检查errCode，如果为0，则发送验证码成功，如果不为0，发送验证码失败
            JSONObject json =JSONObject.parseObject(response);
            if(json.get("errCode").toString().equals("0")){
                String serviceid=json.get("serviceId").toString();

                Iterable<EsignPerson> all = esignPersonRepository.findAll(esignPerson.userId.eq(userId));
                List<EsignPerson> esignUsers = null;
                if (all != null) {
                    esignUsers = ToListUtil.IterableToList(all);
                }
                if (esignUsers.size()>0){
                    esignUsers.stream().forEach(one ->{
                        one.setRealName(name);
                        one.setIdCard(idno);
                        one.setPhone(mobile);
                        one.setServiceId(serviceid);
                        one.setTime(new Date());
                    });
                    esignPersonRepository.save(esignUsers);
                }else {
                    EsignPerson one = new EsignPerson();
                    one.setUserId(userId);
                    one.setRealName(name);
                    one.setIdCard(idno);
                    one.setBank(cardno);
                    one.setPhone(mobile);
                    one.setServiceId(serviceid);
                    one.setTime(new Date());
                    esignPersonRepository.saveAndFlush(one);
                }
                r.setFlag(1);
                r.setMsg("验证码已发送");
            }else{
                //发送验证码失败
                r.setFlag(0);
                r.setMsg("发送验证码失败，"+json.get("errCode").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setFlag(0);
            r.setMsg("error");
        }

        return r;
    }


    /**
     * 个人认证接口第2步，验证验证码
     */
    public Result approvePersonStep2(Long userId,String cardno, String mobile, String name,
                                     String idno, String code) {
        Result r = new Result();

        try {
            Iterable<EsignPerson> all = esignPersonRepository.findAll(esignPerson.userId.eq(userId));
            List<EsignPerson> esignUsers = null;
            if (all != null){
                esignUsers = ToListUtil.IterableToList(all);
            }
            if (esignUsers.size()>0){
                String realname = esignUsers.get(0).getRealName();
                String idcard = esignUsers.get(0).getIdCard();
                String bank = esignUsers.get(0).getBank();
                String phone = esignUsers.get(0).getPhone();
                if (realname.equals(name) && idcard.equals(idno)
                        && bank.equals(cardno) && phone.equals(mobile)) {
                    //数据匹配，验证验证码
                    String serviceid = esignUsers.get(0).getServiceId();
                    CodeValidVo codeValidVo = new CodeValidVo();
                    codeValidVo.setCode(code);
                    codeValidVo.setServiceId(serviceid);

                    //将传输记录到history表
                    Long recordid = recordHistory("codeValidUrl", codeValidVo);

                    String response = ESignApiManager
                            .postInterface(codeValidVo);

                    //将传输返回值更新到history表
                    updateHistory(recordid, response);

                    //检查errCode，如果为0，则验证码正确，如果不为0，验证码错误
                    JSONObject json = JSONObject.parseObject(response);
                    if (json.get("errCode").toString().equals("0")) {
                        r.setFlag(1);
                        r.setMsg("认证正确");
                    } else {
                        r.setFlag(0);
                        r.setMsg("验证失败，" + json.get("errCode").toString());
                    }
                } else {
                    //数据不匹配
                    r.setFlag(0);
                    r.setMsg("数据不匹配");
                }
            } else {
                r.setFlag(0);
                r.setMsg("该用户并没有发送验证码");
            }
            }catch (Exception e) {
            e.printStackTrace();
            r.setFlag(0);
            r.setMsg("error");
        }
        return r;
    }


    /**
     * 企业认证第一步
     * @param peopleid
     * @param companyname
     * @param codeorg
     * @param codeusc
     * @param legalname
     * @param legalidno
     * @param name
     * @param cardno
     * @param subbranch
     * @param bank
     * @param province
     * @param city
     * @return
     */
    public Result approveCompanyStep1(Long peopleid, String companyname,
                                      String codeorg, String codeusc, String legalname, String legalidno,
                                      String name, String cardno, String subbranch, String bank,
                                      String province, String city) {
        Result r = new Result();

        //1.1 企业对公打款，步骤一：企业信息校验
        try {
            if(peopleid==null||peopleid.equals("")||companyname==null||companyname.equals("")
                    ||legalname==null||legalname.equals("")||legalidno==null||legalidno.equals("")
                    ||name==null||name.equals("")||cardno==null||cardno.equals("")
                    ||subbranch==null||subbranch.equals("")||bank==null||bank.equals("")
                    ||province==null||province.equals("")||city==null||city.equals("")){
                r.setFlag(0);
                r.setMsg("参数不完整");
                return r;
            }
            //根据参数拼对象
            OrgInfoAuthVo orgInfoAuthVo = new OrgInfoAuthVo();
            orgInfoAuthVo.setCodeORG(codeorg);
            orgInfoAuthVo.setCodeUSC(codeusc);
            orgInfoAuthVo.setLegalIdno(legalidno);
            orgInfoAuthVo.setLegalName(legalname);
            orgInfoAuthVo.setName(companyname);

            //将传输记录到history表
            Long recordid=recordHistory("orgInfoAuthUrl",orgInfoAuthVo);

            //将参数传给e签宝，得到返回值字符串
            String response = ESignApiManager.postInterface(orgInfoAuthVo);
            //String response = "{\"errCode\":0,\"msg\":\"成功\",\"serviceId\":\"924459e6-5e33-41b0-bccf-3eb07*****\"}";

            //将传输返回值更新到history表
            updateHistory(recordid,response);

            //检查errCode，如果为0，则企业信息校验成功，如果不为0，企业信息校验失败
            JSONObject json =JSONObject.parseObject(response);
            if(json.get("errCode").toString().equals("0")){
                String serviceid=json.get("serviceId").toString();
                Iterable<EsignCompany> all = esignCompanyRepository.findAll(esignCompany.userId.eq(peopleid));
                List<EsignCompany> esignCompanies = null;
                if (all != null){
                    esignCompanies = ToListUtil.IterableToList(all);
                }
                if (esignCompanies.size()>0){
                    esignCompanies.stream().forEach(one ->{
                        one.setCompanyName(companyname);
                        one.setCodeOrg(codeorg);
                        one.setCodeUsc(codeusc);
                        one.setLegalName(legalname);
                        one.setServiceId(serviceid);
                    });
                    esignCompanyRepository.save(esignCompanies);
                }else {
                    EsignCompany one = new EsignCompany();
                    one.setUserId(peopleid);
                    one.setCompanyName(companyname);
                    one.setCodeOrg(codeorg);
                    one.setCodeUsc(codeusc);
                    one.setLegalName(legalname);
                    one.setLegalidNo(legalidno);
                    one.setServiceId(serviceid);
                    esignCompanyRepository.saveAndFlush(one);
                }
//                sql="select * from tsign_company where peopleid='"+peopleid+"';";
//                map.put("sql", sql);
//                List<Map<String, Object>> tsignCompany = informationMapper.runSql(map);

//                Date time = new Date();
//                if(tsignCompany.size()>0){
//                    //更新tsign_company表
//                    sql="update tsign_company set companyname='"+companyname+"'" +
//                            ",codeorg='"+codeorg+"',codeusc='"
//                            +codeusc+"',legalname='"+
//                            legalname+"',legalidno='"+legalidno+"',serviceid='"+serviceid+"',time=NOW() where peopleid='"+peopleid+"';";
//                    map.put("sql", sql);
//                    informationMapper.runSqlOperate(map);
//                }else{
//                    //插入tsign_company表
//                    String id = UUID.randomUUID().toString();
//                    sql="insert into tsign_company (id,peopleid,companyname,codeorg,codeusc,legalname,legalidno,serviceid,time) "
//                            + "values ('"+id+"','"+peopleid+"','"+companyname+"'" +
//                            ",'"+codeorg+"','"+codeusc+"','"+legalname+"','"+legalidno+"'" +
//                            ",'"+serviceid+"',NOW());";
//                    map.put("sql", sql);
//                    informationMapper.runSqlOperate(map);
//                }

                //1.2 企业对公打款，步骤二：打款

                //根据参数拼对象
                OrgToPayVo orgToPayVo = new OrgToPayVo();
                orgToPayVo.setBank(bank);
                orgToPayVo.setCardno(cardno);
                orgToPayVo.setCity(city);
                orgToPayVo.setName(name);
                //orgToPayVo.setNotify(notify);
                //orgToPayVo.setPrcptcd(prcptcd);
                orgToPayVo.setProvice(province);
                orgToPayVo.setServiceId(serviceid);
                orgToPayVo.setSubbranch(subbranch);

                //将传输记录到history表
                Long recordid_pay=recordHistory("orgToPayUrl",orgToPayVo);

                //将参数传给e签宝，得到返回值字符串
                String response_pay = ESignApiManager.postInterface(orgToPayVo);

                //将传输返回值更新到history表
                updateHistory(recordid_pay,response_pay);

                //检查errCode，如果为0，则打款成功，如果不为0，打款失败
                JSONObject json_pay =JSONObject.parseObject(response_pay);
                if(json_pay.get("errCode").toString().equals("0")){
                    String serviceid_pay=json_pay.get("serviceId").toString();

                    //更新tsign_company表
                    EsignCompany one = esignCompanyRepository.findOne(esignCompany.userId.eq(peopleid));
                    one.setName(name);
                    one.setCardNo(cardno);
                    one.setSubbranch(subbranch);
                    one.setBank(bank);
                    one.setProvice(province);
                    one.setCity(city);
                    one.setServiceId(serviceid_pay);
                    one.setTimePay(new Date());
                    esignCompanyRepository.saveAndFlush(one);

                    //打款成功，更新人员表，修改E签宝认证状态
                    //为空或者0则该用户未进行e签宝验证，打款成功，则为1，金额验证成功，则为2
                    TlongUser one1 = tlongUserRepository.findOne(peopleid);
                    one1.setEsgin(1);

                    r.setFlag(1);
                    r.setMsg("打款成功");

                }else{
                    r.setFlag(0);
                    r.setMsg("打款失败，"+json.get("errCode").toString());
                }

            }else{
                r.setFlag(0);
                r.setMsg("企业信息校验失败，"+json.get("errCode").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setFlag(0);
            r.setMsg("error");
        }
        return r;
    }



    //1.3 企业对公打款验证/realname/rest/external/organ/payAuth
    public Result approveCompanyStep2(Long peopleid, String cash) {
        Result r = new Result();

        try {
            Iterable<EsignCompany> all = esignCompanyRepository.findAll(esignCompany.userId.eq(peopleid));
            List<EsignCompany> esignCompanies = null;
            if (all != null){
                esignCompanies = ToListUtil.IterableToList(all);
            }
            if (esignCompanies.size()>0){
                String serviceid = esignCompanies.get(0).getServiceId()
                        .toString();

                OrgPayAuthVo orgPayAuthVo = new OrgPayAuthVo();
                orgPayAuthVo.setCash(Float.valueOf(cash));
                orgPayAuthVo.setServiceId(serviceid);

                //将传输记录到history表
                Long recordid = recordHistory("orgPayAuthUrl", orgPayAuthVo);

                String response = ESignApiManager.postInterface(orgPayAuthVo);

                //将传输返回值更新到history表
                updateHistory(recordid, response);

                //检查errCode，如果为0，则验证正确，如果不为0，验证错误
                JSONObject json = JSONObject.parseObject(response);
                if (json.get("errCode").toString().equals("0")) {
                    r.setFlag(1);
                    r.setMsg("金额正确");
                } else {
                    r.setFlag(0);
                    r.setMsg("金额错误，" + json.get("errCode").toString());
                }
            } else {
                r.setFlag(0);
                r.setMsg("该用户并没有打款记录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setFlag(0);
            r.setMsg("error");
        }

        return r;
    }








    /**
     * 向tsign_history表插入和e签宝的传输记录
     * @param api
     * @param reqObj
     */
    public Long recordHistory(String api,Object reqObj){
        String request= JSONObject.toJSONString(reqObj);
        EsignRecordHistory recordHistory = new EsignRecordHistory();
        recordHistory.setApi(api);
        recordHistory.setReqData(request);
        recordHistory.setResData("");
        recordHistory.setReqTime(new Date());
        EsignRecordHistory es = esignRecordHistoryRepository.save(recordHistory);
        return es.getId();
    }


    /**
     * 更新tsign_history表e签宝返回的response
     * @param id
     * @param response
     */
    public void updateHistory(Long id ,String response){
        EsignRecordHistory one = esignRecordHistoryRepository.findOne(id);
        one.setResTime(new Date());
        one.setResData(response);
        esignRecordHistoryRepository.saveAndFlush(one);
    }

    /**
     * 合同签署
     * @param userid
     * @return
     */
    public Result eSign(Long userid) {
        return eSignSignService.eSign(userid);
    }
}
