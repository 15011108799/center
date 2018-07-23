package com.tlong.center.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.message.MessageSearchRequestDto;
import com.tlong.center.api.dto.tlong.TlongRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.QMessage;
import com.tlong.center.domain.repository.TlongAboutRepository;
import com.tlong.center.domain.web.QTlong;
import com.tlong.center.domain.web.Tlong;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tlong.center.domain.web.QTlong.tlong;

@Component
@Transactional
public class TlongService {
    @Autowired
    private TlongAboutRepository repository;

    public Result addTlongshi(TlongRequestDto requestDto) {
        if (requestDto.getTitle() == null || requestDto.getContent() == null || requestDto.getTitle().equals("") || requestDto.getContent().equals(""))
            return new Result(0, "输入信息不能为空");
        Tlong tlong = new Tlong(requestDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        tlong.setPublishTime(simpleDateFormat.format(new Date()));
        Tlong tlong1 = repository.save(tlong);
        if (tlong1 == null) {
            return new Result(0, "添加失败");
        }
        return new Result(1, "添加成功");
    }

    public PageResponseDto<TlongRequestDto> findAllTlongshi(PageAndSortRequestDto requestDto) {
        PageResponseDto<TlongRequestDto> tlongRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<Tlong> tlongs = repository.findAll(pageRequest);
        List<TlongRequestDto> requestDtos = new ArrayList<>();
        tlongs.forEach(tlong -> {
            requestDtos.add(tlong.toDto());
        });
        tlongRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<Tlong> tlongs1 = repository.findAll();
        tlongs1.forEach(tlong -> {
            count[0]++;
        });
        tlongRequestDtoPageResponseDto.setCount(count[0]);
        return tlongRequestDtoPageResponseDto;
    }

    public Result delTlongshi(Long id) {
        Tlong tlong = repository.findOne(id);
        if (tlong == null)
            return new Result(0, "删除失败");
        repository.delete(id);
        return new Result(1, "删除成功");
    }

    public Result updateTlongshi(TlongRequestDto requestDto) {
        Tlong tlong = new Tlong(requestDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        tlong.setPublishTime(simpleDateFormat.format(new Date()));
        Tlong tlong1 = repository.save(tlong);
        if (tlong1 == null) {
            return new Result(0, "修改失败");
        }
        return new Result(1, "修改成功");
    }

    public void updateTlongState(Long id) {
        Tlong tlong = repository.findOne(id);
        if (tlong.getState() == 0)
            tlong.setState(1);
        else
            tlong.setState(0);
        repository.save(tlong);
    }

    public TlongRequestDto findTlongshiById(Long id) {
        Tlong tlong = repository.findOne(id);
        return tlong.toDto();
    }

    public PageResponseDto<TlongRequestDto> searchTlongshi(MessageSearchRequestDto requestDto) {
        PageResponseDto<TlongRequestDto> tlongRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Predicate pre = tlong.id.isNotNull();
        if (StringUtils.isNotEmpty(requestDto.getTitle()))
            pre = ExpressionUtils.and(pre, tlong.title.eq(requestDto.getTitle()));
        if (requestDto.getState() != 2)
            pre = ExpressionUtils.and(pre, tlong.state.intValue().eq(requestDto.getState()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlong.publishTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlong.publishTime.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre = ExpressionUtils.and(pre, tlong.publishTime.gt(requestDto.getStartTime() + " 00:00:00"));
        Page<Tlong> tlongs = repository.findAll(pre,pageRequest);
        List<TlongRequestDto> requestDtos = new ArrayList<>();
        tlongs.forEach(tlong -> {
            requestDtos.add(tlong.toDto());
        });
        tlongRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<Tlong> tlongs1 = repository.findAll(pre);
        tlongs1.forEach(tlong -> {
            count[0]++;
        });
        tlongRequestDtoPageResponseDto.setCount(count[0]);
        return tlongRequestDtoPageResponseDto;
    }

    public Result delBatchTlongshi(String id) {
        String[] goodsIds;
        if (StringUtils.isNotEmpty(id)) {
            goodsIds = id.split(",");
            for (int i = 0; i < goodsIds.length; i++) {
                delTlongshi(Long.valueOf(goodsIds[i]));
            }
        }
        return new Result(1, "删除成功");
    }

    public void updateBatchTlongState(String id) {
        String[] goodsIds;
        if (StringUtils.isNotEmpty(id)) {
            goodsIds = id.split(",");
            for (int i = 0; i < goodsIds.length; i++) {
                updateTlongState(Long.valueOf(goodsIds[i]));
            }
        }
    }
}
