package com.sanluan.common.handler;

import java.util.HashMap;
import java.util.Map;

public class FacetPageHandler extends PageHandler {
    private Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();

    public FacetPageHandler(Integer pageIndex, Integer pageSize, int totalCount, Integer maxResults) {
        super(pageIndex, pageSize, totalCount, maxResults);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Map<String, Map<String, Integer>> getMap() {
        return map;
    }

}
