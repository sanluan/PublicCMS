package org.publiccms.common.search;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

/**
 *
 * SiteIdFilterFactory
 * 
 */
@SuppressWarnings("deprecation")
public class SiteIdFilterFactory {
    private Integer siteId;

    /**
     * @return
     */
    @Factory
    public Filter getFilter() {
        Query query = NumericRangeQuery.newIntRange("siteId", siteId, siteId, true, true);
        return new CachingWrapperFilter(new QueryWrapperFilter(query));
    }

    /**
     * @param siteId
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
}