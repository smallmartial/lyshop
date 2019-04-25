package cn.smallmartial.auth.web;

import cn.smallmartial.auth.config.JwtProperties;
import cn.smallmartial.auth.entity.UserInfo;
import cn.smallmartial.auth.service.AuthService;
import cn.smallmartial.auth.utils.JwtUtils;
import cn.smallmartial.common.enums.ExceptionEnum;
import cn.smallmartial.common.utils.CookieUtils;
import cn.smallmartial.exception.LyException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author smallmartial
 * @Date 2019/4/23
 * @Email smallmarital@qq.com
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties prop;

    /**todo 单点登录 nginx 未实现
     * 问题描述：通过localhost可以获取cookie，但是通过api.leyou.com无法获取
     * 存在问题：初步确定是zuul网关问题，
     * 排查点 gatway. zuul  nginx 更改springcloud版本 未能成功,maven依赖过深，出现版本不兼容
     * nginx 做过修改
     * zuul yml文件做过修改
     * spring源码 看不懂
     * 断点调试多次 未果
     */
    /**
     * 登录授权
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<String> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
             HttpServletResponse response){
        //登录
        String token = authService.login(username,password);
        //写入cookie
        CookieUtils.setCookie(request, response, prop.getCookieName(),
                token, prop.getCookieMaxAge(), true);
        return ResponseEntity.ok(token);
    }

    /**
     * 校验用户登录状态
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        if (StringUtils.isBlank(token)){
            //如果没有token，则返回403
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        try {
            //解析token
            UserInfo info = JwtUtils.getInfoFromToken(token,prop.getPublicKey());

            // 如果成功，我们还需要刷新token
            String newToken = JwtUtils.generateToken(info,
                    prop.getPrivateKey(), prop.getExpire());
            // 然后写入cookie中
            // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
            CookieUtils.setCookie(request, response, prop.getCookieName(),
                    newToken, prop.getCookieMaxAge(), true);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            //token已过期，或者 token已被修改
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
    }

}
