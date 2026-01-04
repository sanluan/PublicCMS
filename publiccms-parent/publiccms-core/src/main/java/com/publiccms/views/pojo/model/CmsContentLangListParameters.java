package com.publiccms.views.pojo.model;

import java.util.List;

/**
 *
 * CmsContentLangListParameters
 * 
 */
public class CmsContentLangListParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsContentLangParameters> contentLangList;

    /**
     * @return the contentLangList
     */
    public List<CmsContentLangParameters> getContentLangList() {
        return contentLangList;
    }

    /**
     * @param contentLangList
     *            the contentLangList to set
     */
    public void setContentLangList(List<CmsContentLangParameters> contentLangList) {
        this.contentLangList = contentLangList;
    }

}