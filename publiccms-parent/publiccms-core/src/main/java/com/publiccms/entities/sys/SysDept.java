package com.publiccms.entities.sys;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.IDStyleGenerator;
import com.publiccms.common.generator.annotation.GeneratorColumn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

/**
 * SysDept generated by hbm2java
 */
@Entity
@Table(name = "sys_dept", uniqueConstraints = @UniqueConstraint(columnNames = { "site_id", "code" }))
@DynamicUpdate
public class SysDept implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private Integer id;
    @GeneratorColumn(title = "站点", condition = true)
    @JsonIgnore
    private short siteId;
    /**
     * name<p>
     * 名称
     */
    @GeneratorColumn(title = "名称", condition = true, like = true, name = "name")
    @NotNull
    @Length(max = 50)
    private String name;
    /**
     * code<p>
     * 编码
     */
    @GeneratorColumn(title = "编码", condition = true, like = true, name = "name")
    @NotNull
    @Length(max = 50)
    private String code;
    /**
     * parent id<p>
     * 父部门id
     */
    @GeneratorColumn(title = "父部门", condition = true)
    private Integer parentId;
    /**
     * description<p>
     * 描述
     */
    @GeneratorColumn(title = "描述")
    @Length(max = 300)
    private String description;
    /**
     * manage user id<p>
     * 负责用户id
     */
    @GeneratorColumn(title = "负责人", condition = true)
    private Long userId;
    /**
     * max content sort<p>
     * 最大内容置顶级别
     */
    @GeneratorColumn(title = "最大内容置顶级别")
    private int maxSort;
    /**
     * owns all category<p>
     * 拥有全部分类
     */
    @GeneratorColumn(title = "拥有全部分类")
    private boolean ownsAllCategory;
    /**
     * owns all page<p>
     * 拥有全部页面
     */
    @GeneratorColumn(title = "拥有全部页面")
    private boolean ownsAllPage;
    /**
     * owns all config<p>
     * 拥有全部配置
     */
    @GeneratorColumn(title = "拥有全部配置")
    private boolean ownsAllConfig;

    public SysDept() {
    }

    public SysDept(short siteId, String name, String code, int maxSort, boolean ownsAllCategory, boolean ownsAllPage,
            boolean ownsAllConfig) {
        this.siteId = siteId;
        this.name = name;
        this.code = code;
        this.maxSort = maxSort;
        this.ownsAllCategory = ownsAllCategory;
        this.ownsAllPage = ownsAllPage;
        this.ownsAllConfig = ownsAllConfig;
    }

    public SysDept(short siteId, String name, String code, Integer parentId, String description, Long userId, int maxSort,
            boolean ownsAllCategory, boolean ownsAllPage, boolean ownsAllConfig) {
        this.siteId = siteId;
        this.name = name;
        this.code = code;
        this.parentId = parentId;
        this.description = description;
        this.userId = userId;
        this.maxSort = maxSort;
        this.ownsAllCategory = ownsAllCategory;
        this.ownsAllPage = ownsAllPage;
        this.ownsAllConfig = ownsAllConfig;
    }

    @Id
    @GeneratedValue(generator = "cmsGenerator")
    @GenericGenerator(name = "cmsGenerator", type = IDStyleGenerator.class)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
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

    @Column(name = "code", nullable = false, length = 50)
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "parent_id")
    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "description", length = 300)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "user_id")
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "max_sort", nullable = false)
    public int getMaxSort() {
        return this.maxSort;
    }

    public void setMaxSort(int maxSort) {
        this.maxSort = maxSort;
    }

    @Column(name = "owns_all_category", nullable = false)
    public boolean isOwnsAllCategory() {
        return this.ownsAllCategory;
    }

    public void setOwnsAllCategory(boolean ownsAllCategory) {
        this.ownsAllCategory = ownsAllCategory;
    }

    @Column(name = "owns_all_page", nullable = false)
    public boolean isOwnsAllPage() {
        return this.ownsAllPage;
    }

    public void setOwnsAllPage(boolean ownsAllPage) {
        this.ownsAllPage = ownsAllPage;
    }

    @Column(name = "owns_all_config", nullable = false)
    public boolean isOwnsAllConfig() {
        return this.ownsAllConfig;
    }

    public void setOwnsAllConfig(boolean ownsAllConfig) {
        this.ownsAllConfig = ownsAllConfig;
    }
}
