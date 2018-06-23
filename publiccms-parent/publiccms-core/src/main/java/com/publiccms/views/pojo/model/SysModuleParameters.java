package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.sys.SysModuleLang;

/**
 *
 * SysConfigParameters
 * 
 */
public class SysModuleParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<SysModuleLang> langList;

    /**
     * @return the langList
     */
    public List<SysModuleLang> getLangList() {
        return langList;
    }

    /**
     * @param langList
     *            the langList to set
     */
    public void setLangList(List<SysModuleLang> langList) {
        this.langList = langList;
    }

}