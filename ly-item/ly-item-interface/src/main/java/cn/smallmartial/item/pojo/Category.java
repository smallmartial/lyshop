package cn.smallmartial.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author smallmartial
 * @Date 2019/4/12
 * @Email smallmarital@qq.com
 */

@Table(name="tb_category")
@Data
public class Category {
    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent; // 注意isParent生成的getter和setter方法需要手动加上Is
    private Integer sort;
}
