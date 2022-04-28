package org.snbo.msmService.service;

import java.util.Map;

/**
 * @author sunbo
 * @create 2022-04-05-21:44
 */
public interface MsmService {
    void sendMessage(String phone, Map<String, String> param);
}
