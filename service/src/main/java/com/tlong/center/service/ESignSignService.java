package com.tlong.center.service;

import com.timevale.esign.sdk.tech.bean.result.AddAccountResult;
import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.tlong.center.api.dto.Result;
import com.tlong.center.common.utils.AlgorithmHelper;
import com.tlong.center.common.utils.FileHelper;
import com.tlong.center.common.utils.HttpClientUtil;
import com.tlong.center.common.utils.SignHelper;
import org.json.JSONException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
//import org.apache.commons.httpclient.methods.PutMethod;
//import org.apache.commons.httpclient.methods.RequestEntity;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import com.timevale.esign.sdk.tech.bean.result.AddAccountResult;
//import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
//import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
//import com.zjxx.jp.appFunction.webservice.util.HttpClientUtil;
//import com.zjxx.jp.biz.dao.InformationMapper;
//import com.zjxx.jp.hybFunction.pojo.Result;
//import cn.tsign.ching.eSign.SignHelper;
//import cn.tsign.ching.utils.AlgorithmHelper;
//import cn.tsign.ching.utils.FileHelper;

//@RestController
//@RequestMapping("pub/admin/eSign")
@Component
@Transactional
public class ESignSignService {
    private static String encoding = "UTF-8";
    private static String algorithm = "HmacSHA256";
    private static String mode = "package";
    private static String contentType_url = "application/json";
    private static String contentType_upload = "application/octet-stream";
    //e签宝原文保全测试环境
	private static String url_getUrl = "http://smlcunzheng.tsign.cn:8083/evi-service/evidence/v1/preservation/original/url";
	public static String url_SDKinit = "http://121.40.164.61:8080/tgmonitor/rest/app!getAPIInfo2";
	public static String projectId = "1111563517";
	public static String projectSecret = "95439b0863c241c63a861b87d1e647b7";

    //e签宝原文保全正式环境
//    private static String url_getUrl = "http://evislb.tsign.cn:8080/evi-service/evidence/v1/preservation/original/url";
//    public static String url_SDKinit = "http://itsm.tsign.cn/tgmonitor/rest/app!getAPIInfo2";
//    public static String projectId = "1111564360";
//    public static String projectSecret = "644a50a16628dcbfba0545525caddc7f";


//    @Autowired
    private InformationMapper informationMapper;

