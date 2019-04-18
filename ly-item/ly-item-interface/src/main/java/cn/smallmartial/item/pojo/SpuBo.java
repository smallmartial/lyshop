package cn.smallmartial.item.pojo;

import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/15
 * @Email smallmarital@qq.com
 */

@Data
public class SpuBo extends Spu {
    /**
     * 商品分类名称
     */
    @Transient
    private String cname;
    /**
     * 品牌名称
     */
    @Transient
    private String bname;

    /**
     * 商品详情
     */
    @Transient
    private SpuDetail spuDetail;

    /**
     * sku列表
     */
    @Transient
    private List<Sku> skus;


}
