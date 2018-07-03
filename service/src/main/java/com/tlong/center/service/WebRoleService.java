package com.tlong.center.service;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebRoleDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.TlongRolePowerRepository;
import com.tlong.center.domain.repository.TlongRoleRepository;
import com.tlong.center.domain.web.QTlongRole;
import com.tlong.center.domain.web.TlongRole;
import com.tlong.center.domain.web.TlongRolePower;
import com.tlong.core.utils.PropertyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.tlong.center.domain.web.QTlongRole.tlongRole;

@Component
@Transactional
public class WebRoleService {

    final EntityManager entityManager;
    final TlongRoleRepository repository;
    final TlongRolePowerRepository tlongRolePowerRepository;

    public WebRoleService(EntityManager entityManager, TlongRoleRepository repository, TlongRolePowerRepository tlongRolePowerRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.tlongRolePowerRepository = tlongRolePowerRepository;
    }

    /**
     * 角色列表查询
     */
    public PageResponseDto<WebRoleDto> roleList(PageAndSortRequestDto requestDto) {
        PageResponseDto<WebRoleDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongRole> all = repository.findAll(tlongRole.type.intValue().eq(2), pageRequest);
        List<WebRoleDto> webRoleDtos = new ArrayList<>();
        all.forEach(one -> webRoleDtos.add(one.toDto()));
        pageSuppliersResponseDto.setList(webRoleDtos);
        final int[] count = {0};
        Iterable<TlongRole> tlongRoles = repository.findAll(tlongRole.type.intValue().eq(2));
        tlongRoles.forEach(tlongRole -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }


    /**
     * 角色新增
     */
    public Integer addRole(WebRoleDto reqDto) {
        TlongRole tlongRole = new TlongRole(reqDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        tlongRole.setRegistDate(simpleDateFormat.format(new Date()));
        TlongRole tlongRole1 = repository.save(tlongRole);
        if (tlongRole1 == null) {
            return null;
        }
        return tlongRole1.getId().intValue();
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
            PropertyUtils.copyPropertiesOfNotNull(reqDto, one);
            repository.save(one);
            return new TlongResultDto(0, "修改成功");
        }
        return new TlongResultDto(1, "要修改的的角色不存在");

    }

    public void bindPower(Long roleId, String powerIds) {
        String[] powers = powerIds.split(",");
        for (int i = 0; i < powers.length; i++) {
            TlongRolePower tlongRolePower = new TlongRolePower();
            tlongRolePower.setRoleId(roleId);
            tlongRolePower.setPowerId(Long.valueOf(powers[i]));
            tlongRolePowerRepository.save(tlongRolePower);
        }
    }
}
