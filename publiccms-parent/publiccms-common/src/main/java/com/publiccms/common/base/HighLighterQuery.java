package com.publiccms.common.base;

import org.apache.lucene.search.Query;

public class HighLighterQuery {
    private String defaultFieldName;
    private String[] fields;
    private Query query;
    private String preTag;
    private String postTag;

    public HighLighterQuery() {
        super();
    }

    /**
     * @return the defaultFieldName
     */
    public String getDefaultFieldName() {
        return defaultFieldName;
    }

    /**
     * @param defaultFieldName
     *            the defaultFieldName to set
     */
    public void setDefaultFieldName(String defaultFieldName) {
        this.defaultFieldName = defaultFieldName;
    }

    /**
     * @return the fields
     */
    public String[] getFields() {
        return fields;
    }

    /**
     * @param fields
     *            the fields to set
     */
    public void setFields(String[] fields) {
        this.fields = fields;
    }

    /**
     * @return the query
     */
    public Query getQuery() {
        return query;
    }

    /**
     * @param query
     *            the query to set
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * @return the preTag
     */
    public String getPreTag() {
        return preTag;
    }

    /**
     * @param preTag
     *            the preTag to set
     */
    public void setPreTag(String preTag) {
        this.preTag = preTag;
    }

    /**
     * @return the postTag
     */
    public String getPostTag() {
        return postTag;
    }

    /**
     * @param postTag
     *            the postTag to set
     */
    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }

}
