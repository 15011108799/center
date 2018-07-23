package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.message.MessageRequestDto;
import com.tlong.center.api.dto.partner.PartnerRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebGoodsDetailResponseDto;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.Message;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.repository.PartnerRepository;
import com.tlong.center.domain.web.WebPartner;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class PartnerService {
    final EntityManager entityManager;
    final PartnerRepository repository;

    public PartnerService(EntityManager entityManager, PartnerRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    /**
     * 添加合伙人
     *
     * @param s
     * @param reqDto
     * @return
     */
    public Result addPartner(String s, PartnerRequestDto reqDto) {
        reqDto.setPic(s.substring(0, s.length() - 1));
        reqDto.setTitleIcon(FileUploadUtils.readFile(reqDto.getTitleIcon()));
        reqDto.setVideo(FileUploadUtils.readFile(reqDto.getVideo()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        reqDto.setPublishTime(simpleDateFormat.format(new Date()));
        reqDto.setIsCheck("0");
        WebPartner webPartner1 = repository.save(new WebPartner(reqDto));
        if (webPartner1 != null)
            return new Result(1, "添加成功");
        else
            return new Result(0, "添加失败");
    }

    /**
     * 删除合伙人
     *
     * @param id
     * @return
     */
    public Result delPartner(Long id) {
        WebPartner webPartner1 = repository.findOne(id);
        if (webPartner1 == null)
            return new Result(0, "删除失败");
        repository.delete(id);
        return new Result(1, "删除成功");
    }

    /**
     * 修改合伙人信息
     *
     * @param s
     * @param reqDto
     * @return
     */
    public Result updatePartner(String s, PartnerRequestDto reqDto) {
        WebPartner webPartner1 = repository.findOne(Long.valueOf(reqDto.getId()));
        if (s.equals(""))
            reqDto.setPic(reqDto.getPic().substring(1));
        else
            reqDto.setPic(s.substring(0, s.length() - 1) + reqDto.getPic());
        String icon = FileUploadUtils.readFile(reqDto.getTitleIcon());
        if (icon != null && !icon.equals(""))
            reqDto.setTitleIcon(icon);
        else
            reqDto.setTitleIcon(webPartner1.getTitleIcon());
        String video = FileUploadUtils.readFile(reqDto.getVideo());
        if (video != null && !video.equals(""))
            reqDto.setVideo(video);
        else
            reqDto.setVideo(webPartner1.getVideo());
        WebPartner webPartner = new WebPartner(reqDto);
        webPartner.setIsCheck("0");
        webPartner.setPublishTime(webPartner1.getPublishTime());
        webPartner.setId(Long.valueOf(reqDto.getId()));
        WebPartner webPartner2 = repository.save(webPartner);
        if (webPartner2 != null)
            return new Result(1, "修改成功");
        else
            return new Result(0, "修改失败");
    }

    /**
     * 更新审核状态
     *
     * @param id
     */
    public void updatePartnerState(Long id) {
        WebPartner webPartner = repository.findOne(id);
        if ("0".equals(webPartner.getIsCheck()))
            webPartner.setIsCheck("1");
        else
            webPartner.setIsCheck("0");
        repository.save(webPartner);
    }

    /**
     * 查找合伙人通过id
     *
     * @param id
     * @return
     */
    public PartnerRequestDto findPartnerById(Long id) {
        WebPartner webPartner = repository.findOne(id);
        return webPartner.toDto();
    }

    /**
     * 查询所有分页
     * @param requestDto
     * @return
     */
    public PageResponseDto<PartnerRequestDto> findAllPartners(PageAndSortRequestDto requestDto) {
        PageResponseDto<PartnerRequestDto> partnerRequestDtoPageResponseDto=new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebPartner> partners = repository.findAll(pageRequest);
        List<PartnerRequestDto> requestDtos = new ArrayList<>();
        partners.forEach(partner -> {
            PartnerRequestDto partnerRequestDto=partner.toDto();
            partnerRequestDto.setId(partner.getId()+"");
            requestDtos.add(partnerRequestDto);
        });
        partnerRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<WebPartner> webPartners = repository.findAll();
        webPartners.forEach(webPartner -> {
            count[0]++;
        });
        partnerRequestDtoPageResponseDto.setCount(count[0]);
        return partnerRequestDtoPageResponseDto;
    }

    public Result delBatchPartner(String id) {
        String[] goodsIds;
        if (StringUtils.isNotEmpty(id)) {
            goodsIds = id.split(",");
            for (int i = 0; i < goodsIds.length; i++) {
                delPartner(Long.valueOf(goodsIds[i]));
            }
        }
        return new Result(1, "删除成功");
    }

    public void updateBatchPartnerState(String id) {
        String[] goodsIds;
        if (StringUtils.isNotEmpty(id)) {
            goodsIds = id.split(",");
            for (int i = 0; i < goodsIds.length; i++) {
                updatePartnerState(Long.valueOf(goodsIds[i]));
            }
        }
    }
}
