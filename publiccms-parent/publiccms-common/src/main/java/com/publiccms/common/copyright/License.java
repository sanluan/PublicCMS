package com.publiccms.common.copyright;

import java.util.LinkedHashMap;

public class License extends LinkedHashMap<String, String> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String KEY_VERSION = "version";
    public final static String KEY_AUTHORIZATION = "authorization";
    public final static String KEY_ORGANIZATION = "organization";
    public final static String KEY_ISSUE = "issue";
    public final static String KEY_DOMAIN = "domain";
    public final static String KEY_START_DATE = "startDate";
    public final static String KEY_END_DATE = "endDate";
    public final static String KEY_SIGNATURER = "signaturer";

    public String getVersion() {
        return get(KEY_VERSION);
    }

    public void setVersion(String version) {
        this.put(KEY_VERSION, version);
    }

    public String getAuthorization() {
        return get(KEY_AUTHORIZATION);
    }

    public void setAuthorization(String authorization) {
        this.put(KEY_AUTHORIZATION, authorization);
    }

    public String getOrganization() {
        return get(KEY_ORGANIZATION);
    }

    public void setOrganization(String organization) {
        this.put(KEY_ORGANIZATION, organization);
    }

    public String getIssue() {
        return get(KEY_ISSUE);
    }

    public void setIssue(String issue) {
        this.put(KEY_ISSUE, issue);
    }

    public String getDomain() {
        return get(KEY_DOMAIN);
    }

    public void setDomain(String domain) {
        this.put(KEY_DOMAIN, domain);
    }

    public String getStartDate() {
        return get(KEY_START_DATE);
    }

    public void setStartDate(String startDate) {
        this.put(KEY_START_DATE, startDate);
    }

    public String getEndDate() {
        return get(KEY_END_DATE);
    }

    public void setEndDate(String endDate) {
        this.put(KEY_END_DATE, endDate);
    }

    public String getSignaturer() {
        return get(KEY_SIGNATURER);
    }

    public void setSignaturer(String signaturer) {
        this.put(KEY_SIGNATURER, signaturer);
    }
}
