package com.publiccms.views.pojo.diy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * CmsResourceFile 资源文件
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsResourceFile implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String fileType;
    private String filePath;
    private String fileName;
    private int sort;

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType
     *            the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath
     *            the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the sort
     */
    public int getSort() {
        return sort;
    }

    /**
     * @param sort
     *            the sort to set
     */
    public void setSort(int sort) {
        this.sort = sort;
    }
}