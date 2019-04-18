package cn.smallmartial.common.mapper;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @Author smallmartial
 * @Date 2019/4/16
 * @Email smallmarital@qq.com
 */
@RegisterMapper
public interface BaseMapper<T>extends Mapper<T>, IdListMapper<T,Long> , InsertListMapper<T> {
}
