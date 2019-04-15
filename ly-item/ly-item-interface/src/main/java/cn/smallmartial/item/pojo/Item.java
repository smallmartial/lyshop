package cn.smallmartial.item.pojo;

/**
 * @Author smallmartial
 * @Date 2019/4/11
 * @Email smallmarital@qq.com
 */
public class Item {
    private Integer id;
    private String name;
    private Long price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
