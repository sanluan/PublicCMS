package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.common.tools.CmsFileUtils.FileReplaceResult;

/**
 *
 * TemplateReplaceParameters
 * 
 */
public class TemplateReplaceParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<FileReplaceResult> replaceList;

    /**
     * @return the replaceList
     */
    public List<FileReplaceResult> getReplaceList() {
        return replaceList;
    }

    /**
     * @param replaceList
     *            the replaceList to set
     */
    public void setReplaceList(List<FileReplaceResult> replaceList) {
        this.replaceList = replaceList;
    }
}