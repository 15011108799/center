package com.tlong.center.service;

import com.tlong.center.api.dto.app.goods.AppGoodsUploadRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.common.code.CodeUtil;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.goods.AppGoodsPriceSystem;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.repository.GoodsPriceSystemRepository;
import com.tlong.center.domain.repository.GoodsRepository;
import com.tlong.center.domain.repository.TlongUserRepository;
import com.tlong.center.domain.repository.WebOrderRepository;
import com.tlong.center.domain.web.QWebOrder;
import com.tlong.center.domain.web.WebOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.tlong.center.domain.app.goods.QAppGoodsPriceSystem.appGoodsPriceSystem;

@Transactional
@Component
public class AppGoodsService {

    private final WebOrderRepository webOrderRepository;
    private final CodeService codeService;
    private final CodeUtil codeUtil;
    private final GoodsRepository repository;
    private final GoodsPriceSystemRepository goodsPriceSystemRepository;
    private final TlongUserRepository tlongUserRepository;

    public AppGoodsService(WebOrderRepository webOrderRepository, CodeService codeService, CodeUtil codeUtil, GoodsRepository repository, GoodsPriceSystemRepository goodsPriceSystemRepository, TlongUserRepository tlongUserRepository) {
        this.webOrderRepository = webOrderRepository;
        this.codeService = codeService;
        this.codeUtil = codeUtil;
        this.repository = repository;
        this.goodsPriceSystemRepository = goodsPriceSystemRepository;
        this.tlongUserRepository = tlongUserRepository;
    }

    /**
     * APP端商品上传
     */
    public TlongResultDto appGoodsUpload(AppGoodsUploadRequestDto reqDto) {
        reqDto.setGoodsPic(reqDto.getGoodsPic());
        reqDto.setCertificate(reqDto.getCertificate());
        reqDto.setVideo(reqDto.getVideo());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        reqDto.setPublishTime(simpleDateFormat.format(new Date()));
        WebGoods webGoods = new WebGoods(reqDto);
        webGoods.setCertificate(reqDto.getCertificate());
        webGoods.setNewstime(new Date().getTime());
        webGoods.setPicType(Integer.valueOf(reqDto.getPicType()));
        webGoods.setPriceType(Integer.valueOf(reqDto.getPriceType()));
        webGoods.setNum(Integer.valueOf(reqDto.getNum()));
        webGoods.setCircle(reqDto.getCircle());
        webGoods.setIsCheck(0);
        webGoods.setState(0);
        webGoods.setCurState(1);
        webGoods.setIsDeleted(0);
        webGoods.setGoodsClassId(Long.valueOf(reqDto.getGoodsClassId()));
        if (StringUtils.isNotBlank(reqDto.getPublishUserId())) {
            webGoods.setPublishUserId(Long.valueOf(reqDto.getPublishUserId()));
        }

        //TODO 根据价格体系设定当前商品的各个价格
        Double publishPrice = Double.valueOf(reqDto.getPublishPrice());
        AppGoodsPriceSystem one = goodsPriceSystemRepository.findOne(appGoodsPriceSystem.goodsClassId.eq(Long.valueOf(reqDto.getGoodsClassId()))
                .and(appGoodsPriceSystem.intervalUp.goe(publishPrice))
                .and(appGoodsPriceSystem.intervalDown.lt(publishPrice)));

        webGoods.setPublishPrice(publishPrice); //发布价格(最低基准价格)
        webGoods.setFounderPrice(one.getOriginatorRatio() * publishPrice);  //总代
        webGoods.setFlagshipPrice(one.getLagshipRatio() * publishPrice); //1级代
        webGoods.setFactoryPrice(one.getFactoryRatio() * publishPrice);  //出厂价格(最高的)

        //设置商品属性
        webGoods.setGoodsPropertyId(reqDto.getGoodsPropertyId());

        TlongUser one1 = tlongUserRepository.findOne(Long.valueOf(reqDto.getPublishUserId()));

        //商品编号
        if (reqDto.getIsCompany() == 0)
            webGoods.setGoodsCode(one1.getUserCode() + "-" + codeService.createAllCode(1, 0, 1,reqDto.getIsCompany()));
        else if (reqDto.getIsCompany() == 1)
            webGoods.setGoodsCode(one1.getUserCode() + "-" + codeService.createAllCode(1, 0, 1,reqDto.getIsCompany()));

        WebGoods webGoods1 = repository.save(webGoods);
        if (webGoods1 != null)
            return new TlongResultDto(1, "添加成功");
        else
            return new TlongResultDto(0, "添加失败");
    }

    /**
     * 商品下架
     */
    public TlongResultDto downGoods(Long goodsId) {
        updateGoodsCurState(goodsId,3);
        WebOrder one = webOrderRepository.findOne(QWebOrder.webOrder.goodsId.eq(goodsId));
        if (Objects.nonNull(one)){
            one.setState(2);
            webOrderRepository.save(one);
        }
        return new TlongResultDto(0,"下架成功!");
    }

    /**
     * 修改商品状态为3
     */
    private void updateGoodsCurState(Long goodsId, Integer curState){
        WebGoods one = repository.findOne(goodsId);
        one.setCurState(curState);
        repository.save(one);
    }
}