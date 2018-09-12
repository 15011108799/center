package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.partner.PartnerRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.web.WebPartnerApi;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/web/partner")
public class WebPartnerController implements WebPartnerApi {

    @Autowired
    private PartnerService partnerService;

    @Override
    public Page<PartnerRequestDto> findAllPartners(@RequestBody PageAndSortRequestDto requestDto) {
        return partnerService.findAllPartners(requestDto);
    }

    @Override
    public Result addPartner(PartnerRequestDto reqDto) {
        return partnerService.addPartner(reqDto);
    }

    @Override
    public Result delPartner(@RequestBody String id) {
        return partnerService.delPartner(Long.valueOf(id));
    }

    @Override
    public Result delBatchPartner(@RequestBody String id) {
        return partnerService.delBatchPartner(id);
    }

    @Override
    public Result updatePartner(PartnerRequestDto reqDto) {
        return partnerService.updatePartner(reqDto);
    }

    @Override
    public void updatePartnerState(@RequestBody String id) {
        partnerService.updatePartnerState(Long.valueOf(id));
    }

    @Override
    public void updateBatchPartnerState(@RequestBody String id) {
        partnerService.updateBatchPartnerState(id);
    }

    @Override
    public PartnerRequestDto findPartnerById(@RequestBody String id) {
        return partnerService.findPartnerById(Long.valueOf(id));
    }
}
