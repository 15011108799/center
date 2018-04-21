package com.tlong.center.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 各种类型转换为List集合
 */
public class ToListUtil {

    public static<T> List<T> IterableToList(Iterable<T> iter){
        List newList = new ArrayList();
        iter.forEach(one -> newList.add(one));
        return newList;
    }
}
