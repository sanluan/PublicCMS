package com.publiccms.views.pojo.diy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;

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
    /**
     * id
     */
    private String id;
    /**
     * region
     * <p>
     * 区域
     */
    private String region;
    /**
     * name
     * <p>
     * 名称
     */
    @NotNull
    @Length(max = 50)
    private String name;
    /**
     * template
     * <p>
     * 模板
     */
    private String template;
    /**
     * style
     * <p>
     * 样式
     */
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
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region
     *            the region to set
     */
    public void setRegion(String region) {
        this.region = region;
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
