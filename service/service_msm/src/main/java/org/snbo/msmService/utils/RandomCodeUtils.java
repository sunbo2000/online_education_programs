package org.snbo.msmService.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 生成四位随机验证码
 * @author sunbo
 * @create 2022-04-06-12:06
 */
@Component
public class RandomCodeUtils {
    public static String getFourBitRandom() {
        Random random = new Random();
        return String.valueOf(random.nextInt(8999) + 1000);
    }
}
