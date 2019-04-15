package cn.smallmartial.item.mapper;

import cn.smallmartial.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author smallmartial
 * @Date 2019/4/13
 * @Email smallmarital@qq.com
 */
public interface BrandMapper extends Mapper<Brand> {
    /**
     *
     * @param cid
     * @param bid
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);
}
