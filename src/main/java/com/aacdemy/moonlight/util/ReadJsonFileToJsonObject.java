package com.aacdemy.moonlight.util;

import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ReadJsonFileToJsonObject {
    public JSONObject read() throws IOException {
        Resource resource = new ClassPathResource("response.json");
        InputStream inputStream = resource.getInputStream();
        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
        String content = new String(bdata, StandardCharsets.UTF_8);
        return new JSONObject(content);
    }
}
