package com.tlong.center.service;

import com.tlong.center.api.dto.app.activity.InvitationCodeRequestDto;
import com.tlong.center.common.utils.InvitationUtils;
import com.tlong.center.domain.app.activity.InvitationCode;
import com.tlong.center.domain.app.activity.UserInvitation;
import com.tlong.center.domain.repository.InvitationCodeRepository;
import com.tlong.center.domain.repository.UserInvitationRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.Objects;

import static com.tlong.center.domain.app.activity.QUserInvitation.userInvitation;

@Component
@Transactional
public class InvitationService {

    private final UserInvitationRepository userInvitationRepository;
    private final InvitationCodeRepository invitationCodeRepository;

    public InvitationService(UserInvitationRepository userInvitationRepository, InvitationCodeRepository invitationCodeRepository) {
        this.userInvitationRepository = userInvitationRepository;
        this.invitationCodeRepository = invitationCodeRepository;
    }

    /**
     * 获取我的邀请码
     */
    public InvitationCodeRequestDto myInvitationCode(Long userId) {
        //首先判断该用户是否生成过邀请码
        UserInvitation one = userInvitationRepository.findOne(userInvitation.userId.eq(userId));
        if (Objects.nonNull(one)){
            //查询已经邀请的用户数量
            InvitationCode one1 = invitationCodeRepository.findOne(one.getInvitationId());
            return new InvitationCodeRequestDto(one1.getInvitationCode(),one.getInvitationUserCount());
        }else {
            String invitation = InvitationUtils.createInvitation(userId);
            InvitationCode invitationCode = new InvitationCode();
            invitationCode.setCurState(1);
            invitationCode.setIsDeleted(0);
            invitationCode.setInvitationCode(invitation);
            invitationCodeRepository.save(invitationCode);
            return new InvitationCodeRequestDto(invitation,0);
        }

    }
}
