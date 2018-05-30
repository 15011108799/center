package com.tlong.center.common.code;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.domain.common.code.TlongCode;
import com.tlong.center.domain.common.code.TlongCodeRule;
import com.tlong.center.domain.repository.TlongCodeRepository;
import com.tlong.center.domain.repository.TlongCodeRuleRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.tlong.center.domain.common.code.QTlongCode.tlongCode;
import static com.tlong.center.domain.common.code.QTlongCodeRule.tlongCodeRule;


@Component
@Transactional
public class CodeUtil {
    final static Logger logger = LoggerFactory.getLogger(CodeUtil.class);

    final TlongCodeRuleRepository tlongCodeRuleRepository;
    final TlongCodeRepository tlongCodeRepository;
    final EntityManager entityManager;

    JPAQueryFactory queryFactory;
    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public CodeUtil(TlongCodeRuleRepository tlongCodeRuleRepository, TlongCodeRepository tlongCodeRepository, EntityManager entityManager) {
        this.tlongCodeRuleRepository = tlongCodeRuleRepository;
        this.tlongCodeRepository = tlongCodeRepository;
        this.entityManager = entityManager;
    }


    /**
     * 商品编码生成规则
     */
    public String goodsCode(Integer codeType,Integer userType){
        String content;
        String code;
        Integer totalLength1 = 6;
        String placeholder1 = "0";
        List<String> codes = queryFactory.select(tlongCode.code)
                .from(tlongCode,tlongCodeRule)
                .where(tlongCode.ruleId.eq(tlongCodeRule.id)
                    .and(tlongCodeRule.type.eq(codeType)))
                .orderBy(tlongCode.id.desc())
                .fetch();

        //得到编号
        if (CollectionUtils.isEmpty(codes)){
            content = "1";
        }else {
            Integer integer = Integer.valueOf(codes.get(0));
            content = String.valueOf(integer+1);
        }

        //读取规则
        TlongCodeRule tlongCodeRule1 = tlongCodeRuleRepository.findOne(tlongCodeRule.type.eq(codeType)
                .and(tlongCodeRule.userType.eq(userType)));
        if (Objects.isNull(tlongCodeRule1)){
            logger.error("未获取到对应的编码规则");
            return "ERROR";
        }
        //组装编码
        String headContent = tlongCodeRule1.getHeadContent();
            //获取总长度
        int totalLength = content.length() + headContent.length();
            //对比长度
        if (tlongCodeRule1.getTotalLength() != null) {
            totalLength1 = tlongCodeRule1.getTotalLength();
        }
        if (totalLength1 < totalLength){
            logger.error("编码长度过长请修改规则");
            return "ERROR";
        }else {
            //获取补位长度
            int i = tlongCodeRule1.getTotalLength() - totalLength;
            //组装code
            StringBuilder placeholder = new StringBuilder();
            if (tlongCodeRule1.getPlaceholder() != null){
                for(int j=0;j<i;j++){
                    placeholder.append(tlongCodeRule1.getPlaceholder());
                }
            }else {
                for(int j=0;j<i;j++){
                    placeholder.append(placeholder1);
                }
            }


            code = headContent + placeholder + content;
            //存储生成记录到code表
            TlongCode code1 = new TlongCode();
            code1.setCode(code);
            code1.setCreateTime(new Date());
            code1.setRuleId(tlongCodeRule1.getId());
            tlongCodeRepository.save(code1);
        }
        return code;
    }

    /**
     * 用户编码生成规则
     */

}
