package com.tlong.center.service;

import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.WebRoleDto;
import com.tlong.center.domain.repository.TlongRoleRepository;
import com.tlong.center.domain.web.TlongRole;
import com.tlong.core.utils.PropertyUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class WebRoleService {

    final EntityManager entityManager;
    final TlongRoleRepository repository;

    public WebRoleService(EntityManager entityManager, TlongRoleRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    /**
     * 角色列表查询
     */
    public List<WebRoleDto> roleList() {
        List<TlongRole> all = repository.findAll();
        List<WebRoleDto> webRoleDtos = new ArrayList<>();
        all.stream().forEach(one-> webRoleDtos.add(one.toDto()));
        return webRoleDtos;
    }


    /**
     * 角色新增
     */
    public TlongResultDto addRole(WebRoleDto reqDto) {
        Long id = repository.save(new TlongRole(reqDto)).getId();
        return new TlongResultDto(0,String.valueOf(id));
    }

    /**
     * 删除角色
     */
    public TlongResultDto delRole(Long roleId) {
        TlongRole one = repository.findOne(roleId);
        if (Objects.nonNull(one)) {
            repository.delete(roleId);
            return new TlongResultDto(0);
        }
        return new TlongResultDto(1, "要删除的角色不存在");
    }


    /**
     * 修改角色信息
     */
    public TlongResultDto updateRole(WebRoleDto reqDto, Long roleId) {
        TlongRole one = repository.findOne(roleId);
        if (Objects.nonNull(one)) {
            PropertyUtils.copyPropertiesOfNotNull(reqDto,one);
            repository.save(one);
            return new TlongResultDto(0, "修改成功");
        }
        return new TlongResultDto(1, "要修改的的角色不存在");

    }
}
