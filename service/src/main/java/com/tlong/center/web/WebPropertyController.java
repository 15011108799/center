package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebPropertyDto;
import com.tlong.center.api.web.WebPropertyApi;
import com.tlong.center.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/web/property")
public class WebPropertyController implements WebPropertyApi {

    @Autowired
    private PropertyService propertyService;

    @Override
    public Result addProperty(@RequestBody WebPropertyDto requestDto) {
        return propertyService.addProperty(requestDto);
    }

    @Override
    public PageResponseDto<WebPropertyDto> findAllProperty(@RequestBody PageAndSortRequestDto requestDto) {
        return propertyService.findAllProperty(requestDto);
    }

    @Override
    public Result deleteProperty(@RequestBody Long goodsId) {
        return propertyService.deleteProperty(goodsId);
    }

    @Override
    public WebPropertyDto findPropertyById(@RequestBody Long goodsId) {
        return propertyService.findPropertyById(goodsId);
    }

    @Override
    public Result updateProperty(@RequestBody WebPropertyDto webPropertyDto) {
        return propertyService.updateProperty(webPropertyDto);
    }

    @Override
    public List<WebPropertyDto> findAllPropertyNoPage() {
        return propertyService.findAllPropertyNoPage();
    }
}
