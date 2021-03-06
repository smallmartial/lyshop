package cn.smallmartial.item.api;

import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.item.pojo.Sku;
import cn.smallmartial.item.pojo.Spu;
import cn.smallmartial.item.pojo.SpuBo;
import cn.smallmartial.item.pojo.SpuDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */
public interface GoodsApi {

        /**
         * 分页查询商品
         * @param page
         * @param rows
         * @param saleable
         * @param key
         * @return
         */
        @GetMapping("/spu/page")
        PageResult<SpuBo> querySpuByPage(
                @RequestParam(value = "page", defaultValue = "1") Integer page,
                @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable,
                @RequestParam(value = "key", required = false) String key);
        /**
         * 根据spu的id查询spu
         * @param id
         * @return
         */
        @GetMapping("spu/{id}")
        Spu querySpuById(@PathVariable("id") Long id);
        /**
         * 根据spu商品id查询详情
         * @param id
         * @return
         */
        @GetMapping("/spu/detail/{id}")
        SpuDetail querySpuDetailById(@PathVariable("id") Long id);

        /**
         * 根据spu的id查询sku
         * @param id
         * @return
         */
        @GetMapping("sku/list")
       // List<Sku> querySkuBySpuId(@RequestParam("id") Long id);
        List<Sku> queryBySkuSpuId(@RequestParam("id")Long id);
    }

