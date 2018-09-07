package com.tlong.center.domain.app.activity;

import javax.persistence.*;

@Entity
@Table(name = "tlong_user_invitation")
public class UserInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //用户id
    private Long userId;

    //邀请码id
    private Long invitationId;

    //邀请人id
    private Long beInvitedUserId;

    //已邀请人数(父id是本人邀请码人数)
    private Integer invitationUserCount;

    //当前状态
    private Integer curState;

    //是否删除
    private Integer isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public Long getBeInvitedUserId() {
        return beInvitedUserId;
    }

    public void setBeInvitedUserId(Long beInvitedUserId) {
        this.beInvitedUserId = beInvitedUserId;
    }

    public Integer getInvitationUserCount() {
        return invitationUserCount;
    }

    public void setInvitationUserCount(Integer invitationUserCount) {
        this.invitationUserCount = invitationUserCount;
    }

    public Integer getCurState() {
        return curState;
    }

    public void setCurState(Integer curState) {
        this.curState = curState;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
