package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.partner.PartnerRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api("商品增删改查接口")
public interface WebPartnerApi {

    @ApiOperation("获取合伙人列表")
    @PostMapping("/findAllPartners")
    Page<PartnerRequestDto> findAllPartners(@RequestBody PageAndSortRequestDto requestDto);

    @ApiModelProperty("新增合伙人")
    @PostMapping("/addPartner")
    Result addPartner(@RequestParam("file") List<MultipartFile> file, PartnerRequestDto reqDto, @RequestParam String contentClass,@RequestParam String contentType);

    @ApiModelProperty("删除合伙人")
    @PutMapping("/delPartner")
    Result delPartner(@RequestParam String id);

    @ApiModelProperty("批量删除合伙人")
    @PutMapping("/delBatchPartner")
    Result delBatchPartner(@RequestParam String id);

    @ApiModelProperty("修改合伙人信息")
    @PostMapping("/updatePartner")
    Result updatePartner(@RequestParam("file") List<MultipartFile> file, PartnerRequestDto reqDto, @RequestParam String contentClass,@RequestParam String contentType);

    @ApiOperation("修改合伙人状态")
    @PostMapping("/updatePartnerState")
    void updatePartnerState(@RequestBody String id);

    @ApiOperation("批量修改合伙人状态")
    @PutMapping("/updateBatchPartnerState")
    void updateBatchPartnerState(@RequestParam String id);

    @ApiOperation("根据id查询合伙人")
    @PutMapping("/findPartnerById")
    PartnerRequestDto findPartnerById(@RequestParam String id);
}
