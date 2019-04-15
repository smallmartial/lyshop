package cn.smallmartial.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/14
 * @Email smallmarital@qq.com
 */
@Data
@ConfigurationProperties(prefix = "ly.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;

}
