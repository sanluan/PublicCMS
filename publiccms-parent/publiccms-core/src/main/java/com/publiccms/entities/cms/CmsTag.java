package com.publiccms.entities.cms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * CmsTag generated by hbm2java
 */
@Entity
@Table(name = "cms_tag")
@DynamicUpdate
public class CmsTag implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private Long id;
    @GeneratorColumn(title = "站点", condition = true)
    @JsonIgnore
    private short siteId;
    /**
     * name<p>
     * 名称
     */
    @GeneratorColumn(title = "名称", condition = true, like = true)
    private String name;
    /**
     * type id<p>
     * 类型id
     */
    @GeneratorColumn(title = "类型", condition = true)
    private Integer typeId;
    /**
     * search count<p>
     * 搜索次数
     */
    @GeneratorColumn(title = "搜索次数", order = true)
    private int searchCount;

    public CmsTag() {
    }

    public CmsTag(short siteId, String name, int searchCount) {
        this.siteId = siteId;
        this.name = name;
        this.searchCount = searchCount;
    }

    public CmsTag(short siteId, String name, Integer typeId, int searchCount) {
        this.siteId = siteId;
        this.name = name;
        this.typeId = typeId;
        this.searchCount = searchCount;
    }

    @Id
    @GeneratedValue(generator = "cmsGenerator")
    @GenericGenerator(name = "cmsGenerator", strategy = CmsUpgrader.IDENTIFIER_GENERATOR)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "type_id")
    public Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Column(name = "search_count", nullable = false)
    public int getSearchCount() {
        return this.searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

}
