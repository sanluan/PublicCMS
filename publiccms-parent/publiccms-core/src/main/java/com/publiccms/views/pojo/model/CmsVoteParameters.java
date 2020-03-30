package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.cms.CmsVoteItem;

/**
 * CmsVoteParameters
 * 
 */
public class CmsVoteParameters implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsVoteItem> itemList;

    /**
     * @return the itemList
     */
    public List<CmsVoteItem> getItemList() {
        return itemList;
    }

    /**
     * @param itemList
     *            the itemList to set
     */
    public void setItemList(List<CmsVoteItem> itemList) {
        this.itemList = itemList;
    }
}
