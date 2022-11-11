package io.sinso.dataland.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lee
 * @date 2021-02-20
 */
@Data
public class PageResultVo<T> implements Serializable {
    private static final long serialVersionUID = 7350732353906694136L;
    private Integer pageNum;

    private Integer pageSize;

    private Integer totalPage;

    private long totalSize;

    private List<T> content;

    public PageResultVo() {

    }

    private PageResultVo(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = 0;
        this.content = new ArrayList<>();
        this.totalSize = 0;
    }

    private PageResultVo(Page page, List<T> content) {
        this.pageNum = Math.toIntExact(page.getCurrent());
        this.pageSize = Math.toIntExact(page.getSize());
        this.totalSize = Math.toIntExact(page.getTotal());
        if (pageSize > 0) {
            this.totalPage = Math.toIntExact(totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1);
        } else {
            pageSize = 0;
        }
        this.content = content;
    }

    public static <T> PageResultVo<T> page(Integer pageNum, Integer pageSize) {
        return new PageResultVo<>(pageNum, pageSize);
    }

    public static <T> PageResultVo<T> page(Page page, List<T> content) {
        return new PageResultVo<>(page, content);
    }
}
