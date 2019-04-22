package cn.smallmartial;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author smallmartial
 * @Date 2019/4/22
 * @Email smallmarital@qq.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class test {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Test
    public void testSend() throws InterruptedException {
        Map<String,String> msg = new HashMap<>();
        msg.put("phone","15037979258");
        msg.put("code","520");
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);


            Thread.sleep(10000L);

    }
}
