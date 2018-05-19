package com.tlong.center.common.esignBean;

//import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by frank on 2017/6/27.
 */
public class ESignApiManager {

    private static Logger Log = LoggerFactory.getLogger(ESignApiManager.class);

    private static String encoding = "UTF-8";

    private static String contentType = "application/json";

    private static String algorithm = "HmacSHA256";

    private static String projectId = "1111564360";

    private static String projectSecret = "644a50a16628dcbfba0545525caddc7f";
    
    private static String personInfoValidUrl = "http://openapi2.tsign.cn:8081/realname/rest/external/person/bankauth/infoValid";

    private static String codeValidUrl = "http://openapi2.tsign.cn:8081/realname/rest/external/person/bankauth/codeValid";

    private static String orgInfoAuthUrl = "http://openapi2.tsign.cn:8081/realname/rest/external/organ/infoAuth";

    private static String orgToPayUrl = "http://openapi2.tsign.cn:8081/realname/rest/external/organ/toPay";

    private static String orgPayAuthUrl = "http://openapi2.tsign.cn:8081/realname/rest/external/organ/payAuth";



    public static <T> String postInterface(T dataObject) {
        //判断请求地址
        String api = "";
        if (dataObject instanceof PersonInfoValidVo) {
            api = personInfoValidUrl;
        } else if (dataObject instanceof CodeValidVo) {
            api = codeValidUrl;
        } else if (dataObject instanceof OrgInfoAuthVo) {
            api = orgInfoAuthUrl;
        } else if (dataObject instanceof OrgToPayVo) {
            api = orgToPayUrl;
        } else if (dataObject instanceof OrgPayAuthVo) {
            api = orgPayAuthUrl;
        }
        Log.info("请求地址:" + api);
        //发起请求
        try {

            String requestData = JSONObject.toJSONString(dataObject);
            Log.info("请求报文:" + requestData);
            String signature = EncryptionHMAC.getHMACHexString(requestData, projectSecret, algorithm, encoding);
            Log.info("请求签名:" + signature);
            Map<String, String> headers = getHeaders(signature);
            String resultJson = HttpClientUtil.postJson(api, requestData, headers, encoding);
            Log.info("请求结果:" + resultJson);

            //http请求结果为空证明对方服务异常
            if (StringUtils.isBlank(resultJson)) {
                return "{'errCode':-1,'msg':'e签宝服务繁忙'}";
            }

            return resultJson;
        } catch (Exception e) {
            Log.error(e.getMessage());

            return "{'errCode':-1,'msg':'系统配置有误,请检查配置'}";

        }
    }

    private static Map<String, String> getHeaders(String signature) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-timevale-mode", "package");
        headers.put("X-timevale-project-id", projectId);
        headers.put("X-timevale-signature", signature);
        headers.put("signature-algorithm", algorithm);
        headers.put("Content-Type", contentType);
        headers.put("Charset", encoding);
        return headers;
    }


}
