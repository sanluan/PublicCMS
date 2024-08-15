package com.publiccms.entities.cms;
// Generated 2020-3-26 11:25:55 by Hibernate Tools 6.0.0-SNAPSHOT

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import com.publiccms.common.generator.annotation.GeneratorColumn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * CmsVoteItem generated by hbm2java
 */
@Entity
@Table(name = "cms_vote_item")
@DynamicUpdate
public class CmsVoteItem implements java.io.Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private Long id;
    /**
     * vote id<p>
     * 投票id
     */
    @GeneratorColumn(title = "投票", condition = true)
    private long voteId;
    /**
     * title<p>
     * 标题
     */
    @GeneratorColumn(title = "标题")
    @NotBlank
    @Length(max = 100)
    private String title;
    /**
     * votes<p>
     * 票数
     */
    @GeneratorColumn(title = "票数", order = true)
    private int votes;
    /**
     * sort<p>
     * 排序
     */
    @GeneratorColumn(title = "排序", order = true)
    private int sort;

    public CmsVoteItem() {
    }

    public CmsVoteItem(long voteId, String title, int votes, int sort) {
        this.voteId = voteId;
        this.title = title;
        this.votes = votes;
        this.sort = sort;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "vote_id", nullable = false)
    public long getVoteId() {
        return this.voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "votes", nullable = false)
    public int getVotes() {
        return this.votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Column(name = "sort", nullable = false)
    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

}
