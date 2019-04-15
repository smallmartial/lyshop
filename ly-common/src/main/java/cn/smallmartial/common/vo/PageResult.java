package cn.smallmartial.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/13
 * @Email smallmarital@qq.com
 */
//view  object
@Data
public class PageResult<T> {
    private Long total;
    private Integer totalPage;
    private List<T> item;

    public PageResult() {
    }

    public PageResult(Long total, List<T> item) {
        this.total = total;
        this.item = item;
    }

    public PageResult(Long total, Integer totalPage, List<T> item) {
        this.total = total;
        this.totalPage = totalPage;
        this.item = item;
    }
}
