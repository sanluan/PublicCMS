package com.publiccms.logic.mapper.tools;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SqlMapper {
    List<Map<String, Object>> select(@Param("sql") String sql);
    
    Map<String, Object> query(@Param("sql") String sql);

    int insert(@Param("sql") String sql);

    int update(@Param("sql") String sql);

    int delete(@Param("sql") String sql);
}