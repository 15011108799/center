package com.tlong.center.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.goods.GoodsSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebGoodsDetailResponseDto;
import com.tlong.center.api.dto.web.WebGoodsPageRequestDto;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.QTlongUser;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.goods.*;
import com.tlong.center.domain.repository.*;
import com.tlong.center.domain.web.QWebOrg;
import com.tlong.center.domain.web.WebOrg;
import com.tlong.core.utils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;

@Component
@Transactional
public class WebGoodsService {
    final EntityManager entityManager;
    final GoodsRepository repository;
    private final AppUserRepository appUserRepository;
    private final GoodsClassRepository goodsClassRepository;
    private final WebOrgRepository webOrgRepository;
    private final CodeService codeService;
    private final GoodsPriceSystemRepository goodsPriceSystemRepository;

    @Autowired
    public WebGoodsService(EntityManager entityManager, GoodsRepository repository, AppUserRepository appUserRepository, GoodsClassRepository goodsClassRepository, WebOrgRepository webOrgRepository, CodeService codeService, GoodsPriceSystemRepository goodsPriceSystemRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.goodsClassRepository = goodsClassRepository;
        this.webOrgRepository = webOrgRepository;
        this.codeService = codeService;
        this.goodsPriceSystemRepository = goodsPriceSystemRepository;
    }


