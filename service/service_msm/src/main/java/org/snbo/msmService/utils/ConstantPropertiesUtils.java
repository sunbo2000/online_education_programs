package org.snbo.msmService.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean {
    @Value("${tx.sms.secretId}")
    private String secretId;
    @Value("${tx.sms.secretKey}")
    private String secretKey;
    @Value("${tx.sms.sdkAppId}")
    private String sdkAppId;
    @Value("${tx.sms.signName}")
    private String signName;
    @Value("${tx.sms.templateId}")
    private String templateId;

    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String SDK_APP_ID;
    public static String SIGN_NAME;
    public static String TEMPLATE_ID;

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
        SDK_APP_ID = sdkAppId;
        SIGN_NAME = signName;
        TEMPLATE_ID = templateId;
    }
}