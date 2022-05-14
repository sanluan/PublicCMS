package com.publiccms.common.handler;

import java.util.Map;

/**
 *
 * FacetPageHandler<p>
 * 分面结果分页处理器
 * 
 */
public class FacetPageHandler extends PageHandler {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * facet result map<field,<value,count>><p>
     * 分面搜索结果<字段名,<值,结果总数>>
     */
    private Map<String, Map<String, Long>> facetMap;

    /**
     * @param pageIndex
     * @param pageSize
     */
    public FacetPageHandler(Integer pageIndex, Integer pageSize) {
        super(pageIndex, pageSize);
    }

    /**
     * @return facet result map
     */
    public Map<String, Map<String, Long>> getFacetMap() {
        return facetMap;
    }

    /**
     * @param facetMap
     *            the facetMap to set
     */
    public void setFacetMap(Map<String, Map<String, Long>> facetMap) {
        this.facetMap = facetMap;
    }

}
