package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.message.MessageRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("消息管理接口")
public interface WebMessageApi {
    @ApiOperation("添加消息接口")
    @PostMapping("/addMessage")
    Result addMessage(@RequestBody MessageRequestDto requestDto);

    @ApiOperation("查询所有信息接口")
    @PostMapping("/findAllMessage")
    PageResponseDto<MessageRequestDto> findAllMessage(@RequestBody PageAndSortRequestDto requestDto);

    @ApiOperation("删除信息接口")
    @PutMapping("/delMessage")
    Result delMessage(@RequestParam Long id);


    @ApiOperation("修改信息接口")
    @PostMapping("/updateMessage")
    Result updateMessage(@RequestBody MessageRequestDto requestDto);

    @ApiOperation("修改信息状态")
    @PostMapping("/updateMessageState")
    void updateMessageState(@RequestBody Long id);

    @ApiOperation("查找单条信息接口")
    @PutMapping("/findMessageById")
    MessageRequestDto findMessageById(@RequestBody Long id);
}
