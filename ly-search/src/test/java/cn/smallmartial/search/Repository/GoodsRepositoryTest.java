package cn.smallmartial.search.Repository;

import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.item.pojo.Spu;
import cn.smallmartial.item.pojo.SpuBo;
import cn.smallmartial.search.client.GoodClient;
import cn.smallmartial.search.pojo.Goods;
import cn.smallmartial.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodClient goodClient;
    @Autowired
    private SearchService searchService;


    @Test
    public void testCreateIndx(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询spu
            PageResult<SpuBo> result = this.goodClient.querySpuByPage(page, rows, true, null);
            List<SpuBo> spus = result.getItem();

            // spu转为goods
            List<Goods> goods = spus.stream().map(searchService::bulidGoods)
                    .collect(Collectors.toList());

            // 把goods放入索引库
            this.goodsRepository.saveAll(goods);

            size = spus.size();
            //翻页
            page++;
        }while (size == 100);
    }

}