package com.publiccms.common.generator.entity;

/**
 *
 * EntityColumn
 * 
 */
public class EntityColumn {
    
    /**
     * @param name
     * @param type
     * @param order
     * @param title
     */
    public EntityColumn(String name, String type, boolean order, String title) {
        this.name = name;
        this.type = type;
        this.order = order;
        this.title = title;
    }

    private String name;
    private String type;
    private boolean order;
    private String title;

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
     * @return the order
     */
    public boolean isOrder() {
        return order;
    }

    /**
     * @param order
     *            the order to set
     */
    public void setOrder(boolean order) {
        this.order = order;
    }
}
