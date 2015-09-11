package com.sanluan.common.base;

import org.hibernate.Session;

import com.sanluan.common.datasource.MultiDataSource;

public abstract class BaseMultiDataSourceDao<E> extends BaseDao<E> {
	public abstract String getDataSource();

	@Override
	protected Session getSession() {
		MultiDataSource.setDataSourceName(getDataSource());
		return sessionFactory.getCurrentSession();
	}
}
