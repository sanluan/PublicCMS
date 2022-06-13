package com.publiccms.entities.sys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * SysDomain generated by hbm2java
 */
@Entity
@Table(name = "sys_domain")
@DynamicUpdate
public class SysDomain implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * domain name<p>
     * 域名
     */
    @GeneratorColumn(title = "域名")
    private String name;
    /**
     * site id<p>
     * 站点id
     */
    @GeneratorColumn(title = "站点", condition = true)
    private short siteId;
    /**
     * wild<p>
     * 通配域名
     */
    @GeneratorColumn(title = "通配", condition = true)
    private boolean wild;
    /**
     * multiple site<p>
     * 站群
     */
    @GeneratorColumn(title = "多站点", condition = true)
    private boolean multiple;
    /**
     * root template path<p>
     * 模板根目录
     */
    @GeneratorColumn(title = "模板根目录")
    private String path;

    public SysDomain() {
    }

    public SysDomain(String name, short siteId, boolean wild, boolean multiple) {
        this.name = name;
        this.siteId = siteId;
        this.wild = wild;
        this.multiple = multiple;
    }

    public SysDomain(String name, short siteId, boolean wild, boolean multiple, String path) {
        this.name = name;
        this.siteId = siteId;
        this.wild = wild;
        this.multiple = multiple;
        this.path = path;
    }

    @Id
    @Column(name = "name", unique = true, nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Column(name = "wild", nullable = false)
    public boolean isWild() {
        return this.wild;
    }

    public void setWild(boolean wild) {
        this.wild = wild;
    }

    @Column(name = "multiple", nullable = false)
    public boolean isMultiple() {
        return this.multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    @Column(name = "path", length = 100)
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
