package com.publiccms.common.handler;

import java.util.Map;

/**
 *
 * FacetPageHandler
 * 
 */
public class FacetPageHandler extends PageHandler {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, Map<String, Integer>> facetMap;

    /**
     * @param pageIndex
     * @param pageSize
     * @param totalCount
     * @param maxResults
     */
    public FacetPageHandler(Integer pageIndex, Integer pageSize, int totalCount, Integer maxResults) {
        super(pageIndex, pageSize, totalCount, maxResults);
    }

    /**
     * @return facet result map
     */
    public Map<String, Map<String, Integer>> getFacetMap() {
        return facetMap;
    }

    /**
     * @param facetMap
     *            the facetMap to set
     */
    public void setFacetMap(Map<String, Map<String, Integer>> facetMap) {
        this.facetMap = facetMap;
    }

}
