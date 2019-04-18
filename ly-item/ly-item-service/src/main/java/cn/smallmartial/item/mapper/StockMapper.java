package cn.smallmartial.item.mapper;

import cn.smallmartial.item.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;



/**
 * @Author smallmartial
 * @Date 2019/4/16
 * @Email smallmarital@qq.com
 */
public interface StockMapper extends InsertListMapper<Stock>,Mapper<Stock>,IdListMapper<Stock,Long>{
}
