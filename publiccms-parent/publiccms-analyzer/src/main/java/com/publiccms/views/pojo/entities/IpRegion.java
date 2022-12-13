package com.publiccms.views.pojo.entities;

public class IpRegion implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IpRegion(String region) {
        this.region = region;
    }

    /**
     * 地区信息
     */
    private String region;
    /**
     * country
     * <p>
     * 国家
     */
    private String country;
    /**
     * province
     * <p>
     * 省
     */
    private String province;
    /**
     * city
     * <p>
     * 城市
     */
    private String city;

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
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province
     *            the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return (null != country ? (country) : "") + "," + (null != province ? (province) : "") + ","
                + (null != city ? (city) : "");
    }

}
