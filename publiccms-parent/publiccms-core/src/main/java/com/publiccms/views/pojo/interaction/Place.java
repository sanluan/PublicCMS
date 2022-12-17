package com.publiccms.views.pojo.interaction;

import java.util.List;

public class Place implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String path;
    private List<PlaceData> datalist;

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the datalist
     */
    public List<PlaceData> getDatalist() {
        return datalist;
    }

    /**
     * @param datalist
     *            the datalist to set
     */
    public void setDatalist(List<PlaceData> datalist) {
        this.datalist = datalist;
    }
}
