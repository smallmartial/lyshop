package cn.smallmartial.item.service;

import cn.smallmartial.common.enums.ExceptionEnum;
import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.exception.LyException;
import cn.smallmartial.item.mapper.BrandMapper;
import cn.smallmartial.item.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/13
 * @Email smallmarital@qq.com
 */
@Service
public class BrandService {
    /**
     * 学习了分页助手的使用 PageHelper函数
     */
    @Autowired
    private BrandMapper brandMapper;

    /**
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    public PageResult<Brand> queryBrandBypage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        /**
         * WHERE 'name' LIKE '%HTC%' or letter == 'x'
         * order by id desc
         */
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().orLike("name","%"+key+"%")
                    .orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)){

            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            //String orderByClause = sortBy +(desc ? "DESC" : "ASC");
            example.setOrderByClause(orderByClause);
        }
        //查询 ctrl +shirt +u 转为大写
        List<Brand> list = brandMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分页结果 pageInfo对分页进行了封装
        PageInfo<Brand> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),list);
    }
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if (count != 1){
            throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        //新增中间表
        for (Long cid:cids) {
            count = brandMapper.insertCategoryBrand(cid,brand.getId());
            if (count != 1){
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);

            }
        }
    }

    public Brand queryById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand ==null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> list = brandMapper.queryByCategoryId(cid);
        if (list == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }
}
