package com.tlong.center.service;

import com.tlong.center.api.dto.TlongUserInfos;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.department.AddDepartmentRequestDto;
import com.tlong.center.api.dto.web.department.DepartmentInfoResponseDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.TlongRolePowerRepository;
import com.tlong.center.domain.repository.TlongRoleRepository;
import com.tlong.center.domain.repository.TlongUserRepository;
import com.tlong.center.domain.web.QTlongRole;
import com.tlong.center.domain.web.TlongRole;
import com.tlong.center.domain.web.TlongRolePower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Transactional
public class DepartmentService {

    private final TlongRoleRepository roleRepository;
    private final TlongRolePowerRepository rolePowerRepository;
    private final TlongUserRepository userRepository;

    public DepartmentService(TlongRoleRepository roleRepository, TlongRolePowerRepository rolePowerRepository, TlongUserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.rolePowerRepository = rolePowerRepository;
        this.userRepository = userRepository;
    }


    /**
     * 新增部门
     */
    public TlongResultDto addDepartment(AddDepartmentRequestDto requestDto) {
        TlongRole tlongRole = new TlongRole();
        tlongRole.setDes(requestDto.getRoleDesc());
        tlongRole.setRoleName(requestDto.getRoleName());
        tlongRole.setRegistDate(toDateString(new Date()));
        tlongRole.setType(requestDto.getType());
        TlongRole save = roleRepository.save(tlongRole);
        return new TlongResultDto(0,save.getId() + "");
    }

    private String toDateString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }


    /**
     * 获取部门下所有机构列表分页
     */
    public Page<DepartmentInfoResponseDto> departmentPage(Long orgId, PageAndSortRequestDto pageAndSortRequestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(pageAndSortRequestDto);
        Page<TlongRole> all = roleRepository.findAll(QTlongRole.tlongRole.type.eq(2)
                .and(QTlongRole.tlongRole.orgId.eq(orgId)), pageRequest);
        return all.map(one ->{
            DepartmentInfoResponseDto responseDto = new DepartmentInfoResponseDto();
            responseDto.setOrgName("中国国际珠宝交易平台机构");
            responseDto.setDepartmentName(one.getRoleName());
            responseDto.setRegistDate(one.getRegistDate());
            return responseDto;
        });
    }


    /**
     * 部门对应权限绑定
     */
    public TlongResultDto addRolePower(Long roleId, String powerIds){
        String[] powers = powerIds.split(",");
        List<TlongRolePower> collect = Arrays.stream(powers).map(one -> {
            TlongRolePower tlongRolePower = new TlongRolePower();
            tlongRolePower.setRoleId(roleId);
            tlongRolePower.setPowerId(Long.valueOf(one));
            return tlongRolePower;
        }).collect(Collectors.toList());
        rolePowerRepository.save(collect);
        return new TlongResultDto(0, "部门对应权限绑定完成!");
    }

    /**
     * 获取用户信息
     */
    public TlongUserInfos tlongUserInfo(Long userId){
        TlongUser one = userRepository.findOne(userId);
        if (Objects.nonNull(one)){
            TlongUserInfos tlongUserInfos = new TlongUserInfos();
            tlongUserInfos.setUserId(one.getId());
            //设置是否是管理员
            if (one.getUserType() == null){
                //用户是管理员
                tlongUserInfos.setIsRoot(1);
            }
//            //判断用户是什么身份 对应orgClass的值
//            tlongUserInfos.setUserClass();
            return tlongUserInfos;
        }
        throw new RuntimeException("当前用户不存在");
    }

}
