package org.snbo.msmService.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.R;
import org.snbo.msmService.service.MsmService;
import org.snbo.msmService.utils.RandomCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author sunbo
 * @create 2022-04-05-21:43
 */
@RestController
@RequestMapping("/eduMsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/{phoneNumber}")
    @ApiOperation("发送短信验证码")
    public R sendMessage(@PathVariable String phoneNumber) {
        //redis 能获取到,直接返回成功,不用再发送
        //避免重复发送
        String codeRedis = redisTemplate.opsForValue().get(phoneNumber);
        if (!StringUtils.isEmpty(codeRedis)) {
            R.ok();
        }
        //获取不到再用腾讯云进行发送
        String code = RandomCodeUtils.getFourBitRandom();
        Map<String, String> param = new HashMap<>();
        param.put("code", code);
        param.put("time", "2");
        System.out.println(param);
        //验证码放进redis,并设置有效时间
//        msmService.sendMessage(phoneNumber, param);
        redisTemplate.opsForValue().set(phoneNumber, code, 2, TimeUnit.MINUTES);
        return R.ok();
    }

}
