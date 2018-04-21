package com.tlong.center.common.utils;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class PageAndSortUtil {

    //分页排序处理
    public static PageRequest pageAndSort(PageAndSortRequestDto requestDto){
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        PageRequest pageRequest = new PageRequest(0, 10, sort);
        if (Objects.nonNull(requestDto)) {
            //处理sort
            if (requestDto.getOrderProperty() != null && requestDto.getDirection() != null) {
                Sort.Direction direction1 = requestDto.getDirection() == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
                sort = new Sort(new Sort.Order(direction1, requestDto.getOrderProperty()));
            }
            if (requestDto.getOrderProperty() != null && requestDto.getDirection() == null) {
                sort = new Sort(new Sort.Order(Sort.Direction.DESC, requestDto.getOrderProperty()));
            }
            if (requestDto.getDirection() != null && requestDto.getOrderProperty() == null) {
                Sort.Direction direction1 = requestDto.getDirection() == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
                sort = new Sort(new Sort.Order(direction1, "id"));
            }

            //处理page size
            if (requestDto.getPage() != null && requestDto.getSize() != null) {
                pageRequest = new PageRequest(requestDto.getPage(), requestDto.getSize(), sort);
            }
            if (requestDto.getPage() != null && requestDto.getSize() == null){
                pageRequest = new PageRequest(requestDto.getPage(), 10, sort);
            }
            if (requestDto.getSize() != null && requestDto.getPage() == null){
                pageRequest = new PageRequest(0,requestDto.getSize(),sort);
            }
            return pageRequest;
        }
        return pageRequest;
    }
}
