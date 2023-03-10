package com.publiccms.entities.cms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * CmsWord generated by hbm2java
 */
@Entity
@Table(name = "cms_word", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "site_id" }))
@DynamicUpdate
public class CmsWord implements java.io.Serializable {

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
     * name
     * <p>
     * 名称
     */
    @GeneratorColumn(title = "名称", condition = true, like = true)
    @NotNull
    @Length(max = 100)
    private String name;
    /**
     * search count
     * <p>
     * 搜索次数
     */
    @GeneratorColumn(title = "搜索次数", order = true)
    private int searchCount;
    /**
     * hidden
     * <p>
     * 隐藏
     */
    @GeneratorColumn(title = "隐藏", condition = true)
    private boolean hidden;
    /**
     * ip
     */
    @GeneratorColumn(title = "IP")
    private String ip;
    /**
     * create date
     * <p>
     * 创建日期
     */
    @GeneratorColumn(title = "创建日期", condition = true, order = true)
    private Date createDate;

    public CmsWord() {
    }

    public CmsWord(short siteId, String name, int searchCount, boolean hidden, String ip, Date createDate) {
        this.siteId = siteId;
        this.name = name;
        this.searchCount = searchCount;
        this.hidden = hidden;
        this.ip = ip;
        this.createDate = createDate;
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

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "search_count", nullable = false)
    public int getSearchCount() {
        return this.searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    @Column(name = "hidden", nullable = false)
    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Column(name = "ip", nullable = false, length = 130)
    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
