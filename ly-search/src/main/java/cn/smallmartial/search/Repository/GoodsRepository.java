package cn.smallmartial.search.Repository;

import cn.smallmartial.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
