package com.publiccms.common.handler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * FacetPageHandler
 * 
 */
public class FacetPageHandler extends PageHandler {
    private Map<String, Map<String, Integer>> map = new LinkedHashMap<>();

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
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return facet result map
     */
    public Map<String, Map<String, Integer>> getFacetMap() {
        return map;
    }

}
