package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.GoodsTypeResponseDto;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.GoodsClassRequestDto;
import com.tlong.center.api.dto.web.WebGoodsClassRequestDto;
import com.tlong.center.domain.app.goods.AppGoodsPriceSystem;
import com.tlong.center.domain.app.goods.AppGoodsclass;
import com.tlong.center.domain.repository.GoodsClassRepository;
import com.tlong.center.domain.repository.GoodsPriceSystemRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tlong.center.domain.app.goods.QAppGoodsPriceSystem.appGoodsPriceSystem;
import static com.tlong.center.domain.app.goods.QAppGoodsclass.appGoodsclass;

@Component
@Transactional
public class GoodsClassService {
    final GoodsClassRepository repository;
    final EntityManager entityManager;
    JPAQueryFactory queryFactory;
    final GoodsPriceSystemRepository systemRepository;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public GoodsClassService(EntityManager entityManager, GoodsClassRepository repository, GoodsPriceSystemRepository systemRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.systemRepository = systemRepository;
    }

    /**
     * 分页查询分类列表
     *
     * @return
     */
    public GoodsClassRequestDto findAllGoodsClass() {
        List<Tuple> tuples = queryFactory.select(appGoodsclass.id, appGoodsclass.goodsClassName, appGoodsclass.goodsClassLevel, appGoodsclass.goodsClassIdParent,
                appGoodsPriceSystem.factoryRatio, appGoodsPriceSystem.lagshipRatio, appGoodsPriceSystem.originatorRatio, appGoodsPriceSystem.storeRatio, appGoodsclass.publishTime, appGoodsPriceSystem.intervalUp, appGoodsPriceSystem.intervalDown)
                .from(appGoodsclass, appGoodsPriceSystem)
                .where(appGoodsclass.id.eq(appGoodsPriceSystem.goodsClassId))
                .fetch();
        List<WebGoodsClassRequestDto> requestOne = new ArrayList<>();
        List<WebGoodsClassRequestDto> requestTwo = new ArrayList<>();
        final String[] tag = {""};
        tuples.stream().forEach(one -> {
            WebGoodsClassRequestDto requestDto = new WebGoodsClassRequestDto(one.get(appGoodsclass.id), one.get(appGoodsclass.goodsClassName), one.get(appGoodsclass.goodsClassLevel)
                    , one.get(appGoodsclass.goodsClassIdParent), one.get(appGoodsPriceSystem.originatorRatio), one.get(appGoodsPriceSystem.lagshipRatio), one.get(appGoodsPriceSystem.storeRatio), one.get(appGoodsPriceSystem.factoryRatio),
                    one.get(appGoodsclass.publishTime), one.get(appGoodsPriceSystem.intervalUp), one.get(appGoodsPriceSystem.intervalDown));
            if (one.get(appGoodsclass.goodsClassLevel) == 0)
                requestOne.add(requestDto);
            else if (one.get(appGoodsclass.goodsClassLevel) == 1) {
                if (tag[0].indexOf(requestDto.getClassName()) > -1)
                    return;
                tag[0] += requestDto.getClassName();
                requestTwo.add(requestDto);
            }
        });
        GoodsClassRequestDto goodsClassRequestDto = new GoodsClassRequestDto();
        goodsClassRequestDto.setGoodsClassOneDtos(requestOne);
        goodsClassRequestDto.setGoodsClassTwoDtos(requestTwo);
        return goodsClassRequestDto;
    }

