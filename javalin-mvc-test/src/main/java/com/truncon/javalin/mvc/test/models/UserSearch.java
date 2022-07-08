package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;

public final class UserSearch {
    private Integer userId;
    private String searchValue;
    private int offset;
    private int pageSize;
    private String[] orderByFields;

    public Integer getUserId() {
        return userId;
    }

    @FromPath
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSearchValue() {
        return searchValue;
    }

    @FromQuery("search-value")
    public void setSearchValue(String value) {
        this.searchValue = value;
    }

    public int getOffset() {
        return offset;
    }

    @FromQuery
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    @FromQuery("page-size")
    public void setPageSize(int size) {
        this.pageSize = size;
    }

    public String[] getOrderByFields() {
        return orderByFields;
    }

    @FromQuery("order-by")
    public void setOrderByFields(String[] fields) {
        this.orderByFields = fields;
    }
}
