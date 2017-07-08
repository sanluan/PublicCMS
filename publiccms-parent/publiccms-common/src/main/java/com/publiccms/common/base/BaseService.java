package com.publiccms.common.base;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.datasource.MultiDataSource;
/**
 *
 * BaseService
 * @param <E> 
 * 
 */
@Transactional
public abstract class BaseService<E> implements Base {
    protected final Log log = getLog(getClass());
    @Autowired
    protected BaseDao<E> dao;

    /**
     * @param id
     * @return
     */
    public E getEntity(Serializable id) {
        return null != id ? dao.getEntity(id) : null;
    }

    /**
     * @param id
     * @param pk
     * @return
     */
    public E getEntity(Serializable id, String pk) {
        return dao.getEntity(id, pk);
    }

    /**
     * @param ids
     * @param pk
     * @return
     */
    public List<E> getEntitys(Serializable[] ids, String pk) {
        return dao.getEntitys(ids, pk);
    }

    /**
     * @param ids
     * @return
     */
    public List<E> getEntitys(Serializable[] ids) {
        return dao.getEntitys(ids);
    }

    /**
     * @param ids
     */
    public void delete(Serializable[] ids) {
        for (Serializable id : ids) {
            delete(id);
        }
    }

    /**
     * @param id
     */
    public void delete(Serializable id) {
        dao.delete(id);
    }

    /**
     * @param id
     * @param newEntity
     * @param ignoreProperties
     * @return
     */
    public E update(Serializable id, E newEntity, String[] ignoreProperties) {
        E entity = getEntity(id);
        if (null != entity) {
            copyProperties(dao.init(newEntity), entity, ignoreProperties);
        }
        return entity;
    }

    /**
     * @param id
     * @param newEntity
     * @return
     */
    public E update(Serializable id, E newEntity) {
        E entity = getEntity(id);
        if (null != entity) {
            copyProperties(dao.init(newEntity), entity);
        }
        return entity;
    }

    /**
     * @param entity
     * @return
     */
    public Serializable save(E entity) {
        return dao.save(entity);
    }
    
    /**
     * @param entityList
     */
    public void save(List<E> entityList) {
        if (notEmpty(entityList)) {
            for (E entity : entityList) {
                save(entity);
            }
        }
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
}
