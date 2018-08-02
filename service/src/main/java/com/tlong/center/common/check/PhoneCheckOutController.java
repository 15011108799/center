package com.tlong.center.common.check;

import com.tlong.center.api.common.check.PhoneCheckOutApi;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.service.PhoneCheckOutService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/common/check")
public class PhoneCheckOutController implements PhoneCheckOutApi {

    private final PhoneCheckOutService phoneCheckOutService;

    public PhoneCheckOutController(PhoneCheckOutService phoneCheckOutService) {
        this.phoneCheckOutService = phoneCheckOutService;
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @Override
    public TlongResultDto sendMessage(@RequestParam String phone) {
        return phoneCheckOutService.sendMessage(phone);
    }

    /**
     * 验证手机验证码
     */
    @Override
    public TlongResultDto checkMessage(@RequestParam String phone, @RequestParam String checkCode) {
        return phoneCheckOutService.checkMessage(phone,checkCode);
    }
}