    //字段数据转移接口
//    @RequestMapping(value = "/signContract.do")
//    @ResponseBody
//    public Object eSign(String userid) {
    public Result eSign(String userid) {
        Map<String, Object> map = new HashMap<String, Object>();
        /*
         * userType 客户端类型：0-代理商端，1-供应商端
         * scene 签约客户类型：0-个人客户，1-企业客户
         *
         * ----< 个人用户参数 >----
         * name_per 个人客户姓名
         * idNo 身份证号/护照号
         * personArea 个人归属地：0-大陆，1-香港，2-澳门，3-台湾，4-外籍
         *
         * ----< 企业用户参数 >----
         * name_org 企业名称 companyname
         * organType 单位类型：0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构
         * regType 企业注册类型：0-组织机构代码号，1-多证合一，传递社会信用代码号,2-企业工商注册码
         * organCode 组织机构代码号、社会信用代码号或工商注册号
         * userType 注册类型：1-代理人注册，2-法人注册
         * agentName 代理人姓名，当注册类型为1时必填
         * agentIdNo 代理人身份证号，当注册类型为1时必填
         * legalName 法定代表姓名，当注册类型为2时必填
         * legalArea 法定代表人归属地：0-大陆，1-香港，2-澳门，3-台湾，4-外籍，当注册类型为2时必填
         * legalIdNo 法定代表身份证号/护照号，当注册类型为2时必填
         * personServiceId 用户实名认证后的serviceId
         */
        int appType = -1;
        int scene = -1;
        String name_per = "";
        String idNo = "";
        int personArea = -1;
        String name_org = "";
        int organType = -1;
        int regType = -1;
        String organCode = "";
        int userType = -1;
        String agentName = "";
        String agentIdNo = "";
        String legalName = "";
        int legalArea = -1;
        String legalIdNo = "";
        String personServiceId = "";
        Result result = new Result();
        String sql = String.format("select * from zjxx_people where id = '%s'", userid);
        map.put("sql", sql);
        List<Map<String, Object>> peopleInfoList = informationMapper.runSql(map);
        if (peopleInfoList.size() > 0) {
            Map<String, Object> peopleInfo = peopleInfoList.get(0);
            //判断是代理商还是供应商
            String ptype = peopleInfo.get("ptype").toString();
            switch (ptype) {
                case "0":
                    appType = 1;
                    break;
                case "1":
                case "2":
                case "3":
                    appType = 0;
                    break;
                default:
                    result.setFlag(-1);
                    result.setMsg("该用户不是代理商也不是供应商");
                    return result;
            }
            //判断是个人还是企业
            String iscompany = peopleInfo.get("iscompany").toString();
            switch (iscompany) {
                case "0":
                    scene = 0;
                    //<-- 获取个人信息 -->
                    sql = String.format("select * from tsign_person where peopleid = '%s'", userid);
                    map.put("sql", sql);
                    peopleInfoList = informationMapper.runSql(map);
                    if (peopleInfoList.size() > 0) {
                        peopleInfo = peopleInfoList.get(0);
                        idNo = peopleInfo.get("idcard").toString();
                        name_per = peopleInfo.get("realname").toString();
                        personServiceId = peopleInfo.get("serviceid").toString();
                        if (idNo.isEmpty()) {
                            result.setFlag(-1);
                            result.setMsg("个人用户身份证为空");
                            return result;
                        }
                        if (name_per.isEmpty()) {
                            result.setFlag(-1);
                            result.setMsg("个人用户真实姓名为空");
                            return result;
                        }
                        if (personServiceId.isEmpty()) {
                            result.setFlag(-1);
                            result.setMsg("个人用户serviceid为空");
                            return result;
                        }
                        //默认大陆用户
                        personArea = 0;
                    } else {
                        result.setFlag(-1);
                        result.setMsg("该个人用户未进行实名认证");
                        return result;
                    }
                    break;
                case "1":
                    scene = 1;
                    try {
                        String ot = peopleInfo.get("organType").toString();
                        organType = Integer.parseInt(ot);
                    } catch (Exception e) {
                        e.printStackTrace();
                        organType = -1;
                    }
                    if (organType != 0 && organType != 1 && organType != 2 && organType != 3 && organType != 4) {
                        result.setFlag(-1);
                        result.setMsg("企业单位类型错误");
                        return result;
                    }
                    //<-- 获取企业信息 -->
                    sql = String.format("select * from tsign_company where peopleid = '%s'", userid);
                    map.put("sql", sql);
                    peopleInfoList = informationMapper.runSql(map);
                    if (peopleInfoList.size() > 0) {
                        peopleInfo = peopleInfoList.get(0);
                        name_org = peopleInfo.get("companyname").toString();
                        //默认单位类型为0-普通企业
                        organType = 0;
                        //默认企业注册类型为1-多证合一，传递社会信用代码号
                        regType = 1;
                        organCode = peopleInfo.get("codeusc").toString();
                        //默认注册类型为2-法人注册
                        userType = 2;
                        legalName = peopleInfo.get("legalname").toString();
                        //默认法人代表归属地为0-大陆
                        legalArea = 0;
                        legalIdNo = peopleInfo.get("legalidno").toString();
                        personServiceId = peopleInfo.get("serviceid").toString();
                        if (name_org.isEmpty()) {
                            result.setFlag(-1);
                            result.setMsg("企业名称为空");
                            return result;
                        }
                        if (organCode.isEmpty()) {
                            result.setFlag(-1);
                            result.setMsg("社会信用代码号为空");
                            return result;
                        }
                        if (legalName.isEmpty()) {
                            result.setFlag(-1);
                            result.setMsg("法定代表姓名为空");
                            return result;
                        }
                        if (legalIdNo.isEmpty()) {
                            result.setFlag(-1);
                            result.setMsg("法定代表身份证号为空");
                            return result;
                        }
                        if (personServiceId.isEmpty()) {
                            result.setFlag(-1);
                            result.setMsg("企业用户serviceid为空");
                            return result;
                        }
                    } else {
                        result.setFlag(-1);
                        result.setMsg("该企业用户未进行实名认证");
                        return result;
                    }
                    break;
                default:
                    result.setFlag(-1);
                    result.setMsg("该用户不是个人也不是企业");
                    return result;
            }
        } else {
            result.setFlag(-1);
            result.setMsg("用户不存在");
            return result;
        }


        String appTypeName = "";
        switch (appType) {
            case 0:
                appTypeName = "-代理商端";
                break;

            case 1:
                appTypeName = "-供应商端";
                break;
        }
        // 待签署的PDF文件路径
//		String srcPdfFile = File.separator + "LongShi" + File.separator + "pub" + File.separator + "admin" + File.separator + "upload" + File.separator + "pdf" + File.separator + "天珑珠宝服务协议" + appTypeName + ".pdf";
        String srcPdfFile = this.getClass().getClassLoader().getResource("/").getPath() + "../../pub/admin/upload/pdf/天珑珠宝服务协议" + appTypeName + ".pdf";
        // 最终签署后的PDF文件路径
//		String signedFolder ="LongShi" + File.separator + "pub" + File.separator + "admin" + File.separator + "upload" + File.separator + "pdfOut" + File.separator;
        String signedFolder = this.getClass().getClassLoader().getResource("/").getPath() + "../../pub/admin/upload/pdfOut/";
        // 最终签署后PDF文件名称
        String signedFileName = "天珑珠宝服务协议" + appTypeName;
        // 初始化项目，做全局使用，只初始化一次即可
        com.timevale.esign.sdk.tech.bean.result.Result eSignResult = SignHelper.initProject();
        if (eSignResult.getErrCode() != 0) {
            result.setMsg("sdk初始化失败:" + eSignResult.getMsg());
            result.setFlag(-1);
            return result;
        }
        // 应用场景演示
        switch (scene) {
            case 0:
                System.out.println("----<场景演示：个人用户签约>----");
                signedFileName += name_per + idNo;
                result = personSign(srcPdfFile, signedFileName, signedFolder, name_per, idNo, personArea);
                break;
            case 1:
                System.out.println("----<场景演示：企业客户签约>----");
                signedFileName += name_org + organCode;
                result = companySign(srcPdfFile, signedFileName, signedFolder, name_org, organType, regType, organCode, userType, agentName, agentIdNo, legalName, legalArea, legalIdNo);
                break;
            default:
                System.out.println("---- 提示！请选择应用场景...");
                break;
        }
        if (result.getFlag() == 1) {
            //签署成功,调用文件保全
            long fileLength = getFileLength(signedFolder + signedFileName + ".pdf");
            if (fileLength == -1) {
                result.setFlag(-1);
                result.setMsg("获取签署文件大小失败");
                return result;
            }

//			String md5 = getFileMD5(signedFolder + signedFileName + ".pdf");
            String contentMD5 = AlgorithmHelper.getContentMD5(signedFolder + signedFileName + ".pdf");
            if (contentMD5 == "-1") {
                result.setFlag(-1);
                result.setMsg("获取签署文件MD5值失败");
                return result;
            }
            //请求保全文件url的参数
            String param = "{\"eviName\":\"" + signedFileName + "\", \"content\":{\"contentDescription\":\"" + signedFileName + ".pdf\", \"contentLength\":" + fileLength + ", \"contentBase64Md5\":\"" + contentMD5 + "\"}, \"eSignIds\":[{\"type\":0,\"value\":\"" + result.getMsg() + "\" },{\"type\":0,\"value\":\"" + result.getOrgid() + "\" }],\"bizIds\":[{ \"type\":0,\"value\":\"" + personServiceId + "\"}]}";
            //String param = "{\"eviName\":\"天珑珠宝服务协议-代理商端朱安42128119911027415X\", \"content\":{\"contentDescription\":\"天珑珠宝服务协议-代理商端朱安42128119911027415X.pdf\", \"contentLength\":192974, \"contentBase64Md5\":\"SZ+A08mLHX7aAqrpop1v9w==\"}, \"eSignIds\":[{\"type\":0,\"value\":\"898358966793310217\" },{\"type\":0,\"value\":\"898358973206401026\" }],\"bizIds\":[{ \"type\":0,\"value\":\"80f603f2-1383-4735-8946-620034b199f1\"}]}";
            //获取文件保全url的json返回值
            String return_json = HttpClientUtil.postJson(url_getUrl, param, getHeaders(param), encoding);
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(return_json);
                if (jsonObject.getInt("errCode") == 0) {
                    String url = jsonObject.getString("url");
                    String evid = jsonObject.getString("evid");
                    result.setTtime(evid);
                    //调用上传接口
                    int flag = updateFileRequestByPost(url, signedFolder + signedFileName + ".pdf");
                    result = new Result();
                    result.setFlag(flag);
                    if (flag == 200) {
//						result.setTtime(param);
//						result.setUserId(HMACSHA256(param));
                        result.setMsg(evid);
                        sql = String.format("update zjxx_people set evid = '%s' , echecktype = '2' where id = '%s'", evid, userid);
                        map.put("sql", sql);
                        informationMapper.runSqlOperate(map);
                    } else {
//						result.setTtime(param);
//						result.setUserId(HMACSHA256(param));
                        result.setMsg("文件保全失败");
                    }
                } else {
                    result = new Result();
//					result.setTtime(param);
//					result.setUserId(HMACSHA256(param));
                    result.setFlag(-1);
                    result.setMsg("e签宝获取url失败，错误编码：" + jsonObject.getInt("errCode") + ",错误信息：" + jsonObject.getString("msg"));
                    return result;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                result = new Result();
                result.setFlag(-1);
                result.setMsg("e签宝服务器繁忙：" + return_json);
//				result.setTtime(param);
//				result.setUserId(HMACSHA256(param));
                return result;
            }
        } else {
            //签署失败，返回错误信息
            return result;
        }
        System.out.println(srcPdfFile);
        return result;
    }

