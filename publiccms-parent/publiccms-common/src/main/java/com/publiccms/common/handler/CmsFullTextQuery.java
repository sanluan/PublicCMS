package com.publiccms.common.handler;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextQuery;

/**
 * CmsFullTextQuery
 * 
 */
public class CmsFullTextQuery {
    private FullTextQuery FullTextQuery;
    private Query luceneQuery;

    public CmsFullTextQuery(FullTextQuery fullTextQuery, Query luceneQuery) {
        FullTextQuery = fullTextQuery;
        this.luceneQuery = luceneQuery;
    }

    /**
     * @return the fullTextQuery
     */
    public FullTextQuery getFullTextQuery() {
        return FullTextQuery;
    }

    /**
     * @param fullTextQuery
     *            the fullTextQuery to set
     */
    public void setFullTextQuery(FullTextQuery fullTextQuery) {
        FullTextQuery = fullTextQuery;
    }

    /**
     * @return the luceneQuery
     */
    public Query getLuceneQuery() {
        return luceneQuery;
    }

    /**
     * @param luceneQuery
     *            the luceneQuery to set
     */
    public void setLuceneQuery(Query luceneQuery) {
        this.luceneQuery = luceneQuery;
    }
}
