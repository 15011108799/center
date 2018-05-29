package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.message.MessageRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.web.WebMessageApi;
import com.tlong.center.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/web/message")
public class WebMessageController implements WebMessageApi {

    @Autowired
    private MessageService messageService;

    @Override
    public Result addMessage(@RequestBody MessageRequestDto requestDto) {
        return messageService.addMessage(requestDto);
    }

    @Override
    public PageResponseDto<MessageRequestDto> findAllMessage(@RequestBody PageAndSortRequestDto requestDto) {
        return messageService.findAllMessage(requestDto);
    }

    @Override
    public Result delMessage(@RequestBody Long id) {
        return messageService.delMessage(id);
    }

    @Override
    public Result updateMessage(@RequestBody MessageRequestDto requestDto) {
        return messageService.updateMessage(requestDto);
    }

    @Override
    public void updateMessageState(@RequestBody Long id) {
        messageService.updateMessageState(id);
    }

    @Override
    public MessageRequestDto findMessageById(@RequestBody Long id) {
        return messageService.findOne(id);
    }
}
