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

    //app商品上传
    public TlongResultDto appGoodsUpload(AppGoodsUploadRequestDto reqDto) {
//        reqDto.setGoodsPic(file.substring(0, file.length() - 1));
        reqDto.setGoodsPic(reqDto.getGoodsPic());
        reqDto.setCertificate(FileUploadUtils.readFile(reqDto.getCertificate()));
//        reqDto.setVideo(FileUploadUtils.readFile(reqDto.getVideo()));
        reqDto.setVideo(reqDto.getVideo());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        reqDto.setPublishTime(simpleDateFormat.format(new Date()));
        WebGoods webGoods = new WebGoods(reqDto);
        webGoods.setPicType(Integer.valueOf(reqDto.getPicType()));
        webGoods.setPriceType(Integer.valueOf(reqDto.getPriceType()));
        webGoods.setNum(Integer.valueOf(reqDto.getNum()));
        webGoods.setIsCheck(0);
        webGoods.setState(0);
        webGoods.setCurState(1);
        webGoods.setIsDeleted(0);
        webGoods.setGoodsClassId(Long.valueOf(reqDto.getGoodsClassId()));
        if (StringUtils.isNotBlank(reqDto.getPublishUserId())) {
            webGoods.setPublishUserId(Long.valueOf(reqDto.getPublishUserId()));
        }
//        if (reqDto.getIsCompany() != null && reqDto.getIsCompany() == 0) {
//            webGoods.setGoodsCode(reqDto.getUserCode() + "-" + codeService.createAllCode(1, 0, 1,reqDto.getIsCompany()));
//        }else if (reqDto.getIsCompany() != null && reqDto.getIsCompany() == 1)
//            webGoods.setGoodsCode(reqDto.getUserCode() + "-" + codeService.createAllCode(1, 1, 1,reqDto.getIsCompany()));

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
        one.setState(2);
        WebOrder save = webOrderRepository.save(one);
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
//    if (StringUtils.isBlank(reqDto.getState()))
//            webGoods.setState(Integer.valueOf(reqDto.getState()));
//        if (reqDto.getGoodsClassId() != null && !reqDto.getGoodsClassId().equals(""))
//            webGoods.setGoodsClassId(Long.valueOf(reqDto.getGoodsClassId()));
//        if (reqDto.getRealStar() != null && !reqDto.getRealStar().equals(""))
//            webGoods.setRealStar(Integer.valueOf(reqDto.getRealStar()));
//        if (reqDto.getPicType() != null && !reqDto.getPicType().equals(""))
//            webGoods.setPicType(Integer.valueOf(reqDto.getPicType()));
//        if (reqDto.getCircle() != null && !reqDto.getCircle().equals(""))
//            webGoods.setCircle(Integer.valueOf(reqDto.getCircle()));
//        if (reqDto.getNum() != null && !reqDto.getNum().equals(""))
//            webGoods.setNum(Integer.valueOf(reqDto.getNum()));
//        if (reqDto.getPriceType() != null && !reqDto.getPriceType().equals(""))
//            webGoods.setPriceType(Integer.valueOf(reqDto.getPriceType()));
//        if (reqDto.getFactoryPrice() != null && !reqDto.getFactoryPrice().equals(""))
//            webGoods.setFactoryPrice(Double.valueOf(reqDto.getFactoryPrice()));
//        if (reqDto.getFlagshipPrice() != null && !reqDto.getFlagshipPrice().equals(""))
//            webGoods.setFlagshipPrice(Double.valueOf(reqDto.getFlagshipPrice()));
//        if (reqDto.getFounderPrice() != null && !reqDto.getFounderPrice().equals(""))
//            webGoods.setFounderPrice(Double.valueOf(reqDto.getFounderPrice()));
//        if (reqDto.getPublishPrice() != null && !reqDto.getPublishPrice().equals(""))
//            webGoods.setPublishPrice(Double.valueOf(reqDto.getPublishPrice()));
//        if (reqDto.getStorePrice() != null && !reqDto.getStorePrice().equals(""))
//            webGoods.setStorePrice(Double.valueOf(reqDto.getStorePrice()));