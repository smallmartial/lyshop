package cn.smallmartial.api;

import cn.smallmartial.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author smallmartial
 * @Date 2019/4/23
 * @Email smallmarital@qq.com
 */
public interface UserApi {
    @GetMapping("query")
     User queryUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password);
}
