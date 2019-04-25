package cn.smallmartial.auth.client;

import cn.smallmartial.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author smallmartial
 * @Date 2019/4/23
 * @Email smallmarital@qq.com
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
