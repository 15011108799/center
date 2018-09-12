package com.tlong.center.domain.common.quartz;

import javax.persistence.*;

@Entity
@Table(name = "tlong_quartz")
public class TlongQuartz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //需要执行的商品id
    private Long goodsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
