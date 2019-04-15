package cn.smallmartial.item.web;

import cn.smallmartial.item.pojo.Specification;
import cn.smallmartial.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("{id}")
    public ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("id") Long id){
        Specification spec = this.specificationService.queryById(id);
        if (spec == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spec.getSpecifications());
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
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable("id")Long id){

        Specification specification = new Specification();
        specification.setCategoryId(id);
        this.specificationService.deleteSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
