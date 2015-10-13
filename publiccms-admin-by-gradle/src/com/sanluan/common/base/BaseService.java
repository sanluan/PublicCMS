package com.sanluan.common.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class BaseService<E> extends Base {
    @Autowired
    protected BaseDao<E> dao;

    /**
     * @param id
     * @return
     */
    public E getEntity(Serializable id) {
        return dao.getEntity(id);
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
     * @param id
     * @return
     */
    public List<E> getEntitys(Serializable[] ids, String pk) {
        return dao.getEntitys(ids, pk);
    }

    /**
     * @param id
     * @return
     */
    public List<E> getEntitys(Serializable[] ids) {
        return dao.getEntitys(ids);
    }

    /**
     * @param id
     * @return
     */
    public E delete(Serializable id) {
        return dao.delete(id);
    }

    /**
     * @param id
     * @param newEntity
     * @param ignoreProperties
     * @return
     */
    public E update(Serializable id, E newEntity, String[] ignoreProperties) {
        E entity = getEntity(id);
        if (notEmpty(entity)) {
            BeanUtils.copyProperties(dao.init(newEntity), entity, ignoreProperties);
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
        if (notEmpty(entity)) {
            BeanUtils.copyProperties(dao.init(newEntity), entity);
        }
        return entity;
    }

    /**
     * @param entity
     * @return
     */
    public E save(E entity) {
        return dao.save(entity);
    }
}
