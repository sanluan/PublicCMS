package com.publiccms.entities.cms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.generator.annotation.GeneratorColumn;
import com.publiccms.views.pojo.entities.EntityAttribute;

/**
 * CmsPlace generated by hbm2java
 */
@Entity
@Table(name = "cms_place")
@DynamicUpdate
public class CmsPlace extends EntityAttribute implements java.io.Serializable {

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
     * place path

     * 推荐位路径
     */
    @GeneratorColumn(title = "路径", condition = true)
    @NotBlank
    @Length(max = 100)
    private String path;
    /**
     * user id

     * 创建用户id
     */
    @GeneratorColumn(title = "推荐用户", condition = true)
    private Long userId;
    /**
     * check user id

     * 审核用户id
     */
    @GeneratorColumn(title = "审核用户", condition = true)
    private Long checkUserId;
    /**
     * item type

     * 数据项类型
     */
    @GeneratorColumn(title = "项目类型", condition = true)
    private String itemType;
    /**
     * item id

     * 数据项id
     */
    @GeneratorColumn(title = "项目", condition = true)
    private Long itemId;
    /**
     * title

     * 标题
     */
    @GeneratorColumn(title = "标题")
    @NotBlank
    @Length(max = 255)
    private String title;
    /**
     * url

     * 地址
     */
    @GeneratorColumn(title = "地址")
    @Length(max = 1000)
    private String url;
    /**
     * description

     * 描述
     */
    @GeneratorColumn(title = "描述")
    @Length(max = 300)
    private String description;
    /**
     * cover

     * 封面图
     */
    @GeneratorColumn(title = "封面图")
    @Length(max = 255)
    private String cover;
    /**
     * create date

     * 创建日期
     */
    @GeneratorColumn(title = "创建日期", order = true)
    private Date createDate;
    /**
     * publish date

     * 发布日期
     */
    @GeneratorColumn(title = "发布日期", condition = true, order = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;
    /**
     * expiry date

     * 过期日期
     */
    @GeneratorColumn(title = "过期日期", condition = true, order = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;
    /**
     * status(0:Draft,1:Published,2:Pending)

     * 状态(0:草稿,1:已发布,2:待审核,3:已下架)
     */
    @GeneratorColumn(title = "状态", condition = true)
    private int status;
    /**
     * clicks

     * 点击数
     */
    @GeneratorColumn(title = "点击数", order = true)
    private int clicks;
    /**
     * max clicks

     * 最大点击数
     */
    @GeneratorColumn(title = "最大点击数")
    private int maxClicks;
    @GeneratorColumn(title = "已删除", condition = true)
    @JsonIgnore
    private boolean disabled;

    public CmsPlace() {
    }

    public CmsPlace(short siteId, String path, String title, Date createDate, Date publishDate, int status, int clicks,
            int maxClicks, boolean disabled) {
        this.siteId = siteId;
        this.path = path;
        this.title = title;
        this.createDate = createDate;
        this.publishDate = publishDate;
        this.status = status;
        this.clicks = clicks;
        this.maxClicks = maxClicks;
        this.disabled = disabled;
    }

    public CmsPlace(short siteId, String path, Long userId, Long checkUserId, String itemType, Long itemId, String title,
            String url, String cover, Date createDate, Date publishDate, Date expiryDate, int status, int clicks, int maxClicks,
            boolean disabled) {
        this.siteId = siteId;
        this.path = path;
        this.userId = userId;
        this.checkUserId = checkUserId;
        this.itemType = itemType;
        this.itemId = itemId;
        this.title = title;
        this.url = url;
        this.cover = cover;
        this.createDate = createDate;
        this.publishDate = publishDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.clicks = clicks;
        this.maxClicks = maxClicks;
        this.disabled = disabled;
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

    @Column(name = "path", nullable = false, length = 100)
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "user_id")
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "check_user_id")
    public Long getCheckUserId() {
        return this.checkUserId;
    }

    public void setCheckUserId(Long checkUserId) {
        this.checkUserId = checkUserId;
    }

    @Column(name = "item_type", length = 50)
    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Column(name = "item_id")
    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "url", length = 1000)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "description", length = 300)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "cover")
    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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
    @Column(name = "publish_date", nullable = false, length = 19)
    public Date getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", length = 19)
    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Column(name = "status", nullable = false)
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "clicks", nullable = false)
    public int getClicks() {
        return this.clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    @Column(name = "max_clicks", nullable = false)
    public int getMaxClicks() {
        return this.maxClicks;
    }

    public void setMaxClicks(int maxClicks) {
        this.maxClicks = maxClicks;
    }
    @Column(name = "disabled", nullable = false)
    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
