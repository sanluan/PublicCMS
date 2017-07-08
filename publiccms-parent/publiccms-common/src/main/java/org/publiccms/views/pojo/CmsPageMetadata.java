package org.publiccms.views.pojo;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 *
 * CmsPageMetadata
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsPageMetadata implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String alias;
	private String publishPath;
	private boolean useDynamic;
	private boolean needLogin;
	private boolean needBody;
	private String acceptParamters;
	private Integer cacheTime;
	private List<ExtendField> extendList;
	private List<ExtendData> extendDataList;
	private Map<String, String> extendData;

	/**
	 * 
	 */
	public CmsPageMetadata() {
	}

	/**
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return
	 */
	public String getPublishPath() {
		return publishPath;
	}

	/**
	 * @param publishPath
	 */
	public void setPublishPath(String publishPath) {
		this.publishPath = publishPath;
	}

	/**
	 * @return
	 */
	public boolean isNeedLogin() {
		return needLogin;
	}

	/**
	 * @param needLogin
	 */
	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	/**
	 * @return
	 */
	public List<ExtendField> getExtendList() {
		return extendList;
	}

	/**
	 * @param extendList
	 */
	public void setExtendList(List<ExtendField> extendList) {
		this.extendList = extendList;
	}

	/**
	 * @return
	 */
	@JsonIgnore
	public Map<String, String> getExtendData() {
		if (empty(extendData)) {
			extendData = new HashMap<>();
			if (notEmpty(extendDataList)) {
				for (ExtendData extend : extendDataList) {
					extendData.put(extend.getName(), extend.getValue());
				}
			}
		}
		return extendData;
	}

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

	/**
	 * @return
	 */
	public Integer getCacheTime() {
		return cacheTime;
	}

	/**
	 * @param cacheTime
	 */
	public void setCacheTime(Integer cacheTime) {
		this.cacheTime = cacheTime;
	}

	/**
	 * @return
	 */
	public String getAcceptParamters() {
		return acceptParamters;
	}

	/**
	 * @param acceptParamters
	 */
	public void setAcceptParamters(String acceptParamters) {
		this.acceptParamters = acceptParamters;
	}

	/**
	 * @return
	 */
	public boolean isUseDynamic() {
		return useDynamic;
	}

	/**
	 * @param useDynamic
	 */
	public void setUseDynamic(boolean useDynamic) {
		this.useDynamic = useDynamic;
	}

    /**
     * @return
     */
    public boolean isNeedBody() {
        return needBody;
    }

    /**
     * @param needBody
     */
    public void setNeedBody(boolean needBody) {
        this.needBody = needBody;
    }
}