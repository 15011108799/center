package com.tlong.center.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebGoodsDetailResponseDto;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.goods.AppGoodsclass;
import com.tlong.center.domain.app.goods.QWebGoods;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.repository.AppUserRepository;
import com.tlong.center.domain.repository.GoodsClassRepository;
import com.tlong.center.domain.repository.GoodsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;

@Component
@Transactional
public class WebGoodsService {
    final EntityManager entityManager;
    final GoodsRepository repository;
    final AppUserRepository appUserRepository;
    final GoodsClassRepository goodsClassRepository;

    public WebGoodsService(EntityManager entityManager, GoodsRepository repository, AppUserRepository appUserRepository,GoodsClassRepository goodsClassRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.goodsClassRepository=goodsClassRepository;
    }

    /**
     * 查询所有商品分页
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<WebGoodsDetailResponseDto> findAllGoodsByPage(PageAndSortRequestDto requestDto, HttpSession session) {
        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
        PageResponseDto<WebGoodsDetailResponseDto> responseDto = new PageResponseDto<>();
        System.out.println("+++++++++++++page"+requestDto.getPage());
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebGoods> webGoods=null;
        if (user.getUserType()!=null&&user.getUserType()==1) {
            if (user.getIsCompany() == 0)
                webGoods = repository.findAll(QWebGoods.webGoods.publishUserId.longValue().eq(user.getId()), pageRequest);
            else {
                final Predicate[] pre = {QWebGoods.webGoods.id.isNull()};
                Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.eq(user.getOrgId())));
                tlongUser3.forEach(one -> {
                    pre[0] = ExpressionUtils.or(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(one.getId()));
                });
                webGoods = repository.findAll(pre[0], pageRequest);
            }
        }else {
            System.out.println("++++++"+pageRequest.getPageNumber());
            webGoods=repository.findAll(pageRequest);
        }
        List<WebGoodsDetailResponseDto> requestDtos = new ArrayList<>();
        webGoods.forEach(webGoods1 -> {
            WebGoodsDetailResponseDto webGoodsDetailResponseDto = webGoods1.toDto();
            webGoodsDetailResponseDto.setIsCheck(webGoods1.getIsCheck() + "");
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
            if (webGoods1.getState() != null)
                webGoodsDetailResponseDto.setState(webGoods1.getState() + "");
            if (webGoods1.getPublishUserId() != null) {
                TlongUser tlongUser = appUserRepository.findOne(webGoods1.getPublishUserId());
                webGoodsDetailResponseDto.setPublishName(tlongUser.getRealName());
                webGoodsDetailResponseDto.setOrgId(tlongUser.getOrgId());
                webGoodsDetailResponseDto.setPublishPhone(tlongUser.getPhone());
            }
            if (webGoods1.getGoodsClassId()!=null){
                AppGoodsclass appGoodsclass=goodsClassRepository.findOne(webGoods1.getGoodsClassId());
                webGoodsDetailResponseDto.setGoodsClassId(appGoodsclass.getGoodsClassName());
            }
            requestDtos.add(webGoodsDetailResponseDto);
        });
        responseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<WebGoods> appGoods1;
        if (user.getUserType()!=null&&user.getUserType()==1) {
            if (user.getIsCompany() == 0)
                appGoods1 = repository.findAll(QWebGoods.webGoods.publishUserId.longValue().eq(user.getId()));
            else {
                final Predicate[] pre = {QWebGoods.webGoods.id.isNull()};
                Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.eq(user.getOrgId())));
                tlongUser3.forEach(one -> {
                    pre[0] = ExpressionUtils.or(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(one.getId()));
                });
                appGoods1 = repository.findAll(pre[0]);
            }
        }else
            appGoods1=repository.findAll();
        appGoods1.forEach(goods -> {
            count[0]++;
        });
        responseDto.setCount(count[0]);
        return responseDto;
    }

    /**
     * 添加商品
     *
     * @return
     */
    public Result add(String s, WebGoodsDetailResponseDto reqDto,HttpSession session) {
        reqDto.setGoodsPic(s.substring(0, s.length() - 1));
        reqDto.setCertificate(FileUploadUtils.readFile(reqDto.getCertificate()));
        reqDto.setVideo(FileUploadUtils.readFile(reqDto.getVideo()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        reqDto.setPublishTime(simpleDateFormat.format(new Date()));
        WebGoods webGoods = new WebGoods(reqDto);
        webGoods.setIsCheck(0);
        if (reqDto.getState() != null && !reqDto.getState().equals(""))
            webGoods.setState(Integer.valueOf(reqDto.getState()));
        if (reqDto.getGoodsClassId() != null && !reqDto.getGoodsClassId().equals(""))
            webGoods.setGoodsClassId(Long.valueOf(reqDto.getGoodsClassId()));
        if (reqDto.getRealStar() != null && !reqDto.getRealStar().equals(""))
            webGoods.setRealStar(Integer.valueOf(reqDto.getRealStar()));
        if (reqDto.getPicType() != null && !reqDto.getPicType().equals(""))
            webGoods.setPicType(Integer.valueOf(reqDto.getPicType()));
        if (reqDto.getCircle() != null && !reqDto.getCircle().equals(""))
            webGoods.setCircle(Integer.valueOf(reqDto.getCircle()));
        if (reqDto.getNum() != null && !reqDto.getNum().equals(""))
            webGoods.setNum(Integer.valueOf(reqDto.getNum()));
        if (reqDto.getPriceType() != null && !reqDto.getPriceType().equals(""))
            webGoods.setPriceType(Integer.valueOf(reqDto.getPriceType()));
        if (reqDto.getFactoryPrice() != null && !reqDto.getFactoryPrice().equals(""))
            webGoods.setFactoryPrice(Double.valueOf(reqDto.getFactoryPrice()));
        if (reqDto.getFlagshipPrice() != null && !reqDto.getFlagshipPrice().equals(""))
            webGoods.setFlagshipPrice(Double.valueOf(reqDto.getFlagshipPrice()));
        if (reqDto.getFounderPrice() != null && !reqDto.getFounderPrice().equals(""))
            webGoods.setFounderPrice(Double.valueOf(reqDto.getFounderPrice()));
        if (reqDto.getPublishPrice() != null && !reqDto.getPublishPrice().equals(""))
            webGoods.setPublishPrice(Double.valueOf(reqDto.getPublishPrice()));
        if (reqDto.getStorePrice() != null && !reqDto.getStorePrice().equals(""))
            webGoods.setStorePrice(Double.valueOf(reqDto.getStorePrice()));
        webGoods.setPublishUserId(((TlongUser) session.getAttribute("tlongUser")).getId());
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
        if (webGoods == null)
            return new Result(0, "删除失败");
        repository.delete(id);
        return new Result(1, "删除成功");
    }

    /**
     * 修改商品审核状态
     */
    public void updateGoodsState(Long id) {
        WebGoods webGoods = repository.findOne(id);
        if (webGoods.getIsCheck() == 0)
            webGoods.setIsCheck(1);
        else
            webGoods.setIsCheck(0);
        repository.save(webGoods);
    }

    /**
     * 查询某一商品
     */
    public WebGoodsDetailResponseDto findGoodsById(Long id) {
        WebGoods webGoods1 = repository.findOne(id);
        WebGoodsDetailResponseDto webGoodsDetailResponseDto = webGoods1.toDto();
        AppGoodsclass appGoodsclass=goodsClassRepository.findOne(webGoods1.getGoodsClassId());
        webGoodsDetailResponseDto.setParentClassId(appGoodsclass.getGoodsClassIdParent());
        if (webGoods1.getId() != null)
            webGoodsDetailResponseDto.setId(webGoods1.getId() + "");
        if (webGoods1.getRealStar() != null)
            webGoodsDetailResponseDto.setRealStar(webGoods1.getRealStar() + "");
        if (webGoods1.getGoodsClassId() != null)
            webGoodsDetailResponseDto.setGoodsClassId(webGoods1.getGoodsClassId() + "");
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
        if (webGoods1.getState() != null)
            webGoodsDetailResponseDto.setState(webGoods1.getState() + "");
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
        webGoods.setPublishTime(webGoods1.getPublishTime());
        webGoods.setId(Long.valueOf(reqDto.getId()));
        if (reqDto.getState() != null && !reqDto.getState().equals("null"))
            webGoods.setState(Integer.valueOf(reqDto.getState()));
        if (reqDto.getGoodsClassId() != null && !reqDto.getGoodsClassId().equals("null"))
            webGoods.setGoodsClassId(Long.valueOf(reqDto.getGoodsClassId()));
        if (reqDto.getRealStar() != null && !reqDto.getRealStar().equals("null"))
            webGoods.setRealStar(Integer.valueOf(reqDto.getRealStar()));
        if (reqDto.getPicType() != null && !reqDto.getPicType().equals("null"))
            webGoods.setPicType(Integer.valueOf(reqDto.getPicType()));
        if (reqDto.getCircle() != null && !reqDto.getCircle().equals("null"))
            webGoods.setCircle(Integer.valueOf(reqDto.getCircle()));
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
     *
     * @param goodsId
     */
    public void updateState(Long goodsId, int state) {
        WebGoods webGoods = repository.findOne(goodsId);
        if (webGoods.getState() == 0)
            webGoods.setState(0);
        else
            webGoods.setState(state);
        repository.save(webGoods);
    }
}
