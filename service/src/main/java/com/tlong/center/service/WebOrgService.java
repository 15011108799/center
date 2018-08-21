package com.tlong.center.service;

import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.org.AddOrgRequestDto;
import com.tlong.center.domain.repository.WebOrgRepository;
import com.tlong.center.domain.web.WebOrg;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
@Transactional
public class WebOrgService {

    private final WebOrgRepository webOrgRepository;

    public WebOrgService(WebOrgRepository webOrgRepository) {
        this.webOrgRepository = webOrgRepository;
    }

    /**
     * 新增机构
     * @param requestDto
     * @return
     */
    public TlongResultDto addOrg(AddOrgRequestDto requestDto) {
        WebOrg webOrg = new WebOrg();
        webOrg.setOrgName(requestDto.getOrgName());
        webOrg.setCreateDate(new Date());
        webOrg.setCurState(1);
        webOrg.setIsDeleted(0);
        webOrg.setOrgEmail(requestDto.getOrgEmail());
        webOrg.setOrgPhone(requestDto.getOrgPhone());
        webOrg.setOrgDesc(requestDto.getOrgDesc());
        webOrg.setOrgLevel(1);
        webOrg.setParentOrgId(1426L);
        WebOrg save = webOrgRepository.save(webOrg);
        if (Objects.nonNull(save)){
            return new TlongResultDto(0,save.getId()+"");
        }
        return new TlongResultDto(1,"新增机构失败!");

    }
}
