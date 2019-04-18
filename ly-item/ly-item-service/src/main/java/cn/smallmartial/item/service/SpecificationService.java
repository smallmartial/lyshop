package cn.smallmartial.item.service;

import cn.smallmartial.item.mapper.SpecGroupMapper;
import cn.smallmartial.item.mapper.SpecParamMapper;
import cn.smallmartial.item.mapper.SpecificationMapper;
import cn.smallmartial.item.pojo.SpecGroup;
import cn.smallmartial.item.pojo.SpecParam;
import cn.smallmartial.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/14
 * @Email smallmarital@qq.com
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;
    public Specification queryById(Long id) {
        return this.specificationMapper.selectByPrimaryKey(id);
    }

    public void saveSpecification(Specification specification) {
        this.specificationMapper.insert(specification);
    }

    public void updateSpecification(Specification specification) {
        /**
         *  updateByPrimaryKeySelective会对字段进行判断再更新(如果为Null就忽略更新)，
         *  如果你只想更新某一字段，可以用这个方法。
         *
         * updateByPrimaryKey对你注入的字段全部更新，
         * 如果为字段不更新，数据库的值就为null。
         */
        this.specificationMapper.updateByPrimaryKeySelective(specification);
    }

    public void deleteSpecification(Specification specification) {
        this.specificationMapper.deleteByPrimaryKey(specification);
    }

    public List<SpecGroup> queryBySpecGroups(Long cid) {
        SpecGroup t = new SpecGroup();
        t.setCid(cid);
        return this.specGroupMapper.select(t);
    }

    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        param.setGeneric(generic);
        return this.specParamMapper.select(param);
    }
}
