package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.user.settings.TlongUserSettingsRequestDto;
import com.tlong.center.common.code.CodeUtil;
import com.tlong.center.domain.common.code.TlongCodeRule;
import com.tlong.center.domain.repository.TlongCodeRepository;
import com.tlong.center.domain.repository.TlongCodeRuleRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

import static com.tlong.center.domain.common.code.QTlongCode.tlongCode;
import static com.tlong.center.domain.common.code.QTlongCodeRule.tlongCodeRule;
import static com.tlong.center.domain.common.user.QTlongUserSettings.tlongUserSettings;

@Component
@Transactional
public class CodeService {

    final EntityManager entityManager;
    final TlongCodeRuleRepository repository;
    final TlongCodeRepository codeRepository;
    JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public CodeService(EntityManager entityManager, TlongCodeRuleRepository repository, TlongCodeRepository codeRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.codeRepository = codeRepository;
    }


    /**
     * 设置或者修改商品编码前缀规则 数据库需要预设置 只提供修改功能不提供新增功能
     */
    public void updateCodeRule(TlongUserSettingsRequestDto req) {
        TlongCodeRule one = repository.findOne(tlongCodeRule.userType.eq(0)
                .and(tlongCodeRule.type.eq(1)));
        TlongCodeRule two = repository.findOne(tlongCodeRule.userType.eq(1)
                .and(tlongCodeRule.type.eq(1)));

        int head=0;
        if (Objects.nonNull(one)) {
            head=one.getHeadContent().length();
            one.setHeadContent(req.getPersonHeadContent());
            one.setPlaceholder("0");
            repository.save(one);
            createCode(0,head);
        } else {
            createCodeRule(req, 0);
        }
        if (Objects.nonNull(two)) {
            head=two.getHeadContent().length();
            two.setHeadContent(req.getCompanyHeadContent());
            two.setPlaceholder("0");
            repository.save(two);
            createCode(1,head);
        } else {
            createCodeRule(req, 1);
        }
    }

    public void createCode(Integer type,int head) {
        new CodeUtil(repository, codeRepository, entityManager).goodsCode(1, type,head);
    }


    private void createCodeRule(TlongUserSettingsRequestDto req, Integer userType) {

        if (userType == 0) {
            TlongCodeRule tlongCodeRule = new TlongCodeRule();
            tlongCodeRule.setHeadContent(req.getPersonHeadContent());
            tlongCodeRule.setUserType(0);
            tlongCodeRule.setType(1);
            tlongCodeRule.setTotalLength(6);
            tlongCodeRule.setPlaceholder("0");
            repository.save(tlongCodeRule);
            createCode(0,0);
        } else {
            TlongCodeRule tlongCodeRule = new TlongCodeRule();
            tlongCodeRule.setHeadContent(req.getCompanyHeadContent());
            tlongCodeRule.setUserType(1);
            tlongCodeRule.setType(1);
            tlongCodeRule.setPlaceholder("0");
            tlongCodeRule.setTotalLength(6);
            repository.save(tlongCodeRule);
            createCode(1,0);
        }

    }

    public TlongUserSettingsRequestDto findParam() {
        TlongUserSettingsRequestDto requestDto = new TlongUserSettingsRequestDto();
        List<Tuple> tuples = queryFactory.select(tlongCode.code, tlongCodeRule.headContent, tlongUserSettings.goodsReleaseNumber, tlongUserSettings.goodsReReleaseNumber)
                .from(tlongCode, tlongCodeRule, tlongUserSettings)
                .where(tlongCode.ruleId.eq(tlongCodeRule.id)
                        .and(tlongCodeRule.userType.eq(tlongUserSettings.userType))
                        .and(tlongCodeRule.userType.intValue().eq(0)))
                .fetch();
        tuples.stream().forEach(one -> {
            requestDto.setPersonHeadContent(one.get(tlongCodeRule.headContent));
            requestDto.setPersonReleaseNumber(one.get(tlongUserSettings.goodsReleaseNumber));
            requestDto.setPersonReReleaseNumber(one.get(tlongUserSettings.goodsReReleaseNumber));
            requestDto.setPersonBehind(one.get(tlongCode.code).substring(one.get(tlongCodeRule.headContent).length(), one.get(tlongCode.code).length()));
        });
        List<Tuple> tuples1 = queryFactory.select(tlongCode.code, tlongCodeRule.headContent, tlongUserSettings.goodsReleaseNumber, tlongUserSettings.goodsReReleaseNumber)
                .from(tlongCode, tlongCodeRule, tlongUserSettings)
                .where(tlongCode.ruleId.eq(tlongCodeRule.id)
                        .and(tlongCodeRule.userType.eq(tlongUserSettings.userType))
                        .and(tlongCodeRule.userType.intValue().eq(1)))
                .fetch();
        tuples1.stream().forEach(one -> {
            requestDto.setCompanyHeadContent(one.get(tlongCodeRule.headContent));
            requestDto.setCompanyReleaseNumber(one.get(tlongUserSettings.goodsReleaseNumber));
            requestDto.setCompanyReReleaseNumber(one.get(tlongUserSettings.goodsReReleaseNumber));
            requestDto.setCompanyBehind(one.get(tlongCode.code).substring(one.get(tlongCodeRule.headContent).length(), one.get(tlongCode.code).length()));
        });
        return requestDto;
    }

    public String createAllCode(Integer type,Integer userType,Integer head) {
        return new CodeUtil(repository, codeRepository, entityManager).goodsCode(type, userType,head);
    }

}
