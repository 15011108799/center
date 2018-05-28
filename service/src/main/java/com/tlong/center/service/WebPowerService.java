package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.TlongPowerDto;
import com.tlong.center.domain.repository.TlongPowerRepository;
import com.tlong.center.domain.web.TlongPower;
import com.tlong.core.utils.PropertyUtils;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class WebPowerService {

    final EntityManager entityManager;
    final TlongPowerRepository repository;

    public WebPowerService(EntityManager entityManager, TlongPowerRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    /**
     * 权限列表查询
     */
    public List<TlongPowerDto> powerList() {
        List<TlongPower> all = repository.findAll();
        List<TlongPowerDto> tlongPowerDtos = new ArrayList<>();
        all.stream().forEach(one -> tlongPowerDtos.add(one.toDto()));
        return tlongPowerDtos;
    }

    /**
     * 权限新增
     */
    public Result addPower(TlongPowerDto reqDto) {
        Long id = repository.save(new TlongPower(reqDto)).getId();
        return new Result(0, String.valueOf(id));
    }

    /**
     * 删除权限
     */
    public Result delPower(Long powerId) {
        TlongPower one = repository.findOne(powerId);
        if (Objects.nonNull(one)) {
            repository.delete(powerId);
            return new Result(0, "删除成功");
        }
        return new Result(1, "要删除的权限不存在");
    }

    /**
     * 修改权限信息
     * @param reqDto
     * @param powerId
     * @return
     */
    public Result updatePower(TlongPowerDto reqDto, Long powerId) {
        TlongPower one = repository.findOne(powerId);
        if (Objects.nonNull(one)) {
            PropertyUtils.copyPropertiesOfNotNull(reqDto, one);
            repository.save(one);
            return new Result(0, "修改成功");
        }
        return new Result(1, "要修改的的权限不存在");
    }
}
