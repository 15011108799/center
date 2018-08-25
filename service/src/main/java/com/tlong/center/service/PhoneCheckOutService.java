package com.tlong.center.service;

import com.alibaba.fastjson.JSONObject;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.common.utils.HttpClientUtil;
import com.tlong.center.common.utils.InvitationUtils;
import com.tlong.center.domain.app.check.PhoneCheckOut;
import com.tlong.center.domain.repository.PhoneCheckOutRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.tlong.center.domain.app.check.QPhoneCheckOut.phoneCheckOut;

@Component
@Transactional
public class PhoneCheckOutService {

    private final PhoneCheckOutRepository phoneCheckOutRepository;

    public PhoneCheckOutService(PhoneCheckOutRepository phoneCheckOutRepository) {
        this.phoneCheckOutRepository = phoneCheckOutRepository;
    }

    /**
     * 发送手机验证码
     */
    public TlongResultDto sendMessage(String phone) {
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

        String text="【天珑网络科技】您的验证码是" + b;
            Map<String, String> params = new HashMap<String, String>();//请求参数集合
            params.put("apikey", apikey);
            params.put("text", text);
            params.put("mobile", phone);
            String post = HttpClientUtil.post(url, params);
            JSONObject json =JSONObject.parseObject(post);
            if(json.get("code").toString().equals("0")){
            //写入验证码记录表
            PhoneCheckOut phoneCheckOut = new PhoneCheckOut();
            phoneCheckOut.setCheckOutCode(b);
            phoneCheckOut.setPhone(phone);

            SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, 30);
            String format = sim.format(calendar.getTime());
            phoneCheckOut.setEndTime(Long.valueOf(format));

            phoneCheckOutRepository.save(phoneCheckOut);
            return new TlongResultDto(0,"消息发送成功");
        }else {
            return new TlongResultDto(1,"消息发送失败");
        }
    }


    /**
     * 验证手机验证码
     */
    public TlongResultDto checkMessage(String phone, String s) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String nowDate = sim.format(date);

        PhoneCheckOut one = phoneCheckOutRepository.findOne(phoneCheckOut.phone.eq(phone)
                .and(phoneCheckOut.checkOutCode.eq(s))
                .and(phoneCheckOut.endTime.gt(Long.valueOf(nowDate))));
        if (Objects.nonNull(one)){
            return new TlongResultDto(0,"验证成功");
        }
        return new TlongResultDto(1,"验证码错误");
    }
}
