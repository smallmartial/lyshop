package cn.smallmartial.page.client;

import cn.smallmartial.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {

}
