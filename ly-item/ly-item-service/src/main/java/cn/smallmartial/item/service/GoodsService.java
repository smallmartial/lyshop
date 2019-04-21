package cn.smallmartial.item.service;

import cn.smallmartial.common.enums.ExceptionEnum;
import cn.smallmartial.common.vo.PageResult;
import cn.smallmartial.exception.LyException;
import cn.smallmartial.item.mapper.SkuMapper;
import cn.smallmartial.item.mapper.SpuDetailMapper;
import cn.smallmartial.item.mapper.SpuMapper;
import cn.smallmartial.item.mapper.StockMapper;
import cn.smallmartial.item.pojo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;


import javax.persistence.Transient;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author smallmartial
 * @Date 2019/4/15
 * @Email smallmarital@qq.com
 */
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criterion = example.createCriteria();
        //搜索字段过滤
        if (StringUtils.isNotBlank(key)){
            criterion.andLike("title","%"+key+"%");
        }
        //上下架过滤
        if (saleable != null){
            criterion.orEqualTo("saleable",saleable);
        }

        //排序
        example.setOrderByClause("last_update_time DESC");

        //查询
        List<Spu> spus = spuMapper.selectByExample(example);
        //判断
        if (CollectionUtils.isEmpty(spus)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOOD);

        }
        //解析分类和品牌的名称
        loadCategoryAndBrandName(spus);
        //解析分页结果
        PageInfo<Spu> info = new PageInfo<>(spus);

        return new PageResult<>(info.getTotal(),spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu:spus){
            //处理分类名称
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names,"/"));
            //处理品牌信息
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());

        }
    }
    @Transient
    public void saveGoodsA(Spu spu) {
        //新增spu
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);

        int count = spuMapper.insert(spu);
        if (count != 1){
            throw new LyException(ExceptionEnum.GOOD_SAVE_ERROR);
        }
        //新增detail
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        spuDetailMapper.insert(detail);
        //新增sku
        List<Sku> skus = spu.getSkus();

        //定义库存集合
        List<Stock> stockList = new ArrayList<>();
        for (Sku sku:skus){
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            count = skuMapper.insert(sku);
            if (count !=1){
                throw new LyException(ExceptionEnum.GOOD_SAVE_ERROR);
            }
            //新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stockList.add(stock);
//            count = stockMapper.insert(stock);
//            if (count !=1){
//                throw new LyException(ExceptionEnum.GOOD_SAVE_ERROR);
//            }
       }
        stockMapper.insertList(stockList);
        if (count != stockList.size()){
            throw new LyException(ExceptionEnum.GOOD_SAVE_ERROR);
        }
    }
    /**
     * 保存商品
     * @param spu
     */
    @Transactional
    public void saveGoods(SpuBo spu) {
        //保存spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insert(spu);

        //保存spu详情
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
       // System.out.println(spuDetail.getSpecifications().length());
        this.spuDetailMapper.insert(spuDetail);

        //保存sku和库存信息
        saveSkuAndStock(spu.getSkus(),spu.getId());

    }

    private void saveSkuAndStock(List<Sku> skus, Long id) {
        for (Sku sku : skus){
            if (!sku.getEnable()){
                continue;
            }
            //保存sku
            sku.setSpuId(id);
            //默认不参加任何促销
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);

            //保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        }
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */

    public SpuBo queryGoodsById(Long id) {
        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        SpuDetail spuDetail = this.spuDetailMapper.selectByPrimaryKey(spu.getId());

        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",spu.getId());
        List<Sku> skuList = this.skuMapper.selectByExample(example);
        List<Long> skuIdList = new ArrayList<>();
        for (Sku sku:skuList){
            System.out.println(sku);
            skuIdList.add(sku.getId());
        }

        List<Stock> stocks = this.stockMapper.selectByIdList(skuIdList);

        for (Sku sku:skuList){
            for (Stock stock : stocks){
                if (sku.getId().equals(stock.getSkuId())){
                    sku.setStock(stock.getStock());
                }
            }
        }

        SpuBo spuBo = new SpuBo();
        spuBo.setSpuDetail(spuDetail);
        spuBo.setSkus(skuList);
        return spuBo;
    }
    @Transient
    public void updateGoods(SpuBo spuBo) {
        /**
         * 更新策略：
         *      1.判断tb_spu_detail中的spec_template字段新旧是否一致
         *      2.如果一致说明修改的只是库存、价格和是否启用，那么就使用update
         *      3.如果不一致，说明修改了特有属性，那么需要把原来的sku全部删除，然后添加新的sku
         */
        //更新
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
//
//        //更新spu
//        SpuDetail spuDetail = spuBo.getSpuDetail();
//        String oldTemp = this.spuDetailMapper.selectByPrimaryKey(spuBo.getId()).getSpecTemplate();
//        if (spuDetail.getSpecTemplate().equals(oldTemp)){
//            //更新sku和库存信息 相等 更新
//            updateSkuAndStock(spuBo.getSkus(),spuBo.getId(),true);
//
//        }else {
//            //更新sku和库存信息 不等 插入
//            updateSkuAndStock(spuBo.getSkus(),spuBo.getId(),false);
//        }
        //更新spu详情


    }

    private void updateSkuAndStock(List<Sku> skus, Long id, boolean tag) {
        //通过标签获取是insert还是 update
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",id);

        //oldList中保存数据库中spu_id = id 的全部sku
        List<Sku> oldList = this.skuMapper.selectByExample(example);
        if (tag){

            /**
             * 判断更新时候是否被添加
             */
            int count = 0;
            for (Sku sku:skus){
                if (!sku.getEnable()){
                    continue;
                }
                for (Sku old :oldList){
                    if (sku.getOwnSpec().equals(old.getOwnSpec())){
                        System.out.println("更新");
                        List<Sku> list = this.skuMapper.select(old);;
                        if (sku.getPrice() == null){
                            sku.setPrice(0L);
                        }
                        if (sku.getStock() == null){
                            sku.setStock(0);
                        }
                        sku.setId(list.get(0).getId());
                        sku.setCreateTime(list.get(0).getCreateTime());
                        sku.setSpuId(list.get(0).getSpuId());
                        sku.setLastUpdateTime(new Date());
                        this.skuMapper.updateByPrimaryKey(sku);
                        //跟新库存信息
                        Stock stock = new Stock();
                        stock.setSkuId(sku.getId());
                        stock.setStock(sku.getStock());
                        this.stockMapper.updateByPrimaryKeySelective(stock);

                        oldList.remove(old);
                        break;
                    }else {
                        count++;
                    }
                }
                if (count == oldList.size() && count != 0){
                    //当只有一个sku时，更新完因为从oldList中将其移除，所以长度变为0，所以要需要加不为0的条件
                    List<Sku> addSku = new ArrayList<>();
                    addSku.add(sku);
                    saveSkuAndStock(addSku,id);
                    count = 0;
                }else {
                    count = 0;
                }
            }
            if (oldList.size() !=0){
                for (Sku sku:oldList){
                    this.skuMapper.deleteByPrimaryKey(sku.getId());
                    Example e = new Example(Stock.class);
                    e.createCriteria().andEqualTo("skuId",sku.getId());
                    this.stockMapper.deleteByExample(e);
                }
            }
        }else {
            List<Long> ids = oldList.stream().map(Sku::getId).collect(Collectors.toList());
            //删除以前的库存
            Example e = new Example(Stock.class);
            e.createCriteria().andIn("skuId",ids);
            this.stockMapper.deleteByExample(e);
            //新增库存
            saveSkuAndStock(skus,id);
        }


    }
    @Transient
    public void deleteGoods(long id) {
        this.spuMapper.deleteByPrimaryKey(id);

        Example example = new Example(SpuDetail.class);
        example.createCriteria().andEqualTo("spuId",id);
        this.spuDetailMapper.deleteByExample(example);

        List<Sku> skuList = this.skuMapper.selectByExample(example);
        for (Sku sku:skuList){
            this.skuMapper.deleteByPrimaryKey(sku.getId());
            this.stockMapper.deleteByExample(sku.getId());
        }
    }
    @Transactional
    public void save(SpuBo spu) {
        // 保存spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insert(spu);
        // 保存spu详情
        spu.getSpuDetail().setSpuId(spu.getId());
        this.spuDetailMapper.insert(spu.getSpuDetail());

        // 保存sku和库存信息
        saveSkuAndStock(spu.getSkus(), spu.getId());
    }

    public SpuDetail querySpuDetailById(Long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

    public List<Sku> querySkuSpuId(Long spuId) {
        // 查询sku
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        for (Sku sku : skus) {
            // 同时查询出库存
            sku.setStock(this.stockMapper.selectByPrimaryKey(sku.getId()).getStock());
        }
        return skus;
    }

    @Transactional
    public void update(SpuBo spu) {
        // 查询以前sku
        List<Sku> skus = this.querySkuSpuId(spu.getId());
        // 如果以前存在，则删除
        if(!CollectionUtils.isEmpty(skus)) {
            List<Long> ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
            // 删除以前库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", ids);
            this.stockMapper.deleteByExample(example);

            // 删除以前的sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            this.skuMapper.delete(record);

        }
        // 新增sku和库存
        saveSkuAndStock(spu.getSkus(), spu.getId());

        // 更新spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);

        // 更新spu详情
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
    }

    public Spu querySpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //查询sku
        spu.setSkus(querySkuSpuId(id));
        //查询detail
        spu.setSpuDetail(querySpuDetailById(id));

        return spu;
    }












//    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
//        for (Sku sku : skus) {
//            if (!sku.getEnable()) {
//                continue;
//            }
//            // 保存sku
//            sku.setSpuId(spuId);
//            // 初始化时间
//            sku.setCreateTime(new Date());
//            sku.setLastUpdateTime(sku.getCreateTime());
//            this.skuMapper.insert(sku);
//
//            // 保存库存信息
//            Stock stock = new Stock();
//            stock.setSkuId(sku.getId());
//            stock.setStock(sku.getStock());
//            this.stockMapper.insert(stock);
//        }
//    }
}
