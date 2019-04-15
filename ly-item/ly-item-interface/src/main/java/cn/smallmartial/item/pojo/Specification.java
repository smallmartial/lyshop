package cn.smallmartial.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author smallmartial
 * @Date 2019/4/14
 * @Email smallmarital@qq.com
 */
@Data
@Table(name = "tb_specification")
public class Specification {

    @Id
    private Long categoryId;

    private String specifications;
}
