package com.tlong.center.service;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.org.AddOrgRequestDto;
import com.tlong.center.api.dto.web.org.TlongOrgResponseDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.repository.WebOrgRepository;
import com.tlong.center.domain.web.QWebOrg;
import com.tlong.center.domain.web.WebOrg;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    /**
     * 查询子级别代理商机构
     */
    public Page<TlongOrgResponseDto> agentList(Long orgId , PageAndSortRequestDto pageAndSortRequestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(pageAndSortRequestDto);
        //先看当前机构是否存在
        WebOrg one = webOrgRepository.findOne(orgId);
        if (Objects.isNull(one)){
            throw new RuntimeException("当前机构不存在!");
        }

        //看是否存在自己子级机构
        Page<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.parentOrgId.eq(orgId),pageRequest);
        if (all != null){
            //查出所有的子级别代理商机构信息
            return all.map(one1 ->{
                TlongOrgResponseDto dto = new TlongOrgResponseDto();
                dto.setOrgName(one1.getOrgName());
                dto.setCreateDate(one1.getCreateDate());
                dto.setOrgId(one1.getId());
                dto.setOrgLevel(1);
                dto.setParentOrgId(orgId);
                dto.setParentOrgName(one.getOrgName());
                return dto;
            });
        }
        return null;
    }
}
