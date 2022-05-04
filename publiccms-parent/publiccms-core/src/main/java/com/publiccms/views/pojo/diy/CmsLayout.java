package com.publiccms.views.pojo.diy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsLayout diy布局
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsLayout implements java.io.Serializable {
    public static final Pattern PLACE_PATTERN = Pattern.compile("<#--[ ]*position[ ]*-->");
    public static final Pattern SELECTOR_PATTERN = Pattern.compile("/\\*[ ]*selecter[ ]*\\*/");

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String template;
    private String style;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template
     *            the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonIgnore
    public int getCount() {
        int i = 0;
        Matcher matcher = PLACE_PATTERN.matcher(template);
        while (matcher.find()) {
            i++;
        }
        return i;
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style
     *            the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }
}
