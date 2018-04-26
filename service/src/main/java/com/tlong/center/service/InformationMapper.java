package com.tlong.center.service;

import java.util.List;
import java.util.Map;

public interface InformationMapper {

    String getMidByclassid(String classid);

    String getFilenameByclassid(String classid);

    String getNewstempidByclassid(String classid);

    List<Map<String, Object>> runSql(Map<String, Object> sql);

    Map<String, Object> runSqlGetMap(Map<String, Object> sql);

    String runSqlGetString(Map<String, Object> sql);

    int runSqlDelete(Map<String, Object> sql);

    int runSqlOperate(Map<String, Object> sql);

    String getTemptextByTempid(String tempid);

    String getListtempidByclassid(String classid);

    int getLencordByclassid(String classid);

    int getCount(Map<String, Object> sql); //执行sql语句返回总数

    void selectSql(String sql);
}
