package com.tlong.center.service;

import com.tlong.center.api.dto.app.org.AppOrgBaseInfoResponseDto;
import com.tlong.center.domain.repository.WebOrgRepository;
import com.tlong.center.domain.web.WebOrg;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Objects;

@Component
@Transactional
public class AppOrgService {

    private final WebOrgRepository webOrgRepository;

    public AppOrgService(WebOrgRepository webOrgRepository) {
        this.webOrgRepository = webOrgRepository;
    }


    /**
     * 获取上级机构基本信息
     */
    public AppOrgBaseInfoResponseDto findParentOrgInfo(Long orgId) {
        WebOrg one = webOrgRepository.findOne(orgId);
        if (Objects.nonNull(one)){
            AppOrgBaseInfoResponseDto responseDto = new AppOrgBaseInfoResponseDto();
            responseDto.setOrgId(one.getId());
            responseDto.setOrgName(one.getOrgName());
            responseDto.setOrgEmail(one.getOrgEmail());
            responseDto.setOrgPhone(one.getOrgPhone());
            return responseDto;
        }else {
            throw new RuntimeException("当前机构不存在上级机构");
        }
    }
}
