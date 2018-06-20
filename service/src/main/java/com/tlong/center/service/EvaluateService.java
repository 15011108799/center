package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.EvaluateRequestDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.repository.AppUserRepository;
import com.tlong.center.domain.repository.WebEvaluateRepository;
import com.tlong.center.domain.web.WebEvaluate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.tlong.center.domain.app.goods.QWebGoods.webGoods;
import static com.tlong.center.domain.web.QWebEvaluate.webEvaluate;

@Component
@Transactional
public class EvaluateService {
    final WebEvaluateRepository repository;
    final EntityManager entityManager;
    final AppUserRepository appUserRepository;
    JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public EvaluateService(EntityManager entityManager, WebEvaluateRepository repository, AppUserRepository appUserRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.appUserRepository=appUserRepository;
    }

    /**
     * 分页查询评价列表
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<EvaluateRequestDto> findAllEvaluate(PageAndSortRequestDto requestDto) {
        PageResponseDto<EvaluateRequestDto> evaluateRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebEvaluate> evaluates = repository.findAll(pageRequest);
        List<EvaluateRequestDto> requestDtos = new ArrayList<>();
        evaluates.forEach(evaluate -> {
            List<Tuple> tuples = queryFactory.select(webEvaluate.id,webGoods.goodsHead,webGoods.goodsCode,webGoods.goodsPic,webEvaluate.content,webEvaluate.pics,webEvaluate.star,webEvaluate.time)
                    .from(webEvaluate, webGoods)
                    .where(webGoods.id.eq(webEvaluate.goodsId).and(webGoods.id.longValue().eq(evaluate.getGoodsId())))
                    .fetch();
            tuples.stream().forEach(one -> {
                 EvaluateRequestDto evaluateRequestDto=new EvaluateRequestDto();
                 evaluateRequestDto.setId(one.get(webEvaluate.id));
                 evaluateRequestDto.setGoodsName(one.get(webGoods.goodsHead));
                 evaluateRequestDto.setGoodsCode(one.get(webGoods.goodsCode));
                 evaluateRequestDto.setGoodsUrls(one.get(webGoods.goodsPic));
                 evaluateRequestDto.setContent(one.get(webEvaluate.content));
                 evaluateRequestDto.setPics(one.get(webEvaluate.pics));
                 evaluateRequestDto.setStar(one.get(webEvaluate.star));
                 evaluateRequestDto.setTime(one.get(webEvaluate.time));
                requestDtos.add(evaluateRequestDto);
            });
        });
        evaluateRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<WebEvaluate> evaluates1 = repository.findAll();
        evaluates1.forEach(order -> {
            count[0]++;
        });
        evaluateRequestDtoPageResponseDto.setCount(count[0]);
        return evaluateRequestDtoPageResponseDto;
    }
}
