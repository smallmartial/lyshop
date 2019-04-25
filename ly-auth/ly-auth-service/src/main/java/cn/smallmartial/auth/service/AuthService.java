package cn.smallmartial.auth.service;

import cn.smallmartial.auth.client.UserClient;
import cn.smallmartial.auth.config.JwtProperties;
import cn.smallmartial.auth.entity.UserInfo;
import cn.smallmartial.auth.utils.JwtUtils;
import cn.smallmartial.common.enums.ExceptionEnum;
import cn.smallmartial.exception.LyException;
import cn.smallmartial.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @Author smallmartial
 * @Date 2019/4/23
 * @Email smallmarital@qq.com
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties prop;

    public String login(String username, String password) {
        //校验用户名和密码
        try {
            User user = userClient.queryUser(username, password);

            //判断
            if (user == null){
                throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
            }
            // 生成token
            String token = JwtUtils.generateToken(
                    new UserInfo(user.getId(), user.getUsername()),
                    prop.getPrivateKey(), prop.getExpire());
            return token;
        } catch (Exception e) {
            throw  new LyException(ExceptionEnum.CREATE_TOKEN_ERROR);
        }

    }
}
