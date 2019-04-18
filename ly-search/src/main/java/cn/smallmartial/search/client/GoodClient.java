package cn.smallmartial.search.client;

import cn.smallmartial.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */
@FeignClient(value = "item-service")
public interface GoodClient extends GoodsApi {
}
