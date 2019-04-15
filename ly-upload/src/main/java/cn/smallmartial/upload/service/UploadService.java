package cn.smallmartial.upload.service;



import cn.smallmartial.common.enums.ExceptionEnum;
import cn.smallmartial.exception.LyException;
import cn.smallmartial.upload.config.UploadProperties;
import cn.smallmartial.upload.web.UploadController;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @Author smallmartial
 * @Date 2019/4/13
 * @Email smallmarital@qq.com
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {
    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties uploadProperties;
    @Autowired
    private ThumbImageConfig thumbImageConfig;
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
   // private static final List<String> ALLOWTYPES = Arrays.asList("image/jpg","image/png","image/bmp");
   // private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    public String uploadImage(MultipartFile file) {
        //校验文件类型
        String contentType = file.getContentType();
        if(!uploadProperties.getAllowTypes().contains(contentType)){
            throw new LyException(ExceptionEnum.INVALID_NOT_FOND);
        }
        //校验文件内容
        BufferedImage image = null;
        try {
            image = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new LyException(ExceptionEnum.CREATE_NOT_FOND);
        }
        if (image == null){
            throw new LyException(ExceptionEnum.INVALID_NOT_FOND);
        }
//        //1.准备目标路径
//        File dest = new File("E:\\javademo\\upload",file.getOriginalFilename());

        //2.保存文件到本地
        try {
            //String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            //上传到FastDFs
            String  extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //file.transferTo(dest);
            //return "http://smallmartial.cn/"+ storePath.getFullPath();
            return uploadProperties.getBaseUrl()+ storePath.getFullPath();
        } catch (IOException e) {
            //上传失败
            log.error("上传失败",e);
            throw new LyException(ExceptionEnum.CREATE_NOT_FOND);
        }
        //3.返回路径

    }

    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
//            if (!suffixes.contains(type)) {
//                logger.info("上传失败，文件类型不匹配：{}", type);
//                return null;
//            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }
//            // 2、保存图片
//            // 2.1、生成保存目录
//            File dir = new File("E:\\javademo\\upload");
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            // 2.2、保存图片
//            file.transferTo(new File(dir, file.getOriginalFilename()));

            // 2.3、拼接图片地址
            String url = "http://api.leyou.com/upload/" + file.getOriginalFilename();

            return url;
        } catch (Exception e) {
            return null;
        }
    }
    }
