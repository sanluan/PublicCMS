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
     * operator
     * <p>
     * 运营商
     */
    private String operator;

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

    /**
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator
     *            the operator to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAddress() {
        StringBuilder sb = new StringBuilder();
        if (null != country) {
            sb.append(country);
        }
        if (null != province) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(province);
        }
        if (null != city) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(city);
        }
        if (null != operator) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(operator);
        }
        return sb.toString();
    }

}
