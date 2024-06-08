package com.demo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiPaging {
    /**
     * the page number being returned, 0 based
     */
    private Integer pageNumber;

    /**
     * size of the pages
     */
    private Integer pageSize;

    /**
     * total pages for query
     */
    private Integer totalPages;

    /**
     * total elements for query
     */
    private Long totalElements;

    /**
     * true if this is the first page
     */
    private Boolean first;

    /**
     * true if this is the last page
     */
    private Boolean last;

    /**
     * any applied sort or "UNSORTED"
     * see org.springframework.data.domain.Sort.toString()
     */
    private String sort;

    public ApiPaging(Page page) {
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.sort = page.getSort().toString();
    }
}
