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
        TlongUserSettings one;
        //获取用户类型对应的可以上架的商品的数量 用户类型(0个人 1企业 2个人默认设置 3企业默认设置)
         one = repository.findOne(tlongUserSettings.userType.eq(req.getUserType())
                .and(tlongUserSettings.userId.eq(req.getUserId())));
         if (one == null) {
             one = repository.findOne(tlongUserSettings.userType.eq(req.getUserType() + 2));
         }
        if (Objects.nonNull(one)){
            if (req.getUserType() == 0){
                one.setUserId(req.getUserId());
                one.setGoodsReleaseNumber(req.getPersonReleaseNumber());
                one.setGoodsReReleaseNumber(req.getPersonReReleaseNumber());
            }else if (req.getUserType() == 1){
                one.setGoodsReleaseNumber(req.getCompanyReleaseNumber());
                one.setGoodsReReleaseNumber(req.getCompanyReReleaseNumber());
            }
        }else {
            //创建默认参数
            createTlongUserSettings(req,2);
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
    public void addUserSettings(Long userId, Integer userType){
        //获取用户类型支持的商品发布数量和重新发布数量
        TlongUserSettings one1 = repository.findOne(QTlongUserSettings.tlongUserSettings.userType.intValue().eq(userType));

        //在用户设置表中设置当前注册人的商品发布数量和重新发布数量
        TlongUserSettings tlongUserSettings = new TlongUserSettings();
        tlongUserSettings.setGoodsReleaseNumber(one1.getGoodsReleaseNumber());
        tlongUserSettings.setGoodsReReleaseNumber(one1.getGoodsReReleaseNumber());
        tlongUserSettings.setUserId(userId);
    }
}
