package cn.smallmartial.search.pojo;

import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.item.pojo.Brand;
import cn.smallmartial.item.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author smallmartial
 * @Date 2019/4/19
 * @Email smallmarital@qq.com
 */
@Data

public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;//分类过滤条件

    private List<Brand> brands;//品牌过滤条件

    private List<Map<String,Object>> specs; // 规格参数过滤条件

    public SearchResult(){}

    public SearchResult(Long total, Integer totalPage, List<Goods> item, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, item);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}