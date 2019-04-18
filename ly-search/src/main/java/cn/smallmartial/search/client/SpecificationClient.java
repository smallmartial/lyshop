package cn.smallmartial.search.client;

import cn.smallmartial.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */


@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationApi {

}
