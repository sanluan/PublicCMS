package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsContentLang;

/**
 *
 * CmsContentLangParameters
 * 
 */
public class CmsContentLangParameters extends ExtendDataParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CmsContentLang entity;
    private List<CmsContentFile> files;
    private List<CmsContentFile> images;

    /**
     * @return the entity
     */
    public CmsContentLang getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(CmsContentLang entity) {
        this.entity = entity;
    }

    /**
     * @return
     */
    public List<CmsContentFile> getFiles() {
        return files;
    }

    /**
     * @param files
     */
    public void setFiles(List<CmsContentFile> files) {
        this.files = files;
    }

    /**
     * @return
     */
    public List<CmsContentFile> getImages() {
        return images;
    }

    /**
     * @param images
     */
    public void setImages(List<CmsContentFile> images) {
        this.images = images;
    }
}