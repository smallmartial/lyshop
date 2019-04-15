package cn.smallmartial.item.web;

import cn.smallmartial.common.enums.ExceptionEnum;
import cn.smallmartial.exception.LyException;
import cn.smallmartial.item.pojo.Item;
import cn.smallmartial.item.service.ItemSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author smallmartial
 * @Date 2019/4/11
 * @Email smallmarital@qq.com
 */
@RestController
@RequestMapping("item")
public class ItemController {
    @Autowired
    private ItemSevice itemSevice;

    @PostMapping
    public ResponseEntity<Item> saveItem(Item item){
        //校验价格
        if (item.getPrice() == null){
           // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            throw new LyException(ExceptionEnum.PRICE_CANNOT_BE_NULL);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }
}
