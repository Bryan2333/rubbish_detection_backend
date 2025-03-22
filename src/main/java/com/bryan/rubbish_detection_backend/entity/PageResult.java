package com.bryan.rubbish_detection_backend.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private long total; // 总记录数
    private long pageSize; // 每页大小
    private long currentPage; // 当前页
    private long totalPages; // 总页数
    private List<T> records; // 当前页数据

    public PageResult(@NotNull IPage<T> page) {
        this.total = page.getTotal();
        this.pageSize = page.getSize();
        this.currentPage = page.getCurrent();
        this.totalPages = page.getPages();
        this.records = page.getRecords();
    }
}
