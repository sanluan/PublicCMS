package com.publiccms.common.generator.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * EntityCondition
 * 
 */
public class EntityCondition {
    
    /**
     * @param name
     * @param type
     * @param title
     * @param or
     * @param like
     */
    public EntityCondition(String name, String type, String title, boolean or, boolean like) {
        this.name = name;
        this.type = type;
        this.title = title;
        this.or = or;
        this.like = like;
    }

    private String name;
    private String type;
    private String title;
    private boolean or;
    private boolean like;
    private List<String> nameList=new ArrayList<>();

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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {

        this.type = type;
    }

    /**
     * @return the like
     */
    public boolean isLike() {
        return like;
    }

    /**
     * @param like
     *            the like to set
     */
    public void setLike(boolean like) {
        this.like = like;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the name list
     */
    public List<String> getNameList() {
        return nameList;
    }

    /**
     * @param nameList
     */
    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    /**
     * @return the or
     */
    public boolean isOr() {
        return or;
    }

    /**
     * @param or the or to set
     */
    public void setOr(boolean or) {
        this.or = or;
    }
}