    // 个人签约
    public Result personSign(String srcPdfFile, String signedFileName, String signedFolder, String name, String idNo, int personArea) {
        // 开户
        AddAccountResult accountResult = SignHelper.addPersonAccount(name, idNo, personArea);
        String accountId = accountResult.getAccountId();
        // 生成印章
        AddSealResult addSealResult = SignHelper.addPersonTemplateSeal(accountId);
        // 贵公司签署，签署方式：关键词定位,以文件流的方式传递pdf文档,String srcPdfFile,String key,int x,int y,int width
        FileDigestSignResult platformSignResult = SignHelper.platformSignByStreammByKey(srcPdfFile, "平台（盖章）", 50, 0, 80);
        // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档,byte[] pdfFileStream, String accountId,String sealData,String key,int x,int y,int width
        FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByStream(platformSignResult.getStream(), accountId, addSealResult.getSealData(), "用户(盖章)", 115, 7, 120);

        // 销户
        SignHelper.deleteAccount(accountId);
        Result result = new Result();
        // 所有签署完成,将最终签署后的文件流保存到本地
        if (0 == userPersonSignResult.getErrCode()) {
            signedFileName += ".pdf";
            Boolean isOk = SignHelper.saveSignedByStream(userPersonSignResult.getStream(), signedFolder, signedFileName);
            if (isOk) {
                //一切正常
                result.setFlag(1);
                //获取平台签署后的serviceId
                result.setMsg(platformSignResult.getSignServiceId());
                //获取个人签署后的serviceId
                result.setOrgid(userPersonSignResult.getSignServiceId());
            } else {
                result.setFlag(-1);
                result.setMsg("保存文件失败");
            }
        } else {
            result.setFlag(-1);
            if (accountResult.getErrCode() != 0) {
                result.setMsg("个人用户账户出现问题：" + accountResult.getMsg());
            } else if (platformSignResult.getErrCode() != 0) {
                result.setMsg("平台自身出现问题：" + platformSignResult.getMsg());
            } else if (userPersonSignResult.getErrCode() != 0) {
                result.setMsg("个人用户签约出现问题：" + userPersonSignResult.getMsg());
            } else {
                result.setMsg("个人用户签约出现未知错误");
            }
        }
        return result;

    }

