package com.tlong.center.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.message.MessageRequestDto;
import com.tlong.center.api.dto.message.MessageSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.Message;
import com.tlong.center.domain.app.QMessage;
import com.tlong.center.domain.repository.AppMessageRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class MessageService {
    @Autowired
    private AppMessageRepository appMessageRepository;

    public Result addMessage(MessageRequestDto requestDto) {
        if (requestDto.getTitle() == null || requestDto.getContent() == null || requestDto.getTitle().equals("") || requestDto.getContent().equals(""))
            return new Result(0, "输入信息不能为空");
        Message message = new Message();
        message.setTitle(requestDto.getTitle());
        message.setContent(requestDto.getContent());
        message.setUserName(requestDto.getUserName());
        message.setState(requestDto.getState());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        message.setPublishTime(simpleDateFormat.format(new Date()));
        Message message1 = appMessageRepository.save(message);
        if (message1 == null) {
            return new Result(0, "添加失败");
        }
        return new Result(1, "添加成功");
    }

    public PageResponseDto<MessageRequestDto> findAllMessage(PageAndSortRequestDto requestDto) {
        PageResponseDto<MessageRequestDto> messageRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<Message> messages = appMessageRepository.findAll(pageRequest);
        List<MessageRequestDto> requestDtos = new ArrayList<>();
        messages.forEach(message -> {
            MessageRequestDto messageRequestDto = new MessageRequestDto();
            messageRequestDto.setId(message.getId());
            messageRequestDto.setTitle(message.getTitle());
            messageRequestDto.setContent(message.getContent());
            messageRequestDto.setUserName(message.getUserName());
            messageRequestDto.setState(message.getState());
            messageRequestDto.setPublishTime(message.getPublishTime());
            requestDtos.add(messageRequestDto);
        });
        messageRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<Message> messages1 = appMessageRepository.findAll();
        messages1.forEach(message -> {
            count[0]++;
        });
        messageRequestDtoPageResponseDto.setCount(count[0]);
        return messageRequestDtoPageResponseDto;
    }

    public Result delMessage(Long id) {
        Message message = appMessageRepository.findOne(id);
        if (message == null)
            return new Result(0, "删除信息失败");
        appMessageRepository.delete(id);
        return new Result(1, "删除成功");
    }

    public Result updateMessage(@RequestBody MessageRequestDto requestDto) {
        Message message = new Message();
        message.setId(requestDto.getId());
        message.setTitle(requestDto.getTitle());
        message.setContent(requestDto.getContent());
        message.setState(requestDto.getState());
        message.setUserName(requestDto.getUserName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        message.setPublishTime(simpleDateFormat.format(new Date()));
        Message message1 = appMessageRepository.save(message);
        if (message1 == null) {
            return new Result(0, "修改失败");
        }
        return new Result(1, "修改成功");
    }

    public MessageRequestDto findOne(Long id) {
        Message message = appMessageRepository.findOne(id);
        MessageRequestDto requestDto = new MessageRequestDto();
        requestDto.setTitle(message.getTitle());
        requestDto.setContent(message.getContent());
        requestDto.setState(message.getState());
        requestDto.setUserName(message.getUserName());
        return requestDto;
    }

    public void updateMessageState(Long id) {
        Message message = appMessageRepository.findOne(id);
        if (message.getState() == 0)
            message.setState(1);
        else
            message.setState(0);
        appMessageRepository.save(message);
    }

    public PageResponseDto<MessageRequestDto> searchMessage(MessageSearchRequestDto requestDto) {
        PageResponseDto<MessageRequestDto> messageRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Predicate pre = QMessage.message.id.isNotNull();
        if (StringUtils.isNotEmpty(requestDto.getTitle()))
            pre = ExpressionUtils.and(pre, QMessage.message.title.eq(requestDto.getTitle()));
        if (requestDto.getState() != 2)
            pre = ExpressionUtils.and(pre, QMessage.message.state.intValue().eq(requestDto.getState()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, QMessage.message.publishTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, QMessage.message.publishTime.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre = ExpressionUtils.and(pre, QMessage.message.publishTime.gt(requestDto.getStartTime() + " 00:00:00"));
        Page<Message> messages = appMessageRepository.findAll(pre,pageRequest);
        List<MessageRequestDto> requestDtos = new ArrayList<>();
        messages.forEach(message -> {
            MessageRequestDto messageRequestDto = new MessageRequestDto();
            messageRequestDto.setId(message.getId());
            messageRequestDto.setTitle(message.getTitle());
            messageRequestDto.setContent(message.getContent());
            messageRequestDto.setUserName(message.getUserName());
            messageRequestDto.setState(message.getState());
            messageRequestDto.setPublishTime(message.getPublishTime());
            requestDtos.add(messageRequestDto);
        });
        messageRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<Message> messages1 = appMessageRepository.findAll(pre);
        messages1.forEach(message -> {
            count[0]++;
        });
        messageRequestDtoPageResponseDto.setCount(count[0]);
        return messageRequestDtoPageResponseDto;
    }

    public Result delBatchMessage(String id) {
        String[] goodsIds;
        if (StringUtils.isNotEmpty(id)) {
            goodsIds = id.split(",");
            for (int i = 0; i < goodsIds.length; i++) {
                delMessage(Long.valueOf(goodsIds[i]));
            }
        }
        return new Result(1, "删除成功");
    }

    public void updateBatchMessageState(String id) {
        String[] goodsIds;
        if (StringUtils.isNotEmpty(id)) {
            goodsIds = id.split(",");
            for (int i = 0; i < goodsIds.length; i++) {
                updateMessageState(Long.valueOf(goodsIds[i]));
            }
        }
    }
}
