package com.tlong.center.service;

import com.alibaba.fastjson.JSONObject;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.common.utils.HttpClientUtil;
import com.tlong.center.common.utils.InvitationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceApplicationTests {

	Logger logger = LoggerFactory.getLogger(ServiceApplicationTests.class);

	@Test
	public void contextLoads() {


	}

	/**
	 * 测试生成邀请码
	 */
	@Test
	public void createInvitationCode(){
		String invitation = InvitationUtils.createInvitation(1L);
		logger.info("生成的邀请码为:" + invitation);
	}

	@Test
	public void sendMessage(){
		String url="https://sms.yunpian.com/v2/sms/single_send.json";
		String apikey="35c03fcb291e1f4a4ec158f1c80ce221";
		// 随机生产验证码
		int radomInt = new Random().nextInt(999999);
		String b = String.valueOf(radomInt);
		int a = b.length();
		if (a != 6) {
			for (int i = 0; i < 6 - a; i++) {
				b = "0" + b;
			}
		}

		String text="【天珑网络科技】您的验证码是123456" + b;
		Map<String, String> params = new HashMap<String, String>();//请求参数集合
		params.put("apikey", apikey);
		params.put("text", text);
		params.put("mobile", "13175076507");
		String post = HttpClientUtil.post(url, params);
		JSONObject json =JSONObject.parseObject(post);
		if(json.get("code").toString().equals("0")){
			//写入验证码记录表

//			return new TlongResultDto(0,"消息发送成功");
		}else {
//			return new TlongResultDto(1,"消息发送失败");
		}
	}

	@Test
	public void sss(){
//		Date date = new Date();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.add(Calendar.MINUTE, 30);
//		System.out.println(localDateTime);
	}


}
