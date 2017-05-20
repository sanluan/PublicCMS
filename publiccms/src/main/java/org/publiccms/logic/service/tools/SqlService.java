package org.publiccms.logic.service.tools;

import java.util.List;
import java.util.Map;

import org.publiccms.logic.mapper.tools.SqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.datasource.MultiDataSource;

/**
 *
 * SqlService
 * 
 */
@Service
@Transactional
public class SqlService {
    
    /**
     * @param hql
     * @return
     */
    public int insert(String hql) {
        return mapper.insert(hql);
    }

    /**
     * @param hql
     * @return
     */
    public int update(String hql) {
        return mapper.update(hql);
    }

    /**
     * @param hql
     * @return
     */
    public int delete(String hql) {
        return mapper.delete(hql);
    }

    /**
     * @param sql
     * @return
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> select(String sql) {
        return mapper.select(sql);
    }
    
    /**
     * @param dataSourceName
     */
    public void setDataSourceName(String dataSourceName){
        MultiDataSource.setDataSourceName(dataSourceName);
    }

    /**
     * 
     */
    public void resetDataSourceName() {
        MultiDataSource.resetDataSourceName();
    }

    @Autowired
    private SqlMapper mapper;
}
