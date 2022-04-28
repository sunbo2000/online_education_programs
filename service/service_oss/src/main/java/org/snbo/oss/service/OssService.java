package org.snbo.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author sunbo
 * @create 2022-03-22-22:41
 */
public interface OssService {
    String uploadFileAvatar(MultipartFile file);
}
