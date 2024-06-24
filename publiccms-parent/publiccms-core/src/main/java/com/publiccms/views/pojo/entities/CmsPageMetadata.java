package com.publiccms.views.pojo.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.publiccms.entities.sys.SysExtendField;

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
    /**
     * alias
     * <p>
     * 别名
     */
    private String alias;
    /**
     * static file path
     * <p>
     * 静态文件路径
     */
    private String publishPath;
    /**
     * allow dynamic access
     * <p>
     * 允许动态访问
     */
    private boolean useDynamic;
    /**
     * need login
     * <p>
     * 需要登录
     */
    private boolean needLogin;
    /**
     * need body field
     * <p>
     * 需要消息体
     */
    private boolean needBody;
    /**
     * accept parameters
     * <p>
     * 可接受参数名
     */
    private String acceptParameters;
    /**
     * cache time
     * <p>
     * 缓存时间
     */
    private Integer cacheTime;
    /**
     * content type
     * <p>
     * 内存类型
     */
    private String contentType;
    /**
     * extend field list
     * <p>
     * 扩展字段列表
     */
    private List<SysExtendField> extendList;
    /**
     * parameter type map
     * <p>
     * 参数类型表
     */
    private Map<String, ParameterType> parameterTypeMap;

    /**
     * @return alias
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
     * @return publishPath
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
     * @return needLogin
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
     * @return acceptParameters
     */
    public String getAcceptParameters() {
        return acceptParameters;
    }

    /**
     * @param acceptParameters
     */
    public void setAcceptParameters(String acceptParameters) {
        this.acceptParameters = acceptParameters;
    }

    /**
     * @return cacheTime
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
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType
     *            the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return useDynamic
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
     * @return needBody
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

    /**
     * @return extendList
     */
    public List<SysExtendField> getExtendList() {
        return extendList;
    }

    /**
     * @param extendList
     */
    public void setExtendList(List<SysExtendField> extendList) {
        this.extendList = extendList;
    }

    /**
     * @return the parameterTypeMap
     */
    public Map<String, ParameterType> getParameterTypeMap() {
        return parameterTypeMap;
    }

    /**
     * @param parameterTypeMap
     *            the parameterTypeMap to set
     */
    public void setParameterTypeMap(Map<String, ParameterType> parameterTypeMap) {
        this.parameterTypeMap = parameterTypeMap;
    }

    @JsonIgnore
    public Map<String, Object> getAsMap(CmsPageData data) {
        Map<String, Object> map = new HashMap<>();
        map.put("alias", getAlias());
        map.put("publishPath", getPublishPath());
        map.put("useDynamic", isUseDynamic());
        map.put("needLogin", isNeedLogin());
        map.put("needBody", isNeedBody());
        map.put("acceptParameters", getAcceptParameters());
        map.put("cacheTime", getCacheTime());
        map.put("contentType", getContentType());
        map.put("extendList", getExtendList());
        map.put("extendData", data.getExtendData());
        map.put("parameterTypeMap", getParameterTypeMap());
        return map;
    }
}