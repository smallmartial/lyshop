package cn.smallmartial.item.service;

import cn.smallmartial.common.enums.ExceptionEnum;
import cn.smallmartial.exception.LyException;
import cn.smallmartial.item.mapper.CategoryMapper;
import cn.smallmartial.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/12
 * @Email smallmarital@qq.com
 */
@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        //查询条件，mapper会把对象中的非空属性作为查询条件
        Category t = new Category();
        t.setParentId(pid);
        List<Category> list = categoryMapper.select(t);
        //判断查询结果,自定义未查询到的枚举类型 404
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOND);
        }
        return list;
    }

    public List<Category> queryByIds(List<Long> ids){
        List<Category> categories = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOND);
        }
        return categories;
    }
}
