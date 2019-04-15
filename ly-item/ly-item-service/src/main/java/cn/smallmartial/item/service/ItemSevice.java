package cn.smallmartial.item.service;

import cn.smallmartial.item.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @Author smallmartial
 * @Date 2019/4/11
 * @Email smallmarital@qq.com
 */
@Service
public class ItemSevice {
    public Item saveTtem(Item item){
        //商品新增
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
