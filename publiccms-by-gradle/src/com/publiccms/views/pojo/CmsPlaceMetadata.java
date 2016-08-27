package com.publiccms.views.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsPlaceMetadata implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
	private String alias;
    private Integer size;
    private Integer[] adminIds;
    private boolean allowContribute;
    private boolean allowAnonymous;

    private List<ExtendField> extendList;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public boolean isAllowContribute() {
        return allowContribute;
    }

    public void setAllowContribute(boolean allowContribute) {
        this.allowContribute = allowContribute;
    }
    
    public boolean isAllowAnonymous() {
        return allowAnonymous;
    }

    public void setAllowAnonymous(boolean allowAnonymous) {
        this.allowAnonymous = allowAnonymous;
    }

    public List<ExtendField> getExtendList() {
        return extendList;
    }

    public void setExtendList(List<ExtendField> extendList) {
        this.extendList = extendList;
    }

    public void setAdminIds(Integer[] adminIds) {
        this.adminIds = adminIds;
    }

    public Integer[] getAdminIds() {
        return adminIds;
    }
}