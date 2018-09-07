package com.tlong.center.service;

import com.tlong.center.api.dto.app.activity.InvitationCodeRequestDto;
import com.tlong.center.api.dto.app.coupon.FillInBeInvitationCodeRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.exception.CustomException;
import com.tlong.center.common.utils.InvitationUtils;
import com.tlong.center.domain.app.activity.InvitationCode;
import com.tlong.center.domain.app.activity.UserInvitation;
import com.tlong.center.domain.repository.InvitationCodeRepository;
import com.tlong.center.domain.repository.UserInvitationRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.Objects;

import static com.tlong.center.domain.app.activity.QUserInvitation.userInvitation;
import static com.tlong.center.domain.app.activity.QInvitationCode.invitationCode1;

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
            Long codeId = setNewCode(invitation);
            setNewUserInvitation(userId, codeId);
            return new InvitationCodeRequestDto(invitation,0);
        }

    }


    //
    private Long setNewCode(String invitation){
        InvitationCode invitationCode = new InvitationCode();
        invitationCode.setCurState(1);
        invitationCode.setIsDeleted(0);
        invitationCode.setInvitationCode(invitation);
        InvitationCode saveCode = invitationCodeRepository.save(invitationCode);
        return saveCode.getId();
    }

    //
    private Long setNewUserInvitation(Long userId, Long codeId){
        UserInvitation userInvitation = new UserInvitation();
        userInvitation.setUserId(userId);
        userInvitation.setInvitationId(codeId);
        userInvitation.setCurState(1);
        userInvitation.setIsDeleted(0);
        userInvitation.setInvitationUserCount(0);
        UserInvitation save = userInvitationRepository.save(userInvitation);
        return save.getId();
    }

    /**
     * 填写他人邀请码
     */
    public TlongResultDto fillInInvitationCode(FillInBeInvitationCodeRequestDto requestDto) {
        //修改邀请码表中当前用户的邀请人字段
        UserInvitation one = userInvitationRepository.findOne(userInvitation.userId.eq(requestDto.getUserId()));
        if (Objects.isNull(one)){
            throw new CustomException("当前用户邀请码不存在!");
        }
        if (one.getBeInvitedUserId() != null){
            return new TlongResultDto(1,"当前用户已经填写过邀请码");
        }
        UserInvitation one1 = userInvitationRepository.findOne(invitationCode1.invitationCode.eq(requestDto.getInvitationCode()));
        if (Objects.isNull(one1)){
            throw new CustomException("填写的邀请码有误");
        }
        one.setBeInvitedUserId(one1.getId());
        userInvitationRepository.save(one);

        //更新邀请人邀请人数
        UserInvitation one2 = userInvitationRepository.findOne(one1.getId());
        one2.setInvitationUserCount(one2.getInvitationUserCount() != null ? one2.getInvitationUserCount() + 1 : 1);
        userInvitationRepository.save(one2);

        return new TlongResultDto(0,"邀请码验证成功");

    }
}
