package cn.smallmartial.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author smallmartial
 * @Date 2019/4/11
 * @Email smallmarital@qq.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    PRICE_CANNOT_BE_NULL(400, "价格不能为空"),
    CATEGORY_NOT_FOND(404,"商品分类没查到"),
    BRAND_NOT_FOUND(404,"品牌不存在"),
    BRAND_SAVE_ERROR(500,"新增失败"),
    CREATE_NOT_FOND(500,"文件上传失败"),
    INVALID_NOT_FOND(500,"无效文件类型"),
    GOODS_NOT_FOOD(404,"商品不存在"),
    GOOD_SAVE_ERROR(500,"商品创建失败"),
    GOOD_SKU_NOT_FOND(404,"商品库存没查到"),
    SPEC_PARAM_NOT_FIND(404,"商品规格不存在"),
    INVALID_USER_DATA_TYPE(400,"用户数据类型不正确"),
    INVALID_VERIFY_CODE(400,"验证码无效"),
    INVALID_USERNAME_PASSWORD(400,"无效的用户名或者密码")

    ;



    private int code;
    private String msg;

}
