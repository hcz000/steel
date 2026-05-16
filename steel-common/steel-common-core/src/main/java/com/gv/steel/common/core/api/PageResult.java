package com.gv.steel.common.core.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 8354691854993533520L;

    private Long pageSize;
    private Long pageNo;
    private Long totalPage;
    private Long totalCount;

    private List<T> data;

    public PageResult<T> pageResult(Page<T> page) {
        this.pageSize = page.getSize();
        this.pageNo = page.getCurrent();
        this.totalCount = page.getTotal();
        this.totalPage = this.getTotalPage();
        this.data = page.getRecords();
        return this;
    }

    public long getTotalPage() {
        return this.totalCount % this.pageSize == 0L ? this.totalCount / this.pageSize : this.totalCount / this.pageSize + 1L;
    }
}
