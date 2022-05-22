package com.publiccms.views.pojo.diy;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsStyle diy样式
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsStyle implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Pattern(regexp = CssBoxProperties.REG_INPUT_LENGTH)
    private String height;
    private CssBoxProperties margin;
    private CssBoxProperties padding;
    private CssBoxProperties radius;
    private CssBoxProperties borderWidth;
    private CssBoxProperties borderStyle;
    private CssBoxProperties borderColor;
    @Pattern(regexp = CssBoxProperties.REG_INPUT_COLOR)
    private String backgroundColor;

    public boolean isEmpty() {
        return margin.isEmpty() && padding.isEmpty() && radius.isEmpty() && borderWidth.isEmpty()
                && StringUtils.isAllEmpty(height, backgroundColor);
    }

    /**
     * @return the height
     */
    public String getHeight() {
        return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * @return the margin
     */
    public CssBoxProperties getMargin() {
        return margin;
    }

    /**
     * @param margin
     *            the margin to set
     */
    public void setMargin(CssBoxProperties margin) {
        this.margin = margin;
    }

    /**
     * @return the padding
     */
    public CssBoxProperties getPadding() {
        return padding;
    }

    /**
     * @param padding
     *            the padding to set
     */
    public void setPadding(CssBoxProperties padding) {
        this.padding = padding;
    }

    /**
     * @return the radius
     */
    public CssBoxProperties getRadius() {
        return radius;
    }

    /**
     * @param radius
     *            the radius to set
     */
    public void setRadius(CssBoxProperties radius) {
        this.radius = radius;
    }

    /**
     * @return the borderWidth
     */
    public CssBoxProperties getBorderWidth() {
        return borderWidth;
    }

    /**
     * @param borderWidth
     *            the borderWidth to set
     */
    public void setBorderWidth(CssBoxProperties borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * @return the borderStyle
     */
    public CssBoxProperties getBorderStyle() {
        return borderStyle;
    }

    /**
     * @param borderStyle
     *            the borderStyle to set
     */
    public void setBorderStyle(CssBoxProperties borderStyle) {
        this.borderStyle = borderStyle;
    }

    /**
     * @return the borderColor
     */
    public CssBoxProperties getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor
     *            the borderColor to set
     */
    public void setBorderColor(CssBoxProperties borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor
     *            the backgroundColor to set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}