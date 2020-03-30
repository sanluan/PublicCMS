package com.publiccms.logic.service.tools;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updateContentAttribute(String oldurl, String newurl) {
        return mapper.updateContentAttribute(oldurl, newurl);
    }
    /**
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updateContentRelated(String oldurl, String newurl) {
        return mapper.updateContentRelated(oldurl, newurl);
    }
    /**
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updatePlaceAttribute(String oldurl, String newurl) {
        return mapper.updatePlaceAttribute(oldurl, newurl);
    }
    /**
     * @param oldurl
     * @param newurl
     * @return
     */
    public int updatePlace(String oldurl, String newurl) {
        return mapper.updatePlace(oldurl, newurl);
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

    @Autowired
    private SqlMapper mapper;
}
