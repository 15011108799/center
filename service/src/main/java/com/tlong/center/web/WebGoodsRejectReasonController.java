package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.WebGoodsReasonResponseDto;
import com.tlong.center.api.web.WebGoodsRejectReasonApi;
import com.tlong.center.service.GoodsRejectReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/goodsReason")
public class WebGoodsRejectReasonController implements WebGoodsRejectReasonApi {

    @Autowired
    private GoodsRejectReasonService service;
    @Override
    public Result addGoodsRejectReason(@RequestBody  WebGoodsReasonResponseDto reasonResponseDto) {
        return service.addGoodsRejectReason(reasonResponseDto);
    }

    @Override
    public WebGoodsReasonResponseDto findReason(@RequestBody  Long id) {
        return service.findReason(id);
    }

    @Override
    public Result delReason(@RequestBody  Long id) {
        return service.delReason(id);
    }
}
