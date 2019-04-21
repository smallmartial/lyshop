package cn.smallmartial.page.service;

import cn.smallmartial.common.utils.JsonUtils;
import cn.smallmartial.item.pojo.*;
import cn.smallmartial.page.client.BrandClient;
import cn.smallmartial.page.client.CategoryClient;
import cn.smallmartial.page.client.GoodClient;
import cn.smallmartial.page.client.SpecificationClient;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.*;

/**
 * @Author smallmartial
 * @Date 2019/4/20
 * @Email smallmarital@qq.com
 */
@Service
public class PageService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodClient goodsClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private TemplateEngine templateEngine;

    public Map<String, Object> loadModel(Long id) {
        Map<String,Object> model = new HashMap<>();
        //查询spu
        Spu spu = goodsClient.querySpuById(id);
        //查询skus
        List<Sku> skus =spu.getSkus();

        //查询详情
        SpuDetail detail = spu.getSpuDetail();
       // String specialSpec = detail.getSpecialSpec();
        //查询brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
      //  Brand brand = this.brandClient.queryBrandByIds(Collections.singletonList(spu.getBrandId())).get(0);
        // 准备品牌数据
        List<Brand> brands = this.brandClient.queryBrandByIds(
                Arrays.asList(spu.getBrandId()));
        //查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specs = specClient.queryGroupByCid(spu.getCid3());
        //查询规格参数（附加）


        // 查询规格组及组内参数
        List<SpecGroup> groups = specClient.queryGroupByCid(spu.getCid3());

        // 查询商品分类下的特有规格参数
        List<SpecParam> params =
                this.specClient.querySpecSpecParam(null, spu.getCid3(), null, false);
        // 处理成id:name格式的键值对
        Map<Long,String> paramMap = new HashMap<>();
        for (SpecParam param : params) {
            paramMap.put(param.getId(), param.getName());
        }
        /**
         * 对于规格属性的处理需要注意以下几点：
         *      1. 所有规格都保存为id和name形式
         *      2. 规格对应的值保存为id和value形式
         *      3. 都是map形式
         *      4. 将特有规格参数单独抽取
         */
        //获取所有规格参数，然后封装成id和name形式的数据
//        String allSpecJson = detail.getSpecialSpec();
//        List<Map<String, Object>> allSpecs = JsonUtils
//                .nativeRead(allSpecJson, new TypeReference<List<Map<String, Object>>>() {});
//        Map<Integer,String> specName = new HashMap<>();
//        Map<Integer,Object> specValue = new HashMap<>();
//        this.getAllSpecifications(allSpecs,specName,specValue);

        //获取特有规格参数
        model.put("title",spu.getTitle());
        model.put("subTitle",spu.getSubTitle());
        model.put("skus",skus);
        model.put("detail",detail);
        model.put("brand",brand);
        model.put("categories",categories);
        model.put("specs",specs);
        model.put("paramMap", paramMap);
        model.put("groups", groups);
      //  model.put("specialSpec");
        return model;
    }


}
