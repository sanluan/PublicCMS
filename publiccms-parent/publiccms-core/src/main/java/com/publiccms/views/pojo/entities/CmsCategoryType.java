package com.publiccms.views.pojo.entities;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.publiccms.entities.sys.SysExtendField;

/**
 * CmsCategoryType
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsCategoryType implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private String id;
    /**
     * name<p>
     * 名称
     */
    @NotNull
    @Length(max = 50)
    private String name;
    /**
     * extend link<p>
     * 外链
     */
    private boolean onlyUrl;
    /**
     * sort<p>
     * 排序
     */
    private int sort;
    /**
     * static template path<p>
     * 静态化模板路径
     */
    private String templatePath;
    /**
     * path<p>
     * 访问路径
     */
    private String path;
    /**
     * contain child category's content<p>
     * 包含子分类内容
     */
    private boolean containChild;
    /**
     * page size<p>
     * 分页大小
     */
    private Integer pageSize;
    /**
     * extend field list<p>
     * 扩展字段列表
     */
    private List<SysExtendField> extendList;

    /**
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the onlyUrl
     */
    public boolean isOnlyUrl() {
        return onlyUrl;
    }

    /**
     * @param onlyUrl
     *            the onlyUrl to set
     */
    public void setOnlyUrl(boolean onlyUrl) {
        this.onlyUrl = onlyUrl;
    }

    /**
     * @return
     */
    public int getSort() {
        return this.sort;
    }

    /**
     * @param sort
     */
    public void setSort(int sort) {
        this.sort = sort;
    }

    /**
     * @return the templatePath
     */
    public String getTemplatePath() {
        return templatePath;
    }

    /**
     * @param templatePath
     *            the templatePath to set
     */
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the containChild
     */
    public boolean isContainChild() {
        return containChild;
    }

    /**
     * @param containChild
     *            the containChild to set
     */
    public void setContainChild(boolean containChild) {
        this.containChild = containChild;
    }

    /**
     * @return the pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the extendList
     */
    public List<SysExtendField> getExtendList() {
        return extendList;
    }

    /**
     * @param extendList
     *            the extendList to set
     */
    public void setExtendList(List<SysExtendField> extendList) {
        this.extendList = extendList;
    }

}
