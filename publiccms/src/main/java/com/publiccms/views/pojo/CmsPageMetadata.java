package com.publiccms.views.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sanluan.common.base.Base;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsPageMetadata extends Base implements java.io.Serializable {

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

	public CmsPageMetadata() {
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPublishPath() {
		return publishPath;
	}

	public void setPublishPath(String publishPath) {
		this.publishPath = publishPath;
	}

	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	public List<ExtendField> getExtendList() {
		return extendList;
	}

	public void setExtendList(List<ExtendField> extendList) {
		this.extendList = extendList;
	}

	@JsonIgnore
	public Map<String, String> getExtendData() {
		if (empty(extendData)) {
			extendData = new HashMap<String, String>();
			if (Base.notEmpty(extendDataList)) {
				for (ExtendData extend : extendDataList) {
					extendData.put(extend.getName(), extend.getValue());
				}
			}
		}
		return extendData;
	}

	public List<ExtendData> getExtendDataList() {
		return extendDataList;
	}

	public void setExtendDataList(List<ExtendData> extendDataList) {
		this.extendDataList = extendDataList;
	}

	public Integer getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(Integer cacheTime) {
		this.cacheTime = cacheTime;
	}

	public String getAcceptParamters() {
		return acceptParamters;
	}

	public void setAcceptParamters(String acceptParamters) {
		this.acceptParamters = acceptParamters;
	}

	public boolean isUseDynamic() {
		return useDynamic;
	}

	public void setUseDynamic(boolean useDynamic) {
		this.useDynamic = useDynamic;
	}

    public boolean isNeedBody() {
        return needBody;
    }

    public void setNeedBody(boolean needBody) {
        this.needBody = needBody;
    }
}