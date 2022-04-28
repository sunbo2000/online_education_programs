package org.snbo.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author sunbo
 * @create 2022-03-31-19:31
 */
public interface VodService {
    String saveVideo(MultipartFile file);

    void removeVideos(List<String> videoIds);

    String getPlayAuth(String videoSourceId);
}
