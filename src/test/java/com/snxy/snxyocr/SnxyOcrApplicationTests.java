package com.snxy.snxyocr;

import com.baidu.aip.ocr.AipOcr;
import com.snxy.snxyocr.service.AipOcrService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SnxyOcrApplicationTests {

    @Resource
    AipOcrService aipOcrService;

    @Test
    public void contextLoads() {
    }


}
