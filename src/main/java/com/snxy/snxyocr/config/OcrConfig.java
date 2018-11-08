package com.snxy.snxyocr.config;

import com.baidu.aip.ocr.AipOcr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class OcrConfig {

    @Bean
    @ConfigurationProperties(prefix = "baidu.ocr")
    public BaiduOcrInfo getBaiduOcrInfo(){
        return new BaiduOcrInfo();
    }

    @Bean
    public AipOcr getAipCcr(BaiduOcrInfo baiduOcrInfo){

        AipOcr  aipOcr = new AipOcr(baiduOcrInfo.getAppId(),baiduOcrInfo.getApiKey(),baiduOcrInfo.getSecretKey());
        // 可选：设置网络连接参数
        aipOcr.setConnectionTimeoutInMillis(2000);
        aipOcr.setSocketTimeoutInMillis(60000);
        return aipOcr;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaiduOcrInfo {
        private String appId ;

        private String apiKey;

        private String secretKey;
    }
}
