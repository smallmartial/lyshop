package cn.smallmartial.sms.mq;



import cn.smallmartial.sms.config.SmsPropeties;
import cn.smallmartial.sms.utils.SmsUtils;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


import java.util.Map;

/**
 * @Author smallmartial
 * @Date 2019/4/22
 * @Email smallmarital@qq.com
 */
@Slf4j
@Component
@EnableConfigurationProperties(SmsPropeties.class)
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsPropeties prop;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.sms.queue", durable = "true"),
            exchange = @Exchange(value = "ly.sms.exchange",
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String, String> msg) throws Exception {
        if (msg == null || msg.size() <= 0) {
            // 放弃处理
            return;
        }
        String phone = msg.get("phone");
       String code = msg.get("code");

        if (StringUtils.isBlank(phone)) {
            // 放弃处理
            return;
        }

        // 发送消息
        SendSmsResponse resp = this.smsUtils.sendSms(phone,code,
                prop.getSignName(),
                prop.getVerifyCodeTemplate() );
        // 发送失败
       //throw new RuntimeException();
    }
}
