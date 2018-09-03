package com.tlong.center.common.user;

import com.tlong.center.api.dto.user.settings.TlongUserSettingsRequestDto;
import com.tlong.center.domain.common.user.QTlongUserSettings;
import com.tlong.center.domain.common.user.TlongUserSettings;
import com.tlong.center.domain.repository.TlongUserSettingsRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.Objects;

import static com.tlong.center.domain.common.user.QTlongUserSettings.tlongUserSettings;

@Component
@Transactional
public class UserSettingsSerivce {

    private final TlongUserSettingsRepository repository;

    public UserSettingsSerivce(TlongUserSettingsRepository repository) {
        this.repository = repository;
    }

    /**
     * 修改设置商品发布 重新发布数量
     */
    public void updateGoodsReleaseNumber(TlongUserSettingsRequestDto req) {
        //获取用户类型对应的可以上架的商品的数量 用户类型(0个人 1企业 2个人默认设置 3企业默认设置)
        TlongUserSettings one = repository.findOne(tlongUserSettings.userId.eq(req.getUserId()));
        if (Objects.nonNull(one)){
            //个人的数量
            one.setGoodsReleaseNumber(req.getPersonReleaseNumber());
            one.setGoodsReReleaseNumber(req.getPersonReReleaseNumber());
            repository.save(one);
        }
    }

    /**
     * 创建用户基础设置  设置商品发布 重新发布数量
     */
    private void createTlongUserSettings(TlongUserSettingsRequestDto req, Integer userType){
            TlongUserSettings tlongUserSettings = new TlongUserSettings();
            tlongUserSettings.setUserType(userType);
            tlongUserSettings.setGoodsReleaseNumber(req.getPersonReleaseNumber());
            tlongUserSettings.setGoodsReReleaseNumber(req.getPersonReReleaseNumber());
            repository.save(tlongUserSettings);
    }

    /**
     * 新增代理商或者供应商的设置
     */
    public void addUserSettings(Long userId, Integer userType,Integer isCompany){
        //获取用户类型支持的商品发布数量和重新发布数量  这里的userType是是否是公司
        TlongUserSettings one1 = repository.findOne(QTlongUserSettings.tlongUserSettings.userType.intValue().eq(isCompany)
            .and(QTlongUserSettings.tlongUserSettings.userId.isNull()));

        //在用户设置表中设置当前注册人的商品发布数量和重新发布数量
        if (Objects.nonNull(one1)) {
            TlongUserSettings tlongUserSettings = new TlongUserSettings();
            tlongUserSettings.setGoodsReleaseNumber(one1.getGoodsReleaseNumber());
            tlongUserSettings.setGoodsReReleaseNumber(one1.getGoodsReReleaseNumber());
            tlongUserSettings.setUserType(isCompany);
            tlongUserSettings.setUserId(userId);
            repository.save(tlongUserSettings);
        }
    }
}
