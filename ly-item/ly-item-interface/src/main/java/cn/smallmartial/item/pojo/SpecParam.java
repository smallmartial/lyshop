package cn.smallmartial.item.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author smallmartial
 * @Date 2019/4/17
 * @Email smallmarital@qq.com
 */
@Table(name = "tb_spec_param")
@Data
public class SpecParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;

}