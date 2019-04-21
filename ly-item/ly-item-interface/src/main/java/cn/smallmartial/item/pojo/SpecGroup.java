package cn.smallmartial.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/14
 * @Email smallmarital@qq.com
 */
@Data
@Table(name = "tb_spec_group")
public class SpecGroup {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long cid;
    private String name;
    @Transient
    private List<SpecParam> params; // 该组下的所有规格参数集合
}
