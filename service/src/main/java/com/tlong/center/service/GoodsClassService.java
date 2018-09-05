package com.tlong.center.service;

import com.google.common.collect.Iterables;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.GoodsTypeResponseDto;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.goods.GoodsTypeSearchRequestDto;
import com.tlong.center.api.dto.web.GoodsClassRequestDto;
import com.tlong.center.api.dto.web.WebGoodsClassRequestDto;
import com.tlong.center.api.dto.web.user.UserRequestDto;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.goods.AppGoodsPriceSystem;
import com.tlong.center.domain.app.goods.AppGoodsclass;
import com.tlong.center.domain.app.goods.QAppGoodsclass;
import com.tlong.center.domain.repository.GoodsClassRepository;
import com.tlong.center.domain.repository.GoodsPriceSystemRepository;
import com.tlong.center.domain.repository.TlongUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.tlong.center.domain.app.goods.QAppGoodsPriceSystem.appGoodsPriceSystem;
import static com.tlong.center.domain.app.goods.QAppGoodsclass.appGoodsclass;

@Component
@Transactional
public class GoodsClassService {
    private final GoodsClassRepository repository;
    private final TlongUserRepository tlongUserRepository;
    private final EntityManager entityManager;
    private final GoodsPriceSystemRepository systemRepository;
    JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public GoodsClassService(EntityManager entityManager, GoodsClassRepository repository, TlongUserRepository tlongUserRepository, GoodsPriceSystemRepository systemRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.tlongUserRepository = tlongUserRepository;
        this.systemRepository = systemRepository;
    }

