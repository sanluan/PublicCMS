package com.publiccms.views.pojo.diy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsModule diy组件
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsModule implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private String id;
    /**
     * region
     * <p>
     * 区域
     */
    private String region;
    /**
     * name
     * <p>
     * 名称
     */
    private String name;
    /**
     * clone
     * <p>
     * 复制
     */
    private boolean clone;
    /**
     * cover
     * <p>
     * 封面图
     */
    private String cover;
    /**
     * place path
     * <p>
     * 页面片段
     */
    private String place;
    /**
     * file path
     * <p>
     * 文件路径
     */
    private String filePath;

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
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region
     *            the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the clone
     */
    public boolean isClone() {
        return clone;
    }

    /**
     * @param clone
     *            the clone to set
     */
    public void setClone(boolean clone) {
        this.clone = clone;
    }

    /**
     * @return the cover
     */
    public String getCover() {
        return cover;
    }

    /**
     * @param cover
     *            the cover to set
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * @return the place
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place
     *            the place to set
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath
     *            the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
