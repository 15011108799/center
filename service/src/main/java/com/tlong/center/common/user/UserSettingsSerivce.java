package com.tlong.center.common.user;

import com.tlong.center.api.dto.user.settings.TlongUserSettingsRequestDto;
import com.tlong.center.domain.common.user.TlongUserSettings;
import com.tlong.center.domain.repository.TlongUserSettingsRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.Objects;

import static com.tlong.center.domain.common.user.QTlongUserSettings.tlongUserSettings;

@Component
@Transactional
public class UserSettingsSerivce {

    final TlongUserSettingsRepository repository;

    public UserSettingsSerivce(TlongUserSettingsRepository repository) {
        this.repository = repository;
    }

    /**
     * 设置商品发布 重新发布数量
     */
    public void updateGoodsReleaseNumber(TlongUserSettingsRequestDto req) {
        //获取用户类型对应的可以上架的商品的数量 (个人丶企业)
        TlongUserSettings one = repository.findOne(tlongUserSettings.userType.eq(0));
        TlongUserSettings two = repository.findOne(tlongUserSettings.userType.eq(1));

        if(Objects.nonNull(one)){
            one.setGoodsReleaseNumber(req.getPersonReleaseNumber());
            one.setGoodsReReleaseNumber(req.getPersonReReleaseNumber());
            repository.save(one);
        }else {
            createTlongUserSettings(req,0);
        }
        if(Objects.nonNull(two)){
            two.setGoodsReleaseNumber(req.getCompanyReleaseNumber());
            two.setGoodsReReleaseNumber(req.getCompanyReReleaseNumber());
            repository.save(two);
        }else {
            createTlongUserSettings(req,1);
        }
    }

    private void createTlongUserSettings(TlongUserSettingsRequestDto req, Integer userType){
        if (userType == 0){
            TlongUserSettings tlongUserSettings = new TlongUserSettings();
            tlongUserSettings.setUserType(0);
            tlongUserSettings.setGoodsReleaseNumber(req.getPersonReleaseNumber());
            tlongUserSettings.setGoodsReReleaseNumber(req.getPersonReReleaseNumber());
            repository.save(tlongUserSettings);
        }else {
            TlongUserSettings tlongUserSettings = new TlongUserSettings();
            tlongUserSettings.setUserType(1);
            tlongUserSettings.setGoodsReleaseNumber(req.getCompanyReleaseNumber());
            tlongUserSettings.setGoodsReReleaseNumber(req.getCompanyReReleaseNumber());
            repository.save(tlongUserSettings);
        }

    }
}
