package com.sanluan.common.handler;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;

import com.sanluan.common.base.Base;

public class PageHandler extends Base implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_PAGE_SIZE = 30;
    public static final int MAX_PAGE_SIZE = 100;

    private int totalCount;
    private int pageSize;
    private int pageIndex;
    private List<?> list;

    /**
     * @param pageIndex
     * @param pageSize
     * @param totalCount
     * @param maxResults
     */
    public PageHandler(Integer pageIndex, Integer pageSize, int totalCount, Integer maxResults) {
        setTotalCount(totalCount, maxResults);
        setPageSize(notEmpty(pageSize) ? pageSize : 0);
        setPageIndex(notEmpty(pageIndex) ? pageIndex : 1);
        init();
    }

    public void init() {
        pageSize = 1 > pageSize ? DEFAULT_PAGE_SIZE : MAX_PAGE_SIZE < pageSize ? MAX_PAGE_SIZE : pageSize;
        totalCount = 0 > totalCount ? 0 : totalCount;
        pageIndex = 1 > pageIndex ? 1 : pageIndex > getTotalPage() ? getTotalPage() : pageIndex;
    }

    public int getTotalPage() {
        int totalPage = totalCount / pageSize;
        return (0 == totalPage || 0 != totalCount % pageSize) ? ++totalPage : totalPage;
    }

    public int getFirstResult() {
        return (pageIndex - 1) * pageSize;
    }

    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount
     *            the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @param totalCount
     *            the totalCount to set
     */
    public void setTotalCount(Integer totalCount, Integer maxResults) {
        setTotalCount(notEmpty(maxResults) && maxResults < totalCount ? maxResults : totalCount);
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @return
     */
    public boolean isFirstPage() {
        return pageIndex <= 1;
    }

    /**
     * @return
     */
    public boolean isLastPage() {
        return pageIndex >= getTotalPage();
    }

    /**
     * @return
     */
    public int getNextPage() {
        if (isLastPage()) {
            return pageIndex;
        }
        return pageIndex + 1;
    }

    /**
     * @return
     */
    public int getPrePage() {
        if (isFirstPage()) {
            return pageIndex;
        }
        return pageIndex - 1;
    }

    /**
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the pageIndex
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * @param pageIndex
     *            the pageIndex to set
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @return the list
     */
    public List<?> getList() {
        return list;
    }

    /**
     * @param list
     *            the list to set
     */
    public void setList(List<?> list) {
        if (0 == totalCount && isNotEmpty(list)) {
            setTotalCount(list.size());
        }
        this.list = list;
    }
}
