package cn.smallmartial.item.web;

import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.item.pojo.Spu;
import cn.smallmartial.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author smallmartial
 * @Date 2019/4/15
 * @Email smallmarital@qq.com
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",defaultValue = "true") Boolean saleable


    ){
        return ResponseEntity.ok(goodsService.querySpuByPage(page,rows,saleable,key));
    }
}
