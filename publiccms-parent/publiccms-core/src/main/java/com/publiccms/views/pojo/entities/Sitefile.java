package com.publiccms.views.pojo.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * Sitefile
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sitefile implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> cmsVersionList;
    private String name;
    private String version;
    private String author;
    private String cover;
    private String description;
    private String text;

    /**
     * @return the cmsVersionList
     */
    public List<String> getCmsVersionList() {
        return cmsVersionList;
    }

    /**
     * @param cmsVersionList
     *            the cmsVersionList to set
     */
    public void setCmsVersionList(List<String> cmsVersionList) {
        this.cmsVersionList = cmsVersionList;
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
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     *            the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
}