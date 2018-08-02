package com.tlong.center.domain.app.activity;

import javax.persistence.*;

@Entity
@Table(name = "tlong_invitation_code")
public class InvitationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //邀请码
    private String invitationCode;

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

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
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
