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
