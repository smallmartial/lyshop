package cn.smallmartial.search.client;

import cn.smallmartial.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
