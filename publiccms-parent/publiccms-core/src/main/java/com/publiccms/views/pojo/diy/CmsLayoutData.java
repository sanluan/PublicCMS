package com.publiccms.views.pojo.diy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsLayoutData diy布局数据
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsLayoutData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private String id;
    /**
     * list of module data list
     * 模块数据列表的列表
     */
    private List<List<CmsModuleData>> moduleList;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the moduleList
     */
    public List<List<CmsModuleData>> getModuleList() {
        return moduleList;
    }

    /**
     * @param moduleList
     *            the moduleList to set
     */
    public void setModuleList(List<List<CmsModuleData>> moduleList) {
        this.moduleList = moduleList;
    }
}
