package com.publiccms.logic.mapper.tools;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 *
 * SqlMapper
 * 
 */
public interface SqlMapper {

    /**
     * @param sql
     * @return result list
     */
    List<Map<String, Object>> select(@Param("sql") String sql);

    /**
     * @param sql
     * @return result
     */
    Map<String, Object> query(@Param("sql") String sql);

    /**
     * @param sql
     * @return id
     */
    int insert(@Param("sql") String sql);

    /**
     * @param sql
     * @return number of data updated
     */
    int update(@Param("sql") String sql);

    /**
     * @param oldurl
     * @param newurl
     * @return number of data updated
     */
    int updateContentAttribute(@Param("oldurl") String oldurl, @Param("newurl") String newurl);

    /**
     * @param oldurl
     * @param newurl
     * @return number of data updated
     */
    int updateContentRelated(@Param("oldurl") String oldurl, @Param("newurl") String newurl);

    /**
     * @param oldurl
     * @param newurl
     * @return number of data updated
     */
    int updatePlaceAttribute(@Param("oldurl") String oldurl, @Param("newurl") String newurl);

    /**
     * @param oldurl
     * @param newurl
     * @return number of data updated
     */
    int updatePlace(@Param("oldurl") String oldurl, @Param("newurl") String newurl);

    /**
     * @param sql
     * @return number of data deleted
     */
    int delete(@Param("sql") String sql);

}