package com.lifelight.common.result;

import java.util.Collections;
import java.util.List;

public class PaginatedResult<T> extends Result {

    public PaginatedResult(int pageNumber, int pageSize) {
        super(StatusCodes.NOT_FOUND,false);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        items = Collections.emptyList();
        pagesCount = 0;
        totalItemsCount = 0;
    }

    public PaginatedResult(List<T> items, int pageNumber, int pageSize, int totalItemsCount) {
        super(StatusCodes.OK,true);
        this.items = items;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.pagesCount = countPages(pageSize, totalItemsCount);
        this.totalItemsCount = totalItemsCount;
    }
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected final List<T> items;
    private final int pageSize;
    private final int pageNumber;
    private final int pagesCount;
    private final int totalItemsCount;

    
    

    private int countPages(int size, int itemsCount) {
        return (int) Math.ceil((double) itemsCount / size);
    }

    public List<T> getItems() {
        return items;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    public int getTotalItemsCount() {
        return totalItemsCount;
    }
}
