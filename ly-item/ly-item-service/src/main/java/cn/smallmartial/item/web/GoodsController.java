package cn.smallmartial.item.web;

import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.item.pojo.Sku;
import cn.smallmartial.item.pojo.Spu;
import cn.smallmartial.item.pojo.SpuBo;
import cn.smallmartial.item.pojo.SpuDetail;
import cn.smallmartial.item.service.GoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @PostMapping("goods")
//    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
//        goodsService.saveGoods(spu);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spu){
//        this.goodsService.saveGoods(spu);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
        try {
            this.goodsService.save(spu);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * 查询SpuDetial接口
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id) {
        SpuDetail detail = this.goodsService.querySpuDetailById(id);
        if (detail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(detail);
    }
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> queryBySkuSpuId(@Param("id")Long id){
        List<Sku> skus=this.goodsService.querySkuSpuId(id);
        if (skus == null || skus.size() ==0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return ResponseEntity.ok(skus);
    }

    @GetMapping("/spu/{id}")
    public ResponseEntity<SpuBo> queryGoodsById(@PathVariable("id") Long id){
        SpuBo spuBo = this.goodsService.queryGoodsById(id);
        if (spuBo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spuBo);
    }
    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        try {
            this.goodsService.update(spuBo);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 删除商品
     * @param ids
     * @return
     */
    @DeleteMapping("/spu/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("id") String ids){
        String separator = "-";
        if (ids.contains(separator)){
            String[] goodsId = ids.split(separator);
            for (String id: goodsId) {
                this.goodsService.deleteGoods(Long.parseLong(id));
            }
        }else {
            this.goodsService.deleteGoods(Long.parseLong(ids));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
