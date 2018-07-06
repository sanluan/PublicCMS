package com.publiccms.common.base;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.tools.CommonUtils;

/**
 *
 * BaseService
 * 
 * @param <E>
 * 
 */
@Transactional
public abstract class BaseService<E> {
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    protected BaseDao<E> dao;

    /**
     * @param id
     * @return entity
     */
    public E getEntity(Serializable id) {
        return null != id ? dao.getEntity(id) : null;
    }

    /**
     * @param id
     * @param pk
     * @return entity
     */
    public E getEntity(Serializable id, String pk) {
        return dao.getEntity(id, pk);
    }

    /**
     * @param ids
     * @param pk
     * @return entitys list
     */
    public List<E> getEntitys(Serializable[] ids, String pk) {
        return dao.getEntitys(ids, pk);
    }

    /**
     * @param ids
     * @return entitys list
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
     * @return entity
     */
    public E update(Serializable id, E newEntity, String[] ignoreProperties) {
        E entity = getEntity(id);
        if (null != entity) {
            BeanUtils.copyProperties(dao.init(newEntity), entity, ignoreProperties);
        }
        return entity;
    }

    /**
     * @param id
     * @param newEntity
     * @return entity
     */
    public E update(Serializable id, E newEntity) {
        E entity = getEntity(id);
        if (null != entity) {
            BeanUtils.copyProperties(dao.init(newEntity), entity);
        }
        return entity;
    }

    /**
     * @param entity
     * @return id
     */
    public Serializable save(E entity) {
        return dao.save(entity);
    }

    /**
     * @param entityList
     */
    public void save(List<E> entityList) {
        if (CommonUtils.notEmpty(entityList)) {
            for (E entity : entityList) {
                save(entity);
            }
        }
    }
}
