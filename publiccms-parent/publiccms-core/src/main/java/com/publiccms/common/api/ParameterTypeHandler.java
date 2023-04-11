package com.publiccms.common.api;

import java.util.List;

import com.publiccms.entities.sys.SysSite;

/**
 * ParameterTypeHandler 参数类型处理器
 * 
 * @param <E>
 *            实体类型
 * @param <P>
 *            参数类型
 * 
 */
public interface ParameterTypeHandler<E, P> {
    public String getType();

    public List<E> getParameterValueList(SysSite site, P[] values);

    public E getParameterValue(SysSite site, P value);

    public P[] dealParameterValues(String[] values) throws IllegalArgumentException;

    public P dealParameterValue(String value) throws IllegalArgumentException;
}