package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.message.MessageSearchRequestDto;
import com.tlong.center.api.dto.tlong.TlongRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.web.WebTlongAboutApi;
import com.tlong.center.service.TlongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/tlong")
public class WebTlongAboutController implements WebTlongAboutApi {

    @Autowired
    private TlongService tlongService;

    @Override
    public Result addTlongshi(@RequestBody TlongRequestDto requestDto) {
        return tlongService.addTlongshi(requestDto);
    }

    @Override
    public PageResponseDto<TlongRequestDto> findAllTlongshi(@RequestBody PageAndSortRequestDto requestDto) {
        return tlongService.findAllTlongshi(requestDto);
    }

    @Override
    public Result delTlongshi(@RequestBody Long id) {
        return tlongService.delTlongshi(id);
    }

    @Override
    public Result delBatchTlongshi(@RequestBody String id) {
        return tlongService.delBatchTlongshi(id);
    }

    @Override
    public Result updateTlongshi(@RequestBody TlongRequestDto requestDto) {
        return tlongService.updateTlongshi(requestDto);
    }

    @Override
    public void updateTlongState(@RequestBody Long id) {
        tlongService.updateTlongState(id);
    }

    @Override
    public void updateBatchTlongState(@RequestBody String id) {
        tlongService.updateBatchTlongState(id);
    }

    @Override
    public TlongRequestDto findTlongshiById(@RequestBody Long id) {
        return tlongService.findTlongshiById(id);
    }

    @Override
    public PageResponseDto<TlongRequestDto> searchTlongshi(@RequestBody MessageSearchRequestDto requestDto) {
        return tlongService.searchTlongshi(requestDto);
    }
}
