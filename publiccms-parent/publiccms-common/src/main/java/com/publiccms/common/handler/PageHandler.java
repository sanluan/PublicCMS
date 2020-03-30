package com.publiccms.common.handler;

import java.util.List;

/**
 *
 * PageHandler
 * 
 */
public class PageHandler implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 默认每页数据条数
     */
    public static final int DEFAULT_PAGE_SIZE = 30;
    /**
     * 最大每页数据条数
     */
    public static final int MAX_PAGE_SIZE = 500;

    private int totalCount;
    private int pageSize;
    private int pageIndex;
    private int totalPage;
    private List<?> list;

    /**
     * @param pageIndex
     * @param pageSize
     * @param totalCount
     * @param maxCount
     */
    public PageHandler(Integer pageIndex, Integer pageSize, long totalCount, Integer maxCount) {
        this(pageIndex, pageSize, totalCount > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalCount, maxCount);
    }

    /**
     * @param pageIndex
     * @param pageSize
     * @param totalCount
     * @param maxCount
     */
    public PageHandler(Integer pageIndex, Integer pageSize, int totalCount, Integer maxCount) {
        setTotalCount(getTotalCount(totalCount, maxCount));
        setPageSize(null != pageSize ? pageSize : 0);
        setPageIndex(null != pageIndex ? pageIndex : 1);
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        pageSize = 1 > pageSize ? DEFAULT_PAGE_SIZE : MAX_PAGE_SIZE < pageSize ? MAX_PAGE_SIZE : pageSize;
        totalCount = 0 > totalCount ? 0 : totalCount;
        totalPage = getTotalPage(totalCount, pageSize);
        pageIndex = 1 > pageIndex ? 1 : pageIndex > totalPage ? totalPage : pageIndex;
    }

    /**
     * @param totalCount
     * @param maxCount
     * @return total count
     */
    public static int getTotalCount(int totalCount, Integer maxCount) {
        return null != maxCount && maxCount < totalCount ? maxCount : totalCount;
    }

    /**
     * 总页数
     * 
     * @param totalCount
     * @param pageSize
     * 
     * @return total page
     */
    public static int getTotalPage(int totalCount, int pageSize) {
        int totalPage = totalCount / pageSize;
        return (0 == totalPage || 0 != totalCount % pageSize) ? ++totalPage : totalPage;
    }

    /**
     * 第一条结果
     * 
     * @return first result
     */
    public int getFirstResult() {
        return (pageIndex - 1) * pageSize;
    }

    /**
     * 总数据条数
     * 
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
     * 每页数据条数
     * 
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 当前页码
     * 
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
     * 结果数据
     * 
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
        this.list = list;
    }

    /**
     * 是否第一页
     * 
     * @return whether the first page
     */
    public boolean isFirstPage() {
        return pageIndex <= 1;
    }

    /**
     * @return the totalPage
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 是否最后一页
     * 
     * @return whether the last page
     */
    public boolean isLastPage() {
        return pageIndex >= totalPage;
    }

    /**
     * 下一页
     * 
     * @return next page
     */
    public int getNextPage() {
        if (isLastPage()) {
            return pageIndex;
        }
        return pageIndex + 1;
    }

    /**
     * 上一页
     * 
     * @return previous page
     */
    public int getPrePage() {
        if (isFirstPage()) {
            return pageIndex;
        }
        return pageIndex - 1;
    }

}
