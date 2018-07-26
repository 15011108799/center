package com.tlong.center.service;

import com.mchange.v2.lang.ObjectUtils;
import com.tlong.center.api.dto.app.goods.AppGoodsUploadRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.repository.GoodsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

@Transactional
@Component
public class AppGoodsService {

    private final CodeService codeService;
    private final GoodsRepository repository;

    public AppGoodsService(CodeService codeService, GoodsRepository repository) {
        this.codeService = codeService;
        this.repository = repository;
    }

    //app商品上传
    public TlongResultDto appGoodsUpload(String file, AppGoodsUploadRequestDto reqDto) {
        reqDto.setGoodsPic(file.substring(0, file.length() - 1));
        reqDto.setCertificate(FileUploadUtils.readFile(reqDto.getCertificate()));
        reqDto.setVideo(FileUploadUtils.readFile(reqDto.getVideo()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        reqDto.setPublishTime(simpleDateFormat.format(new Date()));
        WebGoods webGoods = new WebGoods(reqDto);
        webGoods.setIsCheck(0);
        if (StringUtils.isNotBlank(reqDto.getPublishUserId())) {
            webGoods.setPublishUserId(Long.valueOf(reqDto.getPublishUserId()));
        }
        if (reqDto.getIsCompany() != null && reqDto.getIsCompany() == 0) {
            webGoods.setGoodsCode(reqDto.getUserCode() + "-" + codeService.createAllCode(1, 0, 1));
        }else if (reqDto.getIsCompany() != null && reqDto.getIsCompany() == 1)
            webGoods.setGoodsCode(reqDto.getUserCode() + "-" + codeService.createAllCode(1, 1, 1));

        //TODO 根据价格体系设定当前商品的各个价格

        WebGoods webGoods1 = repository.save(webGoods);
        if (webGoods1 != null)
            return new TlongResultDto(1, "添加成功");
        else
            return new TlongResultDto(0, "添加失败");
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