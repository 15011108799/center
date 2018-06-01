package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.tlong.TlongRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.repository.TlongAboutRepository;
import com.tlong.center.domain.web.Tlong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class TlongService {
    @Autowired
    private TlongAboutRepository repository;

    public Result addTlongshi(TlongRequestDto requestDto) {
        if (requestDto.getTitle()==null||requestDto.getContent()==null||requestDto.getTitle().equals("")||requestDto.getContent().equals(""))
            return new Result(0, "输入信息不能为空");
        Tlong tlong = new Tlong(requestDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
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
}
