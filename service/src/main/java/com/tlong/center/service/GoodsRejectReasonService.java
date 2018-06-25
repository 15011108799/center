package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.WebGoodsReasonResponseDto;
import com.tlong.center.domain.repository.GoodsRejectReasonRepository;
import com.tlong.center.domain.web.QWebGoodsRejectReason;
import com.tlong.center.domain.web.WebGoodsRejectReason;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.tlong.center.domain.web.QWebGoodsRejectReason.webGoodsRejectReason;

@Component
@Transactional
public class GoodsRejectReasonService {
    @Autowired
    private GoodsRejectReasonRepository repository;

    /**
     * 增加驳回原因
     * @param reasonResponseDto
     * @return
     */
    public Result addGoodsRejectReason(WebGoodsReasonResponseDto reasonResponseDto) {
        WebGoodsRejectReason reason=new WebGoodsRejectReason(reasonResponseDto);
        WebGoodsRejectReason reason1=repository.save(reason);
        if (reason1 != null)
            return new Result(1, "驳回成功");
        else
            return new Result(0, "驳回失败");
    }

    /**
     * 查找驳回原因
     * @param id
     * @return
     */
    public WebGoodsReasonResponseDto findReason(Long id) {
        WebGoodsRejectReason reason=repository.findOne(webGoodsRejectReason.goodsId.longValue().eq(id));
        return  reason.toDto();
    }

    /**
     * 删除驳回原因
     * @param id
     * @return
     */
    public Result delReason(Long id) {
        WebGoodsRejectReason reason=repository.findOne(webGoodsRejectReason.goodsId.longValue().eq(id));
        if (reason == null)
            return new Result(0, "删除失败");
        repository.delete(reason.getId());
        return new Result(1, "删除成功");

    }
}