    /**
     * 添加分类
     *
     * @return
     */
    public Result addGoodsClass(WebGoodsClassRequestDto requestDto) {
        AppGoodsclass appGoodsclass = new AppGoodsclass();
        appGoodsclass.setGoodsClassIdParent(requestDto.getParentClassId());
        if (appGoodsclass.getGoodsClassIdParent() == 0)
            appGoodsclass.setGoodsClassLevel(0);
        else
            appGoodsclass.setGoodsClassLevel(1);
        appGoodsclass.setGoodsClassName(requestDto.getClassName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        appGoodsclass.setPublishTime(simpleDateFormat.format(new Date()));
        AppGoodsclass appGoodsclass1 = repository.save(appGoodsclass);
        AppGoodsPriceSystem appGoodsPriceSystem = new AppGoodsPriceSystem();
        appGoodsPriceSystem.setGoodsClassId(appGoodsclass1.getId());
        appGoodsPriceSystem.setIntervalDown(requestDto.getIntervalDown());
        appGoodsPriceSystem.setIntervalUp(requestDto.getIntervalUp());
        appGoodsPriceSystem.setFactoryRatio(requestDto.getFactoryRatio());
        appGoodsPriceSystem.setLagshipRatio(requestDto.getLagshipRatio());
        appGoodsPriceSystem.setOriginatorRatio(requestDto.getOriginatorRatio());
        appGoodsPriceSystem.setStoreRatio(requestDto.getStoreRatio());
        AppGoodsPriceSystem appGoodsPriceSystem1 = systemRepository.save(appGoodsPriceSystem);
        if (appGoodsPriceSystem1 == null) {
            return new Result(0, "添加失败");
        }
        return new Result(1, "添加成功");
    }

    /**
     * 删除分类
     *
     * @return
     */
    public Result delGoodsClass(Long id) {
        Iterable<AppGoodsclass> goodsclass = repository.findAll(appGoodsclass.id.longValue().eq(id).or(appGoodsclass.goodsClassIdParent.longValue().eq(id)));
        if (goodsclass == null)
            return new Result(0, "删除失败");
        goodsclass.forEach(one -> {
            AppGoodsPriceSystem systemRepositoryOne = systemRepository.findOne(appGoodsPriceSystem.goodsClassId.longValue().eq(one.getId()));
            systemRepository.delete(systemRepositoryOne.getId());
            repository.delete(one.getId());
        });
        return new Result(1, "删除成功");
    }

    /**
     * 查找单个分类
     *
     * @return
     */
    public WebGoodsClassRequestDto findGoodsTypeById(WebGoodsClassRequestDto requestDto) {
        AppGoodsclass appGoodsclass = repository.findOne(requestDto.getId());
        AppGoodsPriceSystem system = systemRepository.findOne(appGoodsPriceSystem.goodsClassId.longValue().eq(requestDto.getId()).and(appGoodsPriceSystem.intervalUp.doubleValue().eq(requestDto.getIntervalUp())));
        WebGoodsClassRequestDto webGoodsClassRequestDto = system.toDto();
        webGoodsClassRequestDto.setId(appGoodsclass.getId());
        webGoodsClassRequestDto.setClassName(appGoodsclass.getGoodsClassName());
        webGoodsClassRequestDto.setParentClassId(appGoodsclass.getGoodsClassIdParent());
        return webGoodsClassRequestDto;
    }

    /**
     * 修改分类
     *
     * @return
     */
    public Result updateGoodsType(WebGoodsClassRequestDto requestDto) {
        AppGoodsclass appGoodsclass = repository.findOne(requestDto.getId());
        appGoodsclass.setGoodsClassName(requestDto.getClassName());
        AppGoodsclass appGoodsclass1 = repository.save(appGoodsclass);
        AppGoodsPriceSystem system = systemRepository.findOne(appGoodsPriceSystem.goodsClassId.longValue().eq(requestDto.getId()));
        AppGoodsPriceSystem system1 = new AppGoodsPriceSystem(requestDto);
        system1.setId(system.getId());
        system1.setGoodsClassId(system.getGoodsClassId());
        AppGoodsPriceSystem appGoodsPriceSystem = systemRepository.save(system1);
        if (appGoodsclass1 != null && appGoodsPriceSystem != null)
            return new Result(1, "修改成功");
        return new Result(0, "修改失败");
    }

    /**
     * 查询一级分类
     *
     * @return
     */
    public List<GoodsTypeResponseDto> findGoodsClass() {
        List<GoodsTypeResponseDto> goodsTypeResponseDtos = new ArrayList<>();
        Iterable<AppGoodsclass> goodsclasses = repository.findAll(appGoodsclass.goodsClassLevel.intValue().eq(0));
        for (AppGoodsclass goodsclass : goodsclasses) {
            GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
            goodsTypeResponseDto.setId(goodsclass.getId());
            goodsTypeResponseDto.setClassName(goodsclass.getGoodsClassName());
            goodsTypeResponseDtos.add(goodsTypeResponseDto);
        }
        return goodsTypeResponseDtos;
    }

    /**
     * 查询二级分类
     *
     * @return
     */
    public List<GoodsTypeResponseDto> findGoodsTwoClass(Long id) {
        List<GoodsTypeResponseDto> goodsTypeResponseDtos = new ArrayList<>();
        Iterable<AppGoodsclass> goodsclasses = repository.findAll(appGoodsclass.goodsClassIdParent.longValue().eq(id));
        for (AppGoodsclass goodsclass : goodsclasses) {
            GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
            goodsTypeResponseDto.setId(goodsclass.getId());
            goodsTypeResponseDto.setClassName(goodsclass.getGoodsClassName());
            goodsTypeResponseDtos.add(goodsTypeResponseDto);
        }
        return goodsTypeResponseDtos;
    }

    public GoodsClassRequestDto findAllGoodsClassByName(String goodsClassName) {
        List<Tuple> tuples = queryFactory.select(appGoodsclass.id, appGoodsclass.goodsClassName, appGoodsclass.goodsClassLevel, appGoodsclass.goodsClassIdParent,
                appGoodsPriceSystem.factoryRatio, appGoodsPriceSystem.lagshipRatio, appGoodsPriceSystem.originatorRatio, appGoodsPriceSystem.storeRatio, appGoodsclass.publishTime, appGoodsPriceSystem.intervalUp, appGoodsPriceSystem.intervalDown)
                .from(appGoodsclass, appGoodsPriceSystem)
                .where(appGoodsclass.id.eq(appGoodsPriceSystem.goodsClassId).and(appGoodsclass.goodsClassName.eq(goodsClassName)))
                .fetch();
        List<WebGoodsClassRequestDto> requestOne = new ArrayList<>();
        tuples.stream().forEach(one -> {
            WebGoodsClassRequestDto requestDto = new WebGoodsClassRequestDto(one.get(appGoodsclass.id), one.get(appGoodsclass.goodsClassName), one.get(appGoodsclass.goodsClassLevel)
                    , one.get(appGoodsclass.goodsClassIdParent), one.get(appGoodsPriceSystem.originatorRatio), one.get(appGoodsPriceSystem.lagshipRatio), one.get(appGoodsPriceSystem.storeRatio), one.get(appGoodsPriceSystem.factoryRatio),
                    one.get(appGoodsclass.publishTime), one.get(appGoodsPriceSystem.intervalUp), one.get(appGoodsPriceSystem.intervalDown));
            requestOne.add(requestDto);
        });
        GoodsClassRequestDto goodsClassRequestDto = new GoodsClassRequestDto();
        goodsClassRequestDto.setGoodsClassOneDtos(requestOne);
        return goodsClassRequestDto;
    }
}