    // 企业签约
    public Result companySign(String srcPdfFile, String signedFileName, String signedFolder, String name, int organType, int regType, String organCode, int userType, String agentName, String agentIdNo, String legalName, int legalArea, String legalIdNo) {
        // 开户
        AddAccountResult accountResult = SignHelper.addOrganizeAccount(name, organType, regType, organCode, userType, agentName, agentIdNo, legalName, legalArea, legalIdNo);
        String accountId = accountResult.getAccountId();
        // 生成印章
        AddSealResult addSealResult = SignHelper.addOrganizeTemplateSeal(accountId, "", "");
        // 贵公司签署，签署方式：关键词定位,以文件流的方式传递pdf文档,String srcPdfFile,String key,int x,int y,int width
        FileDigestSignResult platformSignResult = SignHelper.platformSignByStreammByKey(srcPdfFile, "平台（盖章）", 50, 0, 80);
        // 企业客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档,byte[] pdfFileStream, String accountId,String sealData,String key,int x,int y,int width
        FileDigestSignResult userOrganizeSignByStream = SignHelper.userOrganizeSignByStream(platformSignResult.getStream(), accountId, addSealResult.getSealData(), "用户(盖章)", 115, 7, 120);

        //销户
        SignHelper.deleteAccount(accountId);
        // 所有签署完成,将最终签署后的文件流保存到本地
        Result result = new Result();
        if (0 == userOrganizeSignByStream.getErrCode()) {
            signedFileName += ".pdf";
            Boolean isOk = SignHelper.saveSignedByStream(userOrganizeSignByStream.getStream(), signedFolder, signedFileName);
            if (isOk) {
                //一切正常
                result.setFlag(1);
                //获取平台签署后的serviceId
                result.setMsg(platformSignResult.getSignServiceId());
                //获取企业签署后的serviceId
                result.setOrgid(userOrganizeSignByStream.getSignServiceId());
            } else {
                result.setFlag(-1);
                result.setMsg("保存文件失败");
            }
        } else {
            result.setFlag(-1);
            if (accountResult.getErrCode() != 0) {
                result.setMsg("企业账户出现问题：" + accountResult.getMsg());
            } else if (platformSignResult.getErrCode() != 0) {
                result.setMsg("平台自身出现问题：" + platformSignResult.getMsg());
            } else if (userOrganizeSignByStream.getErrCode() != 0) {
                result.setMsg("企业签约出现问题：" + userOrganizeSignByStream.getMsg());
            } else {
                result.setMsg("企业用户签约出现未知错误");
            }
        }
        return result;
    }

