package com.nim.common;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {
    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 当前页数
     */
    private Long currentPage;

    /**
     * 数据总数
     */
    private Long totalNum;

    /**
     * 页面数据条数
     */
    private Long pageSize;

    /**
     * 内容列表
     */
    private List<T> contentList;
}
