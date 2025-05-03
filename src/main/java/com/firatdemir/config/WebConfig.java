package com.firatdemir.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Tüm endpoint'lere izin verir

				.allowedOrigins("http://192.168.1.140") // Frontend bilgisayarınızın IP adresini burada belirtin
				.allowedMethods("GET", "POST", "PUT", "DELETE") // Hangi HTTP metotlarına izin verileceği
				.allowedHeaders("*"); // Herhangi bir header'a izin verir
	}
}