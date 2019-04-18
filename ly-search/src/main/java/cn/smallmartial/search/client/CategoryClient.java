package cn.smallmartial.search.client;

import cn.smallmartial.item.api.CategoryApi;
import cn.smallmartial.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/18
 * @Email smallmarital@qq.com
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {

}
