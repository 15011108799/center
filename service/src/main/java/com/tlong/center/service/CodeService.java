package com.tlong.center.service;

import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.settings.TlongUserSettingsRequestDto;
import com.tlong.center.domain.common.code.TlongCodeRule;
import com.tlong.center.domain.repository.TlongCodeRuleRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Objects;

import static com.tlong.center.domain.common.code.QTlongCodeRule.tlongCodeRule;

@Component
@Transactional
public class CodeService {

    final EntityManager entityManager;
    final TlongCodeRuleRepository repository;


    public CodeService(EntityManager entityManager, TlongCodeRuleRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }


    /**
     * 设置或者修改商品编码前缀规则
     */
    public void updateCodeRule(TlongUserSettingsRequestDto req) {
        TlongCodeRule one = repository.findOne(tlongCodeRule.userType.eq(0)
            .and(tlongCodeRule.type.eq(1)));
        TlongCodeRule two = repository.findOne(tlongCodeRule.userType.eq(1)
                .and(tlongCodeRule.type.eq(1)));

        if (Objects.nonNull(one)){
            one.setHeadContent(req.getPersonHeadContent());
            repository.save(one);
        }else {
            createCodeRule(req,0);
        }
        if (Objects.nonNull(two)){
            two.setHeadContent(req.getCompanyHeadContent());
            repository.save(one);
        }else {
            createCodeRule(req,1);
        }
    }


    private void createCodeRule(TlongUserSettingsRequestDto req, Integer userType){

        if (userType == 0){
            TlongCodeRule tlongCodeRule = new TlongCodeRule();
            tlongCodeRule.setHeadContent(req.getPersonHeadContent());
            tlongCodeRule.setUserType(0);
            tlongCodeRule.setType(1);
            tlongCodeRule.setTotalLength(6);
            repository.save(tlongCodeRule);
        }else {
            TlongCodeRule tlongCodeRule = new TlongCodeRule();
            tlongCodeRule.setHeadContent(req.getCompanyHeadContent());
            tlongCodeRule.setUserType(1);
            tlongCodeRule.setType(1);
            tlongCodeRule.setTotalLength(6);
            repository.save(tlongCodeRule);
        }

    }
}
