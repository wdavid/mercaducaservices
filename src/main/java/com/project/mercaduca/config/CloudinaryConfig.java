package com.project.mercaduca.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dr76brul6");
        config.put("api_key", "662121372313637");
        config.put("api_secret", "BYYi8QPZCdaRNT7Fpwdz6gGmucs");
        return new Cloudinary(config);
    }
}