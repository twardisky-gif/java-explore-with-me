package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record OffsetPageRequest(long offset, int pageSize, Sort sort) implements Pageable {
    private static final long FIRST_OFFSET = 0L;
    private static final int MIN_PAGE_SIZE = 1;

    public OffsetPageRequest(long offset, int pageSize) {
        this(offset, pageSize, Sort.unsorted());
    }

    public OffsetPageRequest {
        if (offset < FIRST_OFFSET) {
            throw new IllegalArgumentException("Offset must not be negative");
        }
        if (pageSize < MIN_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be positive");
        }
    }

    @Override
    public int getPageNumber() {
        return Math.toIntExact(offset / pageSize);
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPageRequest(offset + pageSize, pageSize, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new OffsetPageRequest(offset - pageSize, pageSize, sort) : first();
    }

    @Override
    public Pageable first() {
        return new OffsetPageRequest(FIRST_OFFSET, pageSize, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetPageRequest((long) pageNumber * pageSize, pageSize, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset >= pageSize;
    }
}
