package cn.smallmartial.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author smallmartial
 * @Date 2019/4/15
 * @Email smallmarital@qq.com
 */
@Table(name = "tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    private Long spuId;
    private String description;
    //private String specifications;
    private String genericSpec;
    private String specialSpec;
    //private String specTemplate;
    private String packingList;
    private String afterService;
}
