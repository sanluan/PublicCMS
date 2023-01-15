package com.publiccms.logic.service.tools;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.logic.mapper.tools.SqlMapper;

/**
 *
 * SqlService
 * 
 */
@Service
@Transactional
public class SqlService {

    /**
     * @param sql
     * @return
     */
    public int insert(String sql) {
        return mapper.insert(sql);
    }

    /**
     * @param sql
     * @return
     */
    public int update(String sql) {
        return mapper.update(sql);
    }

    /**
     * @param siteId
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updateContentAttribute(short siteId, String oldurl, String newurl) {
        return mapper.updateContentAttribute(siteId, oldurl, newurl);
    }

    /**
     * @param siteId
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updateContentRelated(short siteId, String oldurl, String newurl) {
        return mapper.updateContentRelated(siteId, oldurl, newurl);
    }

    /**
     * @param siteId
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updatePlaceAttribute(short siteId, String oldurl, String newurl) {
        return mapper.updatePlaceAttribute(siteId, oldurl, newurl);
    }

    /**
     * @param siteId
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updatePlace(short siteId, String oldurl, String newurl) {
        return mapper.updatePlace(siteId, oldurl, newurl);
    }

    /**
     * @param siteId
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updateCategoryAttribute(short siteId, String oldurl, String newurl) {
        return mapper.updateCategoryAttribute(siteId, oldurl, newurl);
    }
    

    /**
     * @param siteId
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updateConfigData(short siteId, String oldurl, String newurl) {
        return mapper.updateConfigData(siteId, oldurl, newurl);
    }

    /**
     * @param sql
     * @return
     */
    public int delete(String sql) {
        return mapper.delete(sql);
    }

    /**
     * @param sql
     * @return
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> select(String sql) {
        return mapper.select(sql);
    }

    @Resource
    private SqlMapper mapper;
}
