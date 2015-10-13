package com.publiccms.common.index;

import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermRangeQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class PublishDateFilterFactory {
    private Date publishDate;

    @Factory
    public Filter getFilter() {
        Query query = TermRangeQuery.newStringRange("publishDate", null,
                DateTools.dateToString(publishDate, DateTools.Resolution.MILLISECOND), true, true);
        return new CachingWrapperFilter(new QueryWrapperFilter(query));
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}