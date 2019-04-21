package cn.smallmartial.item.web;

import cn.smallmartial.item.pojo.SpecGroup;
import cn.smallmartial.item.pojo.SpecParam;
import cn.smallmartial.item.pojo.Specification;
import cn.smallmartial.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/14
 * @Email smallmarital@qq.com
 */
@RestController
@RequestMapping("spec")
public class SpecifationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 查询模块
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("id") Long id){
        Specification spec = this.specificationService.queryById(id);
        if (spec == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spec.getSpecifications());
    }
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid")Long cid){
        List<SpecGroup> list = this.specificationService.queryBySpecGroups(cid);
        if (list ==null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecSpecParam(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic
    ){
        List<SpecParam> list = this.specificationService.querySpecParams(gid,cid,searching,generic);
        if (list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);

    }


    /**
     *添加规格模板
     * @param specification
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveSpecification(Specification specification){
        this.specificationService.saveSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 更新规格模板
     * @param specification
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateSpecification(Specification specification){
        this.specificationService.updateSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 删除规格模板
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable("id")Long id){

        Specification specification = new Specification();
        specification.setCategoryId(id);
        this.specificationService.deleteSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据分类查询规格组及组内参数
     * @param cid
     * @return
     */
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid){
        return  ResponseEntity.ok(specificationService.queryListByCid(cid));
    }
}
