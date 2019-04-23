package cn.smallmartial.user.service;


import cn.smallmartial.common.enums.ExceptionEnum;
import cn.smallmartial.common.utils.NumberUtils;
import cn.smallmartial.exception.LyException;
import cn.smallmartial.pojo.User;
import cn.smallmartial.user.mapper.UserMapper;
import cn.smallmartial.user.utils.CodecUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author smallmartial
 * @Date 2019/4/23
 * @Email smallmarital@qq.com
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    static final String KEY_PREFIX = "user:code:phone:";


    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        return this.userMapper.selectCount(record) == 0;
    }

    public void sendVerifyCode(String phone) {
        String code = NumberUtils.generateCode(6);

        try {
            //生成key
            String key = KEY_PREFIX +phone;
            //生成验证码
            Map<String,String> msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            //发送验证码
            amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);
            //保存验证码
            redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);


        } catch (Exception e) {
            log.error("发送短信失败。phone：{}， code：{}", phone, code);

        }


    }

    public void register(User user, String code) {
        //从redis中取出验证码
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());

        //校验验证码
        if(!StringUtils.equals(code,cacheCode)){
            throw  new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        //CodecUtils.md5Hex(user.getPassword(),salt);
        //写入数据库
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    public User queryUser(String username, String password) {

        //查询用户
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        //校验
        if (user == null){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        //校验密码
        if(!StringUtils.equals(user.getPassword(),CodecUtils.md5Hex(password,user.getSalt()))){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);

        }

        return user;
    }
}
