package com.sanluan.common.handler;

import static com.sanluan.common.constants.CommonConstants.DEFAULT_PAGE_SIZE;
import static com.sanluan.common.constants.CommonConstants.MAX_PAGE_SIZE;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;

public class PageHandler implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

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
        setTotalCount(null != maxResults && maxResults < totalCount ? maxResults : totalCount);
        setPageSize(null == pageSize ? 0 : pageSize);
        setPageIndex(null == pageIndex ? 1 : pageIndex);
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

    public int getLastResult() {
        return pageIndex * pageSize > totalCount ? pageIndex * pageSize : totalCount;
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
        } else {
            return pageIndex + 1;
        }
    }

    /**
     * @return
     */
    public int getPrePage() {
        if (isFirstPage()) {
            return pageIndex;
        } else {
            return pageIndex - 1;
        }
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