    /**
     * 分页查询分类列表
     */
    public GoodsClassRequestDto findAllGoodsClass() {
        List<Tuple> tuples = queryFactory.select(appGoodsclass.id, appGoodsclass.goodsClassName, appGoodsclass.goodsClassLevel, appGoodsclass.goodsClassIdParent,
                appGoodsPriceSystem.factoryRatio, appGoodsPriceSystem.lagshipRatio, appGoodsPriceSystem.originatorRatio, appGoodsPriceSystem.storeRatio, appGoodsclass.publishTime, appGoodsPriceSystem.intervalUp, appGoodsPriceSystem.intervalDown)
                .from(appGoodsclass, appGoodsPriceSystem)
                .where(appGoodsclass.id.eq(appGoodsPriceSystem.goodsClassId))
                .fetch();
        List<WebGoodsClassRequestDto> requestOne = new ArrayList<>();
        List<WebGoodsClassRequestDto> requestTwo = new ArrayList<>();
        tuples.stream().forEach(one -> {
            WebGoodsClassRequestDto requestDto = new WebGoodsClassRequestDto(one.get(appGoodsclass.id), one.get(appGoodsclass.goodsClassName), one.get(appGoodsclass.goodsClassLevel)
                    , one.get(appGoodsclass.goodsClassIdParent), one.get(appGoodsPriceSystem.originatorRatio), one.get(appGoodsPriceSystem.lagshipRatio), one.get(appGoodsPriceSystem.storeRatio), one.get(appGoodsPriceSystem.factoryRatio),
                    one.get(appGoodsclass.publishTime), one.get(appGoodsPriceSystem.intervalUp), one.get(appGoodsPriceSystem.intervalDown));
            if (one.get(appGoodsclass.goodsClassLevel) == 0)
                requestOne.add(requestDto);
            else if (one.get(appGoodsclass.goodsClassLevel) == 1) {
                if (requestTwo.size() > 0) {
                    for (int i = 0; i < requestTwo.size(); i++) {
                        if (requestTwo.get(i).getClassName().equals(requestDto.getClassName()) && (requestTwo.get(i).getParentClassId().intValue() == requestDto.getParentClassId())) {
                            return;
                        }
                    }
                }
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
     */
    public Result addGoodsClass(WebGoodsClassRequestDto requestDto) {
        AppGoodsclass one = repository.findOne(QAppGoodsclass.appGoodsclass.goodsClassName.eq(requestDto.getClassName())
                .and(QAppGoodsclass.appGoodsclass.goodsClassIdParent.longValue().eq(requestDto.getParentClassId())));
        if (one != null)
            return new Result(0, "此分类已存在");
        AppGoodsclass appGoodsclass = new AppGoodsclass();
        appGoodsclass.setGoodsClassIdParent(requestDto.getParentClassId());
        if (appGoodsclass.getGoodsClassIdParent() == 0)
            appGoodsclass.setGoodsClassLevel(0);
        else
            appGoodsclass.setGoodsClassLevel(1);
        appGoodsclass.setGoodsClassName(requestDto.getClassName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
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
     */
    public Result updateGoodsType(WebGoodsClassRequestDto requestDto) {
        AppGoodsclass appGoodsclass = repository.findOne(requestDto.getId());
        appGoodsclass.setGoodsClassName(requestDto.getClassName());
        AppGoodsclass appGoodsclass1 = repository.save(appGoodsclass);
        AppGoodsPriceSystem system = systemRepository.findOne(appGoodsPriceSystem.goodsClassId.longValue().eq(requestDto.getId()).and(appGoodsPriceSystem.intervalUp.doubleValue().eq(requestDto.getIntervalUp())));
        AppGoodsPriceSystem system1 = new AppGoodsPriceSystem(requestDto);
        system1.setId(system.getId());
        system1.setGoodsClassId(system.getGoodsClassId());
        AppGoodsPriceSystem appGoodsPriceSystem = systemRepository.save(system1);
        if (appGoodsclass1 != null && appGoodsPriceSystem != null)
            return new Result(1, "修改成功");
        return new Result(0, "修改失败");
    }

//    /**
//     * 查询一级分类
//     *
//     * @return
//     */
//    public List<GoodsTypeResponseDto> findGoodsClass(HttpSession session) {
//        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
//        List<GoodsTypeResponseDto> goodsTypeResponseDtos = new ArrayList<>();
//        if (Objects.nonNull(user) && user.getUserType() != null && user.getUserType() == 1) {
//            String[] goodsClass = user.getGoodsClass().split(",");
//            if (goodsClass.length == 0) {
//                return goodsTypeResponseDtos;
//            }
//            HashSet<AppGoodsclass> appGoodsclasses = new HashSet<>();
//
//
////            ArrayList<Long> a1 = new ArrayList<>();
////            Arrays.asList(goodsClass).forEach(one -> a1.add(Long.valueOf(one)));
////            Iterable<AppGoodsclass> all = repository.findAll(appGoodsclass.id.in(a1));
////            List<AppGoodsclass> appGoodsClassesN = ToListUtil.IterableToList(all);
//
////            List<Long> collect = appGoodsClassesN.stream().map(AppGoodsclass::getGoodsClassIdParent).collect(Collectors.toList());
////            Iterable<AppGoodsclass> all1 = repository.findAll(appGoodsclass.id.in(collect));
////            List<AppGoodsclass> appGoodsclasses1 = ToListUtil.IterableToList(all1);
////            appGoodsclasses1.forEach(AppGoodsclass::toDto);
////            return appGoodsclasses1;
//
//
//            for (String goodsclass : goodsClass) {
//                AppGoodsclass appGoodsclass = repository.findOne(Long.valueOf(goodsclass));
//                AppGoodsclass appGoodsclass1 = repository.findOne(appGoodsclass.getGoodsClassIdParent());
//                appGoodsclasses.add(appGoodsclass1);
//            }
//            for (AppGoodsclass goodsclass : appGoodsclasses) {
//                GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
//                goodsTypeResponseDto.setId(goodsclass.getId());
//                goodsTypeResponseDto.setClassName(goodsclass.getGoodsClassName());
//                goodsTypeResponseDtos.add(goodsTypeResponseDto);
//            }
//            return goodsTypeResponseDtos;
//        } else {
//            Iterable<AppGoodsclass> goodsclasses = repository.findAll(appGoodsclass.goodsClassLevel.intValue().eq(0));
//            for (AppGoodsclass goodsclass : goodsclasses) {
//                GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
//                goodsTypeResponseDto.setId(goodsclass.getId());
//                goodsTypeResponseDto.setClassName(goodsclass.getGoodsClassName());
//                goodsTypeResponseDtos.add(goodsTypeResponseDto);
//            }
//            return goodsTypeResponseDtos;
//        }
//    }
//
//    /**
//     * 查询二级分类
//     *
//     * @return
//     */
//    public List<GoodsTypeResponseDto> findGoodsTwoClass(Long id, HttpSession session) {
//        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
//        List<GoodsTypeResponseDto> goodsTypeResponseDtos = new ArrayList<>();
//        if (user.getUserType() != null && user.getUserType() == 1) {
//            String[] goodsClass = user.getGoodsClass().split(",");
//            if (goodsClass.length == 0)
//                return goodsTypeResponseDtos;
//            Predicate pre = appGoodsclass.id.isNull();
//            for (String goodsclass : goodsClass) {
//                pre = ExpressionUtils.or(pre, appGoodsclass.id.longValue().eq(Long.valueOf(goodsclass)));
//            }
//            pre = ExpressionUtils.and(pre, appGoodsclass.goodsClassIdParent.longValue().eq(id));
//            Iterable<AppGoodsclass> goodsclasses = repository.findAll(pre);
//            for (AppGoodsclass goodsclass : goodsclasses) {
//                GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
//                goodsTypeResponseDto.setId(goodsclass.getId());
//                goodsTypeResponseDto.setClassName(goodsclass.getGoodsClassName());
//                goodsTypeResponseDtos.add(goodsTypeResponseDto);
//            }
//            return goodsTypeResponseDtos;
//        } else {
//            Iterable<AppGoodsclass> goodsclasses = repository.findAll(appGoodsclass.goodsClassIdParent.longValue().eq(id));
//            for (AppGoodsclass goodsclass : goodsclasses) {
//                GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
//                goodsTypeResponseDto.setId(goodsclass.getId());
//                goodsTypeResponseDto.setClassName(goodsclass.getGoodsClassName());
//                goodsTypeResponseDtos.add(goodsTypeResponseDto);
//            }
//            return goodsTypeResponseDtos;
//        }
//    }

    /**
     * 查询一级分类
     */
    public List<GoodsTypeResponseDto> findGoodsClassLevelOne() {
        Iterable<AppGoodsclass> all = repository.findAll(appGoodsclass.goodsClassLevel.eq(0));
        List<AppGoodsclass> appGoodsclasses = ToListUtil.IterableToList(all);
        return appGoodsclasses.stream().map(AppGoodsclass::toDto).collect(Collectors.toList());
    }

    /**
     * 根据一级分类查询二级分类列表
     */
    public List<GoodsTypeResponseDto> findGoodsLevelTwo(Long goodsClassId) {
        Iterable<AppGoodsclass> all = repository.findAll(appGoodsclass.goodsClassLevel.eq(1)
                .and(appGoodsclass.goodsClassIdParent.eq(goodsClassId)));
        List<AppGoodsclass> appGoodsclasses = ToListUtil.IterableToList(all);
        return appGoodsclasses.stream().map(AppGoodsclass::toDto).collect(Collectors.toList());
//        TlongUser user = new TlongUser();
//        user.setUserType(request.getUserType());
//        user.setGoodsClass(request.getGoodsClass());
//        user.setId(request.getUserId());
        //id = request.getId();
//        List<GoodsTypeResponseDto> goodsTypeResponseDtos = new ArrayList<>();
//        if (user.getUserType() != null && user.getUserType() == 1) {
//            String[] goodsClass = user.getGoodsClass().split(",");
//            if (goodsClass.length == 0)
//                return goodsTypeResponseDtos;
//            Predicate pre = appGoodsclass.id.isNull();
//            for (String goodsclass : goodsClass) {
//                pre = ExpressionUtils.or(pre, appGoodsclass.id.longValue().eq(Long.valueOf(goodsclass)));
//            }
//            pre = ExpressionUtils.and(pre, appGoodsclass.goodsClassIdParent.longValue().eq(id));
//            Iterable<AppGoodsclass> goodsclasses = repository.findAll(pre);
//            for (AppGoodsclass goodsclass : goodsclasses) {
//                GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
//                goodsTypeResponseDto.setId(goodsclass.getId());
//                goodsTypeResponseDto.setClassName(goodsclass.getGoodsClassName());
//                goodsTypeResponseDtos.add(goodsTypeResponseDto);
//            }
//            return goodsTypeResponseDtos;
//        } else {
//            Iterable<AppGoodsclass> goodsclasses = repository.findAll(appGoodsclass.goodsClassIdParent.longValue().eq(id));
//            for (AppGoodsclass goodsclass : goodsclasses) {
//                GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
//                goodsTypeResponseDto.setId(goodsclass.getId());
//                goodsTypeResponseDto.setClassName(goodsclass.getGoodsClassName());
//                goodsTypeResponseDtos.add(goodsTypeResponseDto);
//            }
//            return goodsTypeResponseDtos;
//        }
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

    public GoodsClassRequestDto searchGoodsType(GoodsTypeSearchRequestDto requestDto1) {
        final Predicate[] pre = {appGoodsclass.id.isNull()};
        final Predicate[] pre1 = {appGoodsclass.id.isNotNull()};
        List<Tuple> tuples;
        if (StringUtils.isNotEmpty(requestDto1.getGoodsTitle())) {
            AppGoodsclass appGoodsclass1 = repository.findOne(appGoodsclass.goodsClassName.eq(requestDto1.getGoodsTitle()));
            if (appGoodsclass1.getGoodsClassLevel() == 0) {
                Iterable<AppGoodsclass> all = repository.findAll(appGoodsclass.goodsClassIdParent.longValue().eq(appGoodsclass1.getId()));
                all.forEach(one -> {
                    pre[0] = ExpressionUtils.or(pre[0], appGoodsclass.id.longValue().eq(one.getId()));
                });
            } else {
                AppGoodsclass appGoodsclass2 = repository.findOne(appGoodsclass.id.longValue().eq(appGoodsclass1.getGoodsClassIdParent()));
                pre[0] = ExpressionUtils.or(pre[0], appGoodsclass.id.longValue().eq(appGoodsclass2.getId()));
            }
            pre[0] = ExpressionUtils.or(pre[0], appGoodsclass.id.longValue().eq(appGoodsclass1.getId()));
            if (requestDto1.getStartTime() != null && requestDto1.getEndTime() != null)
                pre[0] = ExpressionUtils.and(pre[0], appGoodsclass.publishTime.between(requestDto1.getStartTime() + " 00:00:00", requestDto1.getEndTime() + " 23:59:59"));
            else if (requestDto1.getStartTime() == null && requestDto1.getEndTime() != null)
                pre[0] = ExpressionUtils.and(pre[0], appGoodsclass.publishTime.lt(requestDto1.getEndTime() + " 23:59:59"));
            else if (requestDto1.getStartTime() != null && requestDto1.getEndTime() == null)
                pre[0] = ExpressionUtils.and(pre[0], appGoodsclass.publishTime.gt(requestDto1.getStartTime() + " 00:00:00"));
            pre[0] = ExpressionUtils.and(pre[0], appGoodsclass.id.eq(appGoodsPriceSystem.goodsClassId));
            tuples = queryFactory.select(appGoodsclass.id, appGoodsclass.goodsClassName, appGoodsclass.goodsClassLevel, appGoodsclass.goodsClassIdParent,
                    appGoodsPriceSystem.factoryRatio, appGoodsPriceSystem.lagshipRatio, appGoodsPriceSystem.originatorRatio, appGoodsPriceSystem.storeRatio, appGoodsclass.publishTime, appGoodsPriceSystem.intervalUp, appGoodsPriceSystem.intervalDown)
                    .from(appGoodsclass, appGoodsPriceSystem)
                    .where(pre[0])
                    .fetch();
        } else {
            if (requestDto1.getStartTime() != null && requestDto1.getEndTime() != null)
                pre1[0] = ExpressionUtils.and(pre1[0], appGoodsclass.publishTime.between(requestDto1.getStartTime() + " 00:00:00", requestDto1.getEndTime() + " 23:59:59"));
            else if (requestDto1.getStartTime() == null && requestDto1.getEndTime() != null)
                pre1[0] = ExpressionUtils.and(pre1[0], appGoodsclass.publishTime.lt(requestDto1.getEndTime() + " 23:59:59"));
            else if (requestDto1.getStartTime() != null && requestDto1.getEndTime() == null)
                pre1[0] = ExpressionUtils.and(pre1[0], appGoodsclass.publishTime.gt(requestDto1.getStartTime() + " 00:00:00"));
            pre1[0] = ExpressionUtils.and(pre1[0], appGoodsclass.id.eq(appGoodsPriceSystem.goodsClassId));
            tuples = queryFactory.select(appGoodsclass.id, appGoodsclass.goodsClassName, appGoodsclass.goodsClassLevel, appGoodsclass.goodsClassIdParent,
                    appGoodsPriceSystem.factoryRatio, appGoodsPriceSystem.lagshipRatio, appGoodsPriceSystem.originatorRatio, appGoodsPriceSystem.storeRatio, appGoodsclass.publishTime, appGoodsPriceSystem.intervalUp, appGoodsPriceSystem.intervalDown)
                    .from(appGoodsclass, appGoodsPriceSystem)
                    .where(pre1[0])
                    .fetch();
        }
        List<WebGoodsClassRequestDto> requestOne = new ArrayList<>();
        List<WebGoodsClassRequestDto> requestTwo = new ArrayList<>();
        tuples.stream().forEach(one -> {
            WebGoodsClassRequestDto requestDto = new WebGoodsClassRequestDto(one.get(appGoodsclass.id), one.get(appGoodsclass.goodsClassName), one.get(appGoodsclass.goodsClassLevel)
                    , one.get(appGoodsclass.goodsClassIdParent), one.get(appGoodsPriceSystem.originatorRatio), one.get(appGoodsPriceSystem.lagshipRatio), one.get(appGoodsPriceSystem.storeRatio), one.get(appGoodsPriceSystem.factoryRatio),
                    one.get(appGoodsclass.publishTime), one.get(appGoodsPriceSystem.intervalUp), one.get(appGoodsPriceSystem.intervalDown));
            if (one.get(appGoodsclass.goodsClassLevel) == 0)
                requestOne.add(requestDto);
            else if (one.get(appGoodsclass.goodsClassLevel) == 1) {
                if (requestTwo.size() > 0) {
                    for (int i = 0; i < requestTwo.size(); i++) {
                        if (requestTwo.get(i).getClassName().equals(requestDto.getClassName()) && (requestTwo.get(i).getParentClassId().intValue() == requestDto.getParentClassId())) {
                            return;
                        }
                    }
                }
                requestTwo.add(requestDto);
            }
        });
        GoodsClassRequestDto goodsClassRequestDto = new GoodsClassRequestDto();
        goodsClassRequestDto.setGoodsClassOneDtos(requestOne);
        goodsClassRequestDto.setGoodsClassTwoDtos(requestTwo);
        return goodsClassRequestDto;
    }

    public GoodsTypeResponseDto findOneGoodsClass(Long id) {
        AppGoodsclass appGoodsclass = repository.findOne(id);
        GoodsTypeResponseDto goodsTypeResponseDto = new GoodsTypeResponseDto();
        goodsTypeResponseDto.setGoodsClassName(appGoodsclass.getGoodsClassName());
        return goodsTypeResponseDto;
    }


    /**
     * 获取供应商可上货一级分类
     */
    public List<GoodsTypeResponseDto> supplierGoodsClass(Long userId) {
        TlongUser one = tlongUserRepository.findOne(userId);
        if (Objects.nonNull(one)) {
            Iterable<AppGoodsclass> all;
            String goodsClass = one.getGoodsClass();
            //判断是不是存在goodsClass
            if (StringUtils.isNotBlank(goodsClass)) {
                String[] str = goodsClass.split(",");
                List<Long> goodsClassIds = Arrays.stream(str).map(Long::valueOf).collect(Collectors.toList());
                //获取所有的对应的一级分类
                List<AppGoodsclass> all1 = repository.findAll(goodsClassIds);
                List<Long> collect = all1.stream().map(AppGoodsclass::getGoodsClassIdParent).collect(Collectors.toList());
                all = repository.findAll(collect);
            } else {
                //否则返回所有的一级分类
                return this.findGoodsClassLevelOne();
            }
            List<AppGoodsclass> appGoodsclasses = ToListUtil.IterableToList(all);
            //拼接返回数据
            return appGoodsclasses.stream().map(one2 -> {
                GoodsTypeResponseDto responseDto = new GoodsTypeResponseDto();
                responseDto.setId(one2.getId());
                responseDto.setGoodsClassName(one2.getGoodsClassName());
                return responseDto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取供应商可上传商品的二级分类
     */
    public List<GoodsTypeResponseDto> supplierGoodsClassLevelTwo(Long userId, Long goodsClassId) {
        TlongUser one = tlongUserRepository.findOne(userId);
        if (Objects.nonNull(one)) {
            String goodsClass = one.getGoodsClass();
            if (StringUtils.isNotBlank(goodsClass)) {
                String[] str = goodsClass.split(",");
                List<Long> goodsClassIds = Arrays.stream(str).map(Long::valueOf).collect(Collectors.toList());
                //获取所有的对应的一级分类
                Iterable<AppGoodsclass> all = repository.findAll(appGoodsclass.id.in(goodsClassIds)
                        .and(appGoodsclass.goodsClassIdParent.eq(goodsClassId)));
                List<AppGoodsclass> appGoodsclasses = ToListUtil.IterableToList(all);
                return appGoodsclasses.stream().map(AppGoodsclass::toDto).collect(Collectors.toList());
            } else {
                return this.findGoodsLevelTwo(goodsClassId);
            }
        }
        return null;
    }

    /**
     * 接获取当前供应商所有的二级分类的列表
     */
    public List<GoodsTypeResponseDto> selectAllGoodsClassLevelTwo(Long userId) {
        TlongUser one = tlongUserRepository.findOne(userId);
        if (Objects.nonNull(one)) {
            String goodsClass = one.getGoodsClass();
            if (StringUtils.isNotBlank(goodsClass)) {
                String[] str = goodsClass.split(",");
                List<Long> goodsClassIds = Arrays.stream(str).map(Long::valueOf).collect(Collectors.toList());
                List<AppGoodsclass> all = repository.findAll(goodsClassIds);
                return all.stream().map(AppGoodsclass::toDto).collect(Collectors.toList());
            }
        }
        return null;
    }
}