package com.example.demo.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
	private final String CLOUD_NAME = "dbwkwowdx";
	
	private final String API_KEY = "721333624242848";
	private final String API_SECRET = "GzA1-klgDSHR1Ft5IOjbQr52h3w";
	
	@Bean
	public Cloudinary getCloudinary() {
		Map<String,String> config = new HashMap<>();
		config.put("cloud_name", CLOUD_NAME);
		config.put("api_key", API_KEY);
		config.put("api_secret", API_SECRET);
		
		return new Cloudinary(config);
	}
}