    //本地方法 获取发布人姓名 手机号码
    private Map<Long,String[]> publishUserInfo(List<WebGoods> goods){
        Map<Long,String[]> publishUserInfoMap = new HashMap<>();
        List<Long> publishUserIds = new ArrayList<>();
        //过滤空的id值
        List<WebGoods> collect = goods.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            goods.forEach(one -> publishUserIds.add(one.getPublishUserId()));
            //过滤空值
            List<Long> collect1 = publishUserIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
            Iterable<TlongUser> all1 = appUserRepository.findAll(QTlongUser.tlongUser.id.in(collect1));
            all1.forEach(one -> {
                String[] info = new String[3];
                info[0] = one.getRealName();
                info[1] = one.getPhone();
                info[2] = one.getOrgId() + "";
                publishUserInfoMap.put(one.getId(), info);
            });
        }
        return publishUserInfoMap;
    }

    //本地方法获取机构名称
    private Map<Long,String> orgName(List<Long> orgIds){
        Map<Long, String> orgNameMap = new HashMap<>();
        //过滤空的id值
        List<Long> collect = orgIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            Iterable<WebOrg> all2 = webOrgRepository.findAll(QWebOrg.webOrg.id.in(collect));
            all2.forEach(one -> orgNameMap.put(one.getId(), one.getOrgName()));
        } return orgNameMap;
    }

    //获取审核人姓名
    private Map<Long,String> checkUserName(List<WebGoods> goods){
        Map<Long,String> checkUserNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(goods)) {
            ArrayList<Long> checkUserIds = new ArrayList<>();
            goods.forEach(one -> checkUserIds.add(one.getCheckUserId()));
            //过滤空的id值
            List<Long> collect = checkUserIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                Iterable<TlongUser> all2 = appUserRepository.findAll(QTlongUser.tlongUser.id.in(collect));
                all2.forEach(one -> checkUserNameMap.put(one.getId(), one.getUserName()));
            }
        }
        return checkUserNameMap;
    }

    //本地方法 获取商品分类名称
    private Map<Long,String> goodsClassName(List<WebGoods> goods){
        Map<Long,String> goodsClassNameMap = new HashMap<>();
        ArrayList<Long> goodsClassIds = new ArrayList<>();
        goods.forEach(one -> goodsClassIds.add(one.getGoodsClassId()));
        List<Long> collect = goodsClassIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)){
            Iterable<AppGoodsclass> all2 = goodsClassRepository.findAll(QAppGoodsclass.appGoodsclass.id.in(collect));
            all2.forEach(one -> goodsClassNameMap.put(one.getId(),one.getGoodsClassName()));
        }
        return goodsClassNameMap;
    }

    /**
     * 查询所有商品分页(带模糊搜索)
     */
    public Page<WebGoodsDetailResponseDto> findAllGoodsByPage(WebGoodsPageRequestDto requestDto, MultiValueMap<String,String> params) {
        //处理搜索条件
        Predicate[] pre = resove(params);

        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebGoods> all = repository.findAll(pre[0], pageRequest);

        //获取发布人信息
        Map<Long, String[]> publishUserInfoMap = this.publishUserInfo(all.getContent());

        //获取机构名称
        List<Long> orgIds = new ArrayList<>();
        publishUserInfoMap.forEach((key,value) -> orgIds.add(Long.valueOf(value[2])));
        Map<Long, String> orgNameMap = this.orgName(orgIds);

        //获取审核人姓名
        Map<Long, String> checkUserNameMap = this.checkUserName(all.getContent());

        //获取商品分类名称
        Map<Long, String> goodsClassNameMap = this.goodsClassName(all.getContent());

        return all.map(webGoods1 ->{
            WebGoodsDetailResponseDto response = webGoods1.toDto();
                response.setId(webGoods1.getId() + "");
                response.setPicType(webGoods1.getPicType() + "");
                response.setCircle(webGoods1.getCircle() + "");
                response.setNum(webGoods1.getNum() + "");
                response.setPriceType(webGoods1.getPriceType() + "");
                response.setFactoryPrice(webGoods1.getFactoryPrice() + "");
                response.setFlagshipPrice(webGoods1.getFlagshipPrice() + "");
                response.setFounderPrice(webGoods1.getFounderPrice() + "");
                response.setPublishPrice(webGoods1.getPublishPrice() + "");
                response.setCurState(webGoods1.getCurState() + "");
                response.setState(webGoods1.getState());
                //额外信息
                String[] infos = publishUserInfoMap.get(webGoods1.getPublishUserId());
                response.setPublishName(infos[0]);
                response.setPublishPhone(infos[1]);
                response.setOrgId(Long.valueOf(infos[2]) + "");
                response.setOrgName(orgNameMap.get(Long.valueOf(infos[2])));
                String s = checkUserNameMap.get(webGoods1.getCheckUserId());
                response.setCheckUserName(s);
                String s1 = goodsClassNameMap.get(webGoods1.getGoodsClassId());
                response.setGoodsClassId(s1);
            return response;
        });
    }

    /**
     * 添加商品
     */
    public Result add(WebGoodsDetailResponseDto reqDto) {
        //TODO 是否免审核
        reqDto.setGoodsPic(reqDto.getGoodsPic());
        reqDto.setCertificate(FileUploadUtils.readFile(reqDto.getCertificate()));
        reqDto.setVideo(FileUploadUtils.readFile(reqDto.getVideo()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        reqDto.setPublishTime(simpleDateFormat.format(new Date()));
        WebGoods webGoods = new WebGoods(reqDto);
        webGoods.setCertificate(reqDto.getCertificate());
        webGoods.setNewstime(new Date().getTime());
        webGoods.setIsCheck(0);
        webGoods.setState(0);
        webGoods.setState(1);
        if (reqDto.getGoodsClassId() != null && !reqDto.getGoodsClassId().equals(""))
            webGoods.setGoodsClassId(Long.valueOf(reqDto.getGoodsClassId()));
        if (reqDto.getPicType() != null && !reqDto.getPicType().equals(""))
            webGoods.setPicType(Integer.valueOf(reqDto.getPicType()));
        if (reqDto.getCircle() != null && !reqDto.getCircle().equals(""))
            webGoods.setCircle(reqDto.getCircle());
        if (reqDto.getNum() != null && !reqDto.getNum().equals(""))
            webGoods.setNum(Integer.valueOf(reqDto.getNum()));

        //设置价格信息
        AppGoodsPriceSystem one = goodsPriceSystemRepository.findOne(QAppGoodsPriceSystem.appGoodsPriceSystem.goodsClassId.eq(Long.valueOf(reqDto.getGoodsClassId()))
                .and(QAppGoodsPriceSystem.appGoodsPriceSystem.intervalDown.lt(Double.valueOf(reqDto.getPublishPrice())))
                .and(QAppGoodsPriceSystem.appGoodsPriceSystem.intervalUp.gt(Double.valueOf(reqDto.getPublishPrice()))));
        if (Objects.nonNull(one)) {
            if (reqDto.getPriceType() != null && !reqDto.getPriceType().equals(""))
                webGoods.setPriceType(Integer.valueOf(reqDto.getPriceType()));
                webGoods.setFactoryPrice(one.getFactoryRatio() * Double.valueOf(reqDto.getPublishPrice()));
                webGoods.setFlagshipPrice(one.getLagshipRatio() * Double.valueOf(reqDto.getPublishPrice()));
                webGoods.setFounderPrice(one.getOriginatorRatio() * Double.valueOf(reqDto.getPublishPrice()));
                webGoods.setPublishPrice(Double.valueOf(reqDto.getPublishPrice()));
        }

        //设置发布人信息
        webGoods.setPublishUserId(Long.valueOf(reqDto.getPublishUserId()));
        TlongUser one1 = appUserRepository.findOne(Long.valueOf(reqDto.getPublishUserId()));

        //设置商品编码
        if (one1.getIsCompany() == 0)
            webGoods.setGoodsCode(one1.getUserCode() + "-" + codeService.createAllCode(1, 0, 1,one1.getIsCompany()));
        else if (one1.getIsCompany() == 1)
            webGoods.setGoodsCode(one1.getUserCode() + "-" + codeService.createAllCode(1, 1, 1,one1.getIsCompany()));
        WebGoods webGoods1 = repository.save(webGoods);
        if (webGoods1 != null)
            return new Result(1, "添加成功");
        else
            return new Result(0, "添加失败");
    }

    /**
     * 删除商品
     */
    public Result delGoods(Long id) {
        WebGoods webGoods = repository.findOne(id);
        if (webGoods == null) {
            return new Result(0, "删除失败");
        }
        webGoods.setIsDeleted(1);
        repository.delete(id);
        return new Result(1, "删除成功");
    }

    /**
     * 修改商品为审核通过状态
     */
    public void updateGoodsState(Long id, Long checkUserId) {
        WebGoods webGoods = repository.findOne(id);
        webGoods.setIsCheck(1);
        webGoods.setState(1);
        //修改审核人id
        webGoods.setCheckUserId(checkUserId);
        repository.save(webGoods);
    }

    /**
     * 查询某一商品
     */
    public WebGoodsDetailResponseDto findGoodsById(Long id) {
        WebGoods webGoods1 = repository.findOne(id);
        WebGoodsDetailResponseDto webGoodsDetailResponseDto = webGoods1.toDto();
        AppGoodsclass appGoodsclass = goodsClassRepository.findOne(webGoods1.getGoodsClassId());
        webGoodsDetailResponseDto.setParentClassId(appGoodsclass.getGoodsClassIdParent());
            webGoodsDetailResponseDto.setId(webGoods1.getId() + "");
            webGoodsDetailResponseDto.setPublishUserId(webGoods1.getPublishUserId() + "");
            webGoodsDetailResponseDto.setRealStar(webGoods1.getRealStar() + "");
            webGoodsDetailResponseDto.setGoodsClassId(webGoods1.getGoodsClassId() + "");
            webGoodsDetailResponseDto.setPicType(webGoods1.getPicType() + "");
            webGoodsDetailResponseDto.setCircle(webGoods1.getCircle() + "");
//            webGoodsDetailResponseDto.setNum(webGoods1.getNum() + "");
//            webGoodsDetailResponseDto.setPriceType(webGoods1.getPriceType() + "");
            webGoodsDetailResponseDto.setFactoryPrice(webGoods1.getFactoryPrice() + "");
            webGoodsDetailResponseDto.setFlagshipPrice(webGoods1.getFlagshipPrice() + "");
            webGoodsDetailResponseDto.setFounderPrice(webGoods1.getFounderPrice() + "");
            webGoodsDetailResponseDto.setPublishPrice(webGoods1.getPublishPrice() + "");
//            webGoodsDetailResponseDto.setStorePrice(webGoods1.getStorePrice() + "");
            webGoodsDetailResponseDto.setCurState(webGoods1.getCurState() + "");
            webGoodsDetailResponseDto.setState(webGoods1.getState());
        return webGoodsDetailResponseDto;
    }

    /**
     * 修改商品信息
     */
    public Result updateGoods(String s, WebGoodsDetailResponseDto reqDto) {
        WebGoods webGoods1 = repository.findOne(Long.valueOf(reqDto.getId()));
        if (s.equals(""))
            reqDto.setGoodsPic(reqDto.getGoodsPic().substring(1));
        else
            reqDto.setGoodsPic(s.substring(0, s.length() - 1) + reqDto.getGoodsPic());
        String certificate = FileUploadUtils.readFile(reqDto.getCertificate());
        if (certificate != null && !certificate.equals(""))
            reqDto.setCertificate(certificate);
        else
            reqDto.setCertificate(webGoods1.getCertificate());
        String video = FileUploadUtils.readFile(reqDto.getVideo());
        if (video != null && !video.equals(""))
            reqDto.setVideo(video);
        else
            reqDto.setVideo(webGoods1.getVideo());
        WebGoods webGoods = new WebGoods(reqDto);
        webGoods.setIsCheck(0);
        webGoods.setGoodsCode(webGoods1.getGoodsCode());
        webGoods.setPublishTime(webGoods1.getPublishTime());
        webGoods.setId(Long.valueOf(reqDto.getId()));
        if (reqDto.getCurState() != null && !reqDto.getCurState().equals("null"))
            webGoods.setState(Integer.valueOf(reqDto.getCurState()));
        if (reqDto.getGoodsClassId() != null && !reqDto.getGoodsClassId().equals("null"))
            webGoods.setGoodsClassId(Long.valueOf(reqDto.getGoodsClassId()));
        if (reqDto.getRealStar() != null && !reqDto.getRealStar().equals("null"))
            webGoods.setRealStar(Integer.valueOf(reqDto.getRealStar()));
        if (reqDto.getPicType() != null && !reqDto.getPicType().equals("null"))
            webGoods.setPicType(Integer.valueOf(reqDto.getPicType()));
        if (reqDto.getCircle() != null && !reqDto.getCircle().equals("null"))
            webGoods.setCircle(reqDto.getCircle());
        if (reqDto.getNum() != null && !reqDto.getNum().equals("null"))
            webGoods.setNum(Integer.valueOf(reqDto.getNum()));
        if (reqDto.getPriceType() != null && !reqDto.getPriceType().equals("null"))
            webGoods.setPriceType(Integer.valueOf(reqDto.getPriceType()));
        if (reqDto.getFactoryPrice() != null && !reqDto.getFactoryPrice().equals("null"))
            webGoods.setFactoryPrice(Double.valueOf(reqDto.getFactoryPrice()));
        if (reqDto.getFlagshipPrice() != null && !reqDto.getFlagshipPrice().equals("null"))
            webGoods.setFlagshipPrice(Double.valueOf(reqDto.getFlagshipPrice()));
        if (reqDto.getFounderPrice() != null && !reqDto.getFounderPrice().equals("null"))
            webGoods.setFounderPrice(Double.valueOf(reqDto.getFounderPrice()));
        if (reqDto.getPublishPrice() != null && !reqDto.getPublishPrice().equals("null"))
            webGoods.setPublishPrice(Double.valueOf(reqDto.getPublishPrice()));
        if (reqDto.getStorePrice() != null && !reqDto.getStorePrice().equals("null"))
            webGoods.setStorePrice(Double.valueOf(reqDto.getStorePrice()));
        webGoods.setPublishUserId(webGoods1.getPublishUserId());
        WebGoods webGoods2 = repository.save(webGoods);
        if (webGoods2 != null)
            return new Result(1, "修改成功");
        else
            return new Result(0, "修改失败");
    }

    /**
     * 修改商品状态（锁定，解除锁定，结缘）
     */
    public void updateState(Long goodsId, int state) {
        WebGoods webGoods = repository.findOne(goodsId);
        if (webGoods.getState() == 0)
            webGoods.setState(0);
        else
            webGoods.setCurState(state);
        repository.save(webGoods);
    }

    /**
     * 设置审核驳回
     */
    public void updateGoodsStateReject(Long id) {
        WebGoods webGoods = repository.findOne(id);
//        webGoods.setIsCheck(2);
        webGoods.setState(2);
        repository.save(webGoods);
    }

    public PageResponseDto<WebGoodsDetailResponseDto> searchGoods(GoodsSearchRequestDto requestDto) {
        PageResponseDto<WebGoodsDetailResponseDto> responseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<WebGoods> webGoods;
        final Predicate[] pre = {QWebGoods.webGoods.id.isNotNull()};
        final Predicate[] pre1 = {QWebGoods.webGoods.id.isNull()};

        //商品分类模糊
        if (requestDto.getGoodsClassId() != null){
            //判断分类id十一级分类还是二级分类
            AppGoodsclass one = goodsClassRepository.findOne(requestDto.getGoodsClassId());
            if (Objects.nonNull(one)){
                Integer goodsClassLevel = one.getGoodsClassLevel();
                if (goodsClassLevel == 0){ //一级分类
                    //查出一级分类下所有的二级分类
                    Iterable<AppGoodsclass> all = goodsClassRepository.findAll(QAppGoodsclass.appGoodsclass.goodsClassIdParent.eq(one.getId()));
                    List<AppGoodsclass> appGoodsclasses = ToListUtil.IterableToList(all);
                    List<Long> ids = appGoodsclasses.stream().map(AppGoodsclass::getId).collect(Collectors.toList());
                    pre[0] = ExpressionUtils.and(pre[0], QWebGoods.webGoods.goodsClassId.in(ids));
                }else {
                    pre[0] = ExpressionUtils.and(pre[0], QWebGoods.webGoods.goodsClassId.eq(requestDto.getGoodsClassId()));
                }
            }
        }

        if (StringUtils.isNotBlank(requestDto.getPublishName())) {
            TlongUser tlongUser1 = appUserRepository.findOne(tlongUser.realName.eq(requestDto.getPublishName()));
            if (tlongUser1 != null) {
                pre[0] = ExpressionUtils.and(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(tlongUser1.getId()));
            }
        }

        if (requestDto.getCheckState() != null){
            pre[0] = ExpressionUtils.and(pre[0], QWebGoods.webGoods.state.eq(requestDto.getCheckState()));
        }
        if (requestDto.getGoodsState() != null){
            pre[0] = ExpressionUtils.and(pre[0], QWebGoods.webGoods.curState.eq(requestDto.getGoodsState()));
        }

        if (StringUtils.isNotBlank(requestDto.getGoodsName())){
            pre[0] = ExpressionUtils.and(pre[0], QWebGoods.webGoods.goodsHead.like("%" + requestDto.getGoodsName() + "%"));
        }
        if (StringUtils.isNotBlank(requestDto.getGoodsCode())) {
            pre[0] = ExpressionUtils.and(pre[0], QWebGoods.webGoods.goodsCode.like("%" + requestDto.getGoodsCode() + "%"));
        }
        if (requestDto.getIsCompany()!= null) {
            if (requestDto.getIsCompany() == 0 || requestDto.getIsCompany() == 1) {
                pre[0] = ExpressionUtils.and(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(requestDto.getUserId()));
            }
        }

        if (StringUtils.isNotBlank(requestDto.getPublishName())) {
            TlongUser tlongUser1 = appUserRepository.findOne(tlongUser.realName.eq(requestDto.getPublishName()));
            if (tlongUser1 != null) {
                pre1[0] = ExpressionUtils.and(pre1[0], QWebGoods.webGoods.publishUserId.longValue().eq(tlongUser1.getId()));
            }
        }
        if (StringUtils.isNotBlank(requestDto.getGoodsCode()))
            pre1[0] = ExpressionUtils.and(pre1[0], QWebGoods.webGoods.goodsCode.eq(requestDto.getGoodsCode()));
        if (requestDto.getGoodsState() != null)
            pre1[0] = ExpressionUtils.and(pre1[0], QWebGoods.webGoods.curState.eq(requestDto.getGoodsState()));
        if (requestDto.getCheckState()!= null)
            pre1[0] = ExpressionUtils.and(pre1[0], QWebGoods.webGoods.state.eq(requestDto.getCheckState()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre1[0] = ExpressionUtils.and(pre1[0], QWebGoods.webGoods.publishTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre1[0] = ExpressionUtils.and(pre1[0], QWebGoods.webGoods.publishTime.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre1[0] = ExpressionUtils.and(pre1[0], QWebGoods.webGoods.publishTime.gt(requestDto.getStartTime() + " 00:00:00"));
        webGoods = repository.findAll(pre[0], pageRequest);

        List<WebGoodsDetailResponseDto> requestDtos = new ArrayList<>();
        webGoods.forEach(webGoods1 -> {
            WebGoodsDetailResponseDto webGoodsDetailResponseDto = webGoods1.toDto();
            if (webGoods1.getNewstime() != null){
                webGoodsDetailResponseDto.setNewstime(webGoods1.getNewstime());
            }
            if (webGoods1.getId() != null)
                webGoodsDetailResponseDto.setId(webGoods1.getId() + "");
            if (webGoods1.getRealStar() != null)
                webGoodsDetailResponseDto.setRealStar(webGoods1.getStar() + "");
            if (webGoods1.getId() != null)
                webGoodsDetailResponseDto.setId(webGoods1.getId() + "");
            if (webGoods1.getPicType() != null)
                webGoodsDetailResponseDto.setPicType(webGoods1.getPicType() + "");
            if (webGoods1.getCircle() != null)
                webGoodsDetailResponseDto.setCircle(webGoods1.getCircle() + "");
            if (webGoods1.getNum() != null)
                webGoodsDetailResponseDto.setNum(webGoods1.getNum() + "");
            if (webGoods1.getPriceType() != null)
                webGoodsDetailResponseDto.setPriceType(webGoods1.getPriceType() + "");
            if (webGoods1.getFactoryPrice() != null)
                webGoodsDetailResponseDto.setFactoryPrice(webGoods1.getFactoryPrice() + "");
            if (webGoods1.getFlagshipPrice() != null)
                webGoodsDetailResponseDto.setFlagshipPrice(webGoods1.getFlagshipPrice() + "");
            if (webGoods1.getFounderPrice() != null)
                webGoodsDetailResponseDto.setFounderPrice(webGoods1.getFounderPrice() + "");
            if (webGoods1.getPublishPrice() != null)
                webGoodsDetailResponseDto.setPublishPrice(webGoods1.getPublishPrice() + "");
            if (webGoods1.getStorePrice() != null)
                webGoodsDetailResponseDto.setStorePrice(webGoods1.getStorePrice() + "");
            if (webGoods1.getState() != null){
                webGoodsDetailResponseDto.setState(webGoods1.getState());
            }
            if (webGoods1.getCurState() != null)
                webGoodsDetailResponseDto.setCurState(webGoods1.getCurState() + "");
            if (webGoods1.getPublishUserId() != null) {
                TlongUser tlongUser = appUserRepository.findOne(webGoods1.getPublishUserId());
                webGoodsDetailResponseDto.setPublishName(tlongUser.getRealName());
                WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser.getOrgId()));
                webGoodsDetailResponseDto.setOrgId(one.getOrgName());
                webGoodsDetailResponseDto.setPublishPhone(tlongUser.getPhone());
            }
            if (webGoods1.getCheckUserId() != null) {
                TlongUser one = appUserRepository.findOne(webGoods1.getCheckUserId());
                if (Objects.nonNull(one)) {
                    webGoodsDetailResponseDto.setCheckUserName(one.getUserName());
                }
            }
            if (webGoods1.getGoodsClassId() != null) {
                AppGoodsclass appGoodsclass = goodsClassRepository.findOne(webGoods1.getGoodsClassId());
                webGoodsDetailResponseDto.setGoodsClassId(appGoodsclass.getGoodsClassName());
            }
            requestDtos.add(webGoodsDetailResponseDto);
        });
        responseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<WebGoods> appGoods1;
        if (requestDto.getUserType() != null && requestDto.getUserType() == 1 && requestDto.getIsCompany() == 2)
            appGoods1 = repository.findAll(pre1[0]);
        else
            appGoods1 = repository.findAll(pre[0]);
        appGoods1.forEach(goods -> count[0]++);
        responseDto.setCount(count[0]);
        return responseDto;
    }

    public Result delBatchGoods(String goodsId) {
        String[] goodsIds;
        if (StringUtils.isNotBlank(goodsId)) {
            goodsIds = goodsId.split(",");
            for (String goodsId1 : goodsIds) {
                delGoods(Long.valueOf(goodsId1));
            }
        }
        return new Result(1, "删除成功");
    }

    /**
     * 重新发布商品
     */
    public Result publishAgain(WebGoodsDetailResponseDto reqDto) {
        WebGoods webGoods = repository.findOne(Long.valueOf(reqDto.getId()));
        WebGoods webGoods2 = joinWebGoods(webGoods, reqDto);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        webGoods.setPublishTime(simpleDateFormat.format(new Date()));
        webGoods.setNewstime(new Date().getTime() / 1000);
        WebGoods webGoods1 = repository.save(webGoods2);
        if (webGoods1 != null)
            return new Result(1, "重新发布成功");
        else
            return new Result(1, "重新发布失败");
    }

    private WebGoods joinWebGoods(WebGoods webGoods,WebGoodsDetailResponseDto webGoodsDto){
        PropertyUtils.copyPropertiesOfNotNull(webGoodsDto,webGoods);
        return webGoods;
    }


    /**
     * 商品多条件模糊查询
     */
    public Predicate[] resove(MultiValueMap<String,String> params) {

        String priceUp = params.getFirst("PriceUp");
        String priceDown = params.getFirst("PriceDown");
        String kindOfWater = params.getFirst("kindOfWater");
        String color = params.getFirst("color");
        String circle = params.getFirst("circle");
        String goodsState = params.getFirst("goodsState");
        String goodsName = params.getFirst("goodsName");
        String checkState = params.getFirst("checkState");
        String publishUserName = params.getFirst("publishUserName");
        String publishUserId = params.getFirst("publishUserId");
        String goodsCode = params.getFirst("goodsCode");
        String goodsClassId = params.getFirst("goodsClassId");
        String beginTime = params.getFirst("beginTime");
        String endTime = params.getFirst("endTime");

        BooleanExpression kindOfWaterEq = StringUtils.isNotBlank(kindOfWater) ? QWebGoods.webGoods.kindOfWater.eq(kindOfWater) : null;
        BooleanExpression colorLike = StringUtils.isNotBlank(color) ? QWebGoods.webGoods.color.like("%" + color + "%") : null;
        BooleanExpression circleEq = StringUtils.isNotBlank(circle) ? QWebGoods.webGoods.circle.eq(circle) : null;
        BooleanExpression goodsStateEq = StringUtils.isNotBlank(goodsState) ? QWebGoods.webGoods.curState.eq(Integer.valueOf(goodsState)) : null;
        BooleanExpression checkStateEq = StringUtils.isNotBlank(checkState) ? QWebGoods.webGoods.state.eq(Integer.valueOf(checkState)) : null;
        BooleanExpression publishUserNameLike = resoveUserName(publishUserName);
        BooleanExpression priceBetween = resovePrice(priceUp,priceDown);
        BooleanExpression goodsClassIdEq = resoveGoodsClass(goodsClassId);
        BooleanExpression goodsCodeLike = StringUtils.isNotBlank(goodsCode) ? QWebGoods.webGoods.goodsCode.like("%" + goodsCode + "%") : null;
        BooleanExpression publishUserIdEq = StringUtils.isNotBlank(publishUserId) ? QWebGoods.webGoods.publishUserId.eq(Long.valueOf(publishUserId)) : null;
        BooleanExpression goodsNameLike = StringUtils.isNotBlank(goodsName) ? QWebGoods.webGoods.goodsHead.like("%" + goodsName + "%") : null;
        BooleanExpression beforeEndTime = StringUtils.isNotBlank(endTime) ? QWebGoods.webGoods.newstime.lt(Long.valueOf(endTime)) : null;
        BooleanExpression AfterBeginTime = StringUtils.isNotBlank(endTime) ? QWebGoods.webGoods.newstime.gt(Long.valueOf(beginTime)) : null;

        List<BooleanExpression> list = new ArrayList<>();
        list.add(priceBetween);
        list.add(kindOfWaterEq);
        list.add(colorLike);
        list.add(circleEq);
        list.add(goodsStateEq);
        list.add(checkStateEq);
        list.add(publishUserNameLike);
        list.add(goodsClassIdEq);
        list.add(goodsCodeLike);
        list.add(publishUserIdEq);
        list.add(goodsNameLike);
        list.add(beforeEndTime);
        list.add(AfterBeginTime);

        List<BooleanExpression> collect = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Predicate[] pre = {QWebGoods.webGoods.id.isNotNull()};
        collect.forEach(one -> pre[0] = ExpressionUtils.and(pre[0], one));
        return pre;
    }

    //本地方法 价格区间
    private BooleanExpression resovePrice(String priceUp, String priceDown){
        if (StringUtils.isNotBlank(priceUp) && StringUtils.isNotBlank(priceDown)){
            return QWebGoods.webGoods.factoryPrice.lt(Long.valueOf(priceUp))
                        .and(QWebGoods.webGoods.factoryPrice.gt(Long.valueOf(priceDown)));
        }else if (StringUtils.isNotBlank(priceUp) && StringUtils.isBlank(priceDown)){
            return QWebGoods.webGoods.factoryPrice.lt(Long.valueOf(priceUp));
        }else if (StringUtils.isBlank(priceUp) && StringUtils.isNotBlank(priceDown)){
            return QWebGoods.webGoods.factoryPrice.gt(Long.valueOf(priceDown));
        }
        return null;
    }

    //本地方法 处理发布人姓名模糊搜索
    private BooleanExpression resoveUserName(String publishUserName){
        String publishUserName1 = StringUtils.isNotBlank(publishUserName) ? publishUserName : null;
        if (StringUtils.isNotBlank(publishUserName1)){
            Iterable<TlongUser> all = appUserRepository.findAll(QTlongUser.tlongUser.realName.like("%" + publishUserName1 + "%"));
            if (all != null) {
                List<TlongUser> tlongUsers = ToListUtil.IterableToList(all);
                List<Long> collect = tlongUsers.stream().map(TlongUser::getId).collect(Collectors.toList());
                return QWebGoods.webGoods.publishUserId.in(collect);
            }
        }
        return null;
    }

    //本地方法 一级分类模糊搜索
    private BooleanExpression resoveGoodsClass(String goodsClassId){
        if (StringUtils.isNotBlank(goodsClassId)){
            //判断分类id是一级分类还是二级分类
            AppGoodsclass one = goodsClassRepository.findOne(Long.valueOf(goodsClassId));
            if (Objects.nonNull(one)){
                Integer goodsClassLevel = one.getGoodsClassLevel();
                if (goodsClassLevel == 0 && one.getGoodsClassIdParent() == 0){ //一级分类
                    //查出一级分类下所有的二级分类
                    Iterable<AppGoodsclass> all = goodsClassRepository.findAll(QAppGoodsclass.appGoodsclass.goodsClassIdParent.eq(one.getId()));
                    List<AppGoodsclass> goodsclasses = ToListUtil.IterableToList(all);
                    List<Long> ids = goodsclasses.stream().map(AppGoodsclass::getId).collect(Collectors.toList());
                    return QWebGoods.webGoods.goodsClassId.in(ids);
                }else {
                    return QWebGoods.webGoods.goodsClassId.eq(Long.valueOf(goodsClassId));
                }
            }
        }
        return null;
    }

}
