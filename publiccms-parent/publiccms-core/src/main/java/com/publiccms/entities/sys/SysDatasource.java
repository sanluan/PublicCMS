package com.publiccms.entities.sys;
// Generated 2021-8-2 11:20:41 by Hibernate Tools 5.4.32.Final

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * SysDatasource generated by hbm2java
 */
@Entity
@Table(name = "sys_datasource")
public class SysDatasource implements java.io.Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;
    /**
     * name<p>
     * 名称
     */
    @GeneratorColumn(title = "名称")
    private String name;
    /**
     * config<p>
     * 配置
     */
    @GeneratorColumn(title = "配置")
    private String config;
    /**
     * create date<p>
     * 创建日期
     */
    @GeneratorColumn(title = "创建日期", order = true)
    private Date createDate;
    /**
     * update date<p>
     * 更新日期
     */
    @GeneratorColumn(title = "更新日期")
    private Date updateDate;
    @GeneratorColumn(title = "禁用", condition = true)
    @JsonIgnore
    private boolean disabled;

    public SysDatasource() {
    }

    public SysDatasource(String name, String config, Date createDate, boolean disabled) {
        this.name = name;
        this.config = config;
        this.createDate = createDate;
        this.disabled = disabled;
    }

    public SysDatasource(String name, String config, Date createDate, Date updateDate, boolean disabled) {
        this.name = name;
        this.config = config;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.disabled = disabled;
    }

    @Id
    @Column(name = "name", unique = true, nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "config", nullable = false, length = 1000)
    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "disabled", nullable = false)
    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
