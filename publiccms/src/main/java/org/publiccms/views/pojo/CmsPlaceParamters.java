package org.publiccms.views.pojo;

import java.util.List;

/**
 *
 * CmsPlaceParamters
 * 
 */
public class CmsPlaceParamters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
	List<ExtendData> extendDataList;

    /**
     * @return
     */
    public List<ExtendData> getExtendDataList() {
        return extendDataList;
    }

    /**
     * @param extendDataList
     */
    public void setExtendDataList(List<ExtendData> extendDataList) {
        this.extendDataList = extendDataList;
    }
}