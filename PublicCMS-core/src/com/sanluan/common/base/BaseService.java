package com.sanluan.common.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class BaseService<E, D extends BaseDao<E>> extends Base {
	protected abstract D getDao();

	/**
	 * @param id
	 * @return
	 */
	public E getEntity(Serializable id) {
		return getDao().getEntity(id);
	}

	/**
	 * @param id
	 * @param pk
	 * @return
	 */
	public E getEntity(Serializable id, String pk) {
		return getDao().getEntity(id, pk);
	}

	/**
	 * @param id
	 * @return
	 */
	public List<E> getEntitys(Serializable[] ids, String pk) {
		return getDao().getEntitys(ids, pk);
	}

	/**
	 * @param id
	 * @return
	 */
	public List<E> getEntitys(Serializable[] ids) {
		return getDao().getEntitys(ids);
	}

	/**
	 * @param id
	 * @return
	 */
	public E delete(Serializable id) {
		return getDao().delete(id);
	}

	/**
	 * @param id
	 * @param newEntity
	 * @param ignoreProperties
	 * @return
	 */
	public E update(Serializable id, E newEntity, String ignoreProperties[]) {
		E entity = getEntity(id);
		if (notEmpty(entity))
			BeanUtils.copyProperties(newEntity, entity, ignoreProperties);
		return entity;
	}

	/**
	 * @param id
	 * @param newEntity
	 * @return
	 */
	public E update(Serializable id, E newEntity) {
		E entity = getEntity(id);
		if (notEmpty(entity))
			BeanUtils.copyProperties(newEntity, entity);
		return entity;
	}

	/**
	 * @param entity
	 * @return
	 */
	public E save(E entity) {
		return getDao().save(entity);
	}
}
