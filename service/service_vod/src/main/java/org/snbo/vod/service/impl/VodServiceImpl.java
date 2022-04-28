package org.snbo.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.snbo.commonutils.TimeUtils;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.snbo.vod.service.VodService;
import org.snbo.vod.utils.ConstantVodUtils;
import org.snbo.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author sunbo
 * @create 2022-03-31-19:32
 */
@Service
public class VodServiceImpl implements VodService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String saveVideo(MultipartFile file) {
        String videoId;
        try {
            String accessKeyId = ConstantVodUtils.KEY_ID;
            String accessKeySecret = ConstantVodUtils.KEY_SECRET;
            //title: 上传之后文件名称
            //filename:上传文件原始名称
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0, fileName.lastIndexOf('.'));
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return videoId;
    }

    @Override
    public void removeVideos(List<String> videoIds) {
        try {
            //初始化client
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
            //创建删除request
            DeleteVideoRequest request = new DeleteVideoRequest();
            //把list集合元素用,分隔 表示为字符串形式
            String ids = String.join(",", videoIds);
            //向request里设置ids
            request.setVideoIds(ids);
            //调用初始化client删除
            client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new MoguException(20001, "删除视频失败");
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) {
        try {
            //记录放redis
            String videoCountName = TimeUtils.getNowVideoCountName();
            String videoCount = redisTemplate.opsForValue().get(videoCountName);
            if (StringUtils.isEmpty(videoCount)) {
                redisTemplate.opsForValue().set(videoCountName, "1", TimeUtils.getInvalidTime(), TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(videoCountName, String.valueOf(Integer.parseInt(videoCount) + 1), 0);
            }

            //创建初始化client对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
            //创建获取凭证的request对象和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //像request中写入id
            request.setVideoId(videoSourceId);
            //调用client
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            return response.getPlayAuth();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new MoguException(20001, "获取视频凭证失败");
        }
    }
}