    //销户
    public static void toDoList(String accountId) {
        // 注销个人或企业账户,需要accountId
        SignHelper.deleteAccount(accountId);
    }

    //明文用HMACSHA256加密
    public String HMACSHA256(String data_str) {
        String signature = AlgorithmHelper.getXtimevaleSignature(data_str, projectSecret, algorithm, encoding);
        return signature;
//    		byte[] data = data_str.getBytes();
//    		byte[] key = projectSecret.getBytes();
//          try  {
//             SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
//             Mac mac = Mac.getInstance("HmacSHA256");
//             mac.init(signingKey);
//             return byte2hex(mac.doFinal(data));
//          } catch (NoSuchAlgorithmException e) {
//             e.printStackTrace();
//          } catch (InvalidKeyException e) {
//            e.printStackTrace();
//          }
//          return null;
    }

    //加密后的字节数组转换成字符串
    public String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    //获取文件大小
    public long getFileLength(String fileUrl) {
        File f = new File(fileUrl);
        if (f.exists() && f.isFile()) {
            long fl = f.length();
            return fl;
        } else {
            return -1;
        }
    }

    //获取文件md5
//    public String getFileMD5(String fileUrl) {
//    		try {
//            File file = new File(fileUrl);
//            FileInputStream fis = new FileInputStream(file);
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] buffer = new byte[1024];
//            int length = -1;
//            while ((length = fis.read(buffer, 0, 1024)) != -1) {
//                md.update(buffer, 0, length);
//            }
//            BigInteger bigInt = new BigInteger(1, md.digest());
//            System.out.println("文件md5值：" + bigInt.toString(16));
//            return bigInt.toString(16);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    		return "-1";
//	}

    //调用文件保全url时，需要上传的表头
    private Map<String, String> getHeaders(String param) {

        Map<String, String> headers = new HashMap<>();
        headers.put("X-timevale-project-id", projectId);
        headers.put("X-timevale-signature", HMACSHA256(param));
        headers.put("X-timevale-signature-algorithm", algorithm);
        headers.put("X-timevale-mode", mode);
        headers.put("Content-Type", contentType_url);
        return headers;
    }

    /***
     * 模拟上传文档请求 ，请求方式：Put
     *
     * @return
     */
    public int updateFileRequestByPost(String updateUrl, String filePath) {
        StringBuffer strBuffer = null;
        System.out.println("MD5:" + AlgorithmHelper.getContentMD5(filePath));
        try {
            // 建立连接
            URL url = new URL(updateUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true); // 需要输出
            httpURLConnection.setDoInput(true); // 需要输入
            httpURLConnection.setUseCaches(false); // 不允许缓存
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Content-MD5", AlgorithmHelper.getContentMD5(filePath));
            httpURLConnection.setRequestProperty("Content-Type", contentType_upload);
            httpURLConnection.setRequestProperty("Charset", encoding);
            // 连接会话
            httpURLConnection.connect();
            // 建立输入流，向指向的URL传入参数
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());

            // 设置请求参数
            dos.write(FileHelper.getBytes(filePath));
            dos.flush();
            dos.close();
            // 获得响应状态
            int resultCode = httpURLConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                System.out.println("上传成功！Http-Status = " + resultCode + " " + httpURLConnection.getResponseMessage());
                strBuffer = new StringBuffer();
                String readLine = new String();
                BufferedReader responseReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), encoding));
                while ((readLine = responseReader.readLine()) != null) {
                    strBuffer.append(readLine);
                }
                responseReader.close();
            } else {
                System.out.println("上传失败！Http-Status = " + resultCode + " " + httpURLConnection.getResponseMessage());
                strBuffer = new StringBuffer();
                String readLine = new String();
                BufferedReader responseReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), encoding));
                while ((readLine = responseReader.readLine()) != null) {
                    strBuffer.append(readLine);
                }
                responseReader.close();
            }
            return resultCode;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
