package me.znzn.tools.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import me.znzn.tools.common.enums.OssFileTypeEnum;
import me.znzn.tools.common.exception.BusinessException;
import me.znzn.tools.module.user.entity.vo.UserInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static me.znzn.tools.common.constant.CommonConstant.*;

/**
 * @author zhoujingjie
 * @date 2020/4/8
 * @since 1.0
 */
@Slf4j
public class UploadFileUtil {

    public static String upload(MultipartFile file, String contextPath) {

        String fileName = UUID.randomUUID().toString();
        String suffix = MultipartFileUtil.getExtFilename(file);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd/");
        String format = sd.format(new Date());
        String newName = fileName + suffix;
        String path = contextPath + format + newName;
        File img = new File(path);
        if (!img.getParentFile().exists()) {
            img.getParentFile().mkdirs();
        }
        try {
            file.transferTo(img);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("保存文件失败");
        }
        return "/upload/" + format + newName;
    }

    public static String uploadWithRequest(MultipartFile file, HttpServletRequest request) {
        String contextPath = request.getSession().getServletContext().getRealPath("/upload/");
        String filePath = upload(file, contextPath);
        return request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + filePath;
    }

    public static String uploadOSS(MultipartFile file, UserInfoVO user, OssFileTypeEnum bucketNameEnum) {
        String fileName = UUID.randomUUID().toString();
        String suffix = MultipartFileUtil.getExtFilename(file);
        OSS client = new OSSClientBuilder().build(OSS_ENDPOINT, OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET);
        try {
            log.info("上传文件中...");

            String format;
            if (bucketNameEnum == null) {
                bucketNameEnum = OssFileTypeEnum.OTHERS;
            }
            if (bucketNameEnum.equals(OssFileTypeEnum.AVATAR)) {
                format = bucketNameEnum.getName() + user.getId() + "/";
            } else {
                SimpleDateFormat sd = new SimpleDateFormat("/yyyy/MM/dd/");
                format = bucketNameEnum.getName() + user.getId() + sd.format(new Date());
            }
            String newName = format + fileName + suffix;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(getContentType(newName));
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentDisposition("inline;filename="+fileName + suffix);
            PutObjectResult result = client.putObject(OSS_BUCKET_NAME, newName, file.getInputStream(), objectMetadata);
            String eTag = result.getETag();
            log.info("上传完成，结果{}", eTag);
            if (StringUtils.isNotEmpty(eTag)) {
                return newName;
            }
        } catch (IOException e) {
            log.error("获取文件流失败", e);
        } finally {
            client.shutdown();
        }
        return null;
    }

    public static void delFile(String filename) {
        OSS client = new OSSClientBuilder().build(OSS_ENDPOINT, OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET);
        client.deleteObject(OSS_BUCKET_NAME, filename);
    }

    public static String getFileUrl(String fileName, Long expiration) {
        OSS client = new OSSClientBuilder().build(OSS_ENDPOINT, OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET);
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(OSS_BUCKET_NAME, fileName);
        request.setExpiration(expirationDate);
        URL url = client.generatePresignedUrl(request);
        return url.toString();
    }

    public static String getFileUrl(String filename, Long expiration, String style) {
        OSS client = new OSSClientBuilder().build(OSS_ENDPOINT, OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET);
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(OSS_BUCKET_NAME, filename);
        request.setExpiration(expirationDate);
        request.setProcess(style);
        URL url = client.generatePresignedUrl(request);
        return url.toString();
    }

    public static String getContentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase(".jpeg") ||
                filenameExtension.equalsIgnoreCase(".jpg") ||
                filenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase(".pptx") ||
                filenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase(".docx") ||
                filenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }
}
