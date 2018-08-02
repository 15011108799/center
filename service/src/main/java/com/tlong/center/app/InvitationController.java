package com.tlong.center.app;

import com.tlong.center.api.app.activity.InvitationApi;
import com.tlong.center.api.dto.app.activity.InvitationCodeRequestDto;
import com.tlong.center.service.InvitationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/invitation")
public class InvitationController implements InvitationApi {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }


    /**
     * 获取我的邀请码
     */
    @Override
    public InvitationCodeRequestDto myInvitationCode(@RequestParam Long userId) {
        return invitationService.myInvitationCode(userId);
    }
}
