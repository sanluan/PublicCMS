package com.publiccms.logic.service.tools;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.logic.mapper.tools.SqlMapper;
import com.sanluan.common.datasource.MultiDataSource;

@Service
@Transactional
public class SqlService {
    public int insert(String hql) {
        return mapper.insert(hql);
    }

    public int update(String hql) {
        return mapper.update(hql);
    }

    public int delete(String hql) {
        return mapper.delete(hql);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> select(String sql) {
        return mapper.select(sql);
    }
    
    public void setDataSourceName(String dataSourceName){
        MultiDataSource.setDataSourceName(dataSourceName);
    }

    public void resetDataSourceName() {
        MultiDataSource.resetDataSourceName();
    }

    @Autowired
    private SqlMapper mapper;
}
