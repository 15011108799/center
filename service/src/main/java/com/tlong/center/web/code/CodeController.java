package com.tlong.center.web.code;

import com.tlong.center.api.common.code.CodeApi;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.settings.TlongUserSettingsRequestDto;
import com.tlong.center.service.CodeService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/code")
public class CodeController implements CodeApi{

    final CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    /**
     * 修改商品编码规则(个人丶企业)
     */
    @Override
    public void updateCodeRule(@RequestBody TlongUserSettingsRequestDto req) {
        this.codeService.updateCodeRule(req);
    }

    @Override
    public TlongUserSettingsRequestDto findParam() {
        return codeService.findParam();
    }

    /**
     * 生成编码
     */
    @Override
    public String createCode(@RequestParam Integer codeType, @RequestParam Integer userType, @RequestParam Integer head) {
        return codeService.createAllCode(codeType,userType,head,0);
    }


}
