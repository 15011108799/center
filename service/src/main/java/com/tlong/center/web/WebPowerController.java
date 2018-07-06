package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.TlongPowerDto;
import com.tlong.center.api.web.WebPowerApi;
import com.tlong.center.service.WebPowerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/web/power")
public class WebPowerController implements WebPowerApi {


    final WebPowerService webPowerService;

    public WebPowerController(WebPowerService webPowerService) {
        this.webPowerService = webPowerService;
    }

    /**
     * 权限列表查询
     */
    @Override
    public List<TlongPowerDto> powerList() {
        return webPowerService.powerList();
    }

    /**
     * 新增权限
     */
    @Override
    public Result addPower(@RequestBody TlongPowerDto reqDto) {
        return webPowerService.addPower(reqDto);
    }

    /**
     * 删除权限
     */
    @Override
    public Result delPower(@RequestParam Long powerId) {
        return webPowerService.delPower(powerId);
    }

    /**
     * 修改权限
     */
    @Override
    public Result updatePower(@RequestBody TlongPowerDto reqDto, @PathVariable Long powerId) {
        return webPowerService.updatePower(reqDto,powerId);
    }

    @Override
    public List<Integer> powerIdList(@RequestBody Long roleId) {
        return webPowerService.powerIdList(roleId);
    }
}
