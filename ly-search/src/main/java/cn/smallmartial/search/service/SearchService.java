package cn.smallmartial.search.service;

import cn.smallmartial.common.enums.ExceptionEnum;

import cn.smallmartial.common.utils.JsonUtils;
import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.exception.LyException;
import cn.smallmartial.item.pojo.*;
import cn.smallmartial.search.Repository.GoodsRepository;
import cn.smallmartial.search.client.BrandClient;
import cn.smallmartial.search.client.CategoryClient;
import cn.smallmartial.search.client.GoodClient;
import cn.smallmartial.search.client.SpecificationClient;
import cn.smallmartial.search.pojo.Goods;
import cn.smallmartial.search.pojo.SearchRequest;
import cn.smallmartial.search.pojo.SearchResult;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */

@Service
@Slf4j
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodClient goodClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private ElasticsearchTemplate template;

    public Goods bulidGoods(Spu spu){
        Long supId = spu.getId();
        //查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOND);
        }
        List<String> names =categories.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //搜索字段
        String all = spu.getTitle()+ StringUtils.join(names,"")+brand.getName();

        //查询sku
        List<Sku> skuList = goodClient.queryBySkuSpuId(spu.getId());
        if (CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnum.GOOD_SKU_NOT_FOND);
        }
        //对SKU进行处理
        List<Map<String,Object>> skus = new ArrayList<>();
        //价格集合
        List<Long> priceList = new ArrayList<>();
        for (Sku sku : skuList) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("image",StringUtils.substringBefore(sku.getImages(),","));
            skus.add(map);
            //处理价格
            priceList.add(sku.getPrice());
        }
      //  List<Long> priceList = skuList.stream().map(Sku::getPrice).collect(Collectors.toList());

        //查询规格产数
        List<SpecParam> params = specificationClient.querySpecSpecParam(null, spu.getCid3(), true, null);
        if (CollectionUtils.isEmpty(params)){
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FIND);
        }
        //查询商品详情
        SpuDetail spuDetail = goodClient.querySpuDetailById(supId);
        //String json = spuDetail.getGenericSpec();
        Map<Long,String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(),Long.class,String.class);
        //规格参数
        String json =spuDetail.getSpecialSpec();
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(json, new TypeReference<Map<Long, List<String>>>() {
        });
        //规格参数,key是规格参数的名字，值是规格参数的值
        Map<String,Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            //规格名称
            String key = param.getName();
            Object value = "";
            if (param.getGeneric()){
                value =genericSpec.get(param.getId());
                //判断是否是数值类型
                if (param.getNumeric()){
                    //处理成段
                    value = chooseSegment(value.toString(),param);
                }
            }else {
                value =specialSpec.get(param.getId());
            }
            //存入map
            specs.put(key,value);
        }
        //构建goods对象
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setAll(spu.getTitle() + " " + StringUtils.join(names, " "));
        goods.setPrice(priceList);
        goods.setSkus(JsonUtils.serialize(skus));
        goods.setSpecs(specs);
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)){
            return null;
        }
        Integer page = request.getPage() - 1;
        Integer size = request.getSize();
        //创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"}, null));
        //分页
        queryBuilder.withPageable(PageRequest.of(page,size));
        //过滤 todo 如果有问题注释掉
        MatchQueryBuilder basicQuery = QueryBuilders.matchQuery("all", key).operator(Operator.AND);
        //  QueryBuilder basicQuery = QueryBuilders.matchQuery("all", request.getKey());
        // 构建查询条件
        queryBuilder.withQuery(basicQuery);

        //排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if (StringUtils.isNotBlank(sortBy)){
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc? SortOrder.DESC :SortOrder.ASC));
        }
        //聚合分类和品牌
        //聚合分类
        String categoryAggName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //聚合品牌
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        AggregatedPage<Goods> pageInfo = (AggregatedPage<Goods>) this.repository.search(queryBuilder.build());

        //查询
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(),Goods.class);
        //解析结果
        //解析分页结果
        long total = result.getTotalElements();
        Integer totalPage =result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        //解析聚合结果
        Aggregations aggs = result.getAggregations();
         List<Category> categories =parseCategoryAgg(aggs.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));
        //完成规格参数聚合
        List<Map<String,Object>> specs = null;
        if (categories !=null && categories.size() == 1){
         specs = buildSpecificationAgg(categories.get(0).getId(),basicQuery);
          // log.error("spec===="+specs);

        }

        return new SearchResult(total,totalPage,goodsList,categories,brands,specs);
    }

    /**
     * 聚合规格参数查询
     * @param cid
     * @param basicQuery
     * @return
     */
    private List<Map<String,Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        //查询所需要的结果
        List<SpecParam> params = specificationClient.querySpecSpecParam(null, cid, true, null);
        //聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //带上查询条件
        queryBuilder.withQuery(basicQuery);
        for (SpecParam param : params) {
            String name = param.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name)
                    .field("specs."+name+".keyword"));
        }
        //获取结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        // 查询
        //解析结果
       Aggregations aggs = result.getAggregations();
        for (SpecParam param : params) {
            //规格参数名
            Map<String,Object> map = new HashMap();

            //准备map
            String name = param.getName();
            map.put("k",name);

            StringTerms terms = (StringTerms) aggs.get(name);
            map.put("options",terms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList()));
            specs.add(map);
        }

        return specs;
    }

    /**
     * 解析品牌聚合
     * @param terms
     * @return
     */
    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber()
                            .longValue()).collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByIds(ids);
            return brands;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析商品分类聚合
     * @param terms
     * @return
     */
    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber()
                            .longValue()).collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryByIds(ids);
            return categories;
        } catch (Exception e) {
            return null;
        }
    }
}
