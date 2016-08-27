package com.sanluan.common.base;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.util.List;

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
    public void delete(Serializable[] ids) {
        for (Serializable id : ids) {
            delete(id);
        }
    }

    /**
     * @param id
     * @return
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
        if (notEmpty(entity)) {
            copyProperties(dao.init(newEntity), entity, ignoreProperties);
        }
        return entity;
    }

    /**
     * @param id
     * @param newEntity
     * @return
     */
    public void update(Serializable id, E newEntity) {
        E entity = getEntity(id);
        if (notEmpty(entity)) {
            copyProperties(dao.init(newEntity), entity);
        }
    }

    /**
     * @param entity
     * @return
     */
    public Serializable save(E entity) {
        return dao.save(entity);
    }
}
