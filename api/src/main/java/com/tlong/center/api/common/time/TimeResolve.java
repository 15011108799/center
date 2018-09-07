package com.tlong.center.api.common.time;


import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

/**
 * Date时间处理工具
 */
@Component
@Transactional
public class TimeResolve {



//    public static Date  after(Date orangeDate, Integer year,Integer Month,Integer day){
//
//        return date;
//    }
//
//    public static Date  after(Integer year,Integer Month,Integer day,Integer hour,Integer minute,Integer second){
//
//        return date;
//    }
//
//
//
//    public static Date  afterYear(Integer year,Integer Month,Integer day){
//
//        return date;
//    }
//
//    public static Date  afterMonth(Integer year,Integer Month,Integer day){
//
//        return date;
//    }
//
    public static Date  afterDay(Date orangeDate, Integer day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(orangeDate);
        calendar.add(Calendar.DAY_OF_YEAR,day); //增加一天,负数为减少一天
        return calendar.getTime();
    }
//
//    public static Date  afterHour(Integer year,Integer Month,Integer day){
//
//        return date;
//    }
//
//    public static Date  afterMinute(Integer year,Integer Month,Integer day){
//
//        return date;
//    }
//
//    public static Date  afterSecond(Integer year,Integer Month,Integer day){
//
//        return date;
//    }

}
