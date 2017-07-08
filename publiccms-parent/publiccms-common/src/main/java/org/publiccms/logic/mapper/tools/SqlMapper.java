package org.publiccms.logic.mapper.tools;

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
     * @return
     */
    List<Map<String, Object>> select(@Param("sql") String sql);
    
    /**
     * @param sql
     * @return
     */
    Map<String, Object> query(@Param("sql") String sql);

    /**
     * @param sql
     * @return
     */
    int insert(@Param("sql") String sql);

    /**
     * @param sql
     * @return
     */
    int update(@Param("sql") String sql);

    /**
     * @param sql
     * @return
     */
    int delete(@Param("sql") String sql);
    
}