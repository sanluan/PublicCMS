package com.publiccms.common.handler;

import java.util.List;

/**
 *
 * PageHandler
 * <p>
 * 分页结果处理器
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

    /**
     * total count
     * <p>
     * 总数量
     */
    private int totalCount;
    /**
     * page size
     * <p>
     * 每页数量
     */
    private int pageSize;
    /**
     * first result offset
     * <p>
     * 当前开始位置
     */
    private Integer firstResult;
    /**
     * current page index
     * <p>
     * 当前页面
     */
    private int pageIndex;
    /**
     * total page
     * <p>
     * 总页数
     */
    private int totalPage;
    /**
     * result list
     * <p>
     * 结果列表
     */
    private List<?> list;

    /**
     * @param pageIndex
     * @param pageSize
     */
    public PageHandler(Integer pageIndex, Integer pageSize) {
        this(null, pageIndex, pageSize);
    }

    /**
     * @param firstResult
     * @param pageIndex
     * @param pageSize
     */
    public PageHandler(Integer firstResult, Integer pageIndex, Integer pageSize) {
        this.firstResult = firstResult;
        this.pageIndex = null != pageIndex ? pageIndex : 1;
        this.pageSize = null != pageSize ? 1 > pageSize ? DEFAULT_PAGE_SIZE : MAX_PAGE_SIZE < pageSize ? MAX_PAGE_SIZE : pageSize
                : 0;
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
        if (null == firstResult) {
            return (pageIndex - 1) * pageSize;
        } else {
            return firstResult - 1;
        }
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
    public void setTotalCount(long totalCount) {
        setTotalCount(totalCount > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalCount);
    }

    /**
     * @param totalCount
     *            the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount = 0 > totalCount ? 0 : totalCount;
        this.totalPage = getTotalPage(totalCount, pageSize);
        this.pageIndex = 1 > pageIndex ? 1 : pageIndex > totalPage ? totalPage : pageIndex;
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
     * 当前页码
     * 
     * @return the pageIndex
     */
    public int getPageIndex() {
        return pageIndex;
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
